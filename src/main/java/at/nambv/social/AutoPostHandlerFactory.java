package at.nambv.social;

public class AutoPostHandlerFactory {
	public static PostFacebook postFacebook = new PostFacebook();
	public static PostTwitter postTwitter = new PostTwitter();
	public static PostBlog postBlog = new PostBlog();
	public AutoPostHandler getHandler(String type) {
		PostType postType = PostType.valueOf(type);
		if(postType == PostType.FACEBOOK) {
			return postFacebook;
		}
		if(postType == PostType.TWITTER) {
			return postTwitter;
		}
		if(postType == PostType.BLOG) {
			return postBlog;
		}
		return null;
	}
}
