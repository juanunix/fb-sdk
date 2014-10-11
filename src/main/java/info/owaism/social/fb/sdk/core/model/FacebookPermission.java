/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.core.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import info.owaism.social.fb.sdk.constant.FacebookPermissionsE;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single Facebook permission
 */
public class FacebookPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6931377579483116589L;
	
	/**
	 * Facebook permission.
	 */
	private FacebookPermissionsE permission;
	/**
	 * Permission Grant status
	 */
	private boolean granted;

	/**
	 * Default Constructor.
	 * @param permission Facebook Permission.
	 * @param status Status of the permission.
	 */
	@JsonCreator
	public FacebookPermission(@NotBlank @JsonProperty("permission") String permission, @NotBlank @JsonProperty("status")String status) {
		checkArgument(!isNullOrEmpty(permission));
		checkArgument(!isNullOrEmpty(status));
		this.permission = FacebookPermissionsE.valueOf(permission.trim().toUpperCase());
		this.granted = status.trim().equalsIgnoreCase("granted")?true:false;
	}
	
	/**
	 * Gets the facebook permission.
	 * @return
	 */
	public FacebookPermissionsE permission(){
		return this.permission;
	}
	
	/**
	 * Lets you know if the facebook permission is granted or not.
	 * @return
	 */
	public boolean granted(){
		return this.granted;
	}
	
	@Override
	public String toString() {
		return String.format("{\"%s\":%b}",this.permission.name(),this.granted);
	}

}
