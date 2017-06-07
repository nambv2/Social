package at.nambv.social;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

public class PostBlog implements AutoPostHandler{
	
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//	private static final String HOST="https://www.gamespot.com";
//	private static final String CATE="/reviews/?page=";
	
	public static Elements getArticleNextPage(String url, Elements existedE, int size) {
		Element html = Jsoup.parse(HttpUtils.httpURLGET(url));
		Element gameArea = html.getElementById("js-sort-filter-results");
		Elements gameList = gameArea.select("section article a");
		for(int i = 0; i < gameList.size(); i++) {
			existedE.add(gameList.get(i));
			if(existedE.size() == size) return existedE;
		}
		return existedE;
		
	}
	
	public static boolean getArticle(int pageCurrent, List<String> links, String blogId, String contentBlog, String resourcePath,int counter, String titleCraw, String contentCraw) {
		
		StringBuffer br = new StringBuffer(contentCraw);
		br.append("</br>");
		br.append("<i>");
		br.append(contentBlog);
		br.append("</i> <a href=\"");
		br.append(links.get(0));
		br.append("\">");
		br.append(links.get(0));
		br.append("</a>");
		
		//
		return bloggerApi(titleCraw,br.toString(), blogId, resourcePath);
	}
	
	public static boolean bloggerApi(String title, String msg, String blogId, String resourcePath) {
		System.out.println("***Post blooger====>"+title);
		HttpTransport HTTP_TRANSPORT;
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = OAuth2Native.authorize(HTTP_TRANSPORT, JSON_FACTORY, resourcePath);
			Blogger blogger = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName("Blogger-PostsGet-Snippet/1.0").build();
			
			Post content = new Post();
			content.setTitle(title);
			content.setContent(msg);

			Insert postsInsertAction = blogger.posts()
			        .insert(blogId, content);

			postsInsertAction.setFields("author/displayName,content,published,title,url");

			Post post = postsInsertAction.execute();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}
	
	
	public boolean autoPost(String msg, List<String> links, HashMap<String, String> attrs) {
		String blogId = attrs.get("blogId");
		String pageCurrent = attrs.get("currentPage");
		String contentBlog = attrs.get("contentBlog");
		String resourcePath = attrs.get("resourcePath");
		int counter = Integer.valueOf(attrs.get("counter"));
		String titleCraw = attrs.get("titleCraw");
		String contentCraw = attrs.get("contentCraw");
		return getArticle(Integer.valueOf(pageCurrent), links, blogId, contentBlog, resourcePath,counter,titleCraw, contentCraw);
	}

}
