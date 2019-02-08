package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetArtifactIdByConfigId extends BaseGetCommand<String> {
	
	String configId;
	
	public CommandGetArtifactIdByConfigId(String configId){
		super();
		this.configId = configId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/config/findartifactid/" + configId;
	}
}
