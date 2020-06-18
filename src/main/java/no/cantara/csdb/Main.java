package no.cantara.csdb;

import com.sun.jersey.server.impl.provider.RuntimeDelegateImpl;
import no.cantara.csdb.config.ConfigValue;
import no.cantara.csdb.health.HealthResource;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import javax.ws.rs.ext.RuntimeDelegate;

public class Main {

	public static final String ADMIN_ROLE = "admin";
	public static final String USER_ROLE = "user";
	private static final Logger log = LoggerFactory.getLogger(Main.class);


	public static void main(String[] arguments) throws Exception {

		RuntimeDelegate.setInstance(new RuntimeDelegateImpl());

		Server server = new Server(ConfigValue.SERVICE_PORT);
		ServletContextHandler context = new ServletContextHandler(server, ConfigValue.SERVICE_CONTEXT);
		ConstraintSecurityHandler securityHandler = buildSecurityHandler();
		context.setSecurityHandler(securityHandler);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setContextConfigLocation("classpath:webapp/web/mvc-config.xml");

		ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
		context.addServlet(servletHolder, "/*");

		server.start();
		log.info("ConfigService Dashboard started - health check: http://localhost:" + ConfigValue.SERVICE_PORT + ConfigValue.SERVICE_CONTEXT + HealthResource.HEALTH_PATH);
		System.out.println("ConfigService Dashboard started - health check: http://localhost:" + ConfigValue.SERVICE_PORT + ConfigValue.SERVICE_CONTEXT + HealthResource.HEALTH_PATH);
		server.join();
	}

	private static ConstraintSecurityHandler buildSecurityHandler() {

		Constraint roleConstraint = new Constraint();
		roleConstraint.setName(Constraint.__BASIC_AUTH);
		roleConstraint.setRoles(new String[]{USER_ROLE, ADMIN_ROLE});
		roleConstraint.setAuthenticate(true);

		ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

		ConstraintMapping roleConstraintMapping = new ConstraintMapping();
		roleConstraintMapping.setConstraint(roleConstraint);
		roleConstraintMapping.setPathSpec("/application/*");
		securityHandler.addConstraintMapping(roleConstraintMapping);

		roleConstraintMapping = new ConstraintMapping();
		roleConstraintMapping.setConstraint(roleConstraint);
		roleConstraintMapping.setPathSpec("/client/*");
		securityHandler.addConstraintMapping(roleConstraintMapping);

        // Allow healthresource to be accessed without authentication
        ConstraintMapping healthEndpointConstraintMapping = new ConstraintMapping();
        healthEndpointConstraintMapping.setConstraint(new Constraint(Constraint.NONE, Constraint.ANY_ROLE));
//		healthEndpointConstraintMapping.setPathSpec("/api/*");
        healthEndpointConstraintMapping.setPathSpec(HealthResource.HEALTH_PATH);
        securityHandler.addConstraintMapping(healthEndpointConstraintMapping);

        HashLoginService loginService = new HashLoginService("ConfigService");

        String clientUsername =  ConfigValue.LOGIN_READ_USERNAME;
        String clientPassword = ConfigValue.LOGIN_READ_PASSWORD;
        UserStore userStore = new UserStore();
        userStore.addUser(clientUsername, Credential.getCredential(clientPassword), new String[]{USER_ROLE});

        String adminUsername = ConfigValue.LOGIN_ADMIN_USERNAME;
        String adminPassword = ConfigValue.LOGIN_ADMIN_PASSWORD;
        userStore.addUser(adminUsername, Credential.getCredential(adminPassword), new String[]{ADMIN_ROLE});
        loginService.setUserStore(userStore);

		securityHandler.setLoginService(loginService);
		return securityHandler;
	}
}
