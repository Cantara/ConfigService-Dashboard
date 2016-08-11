package no.cantara.csdb.cs_application;

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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/application")
public class ApplicationController {

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getAllApplications(HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ApplicationSessionDao.instance.getAllApplications();
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/", method = RequestMethod.GET)
	public String getConfigForApplication(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ApplicationSessionDao.instance.getConfigForApplication(applicationId);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{artifactId}/status/", method = RequestMethod.GET)
	public String getStatusForArtifactInstances(@PathVariable("artifactId") String artifactId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ApplicationSessionDao.instance.getStatusForArtifactInstances(artifactId);
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

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createApplication(@RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult;
		try {
			jsonResult = ApplicationSessionDao.instance.createApplication(json);
			toResult(model, jsonResult);
		} catch (Exception e) {

		}
		return "json";
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/", method = RequestMethod.POST)
	public String createConfig(@PathVariable("applicationId") String applicationId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult =null;
		if(Main.isAdmin(request)){

			try {
				jsonResult = ApplicationSessionDao.instance.createConfig(applicationId, json);

			} catch (Exception e) {

			}
		}
		toResult(model, jsonResult);
		return "json";
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/{configId}", method = RequestMethod.PUT)
	public String updateConfig(@PathVariable("applicationId") String applicationId, @PathVariable("configId") String configId, @RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult=null;
		if(Main.isAdmin(request)){
			try {
				jsonResult = ApplicationSessionDao.instance.updateConfig(applicationId, configId, json);

			} catch (Exception e) {

			}
		}
		toResult(model, jsonResult);
		return "json";
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}/config/{configId}", method = RequestMethod.DELETE)
	public String deleteConfig(@PathVariable("applicationId") String applicationId, @PathVariable("configId") String configId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult=null;
		if(Main.isAdmin(request)){
			try {
				jsonResult = ApplicationSessionDao.instance.deleteApplicationConfig(applicationId, configId);

			} catch (Exception e) {

			}
		}
		toResult(model, jsonResult);
		return "json";
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@RequestMapping(value = "/{applicationId}", method = RequestMethod.DELETE)
	public String deleteApp(@PathVariable("applicationId") String applicationId, HttpServletRequest request, HttpServletResponse response, Model model) {
		String jsonResult=null;
		
		
		if(Main.isAdmin(request)){
			try {
				jsonResult = ApplicationSessionDao.instance.deleteApplication(applicationId);
				
			} catch (Exception e) {

			}
		}
		toResult(model, jsonResult);
		return "json";
	}
	
	

	
}
