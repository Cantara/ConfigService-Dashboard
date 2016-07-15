package no.cantara.csdb.commands;

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
