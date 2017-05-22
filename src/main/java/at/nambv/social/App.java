package at.nambv.social;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import at.nambv.social.utils.Config;
import at.nambv.social.utils.HttpUtils;
import at.nambv.social.utils.OAuth2Native;
import at.nambv.social.utils.ReadXML;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.Blogger.Posts.Insert;
import com.google.api.services.blogger.model.Post;

/**
 * Hello world!
 *
 */
public class App {
	
	//blogger
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	/*
	private static final String root = "G:\\TNT\\sitemap\\";
	
	//blogger
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String BLOG_ID = "6616750693444969522";
	private static final String POST_ID = "8204492164026074197";
    end blogger	
	
	//twitter
	private static final String consumerKey = "6q8mmmWnKvQrYE4SJeNV7lSXX";
	private static final String consumerSecret = "0epofoXE5X02BlzCTwXff3qp8grebt6p9WY5QVPaLlnQY4IImj";
	private static final String accessToken = "782127930721710083-2sGTsaK1OMXMOfwRZJ4lI1P9v50TnAR";
	private static final String accessTokenSecret = "Xqlo0HwweDVmpvsIDDgvIq6qV1umWGS3TQXkyU0drUTEs";
	end twitter	
	
	
	//facebook
	private static final String id = "1917504088529056";
	private static final String token = "EAACEdEose0cBAJrbKp0L2qevJm17EbzZAZAEVKXZBsMm2z8Ryaj6Sq8ZCWt1MQF1s13gh9qvCPVMs5YPpJpjMbi16Ngb9NoR2VQO0cRUs97IJZCglYglQsSet9FZBni3CuxqXiYlcSbgzUeDZBIrC0LKeBW37O17NJwZBqqpWRz3aS8SElXlKZBLOIVg7FzZAcwPAZD";
	end facebook	
	*/
	
	private static String siteMap;
	private static String blogId;
	private static String consumerKey;
	private static String consumerSecret;
	private static String accessToken;
	private static String accessTokenSecret;
	private static String id;
	private static String token;
	
	
	public static void init(String cfgPath) {
		HashMap<String, String> cfg = Config.loadConfig(cfgPath);
		siteMap=cfg.get("path_sitemap");
		blogId = cfg.get("blog_id");
		consumerKey = cfg.get("consumer_key");
		consumerSecret=cfg.get("consumer_secret");
		accessToken=cfg.get("access_token");
		accessTokenSecret=cfg.get("access_token_secret");
		id=cfg.get("facebook_id");
		token=cfg.get("facebook_token");
	}

	public static void facebookApi(String id, String token, String message,
			String link) {
		String url = "https://graph.facebook.com/1917504088529056/feed";
		System.out.println("***Post facebook====>"+link);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", token);
		params.put("link", link);
		HttpResponse response = HttpUtils.httpPost(url, headers, params);
		System.out.println("response:"+response);

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
			Status status = twitter.updateStatus(link);
			System.out.println("response: "+status);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public static void bloggerApi(String title, String msg) {
		System.out.println("***Post blooger====>"+title);
		HttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = OAuth2Native.authorize(HTTP_TRANSPORT, JSON_FACTORY);
			Blogger blogger = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("Blogger-PostsGet-Snippet/1.0").build();
			
			Post content = new Post();
			content.setTitle(title);
			content.setContent(msg);

			Insert postsInsertAction = blogger.posts()
			        .insert(blogId, content);

			postsInsertAction.setFields("author/displayName,content,published,title,url");

			Post post = postsInsertAction.execute();
			System.out.println("response: "+post.getStatus());

		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void postBlogWithData(String host, String category, String linkFromSitemap) {
			//url ="https://www.gamespot.com/reviews/?page=1"
			String url = host + category;
			Element html = Jsoup.parse(HttpUtils.httpURLGET(url));
			Element gameArea = html.getElementById("js-sort-filter-results");
			Elements gameList = gameArea.select("section article a");
			Element e;
			for(int i = 0; i < gameList.size(); i++) {
				e = gameList.get(i);
				String link = e.select(".js-event-tracking").attr("href").toString();
				String title = e.select(".media-title").text();
				Element conElement = Jsoup.parse(HttpUtils.httpURLGET(host+link));
				String content = conElement.select(".js-content-entity-body").text();
				StringBuffer br = new StringBuffer(content);
				br.append("</br>");
				br.append("<i>You can play and experience more game in here</i> <a href=\"");
				br.append(linkFromSitemap);
				br.append("\">");
				br.append(linkFromSitemap);
				br.append("</a>");
				System.out.println(br.toString());
				bloggerApi(title,br.toString());
				break;
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
					org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
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
		String msg = "A test post";
		String link = "With <code>HTML</code> content";
		init("src/main/resources/config.txt");
		postBlogWithData("https://www.gamespot.com", "/reviews/?page=1", "http://www.juegos1friv.com/left-to-die.html");
		List<String> links = ReadXML.read(siteMap);
	}
}
