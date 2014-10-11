/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.exception;

/**
 *
 */
public class FacebookException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = -7327434188749772816L;

	/**
	 * @param cause
	 */
	public FacebookException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FacebookException(String message, Throwable cause) {
		super(message, cause);
	}

}
