package no.cantara.csdb.cs_client;

import no.cantara.csdb.config.ConstantValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
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
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
        } catch (Exception e) {

        }
        return "json";
    }


}
