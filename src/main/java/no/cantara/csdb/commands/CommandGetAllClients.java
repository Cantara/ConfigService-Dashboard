package no.cantara.csdb.commands;


public class CommandGetAllClients extends BaseGetCommand<String>{



	@Override
	protected String getTargetPath() {
		return "client/";
	}

	
}
