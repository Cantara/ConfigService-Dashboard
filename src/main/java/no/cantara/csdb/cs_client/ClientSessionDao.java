package no.cantara.csdb.cs_client;


import no.cantara.cs.dto.Client;
import no.cantara.cs.dto.ClientStatus;
import no.cantara.csdb.cs_client.commands.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ClientSessionDao {

	instance;
	private ClientSessionDao(){
		
	}
	
	public String getAllClientStatuses(){
		String clientsJson = new CommandGetAllClients().execute();
		if(clientsJson!=null){
			ObjectMapper mapper = new ObjectMapper();
			List<ClientStatus> clientStatusList = new ArrayList<ClientStatus>();
			try {
				List<Client> clients = Arrays.asList(mapper.readValue(clientsJson, Client[].class));
				for(Client client: clients){
					String clientStatusJson = new CommandGetClientStatus(client.clientId).execute();
					if(clientStatusJson!=null){
						ClientStatus clientStatus = mapper.readValue(clientStatusJson, ClientStatus.class);
						clientStatusList.add(clientStatus);
					}
				}
				return  mapper.writeValueAsString(clientStatusList);

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
		String json = new CommandGetClientEnvironment(clientId).execute();
		return json;
	}

	public String getClientStatus(String clientId) {
		String json = new CommandGetClientStatus(clientId).execute();
		return json;
	}

	public String getClientAppConfig(String clientId) {
		String json = new CommandGetClientAppConfig(clientId).execute();
		return json;
		
	}

	public String getClientEvents(String clientId) {
		String json = new CommandGetClientEvents(clientId).execute();
		return json;
	}
	
	
	
}
