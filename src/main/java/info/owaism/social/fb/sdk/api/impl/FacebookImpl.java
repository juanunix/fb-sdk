/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.api.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import info.owaism.social.fb.sdk.annotation.Constant;
import info.owaism.social.fb.sdk.api.Facebook;
import info.owaism.social.fb.sdk.constant.FacebookPermissionsE;
import info.owaism.social.fb.sdk.constant.GeneralConstants;
import info.owaism.social.fb.sdk.core.model.FacebookPermission;
import info.owaism.social.fb.sdk.core.model.FacebookUser;
import info.owaism.social.fb.sdk.proxy.MethodConstant;
import info.owaism.social.fb.sdk.proxy.MethodConstantInvocationHandler;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class FacebookImplConstantNames {
	public static final String FACEBOOK_GRAPH_API_TARGET = "FACEBOOK_GRAPH_API_TARGET";
	public static final String FACEBOOK_GRAPH_API_RESOURCE = "FACEBOOK_GRAPH_API_RESOURCE";
	public static final String ACCESS_TOKEN_PARAM_NAME = "ACCESS_TOKEN_PARAM_NAME";
}

/**
 *
 */
@Constant(constantName = FacebookImplConstantNames.FACEBOOK_GRAPH_API_TARGET, constantValue = "https://graph.facebook.com/v2.1")
@Constant(constantName = FacebookImplConstantNames.ACCESS_TOKEN_PARAM_NAME, constantValue = "access_token")
public class FacebookImpl implements Facebook {
	
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookImpl.class);

	/**
	 * OAuth Token.
	 */
	private String oauthToken;
	/**
	 * Time in millis when the associated OAuth Token will expire.
	 */
	private Long expiresby;

	/**
	 * Gets a fresh instance of {@link Facebook} with the provided OAuth Token
	 * and Refresh Token.
	 * 
	 * @param oauthToken
	 *            OAuth Token for the present facebook user.
	 * @param expiresby
	 *            Time in millis when the associated OAuth Token will expire.
	 * @return new {@link Facebook} instance
	 */
	public static final Facebook newInstance(@NotBlank final String oauthToken,
			final long expiresby) {

		checkArgument(!isNullOrEmpty(oauthToken));

		FacebookImpl facebook = new FacebookImpl();
		facebook.oauthToken = oauthToken;
		facebook.expiresby = expiresby;

		return (Facebook) Proxy.newProxyInstance(
				FacebookImpl.class.getClassLoader(),
				new Class[] { Facebook.class },
				new MethodConstantInvocationHandler(facebook));

	}

	/**
	 * Private constructor.
	 */
	private FacebookImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.owaism.social.fb.sdk.api.FacebookGraphApi#me()
	 */
	@Constant(constantName = FacebookImplConstantNames.FACEBOOK_GRAPH_API_RESOURCE, constantValue = "me")
	public FacebookUser me() {
		//@formatter:off
		WebTarget webTarget = getFacebookGraphApiWebTarget()
									.queryParam(MethodConstant.get(FacebookImplConstantNames.ACCESS_TOKEN_PARAM_NAME), this.oauthToken);
		//@formatter:on
		FacebookUser me = webTarget.request().get(FacebookUser.Builder.class)
				.build();
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.owaism.social.fb.sdk.api.Facebook#oauthToken()
	 */
	public String oauthToken() {
		return oauthToken;
	}

	@Constant(constantName = FacebookImplConstantNames.FACEBOOK_GRAPH_API_RESOURCE, constantValue = "me/permissions")
	public List<FacebookPermission> permissions() {
		//@formatter:off
		WebTarget webTarget = getFacebookGraphApiWebTarget()
									.queryParam(MethodConstant.get(FacebookImplConstantNames.ACCESS_TOKEN_PARAM_NAME), this.oauthToken);
		//@formatter:on

		return webTarget.request().get(FacebookPermissionsListWrapper.class).data();
		
	}

	public long expiresBy() {
		return expiresby;
	}

	/**
	 * Gets the normal Facebook Graph API Web Target.
	 * 
	 * @return
	 */
	private WebTarget getFacebookGraphApiWebTarget() {
		return GeneralConstants
				.jaxrsClient()
				.target(MethodConstant
						.get(FacebookImplConstantNames.FACEBOOK_GRAPH_API_TARGET))
				.path(MethodConstant
						.get(FacebookImplConstantNames.FACEBOOK_GRAPH_API_RESOURCE));
	}
	
	/**
	 * Just a wrapper class for Facebook Permissions.
	 */
	private static final class FacebookPermissionsListWrapper{
		@JsonProperty("data")
		List<FacebookPermission> data;
		
		public List<FacebookPermission> data(){
			return data;
		}
	}
}
