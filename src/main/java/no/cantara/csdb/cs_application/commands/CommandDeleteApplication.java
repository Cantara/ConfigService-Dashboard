package no.cantara.csdb.cs_application.commands;


import no.cantara.csdb.util.basecommands.BaseDeleteCommand;

public class CommandDeleteApplication extends BaseDeleteCommand<String> {

	private String applicationId;

	
	public CommandDeleteApplication(String applicationId){
		this.applicationId = applicationId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + applicationId;
	}
	
	
}
