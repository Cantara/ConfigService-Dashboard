package no.cantara.csdb.commands;


public class CommandGetAllClients extends BaseGetCommand<String>{


	@Override
	protected String getCacheKey() {
		return String.valueOf(null);
	}

	@Override
	protected String getTargetPath() {
		return "client/";
	}

	
}
