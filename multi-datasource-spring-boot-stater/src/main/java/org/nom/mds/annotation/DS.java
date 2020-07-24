package org.nom.mds.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The core Annotation to switch dataSource. It can be annotate at class or method.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    /**
     * groupName or specific database name or spring SPEL name.
     *
     * @return the database you want to switch
     */
    String value();
}
