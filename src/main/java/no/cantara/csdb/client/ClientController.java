package no.cantara.csdb.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.cantara.csdb.config.ConstantValue;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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

	
}
