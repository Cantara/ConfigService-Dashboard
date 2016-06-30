package no.cantara.csdb.commands;

public class CommandGetClientStatus extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientStatus(String clientId){
		this.clientId = clientId;
	}
	
	@Override
	protected String getTargetPath() {
		return "client/" + clientId + "/status";
	}

	
}
