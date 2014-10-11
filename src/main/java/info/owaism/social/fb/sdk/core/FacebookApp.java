/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.core;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;
import static info.owaism.social.fb.sdk.constant.GeneralConstants.jaxrsClient;
import info.owaism.security.impl.CipherFactory;
import info.owaism.social.fb.sdk.api.Facebook;
import info.owaism.social.fb.sdk.api.impl.FacebookImpl;
import info.owaism.social.fb.sdk.constant.FacebookPermissionsE;
import info.owaism.social.fb.sdk.exception.FacebookException;
import info.owaism.social.fb.sdk.util.HttpUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic Facebook App Api to interact with a Particular Facebook App.
 */
public final class FacebookApp implements Serializable {
	
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 457678980643613508L;

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
	 * URL generated using this method will have inherent CSRF protection as it
	 * will generate its own facebook "state" parameter and will validate when
	 * using TODO fill this up
	 * 
	 * @return URL to be used for authentication and authorization with
	 *         facebook.
	 */
	public String authUrl() {
		String state = getState();
		String facebookScopes = appConfig.defaultFacebookScopeString;

		return _authUrlInternal(state, facebookScopes);
	}

	/**
	 * The URL to redirect the browser to in order to compelete facebook login.
	 * URL generated using this method will NOT have inherent CSRF protection.
	 * You MUST validate the unique State on redirection back from facebook.
	 * 
	 * @param uniqueState
	 * @return URL to be used for authentication and authorization with
	 *         facebook.
	 */
	public String authUrl(String uniqueState) {
		return _authUrlInternal(uniqueState, null);
	}

	/**
	 * Gets the facebook api based on the authorization code.
	 * 
	 * @param authorizationCode
	 * @return {@link Facebook} object for the present logged in user.
	 */
	public Facebook facebook(@NotBlank String authorizationCode) {
		//@formatter:off
		WebTarget webTarget = jaxrsClient()
								.target("https://graph.facebook.com").path("oauth/access_token")
									.queryParam("client_id", appConfig.appId)
									.queryParam("client_secret", appConfig.appSecret)
									.queryParam("redirect_uri", "http://www.owais.local/login")
									.queryParam("code", authorizationCode);;
		
			
		//@formatter:on
		String response = null;
		try {
			response = webTarget.request().get(String.class);
		} catch (Exception ex) {
			throw new FacebookException(
					"Error While getting OAuth Token from Facebook", ex);
		}
		
		//TODO refine code.
		Map<String, String> responseMap = HttpUtil.getQueryMap(response);
		String accessToken = responseMap.get("access_token");
		long expiresIn = Long.decode(responseMap.get("expires").trim());

		System.out.println("ACCESS-TOKEN: "+accessToken);
		return FacebookImpl.newInstance(accessToken, expiresIn);
	}
	
	public Facebook facebook(@NotBlank final String accessToken, final long expiresIn) {
		/*
		 * Coverting it to a future time with 100 second grace.
		 */
		long expiresBy = System.currentTimeMillis()+((expiresIn - 100)*1000);
		return FacebookImpl.newInstance(accessToken, expiresBy);
	}

	/**
	 * @param state
	 * @param facebookScopes
	 * @return
	 */
	private String _authUrlInternal(String state, String facebookScopes) {
		// TODO revisit making the formats constants
		String authUrlFormat = "%s%s";

		if (!isNullOrEmpty(facebookScopes)) {
			authUrlFormat = authUrlFormat + "&scope=%s";
			return String.format(authUrlFormat, authUrl, state, facebookScopes);
		}

		return String.format(authUrlFormat, authUrl, state);
	}

	/**
	 * gets the scopes as comma separated string
	 * 
	 * @return CS Format Default Scope
	 */
	private static String permissionsString(List<FacebookPermissionsE> fbPermissions) {
		StringBuffer scopeBuffer = null;
		for (FacebookPermissionsE fbPermission : fbPermissions) {
			if (null == scopeBuffer) {
				scopeBuffer = new StringBuffer(fbPermission.value());
				continue;
			}

			scopeBuffer.append(",");
			scopeBuffer.append(fbPermission.value());
		}
		return scopeBuffer.toString();
	}

