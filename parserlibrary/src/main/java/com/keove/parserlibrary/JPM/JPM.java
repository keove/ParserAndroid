package com.keove.parserlibrary.JPM;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Created by cagriozdes on 08/03/16.
 */


//test
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface JPM
{
    public static final String Jobj = "json_objet";
    public static final String Jar = "json_array";
    public static final String S = "string";
    public static final String Sar = "string_array";
    public static final String D = "double";
    public static final String Dar = "double_array";
    public static final String I = "integer";
    public static final String Iar = "integer_array";
    public static final String F = "float";
    public static final String Far = "float_array";
    public static final String B = "boolean";
    public static final String Bar = "boolean_array";
    public static final String Xobj = "complex_object";
    public static final String Xarr = "complex_object_array";
    public static final String Date = "date";
    public static final String Typeface = "typeface";
    public static final String Enum = "enum";


    public String Type() default S;

    public Class Klass() default String.class;

    public String Format() default "dd.MM.yyyy";

}
