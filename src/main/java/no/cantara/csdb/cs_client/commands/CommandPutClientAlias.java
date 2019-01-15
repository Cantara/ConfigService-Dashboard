package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BasePutCommand;

public class CommandPutClientAlias extends BasePutCommand<String> {

	private String clientId;
	private String clientName;
	
	public CommandPutClientAlias(String clientId, String clientName){
		super();
		this.clientId = clientId;
		this.clientName = clientName;		
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf("");
    }

	@Override
	protected String getTargetPath() {
        return "client/aliases/" + clientId + "/" + clientName;
    }

	
}
