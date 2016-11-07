package com.test.freelance.rest;

import java.io.File;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestUsingHttpClient {
	
	
private final String GET_URL ="http://jsonplaceholder.typicode.com/posts/1";
private final String POST_URL ="http://jsonplaceholder.typicode.com/posts";
private String jsonString;
	



@BeforeClass
public void init(){
	
	
	File jsonfile = new File("./src/test/resources/RequestFiles/file1.json");
	try {
		jsonString = FileUtils.readFileToString(jsonfile);
	} catch (IOException e) {
		throw new RuntimeException("Could not read the json file", e);
	}
}


/*
 * Testing a get call and verifying Id
 * 
 */
//@Test
public void test() throws ClientProtocolException, IOException, JSONException{	
	
	
	HttpGet request = new HttpGet(GET_URL);

	// add request header
	
	HttpResponse response = HttpClientWrapper.executeOnDefaultClient(request);

	validateResponseCode(response, 200);

	String s =EntityUtils.toString(response.getEntity());
	
	validateId(s,1);

}


/*
 * Testing a post call and verifying title.
 */
@Test
public void testPost() throws ParseException, IOException, JSONException{
	
	
	HttpPost post = new HttpPost(POST_URL);
	post.setEntity(new StringEntity(String.format(jsonString,1,2),"UTF-8"));
	HttpResponse response = HttpClientWrapper.executeOnDefaultClient(post);
	

	
	validateResponseCode(response,201);
	
	HttpGet request = new HttpGet(GET_URL);
	HttpResponse getRes = HttpClientWrapper.executeOnDefaultClient(request);

	validateResponseCode(getRes, 200);

	String s =EntityUtils.toString(getRes.getEntity());
	
	validateTitle(s,"silence");
	
}






/*
 * Method to validate the id from json String
 */
public void validateId(String jsonOutput,Integer identity) throws JSONException{
	
	JSONObject object = new JSONObject(jsonOutput);
	int id =  object.getInt("userId");
	
	Assert.assertTrue("Verify the text in the twitter message",identity == id);
	
}

/*
 * Method to validate the title from json String
 */
public void validateTitle(String jsonOutput,String title) throws JSONException{
	
	JSONObject object = new JSONObject(jsonOutput);
	String jtitle =  object.getString("title");
	
	Assert.assertTrue("Verify the text in the twitter message",title.equals(jtitle));
	
}

public void validateResponseCode(HttpResponse response, int code) throws ParseException, IOException{
	
	if (response.getStatusLine().getStatusCode() != code) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatusLine().getStatusCode()+" message" + EntityUtils.toString(response.getEntity()));
		}
}

}
