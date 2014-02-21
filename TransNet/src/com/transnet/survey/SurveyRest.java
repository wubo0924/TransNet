package com.transnet.survey;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;


@Path(value="/getjson")
public class SurveyRest extends SurveyBO{
	 @GET
	 //@Produces("text/plain")
	 @Path("/SumTypeOfFeelingAnonymous")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject SumTypeOfFeelingAnonymousGET(@QueryParam("location") String location){
		 //JSONWithPadding jsp = new JSONWithPadding(super.SumTypeOfFeelingAnonymous(location));
		 return super.SumTypeOfFeelingAnonymous(location);
	}
	 
	 @POST
	 @Path("/SumTypeOfFeelingAnonymous")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfFeelingAnonymousPOST(@QueryParam("location") String location){
		 //JSONWithPadding jsp = new JSONWithPadding(super.SumTypeOfFeelingAnonymous(location));
		 return super.SumTypeOfFeelingAnonymous(location);
	}
	 
	 @PUT
	 @Path("/SumTypeOfFeelingAnonymous")
	 @Consumes( { MediaType.APPLICATION_JSON })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfFeelingAnonymousPUT(@QueryParam("location") String location){
		 //JSONWithPadding jsp = new JSONWithPadding(super.SumTypeOfFeelingAnonymous(location));
		 return super.SumTypeOfFeelingAnonymous(location);
	}
	/*********************************/ 
	 
	 @GET
	 @Path("/SumTypeOfFeelingDistinctive")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject SumTypeOfFeelingDistinctiveGET(@QueryParam("location") String location,@QueryParam("username") String username,@QueryParam("token") String token){
		
		return super.SumTypeOfFeelingDistinctive(location, username, token);
	}
	 @POST
	 @Path("/SumTypeOfFeelingDistinctive")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfFeelingDistinctivePOST(@QueryParam("location") String location,@QueryParam("username") String username,@QueryParam("token") String token){
		
		 return super.SumTypeOfFeelingDistinctive(location, username, token);
	}
	 @PUT
	 @Path("/SumTypeOfFeelingDistinctive")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfFeelingDistinctivePUT(@QueryParam("location") String location,@QueryParam("username") String username,@QueryParam("token") String token){
		
		 return super.SumTypeOfFeelingDistinctive(location, username, token);
	}
	 /*********************************/ 
	
	 @GET
	 //@Produces("text/plain")
	 @Path("/SumTypeOfComfortAnonymous")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject SumTypeOfComfortAnonymousGET(@QueryParam("location") String location){
		 //JSONWithPadding jsp = new JSONWithPadding(super.SumTypeOfFeelingAnonymous(location));
		 return super.SumTypeOfComfortAnonymous(location);
	}
	 
	 @POST
	 @Path("/SumTypeOfComfortAnonymous")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfComfortAnonymousPOST(@QueryParam("location") String location){
		 //JSONWithPadding jsp = new JSONWithPadding(super.SumTypeOfFeelingAnonymous(location));
		 return super.SumTypeOfComfortAnonymous(location);
	}
	 
	 @PUT
	 @Path("/SumTypeOfComfortAnonymous")
	 @Consumes( { MediaType.APPLICATION_JSON })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfComfortAnonymousPUT(@QueryParam("location") String location){
		 return super.SumTypeOfComfortAnonymous(location);
	}
	 /*********************************/ 
		
	 @GET
	 //@Produces("text/plain")
	 @Path("/SumTypeOfComfortDistinctive")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject SumTypeOfComfortDistinctiveGET(@QueryParam("location") String location,@QueryParam("username") String username, @QueryParam("token") String token){
		 return super.SumTypeOfComfortDistinctive(location, username, token);
	}
	 
	 @POST
	 @Path("/SumTypeOfComfortDistinctive")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfComfortDistinctivePOST(@QueryParam("location") String location,@QueryParam("username") String username, @QueryParam("token") String token){
		 return super.SumTypeOfComfortDistinctive(location, username, token);
	}
	 
	 @PUT
	 @Path("/SumTypeOfComfortDistinctive")
	 @Consumes( { MediaType.APPLICATION_JSON })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject SumTypeOfComfortDistinctivePUT(@QueryParam("location") String location,@QueryParam("username") String username, @QueryParam("token") String token){
		 return super.SumTypeOfComfortDistinctive(location, username, token);
	}
	 
	 /*********************************/
	 @GET
	 //@Produces("text/plain")
	 @Path("/GetLocations")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject GetLocationsGET(){
		return super.GetLocations();
	}
	 
	 @POST
	 @Path("/GetLocations")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject GetLocationsPOST(){
		return super.GetLocations();
	}
	 
	 @PUT
	 @Path("/GetLocations")
	 @Consumes( { MediaType.APPLICATION_JSON })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject GetLocationsPUT(){
		return super.GetLocations();
	}

	 /*********************************/
	 @GET
	 //@Produces("text/plain")
	 @Path("/GetCommentsForLocation")
	 @Produces(MediaType.APPLICATION_JSON)
	public JSONObject GetCommentsForLocationGET(@QueryParam("location") String location){
		return super.GetCommentsForLocation(location);
	}
	 
	 @POST
	 @Path("/GetCommentsForLocation")
	 @Consumes( { MediaType.APPLICATION_FORM_URLENCODED })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject GetCommentsForLocationPOST(@QueryParam("location") String location){
		 return super.GetCommentsForLocation(location);
	}
	 
	 @PUT
	 @Path("/GetCommentsForLocation")
	 @Consumes( { MediaType.APPLICATION_JSON })
	 @Produces( { MediaType.APPLICATION_JSON })
	public JSONObject GetCommentsForLocationPUT(@QueryParam("location") String location){
		 return super.GetCommentsForLocation(location);
	}

}
