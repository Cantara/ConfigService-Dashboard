package no.cantara.csdb.commands;

import com.github.kevinsawicki.http.HttpRequest;

public class CommandUpdateConfig extends BasePutCommand<String>{
	
	private String json;
	private String applicationId;
	private String configId;
	
	public CommandUpdateConfig(String applicationId, String configId, String json){
		this.json = json;
		this.configId = configId;
		this.applicationId = applicationId;
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		super.dealWithRequestBeforeSend(request);
		request.contentType("application/json").send(json);
		return request;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + applicationId + "/config/" + configId;
	}
	
	@Override
	protected String dealWithFailedResponse(String responseBody, int statusCode) {
		if(statusCode==404){
			return statusCode + ": Not found";	
		}
		return statusCode + ":" + responseBody;
	}
	
	@Override
	protected String dealWithResponse(String response) {
		return "200" + ":" + super.dealWithResponse(response);
	}
}
