package at.nambv.social;

import java.util.HashMap;
import java.util.List;

import at.nambv.social.utils.Config;
import at.nambv.social.utils.ReadXML;

public class AutoPostRunable {
	
	private static String siteMap;
	private static String blogId;
	private static String consumerKey;
	private static String consumerSecret;
	private static String accessToken;
	private static String accessTokenSecret;
	private static String pageId;
	private static String token;
	private static String currentPage;
	private static String currentArticle;
	private static String contentBlog;
	private static String postNumber;
	private static String postType;
	
	//
	public static void init(String cfgPath) {
		HashMap<String, String> cfg = Config.loadConfig(cfgPath);
		siteMap=cfg.get("path_sitemap");
		blogId = cfg.get("blog_id");
		consumerKey = cfg.get("consumer_key");
		consumerSecret=cfg.get("consumer_secret");
		accessToken=cfg.get("access_token");
		accessTokenSecret=cfg.get("access_token_secret");
		pageId=cfg.get("facebook_id");
		token=cfg.get("facebook_token");
		currentPage = cfg.get("current_pape");
		currentArticle = cfg.get("current_article");
		contentBlog = cfg.get("content_blog");
		postNumber = cfg.get("post_number");
		postType = cfg.get("post_type");
		
	}
	//
	public static void main(String args[]) {
		init("src/main/resources/config.properties");
		for(int i = 0; i < postType.split(",").length; i++) {
			String type = postType.split(",")[i].toUpperCase();
			AutoPostHandlerFactory  factory = new AutoPostHandlerFactory();
			AutoPostHandler handler = factory.getHandler(type);
			List<String> links = ReadXML.read(siteMap, Integer.parseInt(postNumber));
			HashMap<String, String> attrs = new HashMap<String, String>();
			attrs.put("token", token);
			attrs.put("pageId", pageId);
			attrs.put("consumerKey", consumerKey);
			attrs.put("consumerSecret", consumerSecret);
			attrs.put("accessToken", accessToken);
			attrs.put("accessTokenSecret", accessTokenSecret);
			attrs.put("blogId", blogId);
			attrs.put("currentPage", currentPage);
			attrs.put("contentBlog", contentBlog);
			handler.autoPost(contentBlog, links, attrs);
		}
	}
}
