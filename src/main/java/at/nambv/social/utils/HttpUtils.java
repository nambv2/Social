package at.nambv.social.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
}
