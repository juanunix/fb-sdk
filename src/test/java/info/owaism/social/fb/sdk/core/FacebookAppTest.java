package info.owaism.social.fb.sdk.core;

import info.owaism.security.impl.CipherFactory;
import info.owaism.social.fb.sdk.api.Facebook;
import info.owaism.social.fb.sdk.constant.FacebookPermissionsE;
import info.owaism.social.fb.sdk.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FacebookAppTest {
	@Test(expectedExceptions = ConstraintViolationException.class)
	public void testNegetiveCreation1() {
		FacebookApp.Configuration.create("nube").generate();
	}

	@Test
	public void testCreation() throws UnsupportedEncodingException {
		String appId= "441543365985151";
		String appSecret = "d07ad656dc1c647ad4e37f2fcc2caa6b";
		String redirectUrl = "http://www.owais.local/login";
		String stateEncryptionKey = "1234567890123456";
		
		FacebookApp fbapp = fbApp();
		//@formatter:on
		Assert.assertNotNull(fbapp);
		String authUrl = fbapp.authUrl();
		
		Assert.assertNotNull(authUrl);
		Map<String,String> queryMap = HttpUtil.getQueryMap(authUrl.substring(authUrl.indexOf("?")+1));
		Assert.assertEquals(appId, queryMap.get("client_id"));
		Assert.assertEquals(redirectUrl, queryMap.get("redirect_uri"));
		Assert.assertNotNull(queryMap.get("state"));
		Long timestamp = Long.decode(CipherFactory.aesCipher().decrypt(URLDecoder.decode(queryMap.get("state"),StandardCharsets.UTF_8.name()), stateEncryptionKey));
		Assert.assertTrue(timestamp <= System.currentTimeMillis());
		
		//WebTarget target = jaxrsClient().target(authUrl);
		//String accessTokenResponse = target.request().get(String.class);
		System.out.println("AuthURL: "+authUrl);
	}
	
	
	//@Test
	public void facebooktest() throws Exception {
		String code = "AQCP4J0Yq1dU5Zmp8BFnFyqqdxKwehACSGdLBhZl7waryl1YRQ6qFr0A7cHCrxRBpWqL8opawC4Tu40jRvc_6a0hv290aPdAjzcIfzZAgsOJ4Yjm2ZOjxD8Np-hT1GujLucjqlM0SE9T4THs63aANr7AtRi6aSX3b63T4LwZsiSrniMgH5fpOJ_P04K9OGyGXXC6bjtWte0T1eQSeqh81o6ywudJjnuj7XXWdB7HeaXzxIb9FNFb_MdGOYOrlcGkdlQFXH9rcQvddVulWp0UwtM7P9-6jAxavv9eJN5zT75SDmfz6noOsqrymqbxSX67uVAjkaOOGBOkZgVxcUo0olUb";
		
		FacebookApp fbapp = fbApp();
		fbapp.facebook(code).me();
		
	}

	/**
	 * @return
	 */
	private FacebookApp fbApp() {
		String appId= "441543365985151";
		String appSecret = "d07ad656dc1c647ad4e37f2fcc2caa6b";
		String redirectUrl = "http://www.owais.local/login";
		String stateEncryptionKey = "1234567890123456";
		
		//@formatter:off
		FacebookApp fbapp = FacebookApp.Configuration
								.create("nube")
								.appId(appId)
								.appSecret(appSecret)
								.loginRedirectUrl(redirectUrl)
								.stateEncryptionKey(stateEncryptionKey)
								.defaultFacebookPermissions(Arrays.asList(FacebookPermissionsE.EMAIL,FacebookPermissionsE.USER_FRIENDS))
								.generate();
		return fbapp;
	}
	
	@Test
	public void facebookme() throws Exception {
		Facebook fb = fbApp().facebook("CAAGRlNN7g38BAGsggNGLyjQ85CWZCqbkt3ZCfkjyhJrjaCk6ZCxDZCZB4048aqZCSMzIhvcZAKMmVlBYfeZBOs4Hnc46edaqgZBakVrSkIB0zoMw3gJELZCCUz6SftyZB7ywV0pVzY3wnh6h441wn0INbUm5IdeDBVEtSAIWXaCYAKNVDMOc8AcIw9zQ4zodzK6V3h9IXdoJhVkW1A6tDkIpm8r", 50000l);
		System.out.println(fb.me().email());
		System.out.println(new Date(fb.expiresBy()));
	}
	
	@Test
	public void facebookmepermissions() throws Exception {
		Facebook fb = fbApp().facebook("CAAGRlNN7g38BAGsggNGLyjQ85CWZCqbkt3ZCfkjyhJrjaCk6ZCxDZCZB4048aqZCSMzIhvcZAKMmVlBYfeZBOs4Hnc46edaqgZBakVrSkIB0zoMw3gJELZCCUz6SftyZB7ywV0pVzY3wnh6h441wn0INbUm5IdeDBVEtSAIWXaCYAKNVDMOc8AcIw9zQ4zodzK6V3h9IXdoJhVkW1A6tDkIpm8r", 50000l);
		System.out.println(fb.permissions());
	}
	
	
}
