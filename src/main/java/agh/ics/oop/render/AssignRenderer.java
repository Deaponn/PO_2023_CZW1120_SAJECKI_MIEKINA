package agh.ics.oop.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssignRenderer {
    // unit renderer to use
    Class<? extends UnitRenderer<?>> renderer();
    // cannot be used by classes extending this class
    boolean restrictInherit() default false;
}
