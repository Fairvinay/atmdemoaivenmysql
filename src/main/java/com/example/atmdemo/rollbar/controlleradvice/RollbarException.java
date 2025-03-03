package com.example.atmdemo.rollbar.controlleradvice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class RollbarException extends Exception{
	  static  ExceptionData expD  ; 
	
	public RollbarException() {
		 super("Rollbar Exception ");
		 if(Objects.isNull(expD)   ) {
			 expD = new ExceptionData();
		 }
	}
	
	public ExceptionData getRollbarExceptionData() {
		// TODO Auto-generated method stub
		return expD;
	}

}
@Setter
@Getter class ExceptionData { 
	
	String ipAddress;
	String uri;
	String requestType;
	
	 public String toString( ) {
	    return " " +this.ipAddress + "" +this.uri + " " + this.requestType + " "  ;
         } 

	  public Map<String, Object> getRollbarMap( )
	    {
		  Map<String, Object> ret = new HashMap<>();
		  ret.put("ipAddress", this.ipAddress);
		  ret.put("uri",this.uri);
 		 ret.put("requestType",this.requestType);
 	            return ret;
            }
	/*  static public Map<String, Object> getRollbarMap( )
	    {
	        Class co = RollbarException.expD.getClass();
	        Field [] cfields = co.getDeclaredFields();
	        Map<String, Object> ret = new HashMap<>();
	        for(Field f: cfields)
	        {
	            String attributeName = f.getName();
	            String getterMethodName = "get"
	                               + attributeName.substring(0, 1).toUpperCase()
	                               + attributeName.substring(1, attributeName.length());
	            Method m = null;
	            try {
	                m = co.getMethod(getterMethodName);
	                Object valObject = m.invoke(o);
	                ret.put(attributeName, valObject);
	            } catch (Exception e) {
	                continue; 
	            }
	        }
	        return ret;
	    }*/
}