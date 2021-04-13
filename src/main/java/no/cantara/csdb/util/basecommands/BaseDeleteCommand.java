package no.cantara.csdb.util.basecommands;

import com.github.kevinsawicki.http.HttpRequest;
import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.util.HttpSender;

import java.net.URI;
import java.util.Base64;

public abstract class BaseDeleteCommand<T> extends BaseHttpDeleteHystrixCommand<T>{

	public BaseDeleteCommand(String hystrixGroupKey) {
		super(URI.create(ConfigValue.CONFIGSERVICE_URL), hystrixGroupKey, ConstantValue.COMMAND_TIMEOUT);
	}
	
	public BaseDeleteCommand() {
		super(URI.create(ConfigValue.CONFIGSERVICE_URL), "command_group", ConstantValue.COMMAND_TIMEOUT);
	}
	
	@Override
	protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
		//String usernameAndPassword = ConfigValue.CONFIGSERVICE_USERNAME + ":" + ConfigValue.CONFIGSERVICE_PASSWORD;
		//String encoded = Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
		//request.authorization("Basic " + encoded);
		//request.contentType(HttpSender.APPLICATION_JSON);
		request.basic(ConfigValue.CONFIGSERVICE_USERNAME , ConfigValue.CONFIGSERVICE_PASSWORD);
		return super.dealWithRequestBeforeSend(request);
	}
}
