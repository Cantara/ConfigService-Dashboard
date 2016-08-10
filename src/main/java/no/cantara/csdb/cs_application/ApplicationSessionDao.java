package no.cantara.csdb.cs_application;

import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.cs_application.commands.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



public enum ApplicationSessionDao {

	instance;
	private ApplicationSessionDao(){
		
	}

	public String getConfigForApplication(String applicationId) {
		String json = new CommandGetConfigForApplication(applicationId).execute();
		return json;
	}

	public String getStatusForArtifactInstances(String artifactId) {
		String json = new CommandGetApplicationStatus(artifactId).execute();
		return json;
	}

	public String getAllApplications() {
		String json = new CommandGetAllApplications().execute();
		return json;
	}

	public String createConfig(String applicationId, String json) {
		String result = new CommandCreateApplicationConfig(applicationId, json).execute();
		return result;
	}

	public String updateConfig(String applicationId, String configId, String json) {
		String result = new CommandUpdateApplicationConfig(applicationId, configId, json).execute();
		return result;
	}

	public String createApplication(String json) {
		String result = new CommandCreateApplication(json).execute();
		return result;
	}

	public String deleteApplicationConfig(String applicationId, String configId) {
		String result = new CommandDeleteApplicationConfig(applicationId, configId).execute();
		return result;
	}

	public boolean checkLogin(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		String username = obj.getString("username");
		String password = obj.getString("password");
		if(username.equals(ConfigValue.CONFIGSERVICE_USERNAME) && password.equals(ConfigValue.CONFIGSERVICE_PASSWORD)){
			return true;
		} else {
			return false;
		}
	}

	public String deleteApplication(String applicationId) {
		String result = new CommandDeleteApplication(applicationId).execute();
		return result;
	}
	
	
	
	
}
