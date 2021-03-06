package no.cantara.csdb.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.cantara.cs.dto.ClientAlias;
import no.cantara.csdb.cs_client.commands.CommandGetAllClientAliases;
import no.cantara.csdb.cs_client.commands.CommandGetIgnoredClientIds;
import no.cantara.csdb.cs_client.commands.CommandPutAignoredClient;
import no.cantara.csdb.cs_client.commands.CommandPutClientAlias;
import no.cantara.csdb.errorhandling.AppException;

import java.io.IOException;
import java.util.*;


public class CSMappingSettingsDao implements SettingsDao {

	//private Map<String, String> aliases = new HashMap<>();
	//private Set<String> ignoredClients = new HashSet<String>();
	    
	@Override
	public Map<String, String> getAliases() {
//		if(aliases!=null) {
//			Map<String, String> m = new HashMap<String, String>(); 
//			m.putAll(aliases);
//			return m;
//		}
		Map<String, String> aliases = new HashMap<>();
		String clientsJson = new CommandGetAllClientAliases().execute();
		if (clientsJson != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				List<ClientAlias> cal = Arrays.asList(mapper.readValue(clientsJson, ClientAlias[].class));

				for(ClientAlias ca : cal) {
					aliases.put(ca.clientId, ca.clientName);
				}
				
				

			} catch (IOException e) {
				e.printStackTrace();
			}


		}
		return aliases;
	}

	@Override
	public boolean addAlias(String clientId, String alias) throws AppException {
		//update in CS
		CommandPutClientAlias cmd = new CommandPutClientAlias(clientId, alias);
		cmd.execute();
		if(cmd.getStatusCode()==200) {
			//aliases.put(clientId, alias);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<String> getIgnoredClients() {
//		if(ignoredClients!=null) {
//			return new HashSet<String>(ignoredClients);
//		}
		Set<String> ignoredClients = new HashSet<String>();
		String clientIdsJson = new CommandGetIgnoredClientIds().execute();
		if (clientIdsJson != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				List<String> ids = Arrays.asList(mapper.readValue(clientIdsJson, String[].class));
				ignoredClients  = new HashSet<String>(ids);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return ignoredClients;
	}

	@Override
	public boolean setIgnoredFlag(String clientId, boolean ignore) {
		
		CommandPutAignoredClient cmd = new CommandPutAignoredClient(clientId, ignore);
		cmd.execute();
		if(cmd.getStatusCode()==200) {
			return true;
		} else {
			return false;
		}	
		
	}

	

}
