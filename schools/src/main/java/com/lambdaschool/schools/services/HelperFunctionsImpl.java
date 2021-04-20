package com.lambdaschool.schools.services;

import com.lambdaschool.schools.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl implements HelperFunctions{
    @Override
    public List<ValidationError> getValidationErrors(Throwable cause) {
        List<ValidationError> validationErrorList = new ArrayList<>();

      while (cause != null && !(cause instanceof ConstraintViolationException || cause instanceof MethodArgumentNotValidException)) {
          cause = cause.getCause();
      }

      if(cause != null ) {
          if(cause instanceof ConstraintViolationException) {
              ConstraintViolationException ex = (ConstraintViolationException) cause;
              System.out.printf("ConstraintViolationException!!!!!!!!!!!" );
              ValidationError newVE = new ValidationError();
              newVE.setMessage(ex.getConstraintName());
              newVE.setFieldName(ex.getMessage());

              validationErrorList.add(newVE);
          } else {
              MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;
              System.out.printf("MethodArgumentNotValidException!!!!!!!!!!!" );
              List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
              for(FieldError fe : fieldErrors) {
                ValidationError newVE = new ValidationError();
                newVE.setMessage(fe.getDefaultMessage());
                newVE.setFieldName(fe.getField());

                validationErrorList.add(newVE);
              }
          }
      }

        return validationErrorList;
    }
}
