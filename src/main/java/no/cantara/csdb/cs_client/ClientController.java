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
import no.cantara.csdb.cs_client.commands.CommandGetAllClientStatuses;
import no.cantara.csdb.cs_client.commands.CommandGetAllClients;
import no.cantara.csdb.cs_client.commands.CommandGetClient;
import no.cantara.csdb.cs_client.commands.CommandGetClientAppConfig;
import no.cantara.csdb.cs_client.commands.CommandGetClientEnvironment;
import no.cantara.csdb.cs_client.commands.CommandGetClientEvents;
import no.cantara.csdb.cs_client.commands.CommandGetClientHeartBeatData;
import no.cantara.csdb.cs_client.commands.CommandGetClientStatus;
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
    	CommandGetAllClientStatuses cmd = new CommandGetAllClientStatuses();
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(),cmd.getStatusCode());
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
		CommandGetClientStatus cmd = new CommandGetClientStatus(clientId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(),cmd.getStatusCode());
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

