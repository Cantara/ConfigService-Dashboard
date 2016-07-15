package no.cantara.csdb.commands;

public class CommandGetAllApplications extends BaseGetCommand<String>{
	@Override
	protected String getTargetPath() {
		return "application/";
	}
}
