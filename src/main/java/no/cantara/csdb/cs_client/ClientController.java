package no.cantara.csdb.cs_client;

import java.util.Map;

import no.cantara.csdb.Main;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.cs_application.ApplicationSessionDao;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Controller
@RequestMapping("/client")
public class ClientController {


	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getAllClientStatuses(HttpServletRequest request, HttpServletResponse response, Model model) {

		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getAllClientStatuses();
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/env/", method = RequestMethod.GET)
	public String getClientEnvironment(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getClientEnvironment(clientId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/status/", method = RequestMethod.GET)
	public String getClientStatus(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getClientStatus(clientId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}



	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/config/", method = RequestMethod.GET)
	public String getClientAppConfig(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getClientAppConfig(clientId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/events/", method = RequestMethod.GET)
	public String getClientEvents(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getClientEvents(clientId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{clientId}/cloudwatchlog/", method = RequestMethod.GET)
	public String getCloudWatchLog(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ClientSessionDao.instance.getClientCloudWatchLog(clientId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}

	private void toResult(Model model, String jsonResult) {
		if(jsonResult!=null){
			model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
		} else {
			model.addAttribute(ConstantValue.JSON_DATA, "");
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/aliasmap", method = RequestMethod.GET)
	public String getAliasMap(HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonResult = mapper.writeValueAsString(ClientSessionDao.instance.getAliasMap());
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";

	}


	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/alias/{clientId}/{alias}", method = RequestMethod.POST)
	public String setAllias(@PathVariable("clientId") String clientId, @PathVariable("alias") String alias, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException {

		JSONObject obj = new JSONObject();
		obj.put("success", false);
		if(Main.isAdmin(request)){
			try {
				ClientSessionDao.instance.saveAlias(clientId, alias);
				obj.put("success", true);
			}  catch (Exception e) {
				obj.put("message", "500, Internal Server Error");
			}	
		} else {
			obj.put("message", "401, Unauthorized");
		}
		toResult(model, obj.toString());
		return "json";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/ignore/{clientId}", method = RequestMethod.POST)
	public String ignoreClient(@PathVariable("clientId") String clientId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException {

		JSONObject obj = new JSONObject();
		obj.put("success", false);
		if(Main.isAdmin(request)){
			try {
				ClientSessionDao.instance.ignoreClient(clientId);
				obj.put("success", true);
			}  catch (Exception e) {
				obj.put("message", "500, Internal Server Error");
			}	
		} else {
			obj.put("message", "401, Unauthorized");
		}
		toResult(model, obj.toString());
		return "json";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/ignoredClients", method = RequestMethod.GET)
	public String getIgnoredClients(HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonResult = mapper.writeValueAsString(ClientSessionDao.instance.getIngoredClients());
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";

	}

}

