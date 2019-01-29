package faceservice.tools.Constraint;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;


public class CheckImageImpl implements ConstraintValidator<ValidImage,File> {
    @Autowired
    private Image image;
    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }
    @Override
    public boolean isValid(File value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(image.isPic(value)){
            return true;
        }else
            return false;

    }
}