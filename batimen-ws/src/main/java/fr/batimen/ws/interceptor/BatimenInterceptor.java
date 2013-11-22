package fr.batimen.ws.interceptor;

import java.util.Collection;
import java.util.Set;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.batimen.core.exception.ValidationDTOBatimenException;
import fr.batimen.dto.AbstractDTO;

public class BatimenInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(BatimenInterceptor.class);

	@AroundInvoke
	public Object validateDTO(InvocationContext ctx) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Debut de validation des DTO pour la méthode :" + ctx.getMethod().getName());
		}

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		StringBuilder message = new StringBuilder();

		Object[] params = ctx.getParameters();
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null && (params[i] instanceof AbstractDTO)) {
					if (message == null) {
						message = new StringBuilder();
					}
					message = validateObject(validator, message, params[i]);
				} else if (params[i] != null && Collection.class.isAssignableFrom(params[i].getClass())) {
					// Cas d'une collection
					Collection<?> collectionToTest = (Collection<?>) params[i];
					for (Object object : collectionToTest) {
						message = validateObject(validator, message, object);
					}
				}
			}
		}

		if (message.length() > 0) {
			throw new ValidationDTOBatimenException(message.toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Debut de la méthode :" + ctx.getMethod().getName());
		}

		Object results = ctx.proceed();

		if (logger.isDebugEnabled()) {
			logger.debug("Fin de la méthode :" + ctx.getMethod().getName());
		}

		return results;
	}

	private StringBuilder validateObject(Validator validator, StringBuilder message, Object param) {
		Set<ConstraintViolation<Object>> violations = validator.validate(param);
		for (ConstraintViolation<Object> violation : violations) {
			message.append((violation.getRootBeanClass() != null ? violation.getRootBeanClass() : "Inconnue"));
			message.append(".");
			message.append((violation.getPropertyPath() != null ? violation.getPropertyPath() : "Inconnue"));
			message.append(" : ");
			message.append(violation.getMessage());
			message.append(". ");
		}
		return message;
	}

}
