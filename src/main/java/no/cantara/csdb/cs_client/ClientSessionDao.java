package no.cantara.csdb.cs_client;


import no.cantara.cs.dto.Client;
import no.cantara.cs.dto.ClientStatus;
import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.cs_client.commands.*;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

@Service
public class ClientSessionDao {

    private final Map<String, String> aliasMap;
    private final NavigableSet<String> ignoreClientList;
    private DB db;

    private ClientSessionDao() {
        File mapDbPathFile = new File(ConfigValue.CLIENT_ALIAS_DBFILE);
        mapDbPathFile.getParentFile().mkdirs();
        db = DBMaker.newFileDB(mapDbPathFile).make();
        this.aliasMap = db.getHashMap("aliasMap");
        this.ignoreClientList = db.getTreeSet("ignoreClientList");
    }

    public String getAllClientStatuses() {
        String clientsJson = new CommandGetAllClients().execute();
        if (clientsJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<ClientStatus> clientStatusList = new ArrayList<ClientStatus>();
            try {
                List<Client> clients = Arrays.asList(mapper.readValue(clientsJson, Client[].class));
                for (Client client : clients) {
                    String clientStatusJson = new CommandGetClientStatus(client.clientId).execute();
                    if (clientStatusJson != null) {
                        ClientStatus clientStatus = mapper.readValue(clientStatusJson, ClientStatus.class);
                        if (clientStatus.latestClientHeartbeatData != null) {
                            if (!ignoreClientList.contains(client.clientId)) {
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

    public Map<String, String> getAliasMap() {
        return aliasMap;
    }

    public void saveAlias(String clientId, String alias) {
        aliasMap.put(clientId, alias);
        db.commit();
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

    public void ignoreClient(String clientId) {
        ignoreClientList.add(clientId);
        db.commit();
    }

    public String[] getIgnoredClients() {
        return ignoreClientList.toArray(new String[0]);
    }
}
