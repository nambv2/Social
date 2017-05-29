package at.nambv.social;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;

import at.nambv.social.utils.HttpUtils;

public class PostFacebook implements AutoPostHandler{

	public void autoPost(String msg, List<String> links, HashMap<String, String> attrs) {
		System.out.println("hrerererererer");
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
			System.out.println("response:"+response);
		}
	}

}
