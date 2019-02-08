package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandApplicationConfigForApplicationByConfigId extends BaseGetCommand<String> {
	
	String configId;
	
	public CommandApplicationConfigForApplicationByConfigId(String configId){
		super();
		this.configId = configId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/config/" + configId;
	}
}
