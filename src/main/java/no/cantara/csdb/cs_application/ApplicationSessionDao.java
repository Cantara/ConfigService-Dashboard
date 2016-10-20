package no.cantara.csdb.cs_application;

import java.util.Arrays;
import java.util.List;

import no.cantara.cs.dto.Application;
import no.cantara.cs.dto.Client;
import no.cantara.csdb.Main;
import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.cs_application.commands.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSessionDao {

    public String getConfigForApplication(String applicationId) {
        return new CommandGetApplicationConfigsForApplication(applicationId).execute();
    }

    public String getStatusForArtifactInstances(String artifactId) {
        return new CommandGetApplicationStatus(artifactId).execute();
    }

    public String getAllApplications() {
        return new CommandGetAllApplications().execute();
    }

    public String createConfig(String applicationId, String json) {
        return new CommandCreateApplicationConfig(applicationId, json).execute();
    }

    public String updateConfig(String applicationId, String configId, String json) {
        return new CommandUpdateApplicationConfig(applicationId, configId, json).execute();
    }

    public String createApplication(String json) {
        return new CommandCreateApplication(json).execute();
    }

    public String deleteApplicationConfig(String applicationId, String configId) {
        return new CommandDeleteApplicationConfig(applicationId, configId).execute();
    }

    public String getLoginRole(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        String username = obj.getString("username");
        String password = obj.getString("password");
        if (username.equals(ConfigValue.LOGIN_ADMIN_USERNAME) && password.equals(ConfigValue.LOGIN_ADMIN_PASSWORD)) {
            return Main.ADMIN_ROLE;
        } else if (username.equals(ConfigValue.LOGIN_READ_USERNAME) && password.equals(ConfigValue.LOGIN_READ_PASSWORD)) {
            return Main.USER_ROLE;
        } else {
            return null;
        }
    }

    public String deleteApplication(String applicationId) {
        return new CommandDeleteApplication(applicationId).execute();
    }
}
