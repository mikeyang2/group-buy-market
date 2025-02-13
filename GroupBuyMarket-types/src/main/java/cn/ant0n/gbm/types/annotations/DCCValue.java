package cn.ant0n.gbm.types.annotations;


import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DCCValue {
    String value() default "0";
    String path() default "";
}
