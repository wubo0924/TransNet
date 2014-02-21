package com.transnet.common.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {
	private DBManager(){
		
	}
	public static boolean CheckUserName(String username){
		Connection conn = DatabaseBridge.getInstance().getConnectionMYSQL();
		String sql = "select UserName from User_Details where UserName=?";
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()){
				conn.close();
				return true;
			}
			else{
				conn.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean CompareToken(String username,String token){
		Connection conn = DatabaseBridge.getInstance().getConnectionMYSQL();
		String sql = "select Password from User_Details where UserName=?";
		PreparedStatement ps;
		ResultSet rs;
		String password;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()){
				password = rs.getString(1);
				if(token.equals(Tools.getCheckSum(username, password))){
					conn.close();
					return true;
				}else{
					conn.close();
					return false;
				}
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}
