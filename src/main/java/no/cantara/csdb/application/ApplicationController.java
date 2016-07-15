package no.cantara.csdb.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.cantara.csdb.config.ConstantValue;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
        } catch (Exception e) {
          
        }
        return "json";
    }
}
