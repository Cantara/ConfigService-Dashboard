package no.cantara.csdb.config;

import no.cantara.csdb.util.Configuration;

public class ConfigValue {
	public static String CONFIGSERVICE_URL ="";
	public static String CONFIGSERVICE_USERNAME ="";
	public static String CONFIGSERVICE_PASSWORD ="";
	public static int SERVICE_PORT=8087;
	public static String SERVICE_CONTEXT="/dashboard";
	
	static {
		CONFIGSERVICE_PASSWORD = Configuration.getString("configservice.password");
		CONFIGSERVICE_USERNAME = Configuration.getString("configservice.username");
		CONFIGSERVICE_URL = Configuration.getString("configservice.url");
		SERVICE_PORT = Configuration.getInt("service.port", 8087);
	}
}
