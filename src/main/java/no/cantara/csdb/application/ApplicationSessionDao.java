package no.cantara.csdb.application;

import no.cantara.csdb.commands.CommandCreateApplication;
import no.cantara.csdb.commands.CommandCreateConfig;
import no.cantara.csdb.commands.CommandGetAllApplications;
import no.cantara.csdb.commands.CommandGetApplicationStatus;
import no.cantara.csdb.commands.CommandGetClientEnvironment;
import no.cantara.csdb.commands.CommandGetConfigForApplication;
import no.cantara.csdb.commands.CommandUpdateConfig;



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
		String result = new CommandCreateConfig(applicationId, json).execute();
		return result;
	}

	public String updateConfig(String applicationId, String configId, String json) {
		String result = new CommandUpdateConfig(applicationId, configId, json).execute();
		return result;
	}

	public String createApplication(String json) {
		String result = new CommandCreateApplication(json).execute();
		return result;
	}
	
	
	
	
}
