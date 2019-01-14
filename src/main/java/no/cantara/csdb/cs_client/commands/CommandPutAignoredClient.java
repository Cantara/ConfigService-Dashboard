package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BasePutCommand;

public class CommandPutAignoredClient extends BasePutCommand<String> {

	private String clientId;
	private boolean ignore;
	
	public CommandPutAignoredClient(String clientId, boolean ignore){
		this.clientId = clientId;
		this.ignore = ignore;		
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf("");
    }

	@Override
	protected String getTargetPath() {
        return "client/ignoredClients/" + clientId + "/" + Boolean.toString(ignore);
    }

	
}
