package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetClientEvents extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientEvents(String clientId){
		this.clientId = clientId;
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf(clientId);
    }

	@Override
	protected String getTargetPath() {
        return "cs_client/" + clientId + "/events";
    }

	
}
