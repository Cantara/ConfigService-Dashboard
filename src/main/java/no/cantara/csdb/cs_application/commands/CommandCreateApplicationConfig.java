package no.cantara.csdb.cs_application.commands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.util.basecommands.BasePostCommand;

public class CommandCreateApplicationConfig extends BasePostCommand<String> {
	
	private String json;
	private String applicationId;

	public CommandCreateApplicationConfig(String applicationId, String json) {
		super();
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
