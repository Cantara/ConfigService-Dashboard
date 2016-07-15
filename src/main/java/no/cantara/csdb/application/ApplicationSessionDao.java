package no.cantara.csdb.application;

import no.cantara.csdb.commands.CommandGetAllApplications;
import no.cantara.csdb.commands.CommandGetApplicationStatus;
import no.cantara.csdb.commands.CommandGetClientEnvironment;
import no.cantara.csdb.commands.CommandGetConfigForApplication;



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
	
	
	
	
}
