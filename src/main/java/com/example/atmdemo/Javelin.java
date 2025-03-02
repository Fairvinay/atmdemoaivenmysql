package com.example.atmdemo;

import java.util.Optional;

public class Javelin {
	public int does;
  
	public static void main(String[] args) {
		Javelin j = new Javelin();
		System.out.println(j.does );
		//Optional<Object> empty1 = Optional.empty();
		System.out.println( Optional.empty());
		Optional<String> value1  =Optional.of(new String ("this is sample"));
		
		System.out.println(value1);
		
		
		
		
	}
}
