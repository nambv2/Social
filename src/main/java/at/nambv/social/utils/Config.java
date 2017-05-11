package at.nambv.social.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	public static void main(String args[]) {
		Map<String, String> list = loadConfig("resource/config.txt");
		Iterator<Entry<String, String>> i = list.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, String> entry = i.next();
			System.out.println(entry.getValue());
			
		}
	}
}
