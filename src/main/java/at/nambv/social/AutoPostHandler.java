package at.nambv.social;

import java.util.HashMap;
import java.util.List;

public interface AutoPostHandler {
	public boolean autoPost(String msg, List<String> links,HashMap<String, String> attrs); 
}
