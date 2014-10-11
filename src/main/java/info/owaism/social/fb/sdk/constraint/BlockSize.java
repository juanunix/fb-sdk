/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.constraint;

import info.owaism.social.fb.sdk.constraint.validation.BlockSizeValidation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Length of the Field with this annotation should be divisible by the passed in
 * Block Size.
 */
@Target({ FIELD, PARAMETER, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BlockSizeValidation.class)
@Documented
public @interface BlockSize {

	int value();
	
	String message() default "Length of ${validatedValue} is not a multiple of {value}. BlockSize validation requires the input value to be a multiple of {value}.";
	
	Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
