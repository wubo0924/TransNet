package com.transnet.survey;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class SurveyRestService extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(SurveyRest.class);
		return classes;
	}

}
