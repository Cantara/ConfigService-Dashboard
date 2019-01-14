package no.cantara.csdb.util;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.eclipse.jetty.http.HttpContent;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

import no.cantara.csdb.config.ConstantValue;
import no.cantara.csdb.errorhandling.AppException;

public class CommandResponseHandler {

	public static String handle(HttpServletResponse response, Model model, String result, byte[] raw_response, int statusCode) throws AppException {
		if (result != null) {
			
			model.addAttribute(ConstantValue.JSON_DATA, result);
		}

		else {
			String responseMsg = raw_response != null ? StringConv.UTF8(raw_response) : "N/A";
			
			throw new AppException(statusCode != 0 ? HttpStatus.valueOf(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR,
					9999, "Failed connection to the backend server. Response message: " + responseMsg, "", "");
		}

		response.setContentType(ContentType.APPLICATION_JSON.toString());
		return "json";
	}
	
	
}
