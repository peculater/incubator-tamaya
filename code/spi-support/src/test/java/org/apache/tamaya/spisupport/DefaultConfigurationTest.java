/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya.spisupport;

import java.util.Arrays;
import org.apache.tamaya.TypeLiteral;
import org.apache.tamaya.spi.*;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultConfigurationTest {

    /**
     * Tests for get(String)
     */
    @Test(expected = NullPointerException.class)
    public void getDoesNotAcceptNull() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.get(null);
    }

    /**
     * Tests for get(String, Class)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test(expected = NullPointerException.class)
    public void getDoesNotAcceptNullForClassTargetType() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.get("a", (Class) null);
    }

    /**
     * Tests for get(String, TypeLiteral)
     */
    @Test(expected = NullPointerException.class)
    public void getDoesNotAcceptNullForTypeLiteralTargetType() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.get("a", (TypeLiteral<?>) null);
    }

    @Test
    public void getReturnsNullOrNotAsAppropriate() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertNotNull(c.get("valueOfValid"));
        assertNull(c.get("valueOfNull"));
        assertNull(c.get("Filternull")); //get does apply filtering
    }

    /**
     * Tests for getOrDefault(String, Class, String)
     */
    @Test(expected = NullPointerException.class)
    public void getOrDefaultDoesNotAcceptNullAsKeyForThreeParameterVariant() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.getOrDefault(null, String.class, "ok");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test(expected = NullPointerException.class)
    public void getOrDefaultDoesNotAcceptNullAsTargetTypeForThreeParameterVariant() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.getOrDefault("a", (Class) null, "b");
    }

    /**
     * Tests for getOrDefault(String, TypeLiteral, String)
     */
    @Test(expected = NullPointerException.class)
    public void getOrDefaultDoesNotAcceptNullAsKeyForThreeParameterVariantSecondIsTypeLiteral() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.getOrDefault(null, TypeLiteral.of(String.class), "ok");
    }

    @Test
    public void getOrDefaultDoesAcceptNullAsDefaultValueForThreeParameterVariant() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        assertNotNull(c.getOrDefault("a", String.class, null));
        assertNotNull(c.getOrDefault("a", TypeLiteral.of(String.class), null));
    }

    @Test(expected = NullPointerException.class)
    public void getOrDefaultDoesNotAcceptNullAsTargetTypeForThreeParameterVariantSecondIsTypeLiteral() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.getOrDefault("a", (TypeLiteral<String>) null, "b");
    }

    /**
     * Tests for getOrDefault(String, String)
     */
    @Test(expected = NullPointerException.class)
    public void getOrDefaultDoesNotAcceptNullAsKeyForTwoParameterVariantDefaultValueIsSecond() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.getOrDefault(null, "ok");
    }

    @Test
    public void getOrDefaultDoesAcceptNullAsDefaultValueForTwoParameterVariantDefaultValueIsSecond() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertNotNull(c.getOrDefault("a", null));
    }

    @Test
    public void getOrDefaultReturnDefaultIfValueWouldHaveBeenNull() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertEquals("ok", c.getOrDefault("valueOfNull", "ok"));
        assertEquals("ok", c.getOrDefault("valueOfNull", String.class, "ok"));
        assertEquals("ok", c.getOrDefault("valueOfNull", TypeLiteral.of(String.class), "ok"));
    }

    /**
     * Tests for evaluateRawValue(String)
     */
    @Test
    public void evaluateRawValueReturnsNullOrNotAsAppropriate() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertNotNull(c.evaluteRawValue("valueOfValid"));
        assertNull(c.evaluteRawValue("valueOfNull"));
        assertNotNull(c.evaluteRawValue("Filternull")); //evaluateRawValue does not apply filtering
    }

    /**
     * Tests for getProperties()
     */
    @Test
    public void getPropertiesReturnsNullOrNotAsAppropriate() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        Map<String, String> result = c.getProperties();
        assertEquals("valueFromMockedPropertySource", result.get("someKey"));
        assertNull(result.get("notInThePropertiesMock"));
        assertNull(result.get("valueOfNull"));
        assertNull(result.get("Filternull"));
    }

    /**
     * Tests for convertValue(String key, String value, TypeLiteral<T> type)
     */
    @Test
    public void testConvertValue() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertTrue(100 == (Integer) c.convertValue("aHundred", "100", TypeLiteral.of(Integer.class)));
    }

    @Test(expected = NullPointerException.class)
    public void with_Null() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.with(null);
    }

    @Test(expected = NullPointerException.class)
    public void query_Null() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());

        c.query(null);
    }

    @Test
    public void with() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertEquals(c.with(config -> config), c);
    }

    @Test
    public void query() {
        DefaultConfiguration c = new DefaultConfiguration(new DummyConfigurationContext());
        assertEquals(c.query(config -> "testQ"), "testQ");
    }
    
    @Test
    public void testEqualsAndHashAndToStringValues() {
        ConfigurationContext sharedContext = new DummyConfigurationContext();
        DefaultConfiguration config1 = new DefaultConfiguration(sharedContext);
        DefaultConfiguration config2 = new DefaultConfiguration(sharedContext);
        DefaultConfiguration config3 = new DefaultConfiguration(new DummyConfigurationContext());

        assertEquals(config1, config1);
        assertNotEquals(null, config1);
        assertNotEquals(sharedContext, config1);
        assertNotEquals(config1, sharedContext);
        assertNotEquals("aString", config1);
        assertEquals(config1, config2);
        assertNotEquals(config1, config3);
        assertEquals(config1.hashCode(), config2.hashCode());
        assertNotEquals(config1.hashCode(), config3.hashCode());
        assertTrue(config1.toString().contains("Configuration{"));
    }

    public static class DummyConfigurationContext implements ConfigurationContext {

        PropertyConverterManager pcm = new PropertyConverterManager(false);
        
        public DummyConfigurationContext() {
            pcm.register(TypeLiteral.of(Integer.class), new IntegerTestConverter());
        }
        
        @Override
        public void addPropertySources(PropertySource... propertySources) {
            throw new RuntimeException("addPropertySources should be never called in this test");
        }

        @Override
        public List<PropertySource> getPropertySources() {
            return Arrays.asList(new MockedPropertySource());
        }

        @Override
        public PropertySource getPropertySource(String name) {
            return new MockedPropertySource();
        }

        @Override
        public <T> void addPropertyConverter(TypeLiteral<T> type, PropertyConverter<T> propertyConverter) {
            throw new RuntimeException("addPropertyConverter should be never called in this test");
        }

        @Override
        public Map<TypeLiteral<?>, List<PropertyConverter<?>>> getPropertyConverters() {
            return pcm.getPropertyConverters();
        }
   
        @Override
        public <T> List<PropertyConverter<T>> getPropertyConverters(TypeLiteral<T> type) {
            return pcm.getPropertyConverters(type);
        }

        @Override
        public List<PropertyFilter> getPropertyFilters() {
            return Arrays.asList(new MockedPropertyFilter());
        }

        @Override
        public PropertyValueCombinationPolicy getPropertyValueCombinationPolicy() {
            return PropertyValueCombinationPolicy.DEFAULT_OVERRIDING_POLICY;
        }

        @Override
        public ConfigurationContextBuilder toBuilder() {
            throw new RuntimeException("toBuilder should be never called in this test");
        }
    }
}
