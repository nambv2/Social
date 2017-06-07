package at.nambv.social;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;

import at.nambv.social.utils.HttpUtils;

public class PostFacebook implements AutoPostHandler{

	public boolean autoPost(String msg, List<String> links, HashMap<String, String> attrs) {
		String token = attrs.get("token");
		String pageId = attrs.get("pageId");
		String url = "https://graph.facebook.com/"+pageId+"/feed";
		for(String link : links) {
			System.out.println("***Post facebook====>"+link);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("content-type", "application/x-www-form-urlencoded");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("access_token", token);
			params.put("link", link);
			HttpResponse response = HttpUtils.httpPost(url, headers, params);
			if(response.getStatusLine().getStatusCode() == 200)
			return true;
			return false;
		}
		return false;
	}

}
