package com.test.freelance.rest;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

public class TestRestClientWithOauth {

	
	private static final String CONSUMER_KEY="xl5fOHDjeFRvC57tgo6virwkF";
	private static final String CONSUMER_SECRET="arXnzXQL9nTcP0dHPdCJ4hRx473gMdqUIgswuVee9xYKNCPHtW";
	private static final String URL ="https://api.twitter.com/1.1/search/tweets.json";
	private Client client;
	private WebResource webResource;
	
	
	@BeforeClass
	public void init(){
		
		 client = Client.create();
		 webResource = client
				   .resource(URL);
		
		OAuthSecrets secrets = new OAuthSecrets().consumerSecret(CONSUMER_SECRET);
      OAuthParameters params = new OAuthParameters().consumerKey(CONSUMER_KEY).
              signatureMethod("HMAC-SHA1").version("1.0");
      // Create the OAuth client filter
      OAuthClientFilter filter =
              new OAuthClientFilter(client.getProviders(), params, secrets);
      // Add the filter to the resource
      webResource.addFilter(filter);
		
      
      
		
	}
	
	
	@Test
	public void testTwitterApi() throws JSONException{
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	      queryParams.add("q", "arnab");
	      
			ClientResponse response = webResource.queryParams(queryParams).accept("application/json")
	                   .get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus() +" message" + response.getEntity(String.class));
			}

			String output = response.getEntity(String.class);

			validateText(output,"arnab");
		 

		}
	
	public void validateText(String jsonOutput,String textToValidate) throws JSONException{
		
		JSONObject object = new JSONObject(jsonOutput);
		JSONArray array =  object.getJSONArray("statuses");
		JSONObject text = array.getJSONObject(0);
		
		Assert.assertTrue("Verify the text in the twitter message",((String)text.get("text")).contains(textToValidate));
		
	}
	
}
