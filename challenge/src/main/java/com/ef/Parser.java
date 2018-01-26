package com.ef;

import java.text.ParseException;
import java.util.List;

import com.ef.model.LogFile;
import com.ef.service.DataBaseService;
import com.ef.service.LogService;

public class Parser {

	public static void main(String[] args) {
		String starDate = "";
		String duration = "";
		int threshold = 0;
		String accesslog = "";

		try {
			for (String arg : args) {
				String argument = arg.split("=")[0].substring(2);
				String value = arg.split("=")[1];

				switch (argument) {
				case "startDate":
					starDate = value;
					break;
				case "duration":
					duration = value;
					break;
				case "threshold":
					threshold = Integer.parseInt(value);
					break;
				case "accesslog":
					accesslog = value;
					break;
				default:
					break;
				}
			}

			if (!accesslog.isEmpty()) {
				System.out.println("Salvando no banco");
				LogService.saveLogIntoMuSql(accesslog);
			}

			List<LogFile> searchInLog = DataBaseService.searchInLog(starDate, duration, threshold);

			searchInLog.forEach(System.out::println);

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
