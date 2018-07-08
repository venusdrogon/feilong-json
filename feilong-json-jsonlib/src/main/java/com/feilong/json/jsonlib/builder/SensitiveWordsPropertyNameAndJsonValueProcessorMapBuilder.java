/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.json.jsonlib.builder;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.addAllIgnoreNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.feilong.json.SensitiveWords;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;

import net.sf.json.processors.JsonValueProcessor;

/**
 * 专门用来构造 <code>SensitiveWordsPropertyNameAndJsonValueProcessorMap</code> 的构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder{

    /** The cache. */
    private static Map<Class<?>, Map<String, JsonValueProcessor>> CACHE = newHashMap();

    //---------------------------------------------------------------

    /**
     * Builds the sensitive words property name and json value processor map.
     *
     * @param javaBean
     *            the java bean
     * @return the map
     * @since 1.10.3
     */
    static Map<String, JsonValueProcessor> build(Object javaBean){
        Validate.notNull(javaBean, "javaBean can't be null!");

        //---------------------------------------------------------------

        Class<?> klass = javaBean.getClass();
        if (CACHE.containsKey(klass)){
            return CACHE.get(klass);
        }

        //---------------------------------------------------------------
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = build(klass);
        CACHE.put(klass, propertyNameAndJsonValueProcessorMap);
        return propertyNameAndJsonValueProcessorMap;
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param klass
     *            the klass
     * @return the map
     * @since 1.11.5
     */
    private static Map<String, JsonValueProcessor> build(Class<?> klass){
        List<String> list = getWithAnnotationName(klass, SensitiveWords.class);
        //---------------------------------------------------------------------------------------------------------
        //敏感字段
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        for (String propertyName : list){
            propertyNameAndJsonValueProcessorMap.put(propertyName, SensitiveWordsJsonValueProcessor.INSTANCE);
        }

        return propertyNameAndJsonValueProcessorMap;
    }

    //---------------------------------------------------------------

    /**
     * Gets the with annotation name.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return the with annotation name
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getWithAnnotationName(Class<?> klass,Class<A> annotationCls){
        List<String> list = newArrayList();
        addAllIgnoreNull(list, getFiledNamesWithAnnotation(klass, annotationCls));
        addAllIgnoreNull(list, getPropertyNamesWithAnnotation(klass, annotationCls));
        return list;
    }

    //---------------------------------------------------------------
    /**
     * Gets the filed names with annotation.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return the filed names with annotation
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getFiledNamesWithAnnotation(Class<?> klass,Class<A> annotationCls){
        List<String> list = newArrayList();
        List<Field> fieldsList = FieldUtils.getFieldsListWithAnnotation(klass, annotationCls);
        for (Field field : fieldsList){
            list.add(field.getName());
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * Gets the property names with annotation.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return the property names with annotation
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getPropertyNamesWithAnnotation(Class<?> klass,Class<A> annotationCls){
        List<String> list = newArrayList();

        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(klass);

        if (isNotNullOrEmpty(propertyDescriptors)){
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors){

                Method readMethod = propertyDescriptor.getReadMethod();
                A sensitiveWords = MethodUtils.getAnnotation(readMethod, annotationCls, true, true);
                if (null != sensitiveWords){
                    list.add(propertyDescriptor.getName());
                }
            }
        }
        return list;
    }
}