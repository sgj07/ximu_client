package com.ximucredit.client.server;

import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewritePatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by guanjie.sun on 2017/5/14.
 */
public class MainServer {


    public static void main(String[] args) throws Exception {

        Slf4jRequestLog requestLog = new Slf4jRequestLog();
        requestLog.setLoggerName("logback.server");
        requestLog.setExtended(false);
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);


        //urlrewrite handler
        RewriteHandler rewrite = new RewriteHandler();

        RewritePatternRule oldToNew = new RewritePatternRule();
        oldToNew.setPattern("/some/old/spingMvcHandler");
        oldToNew.setReplacement("/someAction?val1=old&val2=spingMvcHandler");
        rewrite.addRule(oldToNew);

        RewriteRegexRule reverse = new RewriteRegexRule();
        reverse.setRegex("/reverse/([^/]*)/(.*)");
        reverse.setReplacement("/reverse/$2/$1");
        rewrite.addRule(reverse);

        ServletContextHandler springMvcHandler = new ServletContextHandler();
        springMvcHandler.setContextPath("/some");
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations(new String[]{"classpath:**applicationContext.xml"});
        springMvcHandler.addEventListener(new ContextLoaderListener(context));
        springMvcHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");

        Server server = new Server(8080);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{
                requestLogHandler,
                rewrite,
                springMvcHandler
        });
        server.setHandler(handlers);
        server.start();
        server.join();

    }
}
