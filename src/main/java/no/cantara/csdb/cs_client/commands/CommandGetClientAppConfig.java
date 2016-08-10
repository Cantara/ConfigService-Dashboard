package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetClientAppConfig extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientAppConfig(String clientId){
		this.clientId = clientId;
	}


    @Override
    protected String getCacheKey() {
        return String.valueOf(clientId);
    }

	@Override
	protected String getTargetPath() {
        return "client/" + clientId + "/config";
    }

	
}