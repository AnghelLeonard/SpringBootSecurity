package org.security.validation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Target;

/**
 *
 * @author newlife
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordsValidator.class)
@Documented
public @interface PasswordsConstraint {

    String message() default "Passwords does not match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
