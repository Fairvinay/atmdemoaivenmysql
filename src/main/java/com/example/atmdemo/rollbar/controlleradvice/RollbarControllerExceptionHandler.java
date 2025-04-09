package com.example.atmdemo.rollbar.controlleradvice;

 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.atmdemo.security.JWT.JwtAuthenticationFilter;
import com.rollbar.notifier.Rollbar;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*  
@Slf4j
	 
	@RequiredArgsConstructor
	 
	@ControllerAdvice
	 */
	public class RollbarControllerExceptionHandler {
	 private static final Logger logger = LoggerFactory.getLogger(RollbarControllerExceptionHandler.class); 
	/*    private final Rollbar rollbar;
	  @ExceptionHandler(value = Exception.class)
	  public void handleExceptions(HttpServletRequest request, HttpServletResponse response, Exception e) {
		logger.info("RollbarControllerExceptionHandler for  "+e.getClass());
		logger.info("RollbarControllerExceptionHandler for  "+request.getRemoteAddr());
		RollbarException  re =   new RollbarException();
		re.getRollbarExceptionData().setIpAddress(request.getRemoteAddr());
	   re.getRollbarExceptionData().setUri(request.getRequestURI());
		logger.info("RollbarControllerExceptionHandler for  "+request.getRequestURI());
	     re.getRollbarExceptionData().setRequestType(request.getMethod());
		   logger.info("RollbarControllerExceptionHandler for  "+request.getMethod());
		 
	   // log.error("e.getMessage()={} [{}]", e.getMessage(), e.getRollbarExceptionData());
	    rollbar.error(e.getMessage(), re.getRollbarExceptionData().getRollbarMap());
	   response.setStatus(HttpStatus.BAD_REQUEST.value());
	 
	   }
	   
	   
	   @ExceptionHandler(TransactionTimedOutException.class)
       public ResponseEntity<String> handleTransactionTimeout(TransactionTimedOutException ex) {
          return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                         .body("The request took too long to complete.");
         }

	   
	   
	   
	   
	   
	   *
	   */
	 
	}