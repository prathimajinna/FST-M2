package activities;

import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssuredProjectActivity {
	
	RequestSpecification requestSpec;

	ResponseSpecification responseSpec;
	
	String gitHubAccessToken = "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";
	String sshKey;
	int sshId;
	String BASE_URI = "https://api.github.com";

	@BeforeClass
	public void setUp() {

		// Create request specification
		requestSpec = new RequestSpecBuilder()
				// Set content type
				.setContentType(ContentType.JSON)
				// set header as githubaccesstoken
				.addHeader("Authorization", "token " + gitHubAccessToken)
				// Set base URL
				.setBaseUri(BASE_URI)
				// Build request specification
				.build();

		responseSpec = new ResponseSpecBuilder()
				// Check response content type
				.expectContentType("application/json")
				// Check if response contains name property
				// Build response specification
				.build();
	}

	// post request
	@Test(priority = 1)
	public void addSSHKey() {
		String reqBody = "{\"title\": \"TestAPIKey\", \"key\":\"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDLVE4ykB8cdCzou+3MPpVVDynMGwID2Z2YbterzMzM/h2D0FoqjdXNpVyagN7YRJ4ciNQbwMoDmPXOwegYDTUAKqjWZ0JpAq7RVby74+Hg7N2iKfzE25H4PG/uj99Ho+nGLwh7Iw6Yn2mBwlEkoC+KQvtk+ER7mngosEmB92HKtBFj0i4Nd2bQX1aKjM83r2FT2sgtUjRZuqdgYBXo29z0LqurFCJcB2QOpFzizvKTGsjwoyNMHC1y0K2kRCPzvNZ0W4zKfu50W9JfagFhG/UsKXAPllmq+ygZXMyw4kiwP2yt4n7Y8Xd684b/bD6Ahc6zlxDJzeGzgi+KFmKwSj7yZdJqaTYmzQHNyHGZxNTV9m9gDCvZloMT10RN95UQ7iUcm7yQ4NLIF1y8euysbLnuvFevVQ7S5M/f2dauiYW+DkpNqr0yXfejAzFj4FcDgkHQIk3an8L6EUwjOF5c3j5LRIZUjE77LytkSpeGXdZK3oUydz7xj8eSzqztSvFq5cU="
				+ "\"}";
		Response response = given().spec(requestSpec) // Use requestSpec
				.body(reqBody) // Send request body
				.when().post(BASE_URI + "/user/keys"); // Send POST request

		System.out.println(response.asPrettyString());
		// Additional Assertion Use responseSpec
		response.then().spec(responseSpec).statusCode(201);

		// save sshId
		sshId = response.then().extract().path("id");

	}

	// get request
	@Test(priority = 2)
	public void getSSHKeys() {
		Response response = given().spec(requestSpec) // Use requestSpec
				.when().get(BASE_URI + "/user/keys"); // Send GET request

		// Print response
		Reporter.log(response.asString());

		// Assertions
		response.then().spec(responseSpec) // Use responseSpec
				.statusCode(200); // Additional Assertion
	}

	
	  //delete request	  
	  @Test(priority=3)
	  public void deleteSSHKeys(){
		  Response response = given().spec(requestSpec) // Use requestSpec
	                .pathParam("keyId", sshId) // Add path parameter
	                .when().get(BASE_URI +"/user/keys/{keyId}"); // Send GET request
	  
			Reporter.log(response.asString());
			
	  // Assertions 
		  response.then().spec(responseSpec) // Use responseSpec
			.statusCode(200); 
		  
	  }
	  
	 
}