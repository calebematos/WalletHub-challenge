package com.ef;

import com.ef.service.LogService;

public class Parser {

	public static void main(String[] args) {
		String starDate = "";
		String duration = "";
		String threshold = "";
		String accesslog = "";

		for (String arg : args) {
			String argument = arg.split("=")[0].substring(2);
			if (argument.equals("startDate"))
				starDate = arg.split("=")[1];
			else if (argument.equals("duration"))
				duration = arg.split("=")[1];
			else if (argument.equals("threshold"))
				threshold = arg.split("=")[1];
			else if (argument.equals("accesslog"))
				accesslog = arg.split("=")[1];
		}

		if (!accesslog.isEmpty()) {
			LogService.saveLogIntoMuSql(accesslog);
		}

	}
}
