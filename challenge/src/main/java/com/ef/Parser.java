package com.ef;

public class Parser {

	public static void main(String[] args) {
		String starDate;
		String duration;
		String threshold;
		
		for (String arg : args) {
			String argument = arg.split("=")[0].substring(1);
			if (argument.equals("startDate"))
				 starDate = argument;
			else if (argument.equals("duration"))
				duration = argument;
			else if (argument.equals("threshold"))
				threshold = argument;
		}
		

	}
}
