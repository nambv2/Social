/**
 * 
 */
package at.nambv.social.utils;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.blogger.BloggerScopes;

/**
 * @author nambv
 *
 * May 4, 2017
 */
public class OAuth2Native {
	  /** Directory to store user credentials. */
	  private static FileDataStoreFactory dataStoreFactory;
	  private static GoogleClientSecrets clientSecrets;
	  public static Credential authorize(HttpTransport httpTransport, JsonFactory JSON_FACTORY, String resourcePath) throws Exception {
		File dataStoreDir = new File(resourcePath);
	    // load client secrets
		dataStoreFactory = new FileDataStoreFactory(dataStoreDir);
	    clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	        new InputStreamReader(OAuth2Native.class.getResourceAsStream("/client_secrets.json")));
	    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
	        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
	      System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
	          + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
	      System.exit(1);
	    }
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets, Arrays.asList(BloggerScopes.BLOGGER)).setDataStoreFactory(
	        dataStoreFactory).build();
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	  }
}
