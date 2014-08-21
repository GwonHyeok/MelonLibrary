package com.hyeok.melon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Jsonparse {

	
	public String[] JsonSongInfo(String jsonsonginfourl) {
		/**
		 * String MUSICURL = parse.urljsonparse(jsonfileurl, "PATH"); 
		 * String SongName = parse.urljsonparse(jsonfileurl, "CNAME"); 
		 * String SingerName = parse.urljsonparse(jsonfileurl, "PNAME"); 
		 * String Bitrate = parse.urljsonparse(jsonfileurl, "BITRATE");
		 * String LyricsURL = parse.urljsonparse(jsonfileurl, "LYRICSPATH");
		 * String Albumid = 'parse.urljsonparse(jsonfileurl, "ALBUMID");
		 */
		try {
			URL url = new URL(jsonsonginfourl);
			url.openStream();
			String out = new Scanner(url.openStream(), "UTF-8").useDelimiter(
					"\\A").next();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(out);
			JSONObject job = (JSONObject) obj;
			String PATH = (String) job.get("PATH");
			String CNAME = (String) job.get("CNAME");
			String PNAME = (String) job.get("PNAME");
			String BITRATE = (String) job.get("BITRATE");
			String LYRICSPATH = (String) job.get("LYRICSPATH");
			String ALBUMID = (String) job.get("ALBUMID");
			String[] songinfo = new String[] { PATH, CNAME, PNAME, BITRATE,
					LYRICSPATH, ALBUMID };
			return songinfo;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String urljsonparse(String aurl, String name) {
		try {
			URL url = new URL(aurl);
			url.openStream();
			String out = new Scanner(url.openStream(), "UTF-8").useDelimiter(
					"\\A").next();
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(out);

			JSONObject jsonObject = (JSONObject) obj;

			String gap = (String) jsonObject.get(name);

			return gap;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String jsonparse(String file, String name) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			String gap = (String) jsonObject.get(name);
			return gap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] melonerror(String aurl, String name, String period) {
		try {
			URL url = new URL(aurl);
			url.openStream();
			String out = new Scanner(url.openStream(), "UTF-8").useDelimiter(
					"\\A").next();
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(out);

			JSONObject jsonObject = (JSONObject) obj;
			String gap = (String) jsonObject.get(name);
			String gap1 = (String) jsonObject.get(period);
			JSONObject messageobject = (JSONObject) jsonObject.get("OPTION");
			String message = "NoMessage";
			String[] data = new String[] { gap, gap1, message };
			try {
				message = (String) messageobject.get("MESSAGE");
			} catch (NullPointerException e) {
			}
			return data;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String downloadParse(String aurl) {
		try {
			URL url = new URL(aurl);
			String out = new Scanner(url.openStream(), "UTF-8").useDelimiter(
					"\\A").next();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(out);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray array = (JSONArray) jsonObject.get("CONTENTLIST");

			JSONObject j = (JSONObject) array.get(0);
			String MP3DOWNURL = (String) j.get("PATH");
			String FILENAME = (String) j.get("FILENAME");
			return MP3DOWNURL + "#" + FILENAME;

		} catch (IOException e) {

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
