package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetRemoveThisAppConfigCheck extends BaseGetCommand<String> {
	
	String configId;
	
	public CommandGetRemoveThisAppConfigCheck(String configId) {
		super();
		this.configId = configId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/canDeleteAppConfig/" + configId;
	}
}
