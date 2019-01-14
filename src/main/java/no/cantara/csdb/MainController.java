package no.cantara.csdb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.errorhandling.AppException;
import no.cantara.csdb.errorhandling.AppExceptionCode;

@Controller
public class MainController {

   
    @Autowired
    public MainController() {
      
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
	public String createConfig(@RequestBody String json, HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException, AppException {
		JSONObject obj = new JSONObject();
		obj.put("success", false);

		String role = getLoginRole(json);
		if(role !=null){
			obj.put("success", true);
			obj.put("role", role);
		} else {
			throw AppExceptionCode.USER_UNAUTHORIZED_6000;
		}

        model.addAttribute(ConstantValue.JSON_DATA, obj.toString());
        return "json";
    }
	
	 public String getLoginRole(String json) throws JSONException {
	        JSONObject obj = new JSONObject(json);
	        String username = obj.getString("username");
	        String password = obj.getString("password");
	        if (username.equals(ConfigValue.LOGIN_ADMIN_USERNAME) && password.equals(ConfigValue.LOGIN_ADMIN_PASSWORD)) {
	            return Main.ADMIN_ROLE;
	        } else if (username.equals(ConfigValue.LOGIN_READ_USERNAME) && password.equals(ConfigValue.LOGIN_READ_PASSWORD)) {
	            return Main.USER_ROLE;
	        } else {
	            return null;
	        }
	    }
	
	
}

