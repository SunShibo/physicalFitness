/**
 * 
 */
package com.ichzh.physicalFitness.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author audin
 *
 */
public class UsernameExistsValidator implements ConstraintValidator<EmailExists, String> {
//	@Autowired DmUserRepository dmUserRepository;

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(EmailExists constraintAnnotation) {
	}

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return false;
//		return dmUserRepository.findByUsername(value) == null;
	}

}
