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

import net.sf.json.util.PropertyFilter;

/**
 * {@code java --> json} 时候的, 属性过滤器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 * @since 1.11.0 change package
 */
class ArrayContainsPropertyNamesPropertyFilter implements PropertyFilter{

    /** The property names. */
    private final String[] propertyNames;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param propertyNames
     *            the property names
     */
    public ArrayContainsPropertyNamesPropertyFilter(String...propertyNames){
        this.propertyNames = propertyNames;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.json.util.PropertyFilter#apply(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean apply(Object source,String name,Object value){
        // [source] the owner of the property
        // [name] the name of the property
        // [value] the value of the property
        return !org.apache.commons.lang3.ArrayUtils.contains(propertyNames, name);
    }
}
