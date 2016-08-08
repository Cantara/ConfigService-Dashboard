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
}
