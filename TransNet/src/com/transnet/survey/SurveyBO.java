package com.transnet.survey;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;

import com.transnet.common.database.Comfort;
import com.transnet.common.database.DBManager;
import com.transnet.common.database.DatabaseBridge;
import com.transnet.common.database.Feeling;
import com.transnet.common.database.Tools;

public class SurveyBO implements ISurveyBO {
	private Connection conn;

	public SurveyBO() {
		DatabaseBridge db = DatabaseBridge.getInstance();
		conn = db.getConnectionMYSQL();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String SaveSurveyDistinctive(String timestamp, String location,
			String feeling, String username, String token) {

		// location = Tools.Escape(location);

		boolean flag = DBManager.CompareToken(username, token);
		if (flag) {
			String surveyID = this.generateSurveyID();
			PreparedStatement ps2;
			Statement st;
			String sql2 = "insert into Survey_Data(ID,UserName,Sent_Date,Location) values('"
					+ surveyID
					+ "','"
					+ username
					+ "',timestamp('"
					+ timestamp
					+ "','24:00:00'),'" + location + "')";
			String sql3;
			try {
				st = conn.createStatement();
				st.executeUpdate(sql2);
				StringTokenizer feelingtoken = new StringTokenizer(feeling, "|");
				ArrayList<String> feeling_array = new ArrayList<String>();
				while (feelingtoken.hasMoreTokens()) {
					feeling_array.add(feelingtoken.nextToken());
				}
				int i = 0;
				int j = 1;
				int size = feeling_array.size();
				while (j < size) {
					sql3 = "insert into Feeling_Data(Survey_ID,Feeling,Feeling_Intensity) values(?,?,?)";
					ps2 = conn.prepareStatement(sql3);
					ps2.setString(1, surveyID);
					ps2.setString(2, feeling_array.get(i));
					ps2.setString(3, feeling_array.get(j));
					ps2.executeUpdate();
					i += 2;
					j += 2;
				}
				conn.commit();
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return "SQLException";
			}

			return surveyID;
		} else {
			return "Invalid User";
		}

	}

	@Override
	public String SaveSurveyAnonymous(String timestamp, String location,
			String feeling) {
		String surveyID = this.generateSurveyID();
		PreparedStatement ps1;
		Statement st;

		// location = Tools.Escape(location);

		String sql1 = "insert into Survey_Data(ID,Sent_Date,Location) values('"
				+ surveyID + "" + "',timestamp('" + timestamp
				+ "','24:00:00'),'" + location + "')";
		String sql2;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql1);
			StringTokenizer feelingtoken = new StringTokenizer(feeling, "|");
			ArrayList<String> feeling_array = new ArrayList<String>();
			while (feelingtoken.hasMoreTokens()) {
				feeling_array.add(feelingtoken.nextToken());
			}
			int i = 0;
			int j = 1;
			int size = feeling_array.size();
			while (j < size) {
				sql2 = "insert into Feeling_Data(Survey_ID,Feeling,Feeling_Intensity) values(?,?,?)";
				ps1 = conn.prepareStatement(sql2);
				ps1.setString(1, surveyID);
				ps1.setString(2, feeling_array.get(i));
				ps1.setString(3, feeling_array.get(j));
				ps1.executeUpdate();
				i += 2;
				j += 2;
			}
			conn.commit();
			if (!conn.isClosed())
				conn.close();
			return surveyID;
		} catch (SQLException e) {
			e.printStackTrace();
			return "SQLException";
		}

	}

	@Override
	public String Login(String username, String pass) {
		String sql = "select UserName from User_Details where UserName=? and Password=?";
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, pass);
			rs = ps.executeQuery();
			conn.commit();

			if (rs.next()) {
				conn.close();
				return Tools.getCheckSum(username, pass);
			} else {
				conn.close();
				return "Invalid User";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "SQLException";
		}
	}

	@Override
	public String CreateUser(String username, String password, String email,
			String gender, String age, String occupation) {
		boolean flag = DBManager.CheckUserName(username);
		if (!flag) {
			String sql = "insert into User_Details values(?,?,?,?,?,?)";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, email);
				ps.setString(4, gender);
				ps.setInt(5, Integer.parseInt(age));
				ps.setString(6, occupation);
				ps.executeUpdate();
				conn.commit();
				conn.close();
				return Tools.getCheckSum(username, password);
			} catch (SQLException e) {
				e.printStackTrace();
				return "SQLException";
			}
		} else {
			return "Invalid User Name";
		}
	}

	@Override
	public JSONObject SumTypeOfFeelingDistinctive(String location,
			String username, String token) {
		boolean flag = DBManager.CompareToken(username, token);
		if (!flag) {
			JSONObject result = new JSONObject();
			result.put("Return", "Invalid User");
			return new JSONObject();
		}

		// location = Tools.Escape(location);

		String sql = "SELECT Feeling, Feeling_Intensity FROM Survey_Data sd, Feeling_Data fd WHERE sd.ID = fd.Survey_ID AND sd.Location = '"
				+ location + "' ORDER BY Feeling";
		Statement st;
		ResultSet rs;
		String feeling;
		int feeling_intensity;
		HashMap<Feeling, ArrayList> hm = new HashMap<Feeling, ArrayList>();
		ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(0, 0, 0,
				0, 0, 0));
		hm.put(Feeling.Sad, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Happy, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Bored, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Excited, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Scared, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Safe, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Angry, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Peaceful, al);

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			conn.commit();

			while (rs.next()) {
				feeling = rs.getString(1);
				feeling_intensity = rs.getInt(2);
				switch (Feeling.valueOf(feeling)) {
				case Sad:
					this.AddFeelingIntensity(hm, Feeling.Sad, feeling_intensity);
					break;
				case Happy:
					this.AddFeelingIntensity(hm, Feeling.Happy,
							feeling_intensity);
					break;
				case Bored:
					this.AddFeelingIntensity(hm, Feeling.Bored,
							feeling_intensity);
					break;
				case Excited:
					this.AddFeelingIntensity(hm, Feeling.Excited,
							feeling_intensity);
					break;
				case Scared:
					this.AddFeelingIntensity(hm, Feeling.Scared,
							feeling_intensity);
					break;
				case Safe:
					this.AddFeelingIntensity(hm, Feeling.Safe,
							feeling_intensity);
					break;
				case Angry:
					this.AddFeelingIntensity(hm, Feeling.Angry,
							feeling_intensity);
					break;
				case Peaceful:
					this.AddFeelingIntensity(hm, Feeling.Peaceful,
							feeling_intensity);
					break;
				default:
					break;
				}
			}
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject jet = new JSONObject();
			jet.put("Return", "SQLException");
			return jet;
		}
		JSONObject jso = new JSONObject(hm);
		System.out.println(jso.toString());
		return jso;
	}

	private void AddFeelingIntensity(HashMap<Feeling, ArrayList> hm,
			Feeling feeling, int feeling_intensity) {
		ArrayList<Integer> al = hm.get(feeling);
		int i;
		switch (feeling_intensity) {
		case 0:
			i = al.get(0);
			i++;
			al.set(0, i);
			break;
		case 1:
			i = al.get(1);
			i++;
			al.set(1, i);
			break;
		case 2:
			i = al.get(2);
			i++;
			al.set(2, i);
			break;
		case 3:
			i = al.get(3);
			i++;
			al.set(3, i);
			break;
		case 4:
			i = al.get(4);
			i++;
			al.set(4, i);
			break;
		case 5:
			i = al.get(5);
			i++;
			al.set(5, i);
			break;
		default:
			break;
		}
	}

	private String generateSurveyID() {
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		String temp = Integer.toString(tlr.nextInt(1000, 10000));
		temp = "s" + temp;
		String sql = "SELECT id FROM Survey_Data WHERE id='" + temp + "'";
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			while (true) {
				rs = st.executeQuery(sql);
				if (rs.next()) {
					temp = Integer.toString(tlr.nextInt(1000, 10000));
					temp = "s" + temp;
					sql = "SELECT id FROM Survey_Data WHERE id='" + temp + "'";
					continue;
				} else {

					break;
				}
			}
		} catch (SQLException e) {
			System.out.println("======" + e.getMessage());
		}
		return temp;
	}

	@Override
	public JSONObject SumTypeOfFeelingAnonymous(String location) {

		// location = Tools.Escape(location);

		String sql = "SELECT Feeling, Feeling_Intensity FROM Survey_Data sd, Feeling_Data fd WHERE sd.ID = fd.Survey_ID AND sd.Location = '"
				+ location + "' ORDER BY Feeling";
		Statement st;
		ResultSet rs;
		String feeling;
		int feeling_intensity;
		HashMap<Feeling, ArrayList> hm = new HashMap<Feeling, ArrayList>();
		ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(0, 0, 0,
				0, 0, 0));
		hm.put(Feeling.Sad, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Happy, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Bored, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Excited, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Scared, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Safe, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Angry, al);

		al = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Feeling.Peaceful, al);

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			conn.commit();

			while (rs.next()) {
				feeling = rs.getString(1);
				feeling_intensity = rs.getInt(2);
				switch (Feeling.valueOf(feeling)) {
				case Sad:
					this.AddFeelingIntensity(hm, Feeling.Sad, feeling_intensity);
					break;
				case Happy:
					this.AddFeelingIntensity(hm, Feeling.Happy,
							feeling_intensity);
					break;
				case Bored:
					this.AddFeelingIntensity(hm, Feeling.Bored,
							feeling_intensity);
					break;
				case Excited:
					this.AddFeelingIntensity(hm, Feeling.Excited,
							feeling_intensity);
					break;
				case Scared:
					this.AddFeelingIntensity(hm, Feeling.Scared,
							feeling_intensity);
					break;
				case Safe:
					this.AddFeelingIntensity(hm, Feeling.Safe,
							feeling_intensity);
					break;
				case Angry:
					this.AddFeelingIntensity(hm, Feeling.Angry,
							feeling_intensity);
					break;
				case Peaceful:
					this.AddFeelingIntensity(hm, Feeling.Peaceful,
							feeling_intensity);
					break;
				default:
					break;
				}
			}
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject jet = new JSONObject();
			jet.put("Return", "SQLException");
			return jet;
		}
		JSONObject jso = new JSONObject(hm);
		System.out.println(jso.toString());
		return jso;
	}

	@Override
	public String SaveComfortLevelAnonymouse(String surveyID, String comfort,
			String comment) {
		String sql;
		PreparedStatement ps;
		StringTokenizer comfort_token = new StringTokenizer(comfort, "|");
		ArrayList<String> comfort_array = new ArrayList<String>();
		while (comfort_token.hasMoreTokens()) {
			comfort_array.add(comfort_token.nextToken());
		}
		try {
			sql = "update Survey_Data set Comfort_Comment=? where ID=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, comment);
			ps.setString(2, surveyID);
			ps.executeUpdate();

			int i = 0;
			int j = 1;
			int size = comfort_array.size();
			while (j < size) {
				if (i == 12) {
					sql = "insert into Comfort_Data(Survey_ID,Comfort_Level,Comfort_Value) values(?,?,?)";
				} else {
					sql = "insert into Comfort_Data(Survey_ID,Comfort_Level,Comfort_Intensity) values(?,?,?)";
				}
				ps = conn.prepareStatement(sql);
				ps.setString(1, surveyID);
				ps.setString(2, comfort_array.get(i));
				ps.setString(3, comfort_array.get(j));
				ps.executeUpdate();

				i += 2;
				j += 2;
			}
			conn.commit();
			if (!conn.isClosed())
				conn.close();
			return "Success";
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return "SQLException";
		}

	}

	@Override
	public String SaveComfortLevelDistinctive(String surveyID, String comfort,
			String username, String comment, String token) {
		boolean flag = DBManager.CompareToken(username, token);
		if (flag) {
			String sql = "update Survey_Data set Comfort_Comment=? where ID=?";

			PreparedStatement ps;
			StringTokenizer comfort_token = new StringTokenizer(comfort, "|");
			ArrayList<String> comfort_array = new ArrayList<String>();
			while (comfort_token.hasMoreTokens()) {
				comfort_array.add(comfort_token.nextToken());
			}
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, comment);
				ps.setString(2, surveyID);
				ps.executeUpdate();

				int i = 0;
				int j = 1;
				int size = comfort_array.size();
				while (j < size) {
					if (i == 12) {
						sql = "insert into Comfort_Data(Survey_ID,Comfort_Level,Comfort_Value) values(?,?,?)";
					} else {
						sql = "insert into Comfort_Data(Survey_ID,Comfort_Level,Comfort_Intensity) values(?,?,?)";
					}
					ps = conn.prepareStatement(sql);
					ps.setString(1, surveyID);
					ps.setString(2, comfort_array.get(i));
					ps.setString(3, comfort_array.get(j));
					ps.executeUpdate();

					i += 2;
					j += 2;
				}
				conn.commit();
				if (!conn.isClosed())
					conn.close();
				return "Success";
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return "SQLException";
			}
		} else {
			return "Invalid User";
		}
	}

	@Override
	public JSONObject SumTypeOfComfortDistinctive(String location,
			String username, String token) {

		// location = Tools.Escape(location);

		boolean flag = DBManager.CompareToken(username, token);
		if (!flag) {
			JSONObject result = new JSONObject();
			result.put("Return", "Invalid User");
			return new JSONObject();
		} else {
			String sql = "SELECT Comfort_Level, Comfort_Intensity,Comfort_Value FROM Survey_Data sd, Comfort_Data fd WHERE sd.ID = fd.Survey_ID AND sd.Location = '"
					+ location + "' ORDER BY Comfort_Level";
			Statement st;
			ResultSet rs;
			HashMap<Comfort, ArrayList> hm = new HashMap<Comfort, ArrayList>();
			ArrayList<Integer> c1 = new ArrayList<Integer>(Arrays.asList(0, 0,
					0, 0, 0, 0));
			hm.put(Comfort.Hot, c1);
			c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
			hm.put(Comfort.Cold, c1);
			c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
			hm.put(Comfort.Noisy, c1);
			c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
			hm.put(Comfort.Quite, c1);
			c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
			hm.put(Comfort.Full, c1);
			c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
			hm.put(Comfort.Empty, c1);
			c1 = new ArrayList<Integer>();
			hm.put(Comfort.Comfort, c1);

			String comfort;
			int comfort_intensity = 0;
			int comfort_value = 0;
			try {
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				conn.commit();
				while (rs.next()) {
					comfort = rs.getString(1);
					if (comfort.equals("Comfort")) {
						comfort_value = rs.getInt(3);
					} else {
						comfort_intensity = rs.getInt(2);
					}
					switch (Comfort.valueOf(comfort)) {
					case Hot:
						this.AddComfortIntensity(hm, Comfort.Hot,
								comfort_intensity);
						break;
					case Cold:
						this.AddComfortIntensity(hm, Comfort.Cold,
								comfort_intensity);
						break;
					case Noisy:
						this.AddComfortIntensity(hm, Comfort.Noisy,
								comfort_intensity);
						break;
					case Quite:
						this.AddComfortIntensity(hm, Comfort.Quite,
								comfort_intensity);
						break;
					case Full:
						this.AddComfortIntensity(hm, Comfort.Full,
								comfort_intensity);
						break;
					case Empty:
						this.AddComfortIntensity(hm, Comfort.Empty,
								comfort_intensity);
						break;
					case Comfort:
						this.AddComfortIntensity(hm, Comfort.Comfort,
								comfort_value);
						break;
					default:
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JSONObject jet = new JSONObject();
				jet.put("Return", "SQLException");
				return jet;
			}
			c1 = hm.remove(Comfort.Comfort);
			int value = 0;
			int size = c1.size();
			if (size == 0) {
				hm.put(Comfort.Comfort,
						new ArrayList<Integer>(Arrays.asList(0)));
				return new JSONObject(hm);
			}
			float average = 0;
			Iterator itr = c1.iterator();
			while (itr.hasNext()) {
				value += (Integer) itr.next();
			}

			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			System.out.println(df.format(value / size));
			average = Float.parseFloat(df.format(value / size));
			ArrayList<Float> af = new ArrayList<Float>();
			af.add(average);
			hm.put(Comfort.Comfort, af);
			JSONObject jso = new JSONObject(hm);
			return jso;
		}
	}

	@Override
	public JSONObject SumTypeOfComfortAnonymous(String location) {

		// location = Tools.Escape(location);

		String sql = "SELECT Comfort_Level, Comfort_Intensity,Comfort_Value FROM Survey_Data sd, Comfort_Data fd WHERE sd.ID = fd.Survey_ID AND sd.Location = '"
				+ location + "' ORDER BY Comfort_Level";
		Statement st;
		ResultSet rs;
		HashMap<Comfort, ArrayList> hm = new HashMap<Comfort, ArrayList>();
		ArrayList<Integer> c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0,
				0, 0, 0));
		hm.put(Comfort.Hot, c1);
		c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Comfort.Cold, c1);
		c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Comfort.Noisy, c1);
		c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Comfort.Quite, c1);
		c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Comfort.Full, c1);
		c1 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
		hm.put(Comfort.Empty, c1);
		c1 = new ArrayList<Integer>();
		hm.put(Comfort.Comfort, c1);

		String comfort;
		int comfort_intensity = 0;
		int comfort_value = 0;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			conn.commit();
			while (rs.next()) {
				comfort = rs.getString(1);
				if (comfort.equals("Comfort")) {
					comfort_value = rs.getInt(3);
				} else {
					comfort_intensity = rs.getInt(2);
				}
				switch (Comfort.valueOf(comfort)) {
				case Hot:
					this.AddComfortIntensity(hm, Comfort.Hot, comfort_intensity);
					break;
				case Cold:
					this.AddComfortIntensity(hm, Comfort.Cold,
							comfort_intensity);
					break;
				case Noisy:
					this.AddComfortIntensity(hm, Comfort.Noisy,
							comfort_intensity);
					break;
				case Quite:
					this.AddComfortIntensity(hm, Comfort.Quite,
							comfort_intensity);
					break;
				case Full:
					this.AddComfortIntensity(hm, Comfort.Full,
							comfort_intensity);
					break;
				case Empty:
					this.AddComfortIntensity(hm, Comfort.Empty,
							comfort_intensity);
					break;
				case Comfort:
					this.AddComfortIntensity(hm, Comfort.Comfort, comfort_value);
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject jet = new JSONObject();
			jet.put("Return", "SQLException");
			return jet;
		}
		c1 = hm.remove(Comfort.Comfort);
		int value = 0;
		int size = c1.size();
		if (size == 0) {
			hm.put(Comfort.Comfort, new ArrayList<Integer>(Arrays.asList(0)));
			return new JSONObject(hm);
		}
		float average = 0;
		Iterator itr = c1.iterator();
		while (itr.hasNext()) {
			value += (Integer) itr.next();
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		System.out.println(df.format(value / size));
		average = Float.parseFloat(df.format(value / size));
		ArrayList<Float> af = new ArrayList<Float>();
		af.add(average);
		hm.put(Comfort.Comfort, af);
		JSONObject jso = new JSONObject(hm);
		return jso;
	}

	private void AddComfortIntensity(HashMap<Comfort, ArrayList> hm,
			Comfort comfort, int comfort_intensity) {
		ArrayList<Integer> al = hm.get(comfort);
		int i;
		switch (comfort_intensity) {
		case 0:
			i = al.get(0);
			i++;
			al.set(0, i);
			break;
		case 1:
			i = al.get(1);
			i++;
			al.set(1, i);
			break;
		case 2:
			i = al.get(2);
			i++;
			al.set(2, i);
			break;
		case 3:
			i = al.get(3);
			i++;
			al.set(3, i);
			break;
		case 4:
			i = al.get(4);
			i++;
			al.set(4, i);
			break;
		case 5:
			i = al.get(5);
			i++;
			al.set(5, i);
			break;
		default:
			al.add(comfort_intensity);
			break;
		}

	}

	@Override
	public JSONObject GetLocations() {
		JSONObject jso = new JSONObject();
		JSONArray jsa = new JSONArray();
		String sql = "select  DISTINCT location from Survey_Data";
		Statement st;
		ResultSet rs;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				jsa.put(rs.getString(1));
			}
			jso.put("Locations", jsa);
			return jso;
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject jsoe = new JSONObject();
			jsoe.put("Return", "SQLEXCEPTION");
		}
		return jso;
	}

	@Override
	public JSONObject GetCommentsForLocation(String location) {
		String sql = "SELECT sd.ID, sd.UserName, sd.Sent_Date, sd.Comfort_Comment, fd.Feeling, fd.Feeling_Intensity FROM Survey_Data sd JOIN Feeling_Data fd ON sd.ID=fd.Survey_ID WHERE sd.Location='"
				+ location + "' ORDER BY sd.Sent_Date DESC";
		Statement st;
		ResultSet rs;
		String idLabel = "ID";
		String unLabel = "UserName";
		String timeLabel = "Sent_Date";
		String commentLabel = "Comfort_Comment";
		String feelingLabel = "Feeling";
		String intensityLabel = "Feeling_Intensity";
		String surveyID = new String("test");
		Integer count = 1;
		HashMap<String, HashMap<String, Object>> mainMap = new HashMap<String, HashMap<String, Object>>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			conn.commit();
           
			while (rs.next()) {
				HashMap<String, Object> itemMap = new HashMap<String, Object>();

				// only one map entry per survey entry despite multiple feelings
				if (surveyID.equals(rs.getString(idLabel))) {
					Integer prevCount = count - 1;
					mainMap.get(location + "-" + prevCount.toString()).put(
							rs.getString(feelingLabel),
							rs.getInt(intensityLabel));
				} else {
					surveyID = rs.getString(idLabel);
					// getting the username and adding it to the hashmap
					itemMap.put(idLabel, rs.getString(idLabel));
					// getting the username and adding it to the hashmap
					itemMap.put(unLabel, rs.getString(unLabel));
					// formatting the timestamp and adding it to the hashmap
					String timeString = new SimpleDateFormat(
							"dd/MM/yy hh:mm:ss aaa").format(rs
							.getTimestamp(timeLabel));
					itemMap.put(timeLabel, timeString);
					// getting the comment and adding it to the hashmap
					itemMap.put(commentLabel, rs.getString(commentLabel));
					// getting the feeling and adding it to the hashmap
					itemMap.put(rs.getString(feelingLabel),
							rs.getInt(intensityLabel));
					// adding the hashmap to the list
					mainMap.put(location + "-" + count.toString(), itemMap);
					count++;
				}
			}
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			JSONObject obj = new JSONObject(mainMap);
			obj.put("Return", "SQLException");
			return obj;
		}
		JSONObject obj = new JSONObject(mainMap);
		System.out.println(obj.toString());
		return obj;

	}

	// public static void main(String[] args){
	// SurveyBO sbo = new SurveyBO();
	// System.out.println("====="+sbo.SumTypeOfFeeling("location1"));
	// }

}
