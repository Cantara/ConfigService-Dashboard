package no.cantara.csdb.commands;

public class CommandGetApplicationStatus extends BaseGetCommand<String> {

	private String artifactId;
	
	public CommandGetApplicationStatus(String artifactId){
		this.artifactId = artifactId;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/" + artifactId + "/status";
	}

	
}
