package no.cantara.csdb;

import javax.ws.rs.ext.RuntimeDelegate;

import no.cantara.csdb.config.ConfigValue;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

		public static void main(String[] arguments) throws Exception {
			

			RuntimeDelegate.setInstance(new
					com.sun.jersey.server.impl.provider.RuntimeDelegateImpl());

			Server server = new Server(ConfigValue.SERVICE_PORT);
			ServletContextHandler context = new ServletContextHandler(server, ConfigValue.SERVICE_CONTEXT);

			DispatcherServlet dispatcherServlet = new DispatcherServlet();
			dispatcherServlet.setContextConfigLocation("classpath:webapp/web/mvc-config.xml");

			ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
			context.addServlet(servletHolder, "/*");

			server.start();
			server.join();
		}
		
}
