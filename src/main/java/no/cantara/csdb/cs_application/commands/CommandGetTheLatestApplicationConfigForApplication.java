package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetTheLatestApplicationConfigForApplication extends BaseGetCommand<String> {

	private String applicationId;

    public CommandGetTheLatestApplicationConfigForApplication(String applicationId) {
    	super();
        this.applicationId = applicationId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + applicationId + "/config";
	}


}
