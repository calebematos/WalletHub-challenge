package com.ef.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import com.ef.model.LogFile;

public class LogService {

	public static void saveLogIntoMuSql(String path) {

		File diretorio = new File(path);
		if (!diretorio.exists())
			System.out.println("Path dont exists!!");
		else {
			try {
				FileReader log = new FileReader(diretorio + "access.log");
				BufferedReader lerArq = new BufferedReader(log);

				String line = lerArq.readLine();
				while (line != null) {
					LogFile logFile = new LogFile();
					String[] lineSplit = line.split("\\|");

					if (null != lineSplit[0]) {
						SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						Date date = dtf.parse(lineSplit[0]);
						logFile.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
					}
					if (null != lineSplit[1])
						logFile.setIp(lineSplit[1]);
					if (null != lineSplit[2])
						logFile.setRequest(lineSplit[2].replace("\"", ""));
					if (null != lineSplit[3])
						logFile.setStatus(lineSplit[3]);
					if (null != lineSplit[4])
						logFile.setUserAgent(lineSplit[4].replace("\"", ""));

					DataBaseService.saveLog(logFile);
					System.out.printf("%s\n", line);

					line = lerArq.readLine();
				}

				log.close();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
