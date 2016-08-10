package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetApplicationStatus extends BaseGetCommand<String> {

	private String artifactId;
	
	public CommandGetApplicationStatus(String artifactId){
		this.artifactId = artifactId;
	}
	
	@Override
	protected String getTargetPath() {
		return "cs_application/" + artifactId + "/status";
	}

	
}
