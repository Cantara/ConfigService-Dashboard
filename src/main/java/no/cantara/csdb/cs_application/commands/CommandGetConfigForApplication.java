package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetConfigForApplication extends BaseGetCommand<String> {

	private String applicationId;
	
	public CommandGetConfigForApplication(String applicationId){
		this.applicationId = applicationId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + applicationId + "/config";
	}

	
}
