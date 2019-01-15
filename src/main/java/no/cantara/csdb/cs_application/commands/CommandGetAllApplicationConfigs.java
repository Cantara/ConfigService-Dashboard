package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetAllApplicationConfigs extends BaseGetCommand<String> {
	
	public CommandGetAllApplicationConfigs(){
		super();
	}
	
	@Override
	protected String getTargetPath() {
		return "application/config";
	}
}
