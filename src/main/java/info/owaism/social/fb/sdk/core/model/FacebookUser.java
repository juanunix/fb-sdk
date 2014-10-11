/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.core.model;

import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The facebook user model.
 */
public class FacebookUser {

	/**
	 * Builder instance that was used to build this FacebookUser instance.
	 */
	@SuppressWarnings("unused")
	private Builder builder;

	private String id;
	private String firstName;
	private String lastName;
	private String gender;
	private String profileLink;
	private Locale locale;
	private String fullName;
	private int timezoneOffset;
	private String[] timezones;
	private String lastUpdatedTime;
	private boolean verified;
	private String email;

	/**
	 * 
	 */
	FacebookUser(Builder builder) {
		this.builder = builder;
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.gender = builder.gender;
		this.profileLink = builder.profileLink;
		this.locale = Locale.forLanguageTag(builder.locale);
		this.fullName = builder.fullName;
		this.timezoneOffset = builder.timezone;
		this.timezones = TimeZone.getAvailableIDs(builder.timezone);
		this.lastUpdatedTime = builder.lastUpdatedTime;
		this.verified = builder.verified;
		this.email = builder.email;
	}

	/**
	 * @return the id
	 */
	@JsonProperty("id")
	public String id() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	@JsonProperty("firstName")
	public String firstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	@JsonProperty("lastName")
	public String lastName() {
		return lastName;
	}

	/**
	 * @return the gender
	 */
	@JsonProperty("gender")
	public String gender() {
		return gender;
	}

	/**
	 * @return the profileLink
	 */
	@JsonProperty("profileLink")
	public String profileLink() {
		return profileLink;
	}

	/**
	 * @return the locale
	 */
	@JsonProperty("locale")
	public Locale locale() {
		return locale;
	}

	/**
	 * @return the fullName
	 */
	@JsonProperty("fullName")
	public String fullName() {
		return fullName;
	}

	/**
	 * @return the timezoneOffset
	 */
	@JsonProperty("timezoneOffset")
	public int timezoneOffset() {
		return timezoneOffset;
	}

	/**
	 * @return the timezones
	 */
	@JsonProperty("timezones")
	public String[] timezones() {
		return timezones;
	}

	/**
	 * @return the lastUpdatedTime
	 */
	@JsonProperty("lastUpdatedTime")
	public String lastUpdatedTime() {
		return lastUpdatedTime;
	}

	/**
	 * @return the verified
	 */
	@JsonProperty("verified")
	public boolean verified() {
		return verified;
	}

	/**
	 * 
	 * @return
	 */
	@JsonProperty("email")
	public String email() {
		return email;
	}

	/**
	 * Builder used to build the {@link FacebookUser} object.
	 */
	public static final class Builder {

		private static Logger logger = LoggerFactory.getLogger(Builder.class);

		/**
		 * Validator used to validate app configuration.
		 */
		private static final Validator FACEBOOK_USER_VALIDATOR = Validation
				.buildDefaultValidatorFactory().getValidator();

		@NotNull
		private String id;
		@NotBlank
		private String firstName;
		@NotBlank
		private String lastName;
		@NotBlank
		private String gender;
		@NotBlank
		private String profileLink;
		@NotBlank
		private String locale;
		@NotBlank
		private String fullName;
		private int timezone;
		@NotBlank
		private String lastUpdatedTime;
		private boolean verified;
		private String email;

		@JsonProperty("id")
		public Builder id(String id) {
			this.id = id;
			return this;
		}

		@JsonProperty("first_name")
		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		@JsonProperty("last_name")
		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		@JsonProperty("gender")
		public Builder gender(String gender) {
			this.gender = gender;
			return this;
		}

		@JsonProperty("link")
		public Builder profileLink(String profileLink) {
			this.profileLink = profileLink;
			return this;
		}

		@JsonProperty("locale")
		public Builder locale(String locale) {
			this.locale = locale;
			return this;
		}

		@JsonProperty("name")
		public Builder fullName(String fullName) {
			this.fullName = fullName;
			return this;
		}

		@JsonProperty("timezone")
		public Builder timezone(int timezone) {
			this.timezone = timezone;
			return this;
		}

		@JsonProperty("updated_time")
		public Builder lastUpdatedTime(String lastUpdatedTime) {
			this.lastUpdatedTime = lastUpdatedTime;
			return this;
		}

		@JsonProperty("verified")
		public Builder verified(boolean verified) {
			this.verified = verified;
			return this;
		}

		@JsonProperty("email")
		public Builder email(String email) {
			this.email = email;
			return this;
		}

		/**
		 * Builds a new {@link FacebookUser}.
		 * 
		 * @return
		 */
		public FacebookUser build() {
			Set<ConstraintViolation<Builder>> constraintViolations = FACEBOOK_USER_VALIDATOR
					.validate(this);

			if (!constraintViolations.isEmpty()) {
				logger.error(constraintViolations.toString());
				throw new ConstraintViolationException(constraintViolations);
			}

			return new FacebookUser(this);

		}

	}

}
