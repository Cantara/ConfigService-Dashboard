package no.cantara.csdb.cs_client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import no.cantara.cs.dto.Client;
import no.cantara.cs.dto.ClientStatus;
import no.cantara.csdb.cs_client.commands.CommandGetAWSCloudWatchLog;
import no.cantara.csdb.cs_client.commands.CommandGetAllClients;
import no.cantara.csdb.cs_client.commands.CommandGetClientAppConfig;
import no.cantara.csdb.cs_client.commands.CommandGetClientEnvironment;
import no.cantara.csdb.cs_client.commands.CommandGetClientEvents;
import no.cantara.csdb.cs_client.commands.CommandGetClientStatus;

@Service
public class ClientSessionDao {

    public String getAllClientStatuses(Set<String> ignoredClients) {
        String clientsJson = new CommandGetAllClients().execute();
        if (clientsJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<ClientStatus> clientStatusList = new ArrayList<>();
            try {
                List<Client> clients = Arrays.asList(mapper.readValue(clientsJson, Client[].class));
                for (Client client : clients) {
                    String clientStatusJson = new CommandGetClientStatus(client.clientId).execute();
                    if (clientStatusJson != null) {
                        ClientStatus clientStatus = mapper.readValue(clientStatusJson, ClientStatus.class);
                        if (clientStatus.latestClientHeartbeatData != null) {
                            if (!ignoredClients.contains(client.clientId)) {
                                clientStatusList.add(clientStatus);
                            }
                        }
                    }
                }
                return mapper.writeValueAsString(clientStatusList);

            } catch (JsonParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getClientEnvironment(String clientId) {
        return new CommandGetClientEnvironment(clientId).execute();
    }

    public String getClientStatus(String clientId) {
        return new CommandGetClientStatus(clientId).execute();
    }

    public String getClientAppConfig(String clientId) {
        return new CommandGetClientAppConfig(clientId).execute();
    }

    public String getClientEvents(String clientId) {
        return new CommandGetClientEvents(clientId).execute();
    }

    public String getClientCloudWatchLog(String clientId) {
        return new CommandGetAWSCloudWatchLog(clientId).execute();
    }
}
