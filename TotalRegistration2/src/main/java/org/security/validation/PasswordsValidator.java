package org.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.security.model.User;

/**
 *
 * @author newlife
 */
public class PasswordsValidator implements ConstraintValidator<PasswordsConstraint, Object> {

    @Override
    public void initialize(final PasswordsConstraint constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final User user = (User) obj;
        return user.getPassword().equals(user.getConfirm());
    }
}
