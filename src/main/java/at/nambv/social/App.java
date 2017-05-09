package at.nambv.social;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Hello world!
 *
 */
public class App {
	private static final String root = "G:\\TNT\\sitemap\\";
	//twitter
	private static final String consumerKey = "6q8mmmWnKvQrYE4SJeNV7lSXX";
	private static final String consumerSecret = "0epofoXE5X02BlzCTwXff3qp8grebt6p9WY5QVPaLlnQY4IImj";
	private static final String accessToken = "782127930721710083-2sGTsaK1OMXMOfwRZJ4lI1P9v50TnAR";
	private static final String accessTokenSecret = "Xqlo0HwweDVmpvsIDDgvIq6qV1umWGS3TQXkyU0drUTEs";
	//facebook
	private static final String id = "1917504088529056";
	private static final String token = "EAACEdEose0cBAKC8pZApHoQxKH5UBXlBV9qScaOFIM9mHpvL0NuTjkJUUuPT75qXLJp7JAmGxUkoP6ZAtmQRbyVU9yR9j5FwjQPlX9gMahfkpgraZAOcRZApokUM7dTmamQp0ly8ji4bzddADuDZBcolY8o2ZCZBSEYxZCGoliRLPN0U02dKZClFZBqp7bIYbONTMZD";
	
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

	public static void facebookApi(String id, String token, String message,
			String link) {
		String url = "https://graph.facebook.com/1917504088529056/feed";
		System.out.println("***Post facebook====>"+link);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", token);
		params.put("message", message);
		params.put("link", link);
		HttpResponse response = httpPost(url, headers, params);

	}

	public static void twitterApi(String msg, String link) {
		System.out.println("***Post twitter====>"+link);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		try {
			twitter.updateStatus(msg+"====>"+link);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public static List<String> read(String pathname) {
		File xml = new File(pathname);
		List<String> listLink = new ArrayList<String>();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("url");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String link = eElement.getElementsByTagName("loc").item(0)
							.getTextContent();
					listLink.add(link);
				}
			}
			return listLink;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		/*String msg = "Welcome all";
		String link = "http://www.juegos1friv.com/big-dig-treasure-clickers.html";
		facebookApi(id, token, msg, link);*/
		twitterApi("Helloworld","http://www.juegos1friv.com/left-to-die.html");
		System.out.println("Auto post socials");
		String pathname = root+"sitemap-abcya2017.com";
		List<String> links = read(pathname);
		for(String link : links) {
			twitterApi(msg, link);
		}
		
	}
}
