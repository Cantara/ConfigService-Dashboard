package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetClientEnvironment extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientEnvironment(String clientId){
		this.clientId = clientId;
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf(clientId);
    }

	@Override
	protected String getTargetPath() {
        return "cs_client/" + clientId + "/env";
    }

	
}