package no.cantara.csdb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import no.cantara.csdb.application.ApplicationSessionDao;
import no.cantara.csdb.client.ClientSessionDao;
import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.config.ConstantValue;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

	@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	@RequestMapping("/")
	public String myapp(HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setContentType(ConstantValue.HTML_CONTENT_TYPE);

		model.addAttribute("userName", ConfigValue.CONFIGSERVICE_USERNAME);

		return "index";
	}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public String createConfig(@RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("success", false);
        try {
        	
            if(ApplicationSessionDao.instance.checkLogin(json)){
            	obj.put("success", true);
            } else {
            	obj.put("message", "401, Unauthorized");
            }
           
        } catch (Exception e) {
        	obj.put("message", "500, Internal Server Error");
        }
        
        model.addAttribute(ConstantValue.JSON_DATA, obj.toString());
        return "json";
    }
	
	
	
}
