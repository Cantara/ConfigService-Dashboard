package no.cantara.csdb.commands;

public class CommandGetClientEnvironment extends BaseGetCommand<String> {

	private String clientId;
	public CommandGetClientEnvironment(String clientId){
		this.clientId = clientId;
	}

    @Override
    protected String getCacheKey() {
        return String.valueOf(clientId);
    }

	@Override
	protected String getTargetPath() {
		return "client/" + clientId + "/env";
	}

	
}