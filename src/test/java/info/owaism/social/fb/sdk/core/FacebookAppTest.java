package info.owaism.social.fb.sdk.core;

import javax.validation.ConstraintViolationException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FacebookAppTest {
	@Test(expectedExceptions = ConstraintViolationException.class)
	public void testNegetiveCreation1() {
		FacebookApp.Configuration.get().generate();
	}

	@Test
	public void testCreation() {
		//@formatter:off
		FacebookApp fbapp = FacebookApp.Configuration
								.get()
								.appId("test")
								.appSecret("testSecret")
								.loginRedirectUrl("http://testapp.com/redirect")
								.generate();
		//@formatter:on
		Assert.assertNotNull(fbapp);
		Assert.assertNotNull(fbapp.authUrl());
		Assert.assertEquals(
				fbapp.authUrl(),
				"https://www.facebook.com/dialog/oauth?client_id=test&redirect_uri=http://testapp.com/redirect");
	}
}
