/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.api;

/**
 * Java API for FacebookGraphApi
 */
public interface Facebook extends FacebookGraphApi {

	/**
	 * Gets the present OAuth token being used by this {@link Facebook}
	 * instance.
	 * 
	 * @return OAuth Token.
	 */
	String oauthToken();

	/**
	 * Gives times in milliseconds (time since Jan 1 1970) when the oauth token
	 * associated with this {@link Facebook} instance will expire.
	 * 
	 * @return Time in milliseconds when the OAuth Token will expire.
	 */
	long expiresBy();
}
