package faceservice.tools.Constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ConstraintImpl.class)
public @interface ValidHost {
    String message();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};

}
