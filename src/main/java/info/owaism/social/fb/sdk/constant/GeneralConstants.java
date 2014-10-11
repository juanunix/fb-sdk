/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.constant;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 *
 */
public class GeneralConstants {

	static ClientConfig cfg = null;
	static {
		cfg = new ClientConfig();
		cfg.register(JacksonJsonProvider.class);
	}
	private static final Client JAXRS_CLIENT = ClientBuilder.newClient(cfg);

	/**
	 * 
	 */
	private GeneralConstants() {
		// TODO Auto-generated constructor stub
	}

	public static final Client jaxrsClient() {
		return JAXRS_CLIENT;
	}

}
