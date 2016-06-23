package no.cantara.csdb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.config.ConstantValue;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	  @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	    @RequestMapping("/")
	    public String myapp(HttpServletRequest request, HttpServletResponse response, Model model) {
	        response.setContentType(ConstantValue.HTML_CONTENT_TYPE);
	       
	        model.addAttribute("userName", ConfigValue.CONFIGSERVICE_USERNAME);
	        
	        return "index";
	    }
	
}
