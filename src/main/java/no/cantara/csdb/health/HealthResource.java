package no.cantara.csdb.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static no.cantara.csdb.health.HealthResource.HEALTH_PATH;


@Path(HEALTH_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    public static final String HEALTH_PATH = "/health";

    private static final Logger log = LoggerFactory.getLogger(HealthResource.class);


    @Autowired
    public HealthResource() {
    }

    @GET
    public Response healthCheck() {
        log.trace("healthCheck");
        return Response.ok(getHealthTextJson()).build();
    }

    public String getHealthTextJson() {
        return "{\n" +
                "  \"Status\": \"OK\",\n" +
                "  \"Version\": \"" + getVersion() + "\"\n" +
                "}\n";
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
}