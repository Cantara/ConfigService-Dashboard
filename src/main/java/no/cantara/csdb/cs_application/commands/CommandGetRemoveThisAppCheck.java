package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetRemoveThisAppCheck extends BaseGetCommand<String> {
	
	String appId;
	
	public CommandGetRemoveThisAppCheck(String appId) {
		super();
		this.appId = appId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/canDeleteApp/" + appId;
	}
}
