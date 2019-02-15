package faceservice.tools.Constraint;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

import static faceservice.tools.Imagehandle.ImageTools.isPic;


public class CheckImageImpl implements ConstraintValidator<ValidImage,File> {

    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }
    @Override
    public boolean isValid(File value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        if(isPic(value)){
            return true;
        }else
            return false;

    }
}