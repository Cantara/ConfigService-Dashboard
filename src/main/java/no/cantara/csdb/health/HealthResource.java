package no.cantara.csdb.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.cantara.csdb.config.ConstantValue;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.Properties;

@Controller
//@Path(HEALTH_PATH)
//@RequestMapping("/api")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    public static final String HEALTH_PATH = "/health";

    private static final Logger log = LoggerFactory.getLogger(HealthResource.class);
    private final static String MAVEN_ARTIFACT_ID = "ConfigService-Dashboard";


    @Autowired
    public HealthResource() {
    }

    public Response healthCheck() {
        log.trace("healthCheck");
        return Response.ok(getHealthTextJson()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @RequestMapping(value = HEALTH_PATH, method = RequestMethod.GET)
    public String healthCheck(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String jsonResult;
        ObjectMapper mapper = new ObjectMapper();
        //jsonResult = mapper.writeValueAsString(getHealthTextJson());
        return toResult(model, response, getHealthTextJson());
    }

    private String toResult(Model model, HttpServletResponse response, String jsonResult) {
        if (jsonResult != null) {
            model.addAttribute(ConstantValue.JSON_DATA, jsonResult);
        } else {
            model.addAttribute(ConstantValue.JSON_DATA, "");
        }
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        return "json";
    }


    public String getHealthTextJson() {
        String json = "{" +
                "\"service\":\"" + MAVEN_ARTIFACT_ID
                + "\",\"Status\":\"" + "OK"
                + "\",\"timestamp\":\"" + Instant.now().toString()
                + "\",\"IP\":\"" + getMyIPAddresssString()
                + "\",\"runningSince\":\"" + getRunningSince()
                + "\",\"version\":\"" + getVersion()
                + "\"}";
        return json;
//        return "{\n" +
//                "  \"Status\": \"OK\",\n" +
//                "  \"Version\": \"" + getVersion() + "\"\n" +
//                "}\n";
    }

    private String getRunningSince() {
        long uptimeInMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        return Instant.now().minus(uptimeInMillis, ChronoUnit.MILLIS).toString();
    }


    public String getVersion() {
        Properties mavenProperties = new Properties();
        String resourcePath = "/META-INF/maven/no.cantara.csdb/ConfigService-Dashboard/pom.properties";
        URL mavenVersionResource = this.getClass().getResource(resourcePath);
        if (mavenVersionResource != null) {
            try {
                mavenProperties.load(mavenVersionResource.openStream());
                return mavenProperties.getProperty("version", "missing version info in " + resourcePath);
            } catch (IOException e) {
                log.warn("Problem reading version resource from classpath: ", e);
            }
        }
        return "(DEV VERSION)";
    }


    public static String getMyIPAddresssesString() {
        String ipAdresses = "";

        try {
            ipAdresses = InetAddress.getLocalHost().getHostAddress();
            Enumeration n = NetworkInterface.getNetworkInterfaces();

            while (n.hasMoreElements()) {
                NetworkInterface e = (NetworkInterface) n.nextElement();

                InetAddress addr;
                for (Enumeration a = e.getInetAddresses(); a.hasMoreElements(); ipAdresses = ipAdresses + "  " + addr.getHostAddress()) {
                    addr = (InetAddress) a.nextElement();
                }
            }
        } catch (Exception e) {
            ipAdresses = "Not resolved";
        }

        return ipAdresses;
    }

    public static String getMyIPAddresssString() {
        String fullString = getMyIPAddresssesString();
        return fullString.substring(0, fullString.indexOf(" "));
    }
}