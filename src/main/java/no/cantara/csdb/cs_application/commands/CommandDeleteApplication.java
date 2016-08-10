package no.cantara.csdb.cs_application.commands;


import no.cantara.csdb.util.basecommands.BaseDeleteCommand;

public class CommandDeleteApplication extends BaseDeleteCommand<String> {
	
	private String json;
	private String applicationId;

	
	public CommandDeleteApplication(String applicationId){
		this.applicationId = applicationId;
	}
	
	@Override
	protected String getTargetPath() {
		return "cs_application/" + applicationId;
	}
	
	@Override
	protected String dealWithFailedResponse(String responseBody, int statusCode) {
		if(statusCode==404){
			return statusCode + ": Aplication Not found";	
		}
		return statusCode + ":" + responseBody;
	}
	
	@Override
	protected String dealWithResponse(String response) {
		return "200" + ":" + super.dealWithResponse(response);
	}
	
}
