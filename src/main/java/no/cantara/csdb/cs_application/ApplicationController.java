package no.cantara.csdb.cs_application;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.cantara.csdb.Main;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.cs_application.commands.CommandCreateApplication;
import no.cantara.csdb.cs_application.commands.CommandCreateApplicationConfig;
import no.cantara.csdb.cs_application.commands.CommandDeleteApplication;
import no.cantara.csdb.cs_application.commands.CommandDeleteApplicationConfig;
import no.cantara.csdb.cs_application.commands.CommandGetAllApplicationConfigs;
import no.cantara.csdb.cs_application.commands.CommandGetAllApplications;
import no.cantara.csdb.cs_application.commands.CommandGetApplicationConfigsForApplication;
import no.cantara.csdb.cs_application.commands.CommandGetApplicationStatus;
import no.cantara.csdb.cs_application.commands.CommandUpdateApplicationConfig;
import no.cantara.csdb.errorhandling.AppException;
import no.cantara.csdb.errorhandling.AppExceptionCode;
import no.cantara.csdb.util.CommandResponseHandler;
import no.cantara.csdb.util.basecommands.BaseGetCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping(value = "/{applicationId}/config/", method = RequestMethod.GET)
	public String getConfigForApplication(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {		
		CommandGetApplicationConfigsForApplication cmd = new CommandGetApplicationConfigsForApplication(applicationId);
		return CommandResponseHandler.handle(response, model, cmd.execute(), cmd.getResponseBodyAsByteArray(), cmd.getStatusCode());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{artifactId}/status/", method = RequestMethod.GET)
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
	@RequestMapping(value = "/{applicationId}/config/", method = RequestMethod.POST)
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
	@RequestMapping(value = "/{applicationId}/config/{configId}", method = RequestMethod.DELETE)
	public String deleteConfig(@PathVariable("applicationId") String applicationId, @PathVariable("configId") String configId, HttpServletRequest request, HttpServletResponse response, Model model) throws AppException {
		if(isAdmin(request)){
			CommandDeleteApplicationConfig cmd = new CommandDeleteApplicationConfig(applicationId, configId);
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

	
	
	private boolean isAdmin(HttpServletRequest request) {
		return request.isUserInRole(Main.ADMIN_ROLE);
	}

	
}
