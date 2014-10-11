/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines Constants required for this method execution.
 */
/**
 * Defines the resource of the graph API.
 */
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(Contants.class)
@Documented
public @interface Constant {

	String constantName();
	String constantValue();
}
