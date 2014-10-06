/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.constant;


/**
 * Enum containing all Facebook Permission Scopes
 */
public enum FacebookScopesE {
	/**
	 * Gets access to FB users public profile.
	 */
	PUBLIC_PROFILE,
	/**
	 * Gets Access to FB Users email.
	 */
	EMAIL,
	/**
	 * Gets access to FB users friends List.
	 */
	USER_FRIENDS;
	
	/**
	 * Gets the FB Permission scope value.
	 * @return
	 * 	FB Permission scope value
	 */
	public String value(){
		return this.name().toLowerCase();
	}

}
