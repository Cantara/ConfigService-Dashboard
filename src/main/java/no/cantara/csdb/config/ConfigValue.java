package no.cantara.csdb.config;

import no.cantara.csdb.util.Configuration;

public class ConfigValue {
    public final static String CONFIGSERVICE_URL = Configuration.getString("configservice.url");
    public final static String CONFIGSERVICE_USERNAME = Configuration.getString("configservice.username");
    public final static String CONFIGSERVICE_PASSWORD = Configuration.getString("configservice.password");
    public final static String LOGIN_READ_USERNAME = Configuration.getString("login.user");
    public final static String LOGIN_READ_PASSWORD = Configuration.getString("login.password");
    public final static String LOGIN_ADMIN_USERNAME = Configuration.getString("login.admin.user");
    public final static String LOGIN_ADMIN_PASSWORD = Configuration.getString("login.admin.password");
    public final static String MAPDB_PATH = Configuration.getString("mapdb.path");
    public final static String SERVICE_CONTEXT = "/dashboard";
    public final static int SERVICE_PORT = Configuration.getInt("service.port", 8087);
}
