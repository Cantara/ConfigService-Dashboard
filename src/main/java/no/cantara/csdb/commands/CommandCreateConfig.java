package no.cantara.csdb.commands;

import com.github.kevinsawicki.http.HttpRequest;

public class CommandCreateConfig extends BasePostCommand<String>{
	
	private String json;
	private String applicationId;
	
	public CommandCreateConfig(String applicationId, String json){
		this.json = json;
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
		return "application/" + applicationId + "/config";
	}
}
