package no.cantara.csdb.cs_client.commands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetClientEvents extends BaseGetCommand<String> {

	private String clientId;

	public CommandGetClientEvents(String clientId) {
		super();
		this.clientId = clientId;
	}

	@Override
	protected String getCacheKey() {
		return String.valueOf(clientId);
	}

	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		super.dealWithRequestBeforeSend(request);
		//request.accept(MediaType.APPLICATION_JSON);
		return request;
	}

	@Override
	protected String getTargetPath() {
		return "client/" + clientId + "/events";
	}


}
