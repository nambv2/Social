package at.nambv.social;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.Blogger.Posts.Insert;
import com.google.api.services.blogger.model.Post;

import at.nambv.social.utils.HttpUtils;
import at.nambv.social.utils.OAuth2Native;
import at.nambv.social.utils.ReadXML;
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
	
	//blogger
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String BLOG_ID = "6616750693444969522";
	private static final String POST_ID = "8204492164026074197";
    /*end blogger*/	
	
	//twitter
	private static final String consumerKey = "6q8mmmWnKvQrYE4SJeNV7lSXX";
	private static final String consumerSecret = "0epofoXE5X02BlzCTwXff3qp8grebt6p9WY5QVPaLlnQY4IImj";
	private static final String accessToken = "782127930721710083-2sGTsaK1OMXMOfwRZJ4lI1P9v50TnAR";
	private static final String accessTokenSecret = "Xqlo0HwweDVmpvsIDDgvIq6qV1umWGS3TQXkyU0drUTEs";
	/*end twitter*/	
	
	
	//facebook
	private static final String id = "1917504088529056";
	private static final String token = "EAACEdEose0cBAKC8pZApHoQxKH5UBXlBV9qScaOFIM9mHpvL0NuTjkJUUuPT75qXLJp7JAmGxUkoP6ZAtmQRbyVU9yR9j5FwjQPlX9gMahfkpgraZAOcRZApokUM7dTmamQp0ly8ji4bzddADuDZBcolY8o2ZCZBSEYxZCGoliRLPN0U02dKZClFZBqp7bIYbONTMZD";
	/*end facebook*/	

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
		HttpResponse response = HttpUtils.httpPost(url, headers, params);

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
	
	public static void bloggerApi(String msg, String link) {
		System.out.println("***Post blooger====>"+link);
		HttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = OAuth2Native.authorize(HTTP_TRANSPORT, JSON_FACTORY);
			Blogger blogger = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("Blogger-PostsGet-Snippet/1.0").build();
			
			Post content = new Post();
			content.setTitle(msg);
			content.setContent(link);

			Insert postsInsertAction = blogger.posts()
			        .insert(BLOG_ID, content);

			postsInsertAction.setFields("author/displayName,content,published,title,url");

			Post post = postsInsertAction.execute();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*String msg = "Welcome all";
		String link = "http://www.juegos1friv.com/big-dig-treasure-clickers.html";
		facebookApi(id, token, msg, link);*/
		twitterApi("Helloworld","http://www.juegos1friv.com/left-to-die.html");
		System.out.println("Auto post socials");
		String pathname = root+"sitemap-abcya2017.com";
		String msg = "";
		List<String> links = ReadXML.read(pathname);
		for(String link : links) {
			twitterApi(msg, link);
		}
		
	}
}
