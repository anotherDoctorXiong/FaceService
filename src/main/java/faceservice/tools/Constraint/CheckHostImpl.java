package faceservice.tools.Constraint;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static faceservice.tools.Constraint.Host.isHostConnectable;

public class CheckHostImpl implements ConstraintValidator<ValidHost,Object> {

    @Override
    public void initialize(ValidHost constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(isHostConnectable(value.toString())){
            return true;
        }else
            return false;

    }
}
