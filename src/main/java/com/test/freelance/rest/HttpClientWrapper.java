package com.test.freelance.rest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientWrapper {

	private static HttpClient client;
	
	public static HttpResponse executeOnDefaultClient(HttpUriRequest request){
		
		client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try{
			response = client.execute(request);
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
		
		return response;
	}
	
	
}
