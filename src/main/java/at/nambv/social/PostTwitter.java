package at.nambv.social;

import java.util.HashMap;
import java.util.List;

import at.nambv.social.utils.Config;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class PostTwitter implements AutoPostHandler{
	
	private static String consumerKey;
	private static String consumerSecret;
	private static String accessToken;
	private static String accessTokenSecret;

	public void autoPost(String msg, List<String> links, HashMap<String, String> attrs) {
		//Load config 
		consumerKey = attrs.get("consumerKey");
		consumerSecret= attrs.get("consumerSecret");
		accessToken= attrs.get("accessToken");
		accessTokenSecret= attrs.get("accessTokenSecret");
		//
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		for(String link : links) {
			try {
				System.out.println("***Post twitter====>"+link);
				Status status = twitter.updateStatus(link);
				System.out.println("response: "+status);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}
	
}
