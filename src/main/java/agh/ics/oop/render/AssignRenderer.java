package agh.ics.oop.render;

import agh.ics.oop.model.WorldElement;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssignRenderer {
    Class<? extends WorldElementRenderer<? extends WorldElement>> renderer();
}
