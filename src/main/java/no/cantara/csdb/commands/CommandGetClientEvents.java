package no.cantara.csdb.commands;

public class CommandGetClientEvents extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientEvents(String clientId){
		this.clientId = clientId;
	}
	
	@Override
	protected String getTargetPath() {
		return "client/" + clientId + "/events";
	}

	
}
