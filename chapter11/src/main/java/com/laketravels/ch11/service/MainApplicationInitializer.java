package com.laketravels.ch11.service;

import com.laketravels.ch11.service.util.ESUtil;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.net.UnknownHostException;


@Order(value = 1)
@Configuration
@EnableAutoConfiguration
public class MainApplicationInitializer implements ServletContextInitializer {

    @Autowired
    ESUtil esUtil;


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        registerInContext(rootContext);
        rootContext.setDisplayName(getApplicationName());
        servletContext.setInitParameter("com.newrelic.agent.APPLICATION_NAME", getApplicationName());
        servletContext.addListener(new RequestContextListener());
        initJersey(servletContext);
        initDefaultJaxrsConfig(servletContext);
        initBootstrap(servletContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcherServlet",
                new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
        dispatcher.addMapping("*.css");
        dispatcher.addMapping("*.eot");
        dispatcher.addMapping("*.svg");
        dispatcher.addMapping("*.ttf");
        dispatcher.addMapping("*.woff");
        dispatcher.addMapping("*.map");
        dispatcher.addMapping("*.js");
        dispatcher.addMapping("*.ico");
    }

    private void registerInContext(AnnotationConfigWebApplicationContext rootContext) {
        rootContext.register(getClass());
    }

    private String[] getBaseScanPackages() {
        return new String[]{
                "com"
        };
    }

    private String getApplicationName() {
        return "laketravels-services-app";
    }

    private String getApplicationClassName() {
        return CustomerServiceApplication.class.getName();
    }

    protected void initJersey(ServletContext servletContext) {
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("jersey", new ServletContainer());
        dispatcher.setInitParameter("javax.ws.rs.Application", getApplicationClassName());
        dispatcher.setInitParameter(ServerProperties.WADL_FEATURE_DISABLE, "false");
        dispatcher.setInitParameter(ServerProperties.APPLICATION_NAME, getApplicationName());
        dispatcher.addMapping("/services/*");
        dispatcher.setLoadOnStartup(1);
    }

    protected void initDefaultJaxrsConfig(ServletContext servletContext) {
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DefaultJaxrsConfig", DefaultJaxrsConfig.class.getName());
        dispatcher.setInitParameter("api.version", "1.0.0");
        dispatcher.setInitParameter("api.basePath", "/services");
        dispatcher.setLoadOnStartup(2);
    }


    protected void initBootstrap(ServletContext servletContext) {
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SwaggerBootstrap", Bootstrap.class.getName());
        dispatcher.setLoadOnStartup(2);
    }

    @Bean(autowire = Autowire.BY_NAME, name ="esUtil")
    protected ESUtil esUtil() throws UnknownHostException {
        return new ESUtil();
    }

}