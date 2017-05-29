package at.nambv.social;

public enum PostType {
	FB("FACEBOOK"),
	TWT("TWITTER"),
	BLOG("BLOG");
	private String type;
	private PostType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
