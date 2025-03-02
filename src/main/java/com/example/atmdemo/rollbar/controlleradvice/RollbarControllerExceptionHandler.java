package com.example.atmdemo.rollbar.controlleradvice;

 

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rollbar.notifier.Rollbar;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
	 
	@RequiredArgsConstructor
	 
	@ControllerAdvice
	 
	public class RollbarControllerExceptionHandler {
	 
	   private final Rollbar rollbar;
	  @ExceptionHandler(value = Exception.class)
	  public void handleExceptions(HttpServletRequest request, HttpServletResponse response, RollbarException e) {
	     e.getRollbarExceptionData().setIpAddress(request.getRemoteAddr());
	    e.getRollbarExceptionData().setUri(request.getRequestURI());
	 
	       e.getRollbarExceptionData().setRequestType(request.getMethod());
	   // log.error("e.getMessage()={} [{}]", e.getMessage(), e.getRollbarExceptionData());
	    rollbar.error(e.getMessage(), e.getRollbarExceptionData().getRollbarMap());
	   response.setStatus(HttpStatus.BAD_REQUEST.value());
	 
	   }
	 
	}