package com.ichzh.physicalFitness.security;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;

public class ReflectionUserSource {

	private String userPropertyToUse = "user";
	
	public Object getUser(UserDetails user) {
		Method saltMethod = findSaltMethod(user);

		try {
			return saltMethod.invoke(user);
		}
		catch (Exception exception) {
			throw new AuthenticationServiceException(exception.getMessage(), exception);
		}
	}
	
	private Method findSaltMethod(UserDetails user) {
		Method saltMethod = ReflectionUtils.findMethod(user.getClass(),
				userPropertyToUse, new Class[0]);

		if (saltMethod == null) {
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(user.getClass(),
					userPropertyToUse);

			if (pd != null) {
				saltMethod = pd.getReadMethod();
			}

			if (saltMethod == null) {
				throw new AuthenticationServiceException(
						"Unable to find user method on user Object. Does the class '"
								+ user.getClass().getName()
								+ "' have a method or getter named '" + userPropertyToUse
								+ "' ?");
			}
		}

		return saltMethod;
	}

	
}