	/**
	 * Gets the state for this facebook transaction
	 * 
	 * @return
	 */
	private String getState() {
		checkState(
				null != appConfig.stateEncryptionKey,
				"State encryption key has not been provided. Unable to generate state parameter for CSRF protection. If you dont want to provide the State Encryption Key in the Configuration then use another method authUrl(String ...) for generating the auth url.");

		// State is just the timestamp which is AES encrypted (to make sure it
		// is generated by this app) and then URL encoded for use as a browser
		// URL.
		String state = CipherFactory.aesCipher().encrypt(
				Long.toString(System.currentTimeMillis()),
				appConfig.stateEncryptionKey);
		try {
			state = URLEncoder.encode(state, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			// unlikely but just taking care.
			throw new IllegalStateException(StandardCharsets.UTF_8.name()
					+ " not supported.", ex);
		}
		return state;
	}

	/**
	 * Builder class for a {@link FacebookApp}. This defines all the
	 * configurations for the facebook app.
	 */
	public static final class Configuration implements Serializable {

		/**
		 * Serial Version.
		 */
		private static final long serialVersionUID = 1L;

		private static final Logger logger = LoggerFactory
				.getLogger(Configuration.class);
		/**
		 * Validator used to validate app configuration.
		 */
		private static Validator appConfigValidator = Validation
				.buildDefaultValidatorFactory().getValidator();

		/**
		 * Gets a new instance of {@link Configuration}
		 * 
		 * @return new Instance of {@link Configuration}.
		 */
		public static final Configuration create(String appName) {
			Configuration config = new Configuration();
			config.appName = appName;
			return config;
		}

		/**
		 * Private Constructor.
		 */
		private Configuration() {

		}

		/**
		 * Facebook App Name
		 */
		private String appName;
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
		private List<FacebookPermissionsE> defaultFacebookPermissions;

		/**
		 * Default Facebook Permissions to be used while request authorization
		 * from the user as a comma separated value.
		 */
		private String defaultFacebookScopeString;

		/**
		 * Encryption key to be used for encrypting and decrypting the state
		 * string which helps is protecting against CSRF issues.
		 */
		@Size(min = 16, max = 16)
		private String stateEncryptionKey;

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
		public Configuration defaultFacebookPermissions(
				@NotEmpty final List<FacebookPermissionsE> facebookScopes) {
			this.defaultFacebookPermissions = facebookScopes;
			return this;
		}

		/**
		 * Set the Encryption key to be used for encrypting and decrypting the
		 * state string which helps is protecting against CSRF issues.
		 * 
		 * @param stateEncryptionKey
		 *            The encryption key to be used for CSRF protection.
		 * @return this instance of facebook app {@link Configuration}
		 */
		public Configuration stateEncryptionKey(
				@NotBlank String stateEncryptionKey) {
			this.stateEncryptionKey = stateEncryptionKey;
			return this;
		}

		/**
		 * Gets instance of facebook app based on the information provided.
		 * 
		 * @return Instance of the {@link FacebookApp}.
		 */
		public FacebookApp generate() {

			Set<ConstraintViolation<Configuration>> validationErrors = appConfigValidator
					.validate(this);

			if (!validationErrors.isEmpty()) {
				logger.error(validationErrors.toString());
				throw new ConstraintViolationException(
						"Error Validating Facebook App Configurations.",
						validationErrors);
			}

			if (null != this.defaultFacebookPermissions
					&& !this.defaultFacebookPermissions.isEmpty()) {
				this.defaultFacebookScopeString = permissionsString(this.defaultFacebookPermissions);
			}

			FacebookApp fbapp = new FacebookApp();
			fbapp.appConfig = this;
			fbapp.authUrl = String
					.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&state=",
							this.appId, this.loginRedirectUrl);
			return fbapp;
		}

	}

}
