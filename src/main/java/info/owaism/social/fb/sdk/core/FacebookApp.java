/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.core;

import info.owaism.social.fb.sdk.constant.FacebookScopesE;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic Facebook App Api to interact with a Particular Facebook App.
 */
public final class FacebookApp {


	/**
	 * Configurations of this {@link FacebookApp}.
	 */
	@NotNull
	private Configuration appConfig;

	/**
	 * This is the URL to facebook login page. This property is dervied from the
	 * other properties.
	 */
	@NotBlank
	private String authUrl;

	/**
	 * Instance can be created only by this class or child classes.
	 */
	private FacebookApp() {
	}

	/**
	 * The URL to redirect the browser to in order to compelete facebook login.
	 * 
	 * @return Facebook Login Url.
	 */
	public String authUrl() {
		return authUrl;
	}

	/**
	 * Gets the facebook user based on the authorization code.
	 * 
	 * @param authorizationCode
	 * @return
	 */
	public FacebookUser facebookUser(@NotBlank String authorizationCode) {
		return new FacebookUser();

	}

	/**
	 * Builder class for a {@link FacebookApp}. This defines all the
	 * configurations for the facebook app.
	 */
	public static final class Configuration {
		
		private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
		/**
		 * Validator used to validate app configuration.
		 */
		private static Validator appConfigValidator =Validation.buildDefaultValidatorFactory().getValidator();
		
		
		/**
		 * Gets a new instance of {@link Configuration}
		 * @return new Instance of {@link Configuration}.
		 */
		public static final  Configuration get() {
			return new Configuration();
		}
		
		/**
		 * Private Constructor.
		 */
		private Configuration(){
			
		}
		

		/**
		 * Facebook App Id.
		 */
		@NotBlank
		private String appId;
		/**
		 * Facebook App Secret.
		 */
		@NotBlank
		private String appSecret;
		/**
		 * The url facebook should redirect to after attempted authentication.
		 */
		@NotBlank
		private String loginRedirectUrl;
		/**
		 * Default Facebook Permissions to be used while request authorization
		 * from the user.
		 */
		private List<FacebookScopesE> defaultFacebookScopes;

		/**
		 * Setting the App Id associated with the Facebook App.
		 * 
		 * @param appId
		 *            - Facebook App Id.
		 * @return this instance of facebook app {@link Configuration}
		 */
		public Configuration appId(@NotBlank final String appId) {
			this.appId = appId;
			return this;
		}

		/**
		 * Sets the App Secret associate with the Facebook App.
		 * 
		 * @param appSecret
		 *            Facebook App Secret
		 * @return this instance of facebook app {@link Configuration}
		 */
		public Configuration appSecret(@NotBlank final String appSecret) {
			this.appSecret = appSecret;
			return this;
		}

		/**
		 * Sets the Login Redirect URL. This is the URL that facebook would
		 * redirect to after attempted authentication (successful or
		 * unsuccessful). This URL should be part your application.
		 * 
		 * @param loginRedirectUrl
		 *            Url That Facebook would redirect to after attempted fb
		 *            authentication.
		 * @return this instance of facebook app {@link Configuration}
		 */
		public Configuration loginRedirectUrl(
				@NotBlank final String loginRedirectUrl) {
			this.loginRedirectUrl = loginRedirectUrl;
			return this;
		}

		/**
		 * Default set of permissions being requested on any Facebook profile.
		 * This can been overriden during actual attempt to get the login url.
		 * 
		 * @param facebookScopes
		 *            Default set of facebook profile permissions requested by
		 *            this app.
		 * @return this instance of facebook app {@link Configuration}
		 */
		public Configuration defaultFacebookScopes(
				@NotEmpty final List<FacebookScopesE> facebookScopes) {
			this.defaultFacebookScopes = facebookScopes;
			return this;
		}
		
		/**
		 * Gets instance of facebook app based on the information provided.
		 * @return Instance of the {@link FacebookApp}.
		 */
		public FacebookApp generate(){
			
			Set<ConstraintViolation<Configuration>> validationErrors = appConfigValidator.validate(this);
			
			if(!validationErrors.isEmpty()){
				logger.error(validationErrors.toString());
				throw new ConstraintViolationException(/*"Error Validating Facebook App Configurations.",*/ validationErrors);
			}
			FacebookApp fbapp = new FacebookApp();
			fbapp.appConfig = this;
			fbapp.authUrl = String
					.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s",
							this.appId, this.loginRedirectUrl);
			return fbapp;
		}

	}

}
