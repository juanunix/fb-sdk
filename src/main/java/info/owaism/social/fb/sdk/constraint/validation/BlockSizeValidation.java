package info.owaism.social.fb.sdk.constraint.validation;

import info.owaism.social.fb.sdk.constraint.BlockSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates the {@link BlockSize} constraint.
 */
public class BlockSizeValidation implements ConstraintValidator<BlockSize, CharSequence> {

	/**
	 * Defined block size.
	 */
	private int blockSize;
	
	/*
	 * (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	public void initialize(BlockSize constraintAnnotation) {
		blockSize = constraintAnnotation.value();
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	public boolean isValid(CharSequence input, ConstraintValidatorContext constraintContext) {
		if(null == input)
		{
			return true;
		}
		
		return (0 != input.length() % blockSize)? false:true;
	}



}
