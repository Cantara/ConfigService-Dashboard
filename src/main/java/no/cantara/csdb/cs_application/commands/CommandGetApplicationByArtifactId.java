package no.cantara.csdb.cs_application.commands;

import no.cantara.csdb.util.basecommands.BaseGetCommand;

public class CommandGetApplicationByArtifactId extends BaseGetCommand<String> {
	
	String artifactId;
	
	public CommandGetApplicationByArtifactId(String artifactId) {
		super();
		this.artifactId = artifactId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + artifactId;
	}
}
