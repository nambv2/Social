package at.nambv.social.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
	public static HashMap<String, String> loadConfig(String pathname) {
		Map<String, String> listCfg = new HashMap<String, String>();
		FileReader fr;
		BufferedReader bf;
		try {
			fr = new FileReader(new File(pathname));
			bf = new BufferedReader(fr);
			String currentLine ;
			
			while((currentLine = bf.readLine()) != null) {
				if ("//".equals(currentLine.substring(0, 2).toString())) continue;
				if("".equals(currentLine.trim())) continue;
				listCfg.put(currentLine.split("=")[0], currentLine.split("=")[1]);
			}
			return (HashMap<String, String>) listCfg;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void updateConfig(String pathname, String param, String value) {
		FileWriter fw;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(new File(pathname));
			bw = new BufferedWriter(fw);
			bw.write("current_page="+value);
			bw.flush();
            bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Map<String, String> list = loadConfig("src/main/resources/config.txt");
//		Iterator<Entry<String, String>> i = list.entrySet().iterator();
//		while(i.hasNext()) {
//			Entry<String, String> entry = i.next();
//			System.out.println(entry.getValue());
//			
//		}
		updateConfig("src/main/resources/local_storage.txt", "current_page", "1");
	}
}
