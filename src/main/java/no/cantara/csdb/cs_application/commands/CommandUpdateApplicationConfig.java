package no.cantara.csdb.cs_application.commands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.util.basecommands.BasePutCommand;

public class CommandUpdateApplicationConfig extends BasePutCommand<String> {
	
	private String json;
	private String applicationId;
	private String configId;

	public CommandUpdateApplicationConfig(String applicationId, String configId, String json) {
		super();
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
