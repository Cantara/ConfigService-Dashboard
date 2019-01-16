package no.cantara.csdb.cs_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.entity.ContentType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.cantara.cs.dto.Client;
import no.cantara.cs.dto.ClientEnvironment;
import no.cantara.cs.dto.ClientHeartbeatData;
import no.cantara.cs.dto.ClientStatus;
import no.cantara.csdb.Main;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.cs_application.commands.CommandUpdateClient;
import no.cantara.csdb.cs_client.commands.CommandGetAWSCloudWatchLog;
import no.cantara.csdb.cs_client.commands.CommandGetAllClientEnvironments;
import no.cantara.csdb.cs_client.commands.CommandGetAllClientHeartBeatData;
import no.cantara.csdb.cs_client.commands.CommandGetAllClients;
import no.cantara.csdb.cs_client.commands.CommandGetClient;
import no.cantara.csdb.cs_client.commands.CommandGetClientAppConfig;
import no.cantara.csdb.cs_client.commands.CommandGetClientEnvironment;
import no.cantara.csdb.cs_client.commands.CommandGetClientEvents;
import no.cantara.csdb.cs_client.commands.CommandGetClientHeartBeatData;
import no.cantara.csdb.errorhandling.AppException;
import no.cantara.csdb.errorhandling.AppExceptionCode;
import no.cantara.csdb.settings.SettingsDao;
import no.cantara.csdb.util.CommandResponseHandler;

@Controller
@RequestMapping("/client")
public class ClientController {


    private final SettingsDao settingsDao;

