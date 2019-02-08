package no.cantara.csdb.cs_application.commands;


import no.cantara.csdb.util.basecommands.BaseDeleteCommand;

public class CommandDeleteApplicationConfig extends BaseDeleteCommand<String> {
	
	private String configId;
	
	public CommandDeleteApplicationConfig(String configId){
		super();
		this.configId = configId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/config/" + configId;
	}
	

	
}
