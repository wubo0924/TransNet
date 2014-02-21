package com.transnet.survey;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

public interface ISurveyBO {
	public String SaveSurveyDistinctive(String timestamp,String location,String feeling,String username,String token); 
	public String SaveSurveyAnonymous(String timestamp,String location,String feeling);
	
	public String SaveComfortLevelDistinctive(String surveyID, String comfort,String comment,String username,String token);
	public String SaveComfortLevelAnonymouse(String surveyID, String comfort,String comment);
    
	public String Login(String username, String pass);
	public String CreateUser(String username,String password,String email,String gender,String age,String occupation);
	
	public JSONObject SumTypeOfFeelingDistinctive(String location,String username,String token);
	public JSONObject SumTypeOfFeelingAnonymous(String location);
	
	public JSONObject SumTypeOfComfortDistinctive(String location,String username,String token);
	public JSONObject SumTypeOfComfortAnonymous(String location);
	
	public JSONObject GetLocations();
	
	public JSONObject GetCommentsForLocation(String location);
	
	//public JSONObject SumTypeOfFeelingJSON(String location);
	
	//public String generateSurveyID();
	//public String generateFeelingID();
	
}
