package at.nambv.social.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {
	public static HttpResponse httpGet(String url,
			HashMap<String, String> headers) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		if (headers.size() != 0) {
			Iterator<Entry<String, String>> i = headers.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, String> e = i.next();
				get.setHeader(e.getKey(), e.getValue());
			}
		}
		try {
			response = client.execute(get);
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse httpPost(String url,
			HashMap<String, String> headers, HashMap<String, String> params) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		if (headers.size() != 0) {
			Iterator<Entry<String, String>> i = headers.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, String> e = i.next();
				post.setHeader(e.getKey(), e.getValue());
			}
		}
		if (params.size() != 0) {
			Iterator<Entry<String, String>> i1 = params.entrySet().iterator();
			while (i1.hasNext()) {
				Entry<String, String> e1 = i1.next();
				urlParameters.add(new BasicNameValuePair(e1.getKey(), e1
						.getValue()));
			}
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String httpURLGET(String url) {
		URL obj;
		try {
			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
