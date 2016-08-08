package no.cantara.csdb.commands;

import com.github.kevinsawicki.http.HttpRequest;

public class CommandCreateApplication extends BasePostCommand<String>{
	
	private String json;
	
	public CommandCreateApplication(String json){
		this.json = json;
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		super.dealWithRequestBeforeSend(request);
		request.contentType("application/json").send(json);
		return request;
	}
	
	@Override
	protected String getTargetPath() {
		return "application/";
	}
}
