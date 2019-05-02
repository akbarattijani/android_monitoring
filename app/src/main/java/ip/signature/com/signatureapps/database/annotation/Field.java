package ip.signature.com.signatureapps.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
    String name();
    String type() default "VARCHAR";
    int length() default 255;
    String defaultValue() default "";
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
    boolean notNull() default false;
}
