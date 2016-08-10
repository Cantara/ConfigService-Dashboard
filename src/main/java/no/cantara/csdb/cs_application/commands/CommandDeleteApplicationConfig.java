package no.cantara.csdb.cs_application.commands;


import no.cantara.csdb.util.basecommands.BaseDeleteCommand;

public class CommandDeleteApplicationConfig extends BaseDeleteCommand<String> {
	
	private String json;
	private String applicationId;
	private String configId;
	
	public CommandDeleteApplicationConfig(String applicationId, String configId){
		this.configId = configId;
		this.applicationId = applicationId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + applicationId + "/config/" + configId;
	}
	
	@Override
	protected String dealWithFailedResponse(String responseBody, int statusCode) {
		return statusCode + ":" + responseBody;
	}
	
	@Override
	protected String dealWithResponse(String response) {
		return "200" + ":" + super.dealWithResponse(response);
	}
	
}
