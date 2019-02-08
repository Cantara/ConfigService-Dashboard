package no.cantara.csdb.cs_application.commands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.util.basecommands.BasePutCommand;

public class CommandUpdateClientList extends BasePutCommand<String> {
	
	private String json;
	private String clientId;

	public CommandUpdateClientList(String json) {
		super();
		this.json = json;
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		super.dealWithRequestBeforeSend(request);
		request.contentType("application/json").send(json);
		return request;
	}
	
	@Override
	protected String getTargetPath() {
		return "client/updateClientList";
	}
	

}
