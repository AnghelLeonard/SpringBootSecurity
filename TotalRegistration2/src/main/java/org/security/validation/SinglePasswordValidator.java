package org.security.validation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RepeatCharacterRegexRule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

/**
 *
 * @author newlife
 */
public class SinglePasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize(final PasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {

        final PasswordValidator validator = new PasswordValidator(Arrays.
                asList(new LengthRule(6, 20),
                        new RepeatCharacterRegexRule(),
                        new WhitespaceRule()
                // add here more rules
                ));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validator.getMessages(result).toString()).addConstraintViolation();
        return false;
    }

}
