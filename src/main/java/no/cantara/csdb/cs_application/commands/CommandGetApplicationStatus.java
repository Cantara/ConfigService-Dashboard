package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetApplicationStatus extends BaseGetCommand<String> {

	private String artifactId;
	
	public CommandGetApplicationStatus(String artifactId){
		super();
		this.artifactId = artifactId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + artifactId + "/status";
	}

	
}
