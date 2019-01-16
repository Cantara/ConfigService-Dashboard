package no.cantara.csdb.cs_client.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetAllClientHeartBeatData extends BaseGetCommand<String> {

	public CommandGetAllClientHeartBeatData(){
		super();
		
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf("");
    }

	@Override
	protected String getTargetPath() {
        return "client/status";
    }

	
}
