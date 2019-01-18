package faceservice.tools.Constraint;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConstraintImpl implements ConstraintValidator<ValidHost,Object> {
    @Autowired
    private Host host;
    @Override
    public void initialize(ValidHost constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(host.isHostConnectable(value.toString())){
            return true;
        }else
            return false;

    }
}
