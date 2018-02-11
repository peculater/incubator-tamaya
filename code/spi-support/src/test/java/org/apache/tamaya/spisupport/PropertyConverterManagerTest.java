/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.tamaya.spisupport;



import java.lang.reflect.Method;
import org.apache.tamaya.ConfigurationProvider;
import org.apache.tamaya.spi.ConversionContext;
import org.apache.tamaya.spi.PropertyConverter;
import org.apache.tamaya.TypeLiteral;
import org.junit.Test;

import java.util.List;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.junit.Ignore;

public class PropertyConverterManagerTest {

    private final ConversionContext DUMMY_CONTEXT = new ConversionContext.Builder(
            "someKey", TypeLiteral.of(Object.class)).build();

    @Test
    public void customTypeWithFactoryMethodOfIsRecognizedAsSupported() {
        PropertyConverterManager manager = new PropertyConverterManager(true);

        assertThat(manager.isTargetTypeSupported(TypeLiteral.of(MyType.class)),
                   is(true));
    }

    @Test
    public void factoryMethodOfIsUsedAsConverter() {
        PropertyConverterManager manager = new PropertyConverterManager(true);

        List<PropertyConverter<MyType>> converters = manager.getPropertyConverters(
                (TypeLiteral)TypeLiteral.of(MyType.class));

        assertThat(converters, hasSize(1));

        PropertyConverter<MyType> converter = converters.get(0);

        Object result = converter.convert("IN", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(MyType.class));
        assertThat(((MyType)result).getValue(), equalTo("IN"));
    }

    @Test
    public void testDirectConverterMapping(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<C>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(C.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<C> converter = converters.get(0);
        C result = converter.convert("testDirectConverterMapping", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat((result).getInValue(), equalTo("testDirectConverterMapping"));
    }

    @Test
    public void testDirectSuperclassConverterMapping(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<B>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(B.class)));
        assertThat(converters, hasSize(1));
        converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(B.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<B> converter = converters.get(0);
        B result = converter.convert("testDirectSuperclassConverterMapping", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat(((C)result).getInValue(), equalTo("testDirectSuperclassConverterMapping"));
    }

    @Test
    public void testMultipleConverterLoad(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<B>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(B.class)));
        assertThat(converters, hasSize(1));
        manager = new PropertyConverterManager(true);
        converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(B.class)));
        assertThat(converters, hasSize(1));
    }

    @Test
    public void testTransitiveSuperclassConverterMapping(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<A>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(A.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<A> converter = converters.get(0);
        A result = converter.convert("testTransitiveSuperclassConverterMapping", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat(((C)result).getInValue(), equalTo("testTransitiveSuperclassConverterMapping"));
    }

    @Test
    public void testDirectInterfaceMapping(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<Readable>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(Readable.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<Readable> converter = converters.get(0);
        Readable result = converter.convert("testDirectInterfaceMapping", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat(((C)result).getInValue(), equalTo("testDirectInterfaceMapping"));
    }

    @Test
    public void testTransitiveInterfaceMapping1(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<Runnable>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(Runnable.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<Runnable> converter = converters.get(0);
        Runnable result = converter.convert("testTransitiveInterfaceMapping1", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat(((C)result).getInValue(), equalTo("testTransitiveInterfaceMapping1"));
    }

    @Test
    public void testTransitiveInterfaceMapping2(){
        PropertyConverterManager manager = new PropertyConverterManager(true);
        List<PropertyConverter<AutoCloseable>> converters = List.class.cast(manager.getPropertyConverters(TypeLiteral.of(AutoCloseable.class)));
        assertThat(converters, hasSize(1));

        PropertyConverter<AutoCloseable> converter = converters.get(0);
        AutoCloseable result = converter.convert("testTransitiveInterfaceMapping2", DUMMY_CONTEXT);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(C.class));
        assertThat(((C)result).getInValue(), equalTo("testTransitiveInterfaceMapping2"));
    }
    
    @Ignore
    @Test
    public void testMapBoxedType() throws Exception{
        PropertyConverterManager manager = new PropertyConverterManager(false);        
        Method method = PropertyConverterManager.class.getDeclaredMethod("mapBoxedType", TypeLiteral.class);
        method.setAccessible(true);
        
        assertEquals(TypeLiteral.of(Integer.class),
                method.invoke(manager, TypeLiteral.of(int.class)));
        assertEquals(TypeLiteral.of(Short.class),
                method.invoke(manager, TypeLiteral.of(short.class)));
        assertEquals(TypeLiteral.of(Byte.class),
                method.invoke(manager, TypeLiteral.of(byte.class)));
        assertEquals(TypeLiteral.of(Long.class),
                method.invoke(manager, TypeLiteral.of(long.class)));
        assertEquals(TypeLiteral.of(Boolean.class),
                method.invoke(manager, TypeLiteral.of(boolean.class)));
        assertEquals(TypeLiteral.of(Character.class),
                method.invoke(manager, TypeLiteral.of(char.class)));
        assertEquals(TypeLiteral.of(Float.class),
                method.invoke(manager, TypeLiteral.of(float.class)));
        assertEquals(TypeLiteral.of(Double.class),
                method.invoke(manager, TypeLiteral.of(double.class)));
        
        assertEquals(TypeLiteral.of(Integer[].class),
                method.invoke(manager, TypeLiteral.of(int[].class)));
        assertEquals(TypeLiteral.of(Short[].class),
                method.invoke(manager, TypeLiteral.of(short[].class)));
        assertEquals(TypeLiteral.of(Byte[].class),
                method.invoke(manager, TypeLiteral.of(byte[].class)));
        assertEquals(TypeLiteral.of(Long[].class),
                method.invoke(manager, TypeLiteral.of(long[].class)));
        assertEquals(TypeLiteral.of(Boolean[].class),
                method.invoke(manager, TypeLiteral.of(boolean[].class)));
        assertEquals(TypeLiteral.of(Character[].class),
                method.invoke(manager, TypeLiteral.of(char[].class)));
        assertEquals(TypeLiteral.of(Float[].class),
                method.invoke(manager, TypeLiteral.of(float[].class)));
        assertEquals(TypeLiteral.of(Double[].class),
                method.invoke(manager, TypeLiteral.of(double[].class)));        
    }

    public static class MyType {
        private final String typeValue;

        private MyType(String value) {
            typeValue = value;
        }

        public static MyType of(String source) {
            return new MyType(source);
        }

        public String getValue() {
            return typeValue;
        }

    }

}