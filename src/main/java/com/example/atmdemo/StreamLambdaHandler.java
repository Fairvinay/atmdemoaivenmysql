package com.example.atmdemo;


import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.example.atmdemo.security.CustomUserDetailsService;
import com.example.atmdemo.security.JWT.JwtAuthenticationFilter;
import com.example.atmdemo.security.JWT.JwtUtils;
import com.fasterxml.jackson.databind.ObjectReader;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.EnumSet;

import org.springframework.web.context.support.WebApplicationContextUtils;

 


public class StreamLambdaHandler implements RequestStreamHandler {

 private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

static {
    try {
        //handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(SituationalFlexibilityApp.class);

        // For applications that take longer than 10 seconds to start, use the async builder:
            long startTime = Instant.now().toEpochMilli();

          LambdaContainerHandler.getContainerConfig().setInitializationTimeout(20_000);

         
         handler =  // SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
         
          new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                            .defaultProxy()
                            .asyncInit(startTime)
                            .springBootApplication(Application.class)
                            .buildAndInitialize();/**/

        // we use the onStartup method of the handler to register our custom filter
         /*  handler.onStartup(servletContext -> {
            WebApplicationContextUtils
                         .getRequiredWebApplicationContext(servletContext)
                         .getBean(JwtUtils.class);
                         WebApplicationContextUtils
                         .getRequiredWebApplicationContext(servletContext)
                         .getBean(CustomUserDetailsService.class);
                        
                       FilterRegistration.Dynamic registration = servletContext.addFilter("JwtAuthenticationFilter",JwtAuthenticationFilter.class);
            registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        }); */
    } catch (ContainerInitializationException e) {
        // if we fail here. We re-throw the exception to force another cold start
        e.printStackTrace();
        throw new RuntimeException("Could not initialize Spring Boot application", e);
    }
}






   /*  static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }*/

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
              
        //  ObjectReader  objectReader = handler. getObjectMapper().readerFor(AwsProxyRequest.class);
          
              
               // RequestType request = handler.getObjectMapper().readValue(inputStream).;
              //  ResponseType resp = this.proxy(request, context);
        handler.proxyStream(inputStream, outputStream, context);
       
        // handler.proxyStream(inputStream, outputStream, context)      .addAllowedOrigin();
    }
}