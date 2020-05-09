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
public class MobileExistsValidator implements ConstraintValidator<MobileExists, String> {
//	@Autowired DmUserRepository dmUserRepository;

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(MobileExists constraintAnnotation) {
	}

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
//		return userRepository.findByMobile(value) == null;
		return false;
	}

}
