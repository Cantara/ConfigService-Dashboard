package no.cantara.csdb.cs_application;

import no.cantara.csdb.Main;
import no.cantara.csdb.cs_application.commands.*;
import no.cantara.csdb.errorhandling.AppException;
import no.cantara.csdb.errorhandling.AppExceptionCode;
import no.cantara.csdb.util.CommandResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Controller
@RequestMapping("/application")
public class ApplicationController {


	@Autowired
	public ApplicationController() {

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getAllApplications(HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetAllApplications cmd = new CommandGetAllApplications();
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String getAllApplicationConfigs(HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetAllApplicationConfigs cmd = new CommandGetAllApplicationConfigs();
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/config/{configId}", method = RequestMethod.GET)
	public String getConfigForApplicationByConfigId(@PathVariable("configId") String configId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		CommandApplicationConfigForApplicationByConfigId cmd = new CommandApplicationConfigForApplicationByConfigId(configId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/config/findartifactid/{configId}", method = RequestMethod.GET)
	public String getArtifactIdByConfigId(@PathVariable("configId") String configId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		CommandGetArtifactIdByConfigId cmd = new CommandGetArtifactIdByConfigId(configId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config", method = RequestMethod.GET)
	public String getConfigForApplication(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		CommandGetTheLatestApplicationConfigForApplication cmd = new CommandGetTheLatestApplicationConfigForApplication(applicationId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/list", method = RequestMethod.GET)
	public String getAllConfigsForApplication(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		CommandGetApplicationConfigsForApplication cmd = new CommandGetApplicationConfigsForApplication(applicationId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{artifactId}/status", method = RequestMethod.GET)
	public String getStatusForArtifactInstances(@PathVariable("artifactId") String artifactId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetApplicationStatus cmd = new CommandGetApplicationStatus(artifactId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createApplication(@RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		
		CommandCreateApplication cmd = new CommandCreateApplication(json);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config", method = RequestMethod.POST)
	public String createConfig(@PathVariable("applicationId") String applicationId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		
		if(isAdmin(request)){
			CommandCreateApplicationConfig cmd = new CommandCreateApplicationConfig(applicationId, json);
			return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());

		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/{configId}", method = RequestMethod.PUT)
	public String updateConfig(@PathVariable("applicationId") String applicationId, @PathVariable("configId") String configId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		if(isAdmin(request)){
			CommandUpdateApplicationConfig cmd = new CommandUpdateApplicationConfig(applicationId, configId, json);
			return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());

		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/config/{configId}", method = RequestMethod.DELETE)
	public String deleteConfig(@PathVariable("configId") String configId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		if(isAdmin(request)){
			CommandDeleteApplicationConfig cmd = new CommandDeleteApplicationConfig(configId);
			return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}", method = RequestMethod.DELETE)
	public String deleteApp(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		if(isAdmin(request)){
			CommandDeleteApplication cmd = new CommandDeleteApplication(applicationId);
			return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());

		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{artifactId}", method = RequestMethod.GET)
	public String getApplicationByArtifactId(@PathVariable("artifactId") String artifactId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetApplicationByArtifactId cmd = new CommandGetApplicationByArtifactId(artifactId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/canDeleteApp/{appid}", method = RequestMethod.GET)
	public String canRemoveThisApp(@PathVariable("appid") String appid, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetRemoveThisAppCheck cmd = new CommandGetRemoveThisAppCheck(appid);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/canDeleteAppConfig/{configid}", method = RequestMethod.GET)
	public String canRemoveThisApConfig(@PathVariable("configid") String configid, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		CommandGetRemoveThisAppConfigCheck cmd = new CommandGetRemoveThisAppConfigCheck(configid);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}
	
	private boolean isAdmin(HttpServletRequest request) {
		return request.isUserInRole(Main.ADMIN_ROLE);
	}

	
}
