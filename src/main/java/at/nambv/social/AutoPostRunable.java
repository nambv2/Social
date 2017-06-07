package at.nambv.social;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.nambv.social.utils.Config;
import at.nambv.social.utils.HttpUtils;
import at.nambv.social.utils.ReadXML;

public class AutoPostRunable {
	
	private static final String HOST="https://www.gamespot.com";
	private static final String CATE="/reviews/?page=";
	
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
	private static String resourcePath;
	
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
		resourcePath = cfg.get("resource_path");
		
	}
	//
	public static Elements getArticleNextPage(String url, Elements existedE, int size) {
		org.jsoup.nodes.Document html = Jsoup.parse(HttpUtils.httpURLGET(url));
		org.jsoup.nodes.Element gameArea = html.getElementById("js-sort-filter-results");
		Elements gameList = gameArea.select("section article a");
		for(int i = 0; i < gameList.size(); i++) {
			existedE.add(gameList.get(i));
			if(existedE.size() == size) return existedE;
		}
		return existedE;
		
	}
	//
	public static Elements getData() {
		String gamePage = HOST + CATE;
		String url = gamePage + currentPage;
		if(HttpUtils.httpURLGET(url) == null) {
			System.out.println("FakeIp de tiep tuc. Vi page review no gioi han crawl");
			return null;
		}
		org.jsoup.nodes.Document html = Jsoup.parse(HttpUtils.httpURLGET(url));
		org.jsoup.nodes.Element gameArea = html.getElementById("js-sort-filter-results");
		Elements gameList = gameArea.select("section article a");
		Element e;
		int sizeGameList = gameList.size(); 
		int pageCurrent = Integer.valueOf(currentPage);
		while(Integer.valueOf(postNumber) > sizeGameList) {
			pageCurrent++;
			String nextPage = gamePage + String.valueOf(pageCurrent);
			gameList = getArticleNextPage(nextPage,gameList,Integer.valueOf(postNumber));
			sizeGameList = gameList.size();
		}
		return gameList;
	}
	//
	public static void main(String args[]) {
		init("src/main/resources/config.properties");
		for(int i = 0; i < postType.split(",").length; i++) {
			String type = postType.split(",")[i].toUpperCase();
			AutoPostHandlerFactory  factory = new AutoPostHandlerFactory();
			AutoPostHandler handler = factory.getHandler(type);
			List<String> links ;
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
			attrs.put("resourcePath", "resourcePath");
			
			//
			File xml = new File(siteMap);
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			int count = 0;
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				Document doc = builder.parse(xml);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("url");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if("true".equalsIgnoreCase(eElement.getAttribute("used"))) continue;
						String link = eElement.getElementsByTagName("loc").item(0)
								.getTextContent();
						links = new ArrayList<>();
						links.add(link);
						
						//
						org.jsoup.nodes.Element e ;
						e = getData().get(count);
						String subLink = e.select(".js-event-tracking").attr("href").toString();
						String titleCraw = e.select(".media-title").text();
						if(HttpUtils.httpURLGET(HOST+link) == null) {
							System.out.println("FakeIp de tiep tuc. Vi page review no gioi han crawl");
							return;
						}
						org.jsoup.nodes.Document conElement = Jsoup.parse(HttpUtils.httpURLGET(HOST+link));
						String contentCraw = conElement.select(".js-content-entity-body").text();
						attrs.put("titleCraw", titleCraw);
						attrs.put("contentCraw", contentCraw);
						//
						if(handler.autoPost(contentBlog, links, attrs) == true) {
							//doc.renameNode(nNode, null, "done-url");
							eElement.setAttribute("used", "true");
							// write the content into xml file
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource source = new DOMSource(doc);
							StreamResult result = new StreamResult(new File(siteMap));
							transformer.transform(source, result);
						}
						//
						count++;
						if(count == Integer.parseInt(postNumber)) break;
					}
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
