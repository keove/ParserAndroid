package com.keove.parserlibrary;

import android.support.annotation.Nullable;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * Created by cagriozdes on 08/03/16.
 */

public class ParseUtils
{
    public static List<Field> getFieldsUpTo(Class<?> startClass, @Nullable Class<?> exclusiveParent)
    {
        List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&  (exclusiveParent == null || !(parentClass.equals(exclusiveParent))))
        {
            List<Field> parentClassFields = (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }
}
