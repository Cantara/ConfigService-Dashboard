package no.cantara.csdb;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ext.RuntimeDelegate;

import no.cantara.csdb.config.ConfigValue;

import org.eclipse.jetty.http.security.Constraint;
import org.eclipse.jetty.http.security.Password;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

	public static final String ADMIN_ROLE = "admin";
	public static final String USER_ROLE = "user";

	public static void main(String[] arguments) throws Exception {


		RuntimeDelegate.setInstance(new
				com.sun.jersey.server.impl.provider.RuntimeDelegateImpl());

		Server server = new Server(ConfigValue.SERVICE_PORT);
		ServletContextHandler context = new ServletContextHandler(server, ConfigValue.SERVICE_CONTEXT);
		ConstraintSecurityHandler securityHandler = buildSecurityHandler();
		context.setSecurityHandler(securityHandler);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setContextConfigLocation("classpath:webapp/web/mvc-config.xml");

		ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
		context.addServlet(servletHolder, "/*");

		server.start();
		server.join();
	}


	private static ConstraintSecurityHandler buildSecurityHandler() {
//		Constraint userRoleConstraint = new Constraint();
//		userRoleConstraint.setName(Constraint.__BASIC_AUTH);
//		userRoleConstraint.setRoles(new String[]{USER_ROLE, ADMIN_ROLE});
//		//		        userRoleConstraint.setAuthenticate(true);

		Constraint adminRoleConstraint = new Constraint();
		adminRoleConstraint.setName(Constraint.__BASIC_AUTH);
		adminRoleConstraint.setRoles(new String[]{USER_ROLE, ADMIN_ROLE});
		adminRoleConstraint.setAuthenticate(true);

		ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

		ConstraintMapping adminRoleConstraintMapping = new ConstraintMapping();
		adminRoleConstraintMapping.setConstraint(adminRoleConstraint);
		adminRoleConstraintMapping.setPathSpec("/cs_application/*");
		securityHandler.addConstraintMapping(adminRoleConstraintMapping);

		adminRoleConstraintMapping = new ConstraintMapping();
		adminRoleConstraintMapping.setConstraint(adminRoleConstraint);
		adminRoleConstraintMapping.setPathSpec("/cs_client/*");
		securityHandler.addConstraintMapping(adminRoleConstraintMapping);



		HashLoginService loginService = new HashLoginService("ConfigService");

		String userName = ConfigValue.LOGIN_ADMIN_USERNAME;
		String password = ConfigValue.LOGIN_ADMIN_PASSWORD;
		loginService.putUser(userName, new Password(password), new String[]{ADMIN_ROLE});

		userName = ConfigValue.LOGIN_READ_USERNAME;
		password = ConfigValue.LOGIN_READ_PASSWORD;
		loginService.putUser(userName, new Password(password), new String[]{USER_ROLE});

		securityHandler.setLoginService(loginService);
		return securityHandler;
	}

	public static boolean isAdmin(HttpServletRequest request){



		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials),
					Charset.forName("UTF-8"));
			// credentials = username:password
			final String[] values = credentials.split(":",2);
			if(values[0].equals(ConfigValue.LOGIN_ADMIN_USERNAME) &&
					values[1].equals(ConfigValue.LOGIN_ADMIN_PASSWORD)){
				return true;

			}
		}
		
		return false;


	}
}

