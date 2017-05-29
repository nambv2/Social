package at.nambv.social;

public class AutoPostHandlerFactory {
	public static PostFacebook postFacebook;
	public static PostTwitter postTwitter;
	public static PostBlog postBlog;
	public AutoPostHandler getHandler(String type) {
		PostType postType = PostType.valueOf(type);
		if(postType.equals(PostType.FB)) {
			return postFacebook;
		}
		if(postType.equals(PostType.TWT)) {
			return postTwitter;
		}
		if(postType.equals(PostType.BLOG)) {
			return postBlog;
		}
		return null;
	}
}
