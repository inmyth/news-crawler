package com.mbcu.nc.utils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


public class GsonUtils
{
	public static <T>T toBean(String response,  Class<T> classOfT){
		Gson gson = new Gson();		
		try {
			return gson.fromJson(response, classOfT);
		}catch (JsonSyntaxException e) {
			 return null;
		}			
	}


	
	public static String toJson(Object object){
		Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()	
		.create();
		String json = gson.toJson(object);
		return json;
	}
	
}