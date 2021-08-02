package embrace.devops;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenkinsOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsOperations.class);
	
	@MediaType(value = ANY, strict = false)
	@Alias("Run-Job-with-curl")
	public String runJob(@Config JenkinsConfiguration jenkinsConfig, @ParameterGroup(name= "Additional properties") JenkinsParameters JenkinsParams) throws IOException, InterruptedException {
		String response = null;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();
		String jobName = JenkinsParams.getJobname();
		
		
    	String CURL = "curl -X POST "+ protocol + host + port + "/job/" + jobName + "/build --user " + user + ":" + apiToken;

    	LOGGER.info("Jenkins job url: " + CURL);  			
    	
  		Process processRuntime = Runtime.getRuntime().exec(CURL);
  			
    	LOGGER.info("URL triggered: " + CURL);  	
  		
		InputStream ins = processRuntime.getInputStream();
		// creating a buffered reader
		BufferedReader read = new BufferedReader(new InputStreamReader(ins));
		StringBuilder sb = new StringBuilder();
		read
		.lines()
		.forEach(line -> {
			LOGGER.info("Logging: " +line);
		     sb.append(line);
		});
		// close the buffered reader
		read.close();

		processRuntime.waitFor();


		int exitCode = processRuntime.exitValue();
		LOGGER.info("exitCode: " + exitCode);
		
		// finally destroy the process
		processRuntime.destroy();

		if (exitCode==0) {
			response = "Successfully executed.";
		} else {
			response = "Error during execution with exitCode: " + exitCode;
		}
		return response;
	}
	
	@MediaType(value = ANY, strict = false)
	@Alias("Run-Job-with-rest-client")
	public String runJobHttp(@Config JenkinsConfiguration jenkinsConfig, @ParameterGroup(name= "Additional properties") JenkinsParameters JenkinsParams) throws IOException, InterruptedException {
		String response = null;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();
		String jobName = JenkinsParams.getJobname();
		
    	URL url = new URL(protocol + host + port + "/job/" + jobName + "/build");
    	LOGGER.info("Jenkins job url: " + url);  		
    	
    	URLConnection conn = url.openConnection();

    	if(conn instanceof HttpsURLConnection){
			LOGGER.info("Processing HTTPS request");
			HttpsURLConnection https = (HttpsURLConnection) conn;
	    	https.setRequestMethod("POST");
	    	https.addRequestProperty("Accept", "*/*");
	    	
	    	String userCredentials = user + ":" + apiToken;
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

	    	https.setRequestProperty ("Authorization", basicAuth);
	    	
		
	    	response = String.valueOf(https.getResponseCode());
    	} 
    	else {
    		LOGGER.info("Processing HTTP request");
        	HttpURLConnection http = (HttpURLConnection) conn;
        	http.setRequestMethod("POST");
        	http.addRequestProperty("Accept", "*/*");
        	
        	String userCredentials = user + ":" + apiToken;
        	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        	http.setRequestProperty ("Authorization", basicAuth);
        	
    	
        	response = "{ \"message\": " + String.valueOf(http.getResponseCode()) + " }";
    	}


    	
    	LOGGER.info("Jenkins job ReturnCode: " + response);
    	
		return response;
		
		
	}	

	@MediaType(value = ANY, strict = false)
	@Alias("Get-all-jobs")
	public String getAllJobs(@Config JenkinsConfiguration jenkinsConfig) throws IOException, InterruptedException {

		StringBuilder responseBodyReturn;
		int responseCode = 0;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();

		
    	URL url = new URL(protocol + host + port + "/api/json?tree=jobs[name,color]");
    	LOGGER.info("Jenkins job url: " + url);  		
    	
    	URLConnection conn = url.openConnection();

    	if(conn instanceof HttpsURLConnection){
			LOGGER.info("Processing HTTPS request");
			HttpsURLConnection https = (HttpsURLConnection) conn;
	    	https.setRequestMethod("GET");
	    	https.addRequestProperty("Accept", "*/*");
	    	
	    	String userCredentials = user + ":" + apiToken;
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

	    	https.setRequestProperty ("Authorization", basicAuth);
	    	
        	responseCode = https.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = https.getInputStream();
            } else {
                inputStream = https.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();
            
            responseBodyReturn = responseBody;

    	} 
    	else {
    		LOGGER.info("Processing HTTP request");
        	HttpURLConnection http = (HttpURLConnection) conn;
        	http.setRequestMethod("GET");
        	http.addRequestProperty("Accept", "*/*");
        	
        	String userCredentials = user + ":" + apiToken;
        	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        	http.setRequestProperty ("Authorization", basicAuth);
        	
    	
        	responseCode = http.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();

            responseBodyReturn = responseBody;
    	}

    	

    	
    	LOGGER.info("Jenkins job ReturnCode: " + String.valueOf(responseCode));
    	

		return responseBodyReturn.toString();
		
		
	}	

	@MediaType(value = ANY, strict = false)
	@Alias("Get-last-build-info")
	public String getLastBuild(@Config JenkinsConfiguration jenkinsConfig, @ParameterGroup(name= "Additional properties") JenkinsParameters JenkinsParams) throws IOException, InterruptedException {

		StringBuilder responseBodyReturn;
		int responseCode = 0;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();
		String jobName = JenkinsParams.getJobname();
		
    	URL url = new URL(protocol + host + port + "/job/" + jobName + "/lastBuild/api/json");
    	LOGGER.info("Jenkins job url: " + url);  		
    	
    	URLConnection conn = url.openConnection();

    	if(conn instanceof HttpsURLConnection){
			LOGGER.info("Processing HTTPS request");
			HttpsURLConnection https = (HttpsURLConnection) conn;
	    	https.setRequestMethod("GET");
	    	https.addRequestProperty("Accept", "*/*");
	    	
	    	String userCredentials = user + ":" + apiToken;
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

	    	https.setRequestProperty ("Authorization", basicAuth);
	    	
        	responseCode = https.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = https.getInputStream();
            } else {
                inputStream = https.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();
            
            responseBodyReturn = responseBody;

    	} 
    	else {
    		LOGGER.info("Processing HTTP request");
        	HttpURLConnection http = (HttpURLConnection) conn;
        	http.setRequestMethod("GET");
        	http.addRequestProperty("Accept", "*/*");
        	
        	String userCredentials = user + ":" + apiToken;
        	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        	http.setRequestProperty ("Authorization", basicAuth);
        	
    	
        	responseCode = http.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();

            responseBodyReturn = responseBody;
    	}

    	

    	
    	LOGGER.info("Jenkins job ReturnCode: " + String.valueOf(responseCode));
    	

		return responseBodyReturn.toString();
		
		
	}	

	@MediaType(value = ANY, strict = false)
	@Alias("Get-all-builds")
	public String getAllBuilds(@Config JenkinsConfiguration jenkinsConfig, @ParameterGroup(name= "Additional properties") JenkinsParameters JenkinsParams) throws IOException, InterruptedException {

		StringBuilder responseBodyReturn;
		int responseCode = 0;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();
		String jobName = JenkinsParams.getJobname();
		
    	URL url = new URL(protocol + host + port + "/job/" + jobName + "/api/json?tree=builds[number,status,timestamp,id,result]");
    	LOGGER.info("Jenkins job url: " + url);  		
    	
    	URLConnection conn = url.openConnection();

    	if(conn instanceof HttpsURLConnection){
			LOGGER.info("Processing HTTPS request");
			HttpsURLConnection https = (HttpsURLConnection) conn;
	    	https.setRequestMethod("GET");
	    	https.addRequestProperty("Accept", "*/*");
	    	
	    	String userCredentials = user + ":" + apiToken;
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

	    	https.setRequestProperty ("Authorization", basicAuth);
	    	
        	responseCode = https.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = https.getInputStream();
            } else {
                inputStream = https.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();
            
            responseBodyReturn = responseBody;

    	} 
    	else {
    		LOGGER.info("Processing HTTP request");
        	HttpURLConnection http = (HttpURLConnection) conn;
        	http.setRequestMethod("GET");
        	http.addRequestProperty("Accept", "*/*");
        	
        	String userCredentials = user + ":" + apiToken;
        	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        	http.setRequestProperty ("Authorization", basicAuth);
        	
    	
        	responseCode = http.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();

            responseBodyReturn = responseBody;
    	}

    	

    	
    	LOGGER.info("Jenkins job ReturnCode: " + String.valueOf(responseCode));
    	

		return responseBodyReturn.toString();
		
		
	}	

	@MediaType(value = ANY, strict = false)
	@Alias("Get-last-build-console-text")
	public String getLastBuildConsoleText(@Config JenkinsConfiguration jenkinsConfig, @ParameterGroup(name= "Additional properties") JenkinsParameters JenkinsParams) throws IOException, InterruptedException {

		StringBuilder responseBodyReturn;
		int responseCode = 0;
		
		String protocol = jenkinsConfig.getProtocol().equals("https") ? "https://"  : "http://";
		String host = jenkinsConfig.getHost();
		String port = jenkinsConfig.getPort().isEmpty() ? "" : ":" + jenkinsConfig.getPort();
		String user = jenkinsConfig.getUser();
		String apiToken = jenkinsConfig.getApiToken();
		String jobName = JenkinsParams.getJobname();
		
    	URL url = new URL(protocol + host + port + "/job/" + jobName + "/lastBuild/consoleText");
    	LOGGER.info("Jenkins job url: " + url);  		
    	
    	URLConnection conn = url.openConnection();

    	if(conn instanceof HttpsURLConnection){
			LOGGER.info("Processing HTTPS request");
			HttpsURLConnection https = (HttpsURLConnection) conn;
	    	https.setRequestMethod("GET");
	    	https.addRequestProperty("Accept", "*/*");
	    	
	    	String userCredentials = user + ":" + apiToken;
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

	    	https.setRequestProperty ("Authorization", basicAuth);
	    	
        	responseCode = https.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = https.getInputStream();
            } else {
                inputStream = https.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();
            
            responseBodyReturn = responseBody;

    	} 
    	else {
    		LOGGER.info("Processing HTTP request");
        	HttpURLConnection http = (HttpURLConnection) conn;
        	http.setRequestMethod("GET");
        	http.addRequestProperty("Accept", "*/*");
        	
        	String userCredentials = user + ":" + apiToken;
        	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        	http.setRequestProperty ("Authorization", basicAuth);
        	
    	
        	responseCode = http.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = http.getInputStream();
            } else {
                inputStream = http.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    inputStream));

            StringBuilder responseBody = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null) 
            	responseBody.append(currentLine);

            in.close();

            responseBodyReturn = responseBody;
    	}

    	

    	
    	LOGGER.info("Jenkins job ReturnCode: " + String.valueOf(responseCode));
    	

		return responseBodyReturn.toString();
		
		
	}	


}
