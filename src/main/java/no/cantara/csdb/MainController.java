package no.cantara.csdb;

import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.cs_application.ApplicationSessionDao;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Controller
public class MainController {

    private final ApplicationSessionDao applicationSessionDao;

    @Autowired
    public MainController(ApplicationSessionDao applicationSessionDao) {
        this.applicationSessionDao = applicationSessionDao;
    }

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
        	String role = applicationSessionDao.getLoginRole(json);
            if(role !=null){
            	obj.put("success", true);
            	obj.put("role", role);
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

