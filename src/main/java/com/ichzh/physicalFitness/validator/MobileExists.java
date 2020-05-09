/**
 * 
 */
package com.ichzh.physicalFitness.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author audin
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MobileExistsValidator.class)
public @interface MobileExists {
	String message() default "{com.ichzh.ocpmpfs.validator.MobileExists.message}";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