    @Autowired
    public ClientController(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getAllClientStatuses(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

    	String jsonResult;
    	jsonResult = getAllClientStatuses(settingsDao.getIgnoredClients());
    	return toResult(model, response, jsonResult);
		

	}
    
    String getAllClientStatuses(Set<String> ignoredClients) throws Exception {
    	List<ClientStatus> clientStatusList = new ArrayList<>();
    	ObjectMapper mapper = new ObjectMapper();
    	
        String clientsJson = new CommandGetAllClients().execute();
        String allClientHeartBeatsJson = new CommandGetAllClientHeartBeatData().execute();
        String allClientEnvsJson = new CommandGetAllClientEnvironments().execute();
        
       
        if(clientsJson!=null && allClientEnvsJson !=null && allClientHeartBeatsJson!=null) {
        	
        	List<Client> clients = Arrays.asList(mapper.readValue(clientsJson, Client[].class));
        	Map<String, ClientHeartbeatData> heartbeats = mapper.readValue(allClientHeartBeatsJson,  new TypeReference<HashMap<String,ClientHeartbeatData>>() {});
        	Map<String, ClientEnvironment> envs = mapper.readValue(allClientEnvsJson, new TypeReference<HashMap<String,ClientEnvironment>>() {});
        	
        	for (Client client : clients) {
        		if (!ignoredClients.contains(client.clientId)) {
        			ClientHeartbeatData clientHeartBeat = heartbeats.get(client.clientId);
        			if(clientHeartBeat!=null) {
        				ClientEnvironment env = envs.get(client.clientId);
        				if(env!=null) {
        					clientHeartBeat.clientName = makeUpADefaultClientName(env);
        				}
        			}
        			
					clientStatusList.add(new ClientStatus(client, clientHeartBeat));
				}
        	}         
        
        	
        }
        

        return mapper.writeValueAsString(clientStatusList);
    }

	private String makeUpADefaultClientName(ClientEnvironment env) {
		String computerName = env.envInfo.get("COMPUTERNAME");
		String localIP = "";
		for(String key : env.envInfo.keySet()) {
			if(key.startsWith("networkinterface_")) {
				localIP = env.envInfo.get(key);
				break;
			}
		}
		String wrapped_os = env.envInfo.containsKey("WRAPPER_OS")? 
				env.envInfo.get("WRAPPER_OS"): env.envInfo.get("OS");
		return computerName + " - " + localIP + " - " + wrapped_os;
	}

 
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/env", method = RequestMethod.GET)
	public String getClientEnvironment(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		
		CommandGetClientEnvironment cmd = new CommandGetClientEnvironment(clientId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(),cmd.getStatusCode());
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/status", method = RequestMethod.GET)
	public String getClientStatus(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException, IOException {
		
	
    	ObjectMapper mapper = new ObjectMapper();
    	ClientStatus status = null;
		String heartBeatJson = new CommandGetClientHeartBeatData(clientId).execute();
		String clientJson = new CommandGetClient(clientId).execute();
		String envJson = new CommandGetClientEnvironment(clientId).execute();
		
		if(heartBeatJson!=null && clientJson!=null && envJson !=null) {
			Client client = mapper.readValue(clientJson, Client.class);
			ClientHeartbeatData heartbeat = mapper.readValue(heartBeatJson, ClientHeartbeatData.class);
		    heartbeat.clientName = makeUpADefaultClientName(mapper.readValue(envJson, ClientEnvironment.class));
		    status = new ClientStatus(client, heartbeat);
		}
		
		return toResult(model, response, mapper.writeValueAsString(status));
    
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/config", method = RequestMethod.GET)
	public String getClientAppConfig(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetClientAppConfig cmd = new CommandGetClientAppConfig(clientId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/events", method = RequestMethod.GET)
	public String getClientEvents(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetClientEvents cmd = new CommandGetClientEvents(clientId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/cloudwatchlog", method = RequestMethod.GET)
	public String getCloudWatchLog(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetAWSCloudWatchLog cmd = new CommandGetAWSCloudWatchLog(clientId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	private String toResult(Model model, HttpServletResponse response, String jsonResult) {
		if(jsonResult!=null){
			model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
		} else {
			model.addAttribute(ConstantValue.JSON_DATA, "");
		}
		response.setContentType(ContentType.APPLICATION_JSON.toString());
		return "json";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/aliasmap", method = RequestMethod.GET)
	public String getAliasMap(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String jsonResult;
		ObjectMapper mapper = new ObjectMapper();
		jsonResult = mapper.writeValueAsString(settingsDao.getAliases());
		return toResult(model, response, jsonResult);
	}


	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/alias/{clientId}/{alias}", method = RequestMethod.POST)
	public String setAllias(@PathVariable("clientId") String clientId, @PathVariable("alias") String alias, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		JSONObject obj = new JSONObject();
		obj.put("success", false);
		if(isAdmin(request)){
			try {
				settingsDao.addAlias(clientId, alias);
				obj.put("success", true);
			}  catch (Exception e) {
				throw e;
			}	
		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000; 
		}
		return toResult(model, response, obj.toString());
	
	}

    @POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/ignore/{clientId}", method = RequestMethod.POST)
	public String ignoreClient(@PathVariable("clientId") String clientId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException, AppException {

		JSONObject obj = new JSONObject();
		obj.put("success", false);
		if(isAdmin(request)){
			try {
				settingsDao.setIgnoredFlag(clientId, json.contains("true") || json.contains("1"));
				obj.put("success", true);
			}  catch (Exception e) {
				throw e;
			}
		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000; 
		}
		return toResult(model, response, obj.toString());
		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/ignoredClients", method = RequestMethod.GET)
	public String getIgnoredClients(HttpServletRequest request, HttpServletResponse response, Model model) throws JsonProcessingException {
		String jsonResult;

		ObjectMapper mapper = new ObjectMapper();
		jsonResult = mapper.writeValueAsString(settingsDao.getIgnoredClients());
		return toResult(model, response, jsonResult);


	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}", method = RequestMethod.PUT)
	public String putClient(@PathVariable("clientId") String clientId, @RequestBody String jsonRequest, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {

		if(isAdmin(request)){
			CommandUpdateClient cmd = new CommandUpdateClient(clientId, jsonRequest);
			return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());

		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}
		
	}

    private boolean isAdmin(HttpServletRequest request) {
        return request.isUserInRole(Main.ADMIN_ROLE);
    }

    
}

