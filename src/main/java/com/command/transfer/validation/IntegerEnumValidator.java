package com.command.transfer.validation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Map;

/**
 * IntegerEnumValidator
 *
 * @author liulei
 * @date 2020/11/9
 */
public class IntegerEnumValidator implements ConstraintValidator<IntegerEnum, Object> {
   private final String INTVALUES = "intValues";
   private IntegerEnum annotation;

   @Override
   public void initialize(IntegerEnum constraintAnnotation) {
      System.out.println("qwd");
      annotation = constraintAnnotation;
   }

   @Override
   public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
      if (o == null) {
         return true;
      }
      ConstraintDescriptor<?> constraintDescriptor = ((ConstraintValidatorContextImpl) constraintValidatorContext).getConstraintDescriptor();
      Map<String, Object> attributes = constraintDescriptor.getAttributes();
      int[] intValues = (int[]) attributes.get(INTVALUES);
      boolean hasFlag = false;
      for (int intValue : intValues) {
         if (intValue == Integer.parseInt(o.toString())) {
            hasFlag = true;
            break;
         }
      }
      return hasFlag;
   }
}
