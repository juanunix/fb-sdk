/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.api;

import info.owaism.social.fb.sdk.core.model.FacebookPermission;
import info.owaism.social.fb.sdk.core.model.FacebookUser;

import java.util.List;

/**
 * Java API for Facebook Graph APIs.
 */
public interface FacebookGraphApi {
	
	/**
	 * Gets the public profile of the facebook user.
	 * @return Public profile of facebook user.
	 */
	FacebookUser me();
	
	/**
	 * Returns the permissions granted to this Auth Token by the Facebook user.
	 * @return Granted Permissions.
	 */
	List<FacebookPermission> permissions();

}
