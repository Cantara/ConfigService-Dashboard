package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetAllApplications extends BaseGetCommand<String> {
	
	public CommandGetAllApplications() {
		super();
	}
	
	@Override
	protected String getTargetPath() {
		return "application/";
	}
}
