package no.cantara.csdb.cs_application.commands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.util.basecommands.BasePutCommand;

public class CommandUpdateClient extends BasePutCommand<String> {
	
	private String json;
	private String clientId;

	public CommandUpdateClient(String clientId, String json) {
		super();
		this.json = json;
		this.clientId= clientId;
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		super.dealWithRequestBeforeSend(request);
		request.contentType("application/json").send(json);
		return request;
	}
	
	@Override
	protected String getTargetPath() {
		return "client/" + clientId;
	}
	

}
