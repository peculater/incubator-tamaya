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

import java.util.List;
import java.util.Map;
import org.apache.tamaya.TypeLiteral;
import org.apache.tamaya.spi.ConfigurationContextBuilder;
import org.apache.tamaya.spi.PropertyConverter;
import org.apache.tamaya.spi.PropertyFilter;
import org.apache.tamaya.spi.PropertySource;
import org.apache.tamaya.spi.PropertyValueCombinationPolicy;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author William.Lieurance 2018.02.18
 */
public class DefaultConfigurationContextTest {
    
    @Test
    public void testEqualsAndHashAndToStringValues() {
        PropertySource sharedSource = new MockedPropertySource();
        DefaultConfigurationContext ctx1 = (DefaultConfigurationContext) new DefaultConfigurationContextBuilder().build();
        ctx1.addPropertySources(sharedSource);
        DefaultConfigurationContext ctx2 = (DefaultConfigurationContext) new DefaultConfigurationContextBuilder().build();
        ctx2.addPropertySources(sharedSource);
        DefaultConfigurationContext ctx3 = (DefaultConfigurationContext) new DefaultConfigurationContextBuilder().build();
        ctx3.addPropertySources(new MockedPropertySource());

        assertEquals(ctx1, ctx1);
        assertNotEquals(null, ctx1);
        assertNotEquals("aString", ctx1);
        assertEquals(ctx1, ctx2);
        assertNotEquals(ctx1, ctx3);
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertNotEquals(ctx1.hashCode(), ctx3.hashCode());
        String spaces = new String(new char[70 - sharedSource.getName().length()]).replace("\0", " ");
        System.out.println(ctx1.toString());
        assertTrue(ctx1.toString().contains(sharedSource.getName() + spaces));
    }

    /**
     * Test of getPropertySources method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertySources() {
        System.out.println("getPropertySources");
        DefaultConfigurationContext instance = null;
        List<PropertySource> expResult = null;
        List<PropertySource> result = instance.getPropertySources();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertySource method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertySource() {
        System.out.println("getPropertySource");
        String name = "";
        DefaultConfigurationContext instance = null;
        PropertySource expResult = null;
        PropertySource result = instance.getPropertySource(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPropertyConverter method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testAddPropertyConverter() {
        System.out.println("addPropertyConverter");
        DefaultConfigurationContext instance = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertyConverters method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertyConverters_0args() {
        System.out.println("getPropertyConverters");
        DefaultConfigurationContext instance = null;
        Map<TypeLiteral<?>, List<PropertyConverter<?>>> expResult = null;
        Map<TypeLiteral<?>, List<PropertyConverter<?>>> result = instance.getPropertyConverters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertyConverters method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertyConverters_TypeLiteral() {
        System.out.println("getPropertyConverters");
        DefaultConfigurationContext instance = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertyFilters method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertyFilters() {
        System.out.println("getPropertyFilters");
        DefaultConfigurationContext instance = null;
        List<PropertyFilter> expResult = null;
        List<PropertyFilter> result = instance.getPropertyFilters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPropertyValueCombinationPolicy method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testGetPropertyValueCombinationPolicy() {
        System.out.println("getPropertyValueCombinationPolicy");
        DefaultConfigurationContext instance = null;
        PropertyValueCombinationPolicy expResult = null;
        PropertyValueCombinationPolicy result = instance.getPropertyValueCombinationPolicy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toBuilder method, of class DefaultConfigurationContext.
     */
    @Ignore
    @Test
    public void testToBuilder() {
        System.out.println("toBuilder");
        DefaultConfigurationContext instance = null;
        ConfigurationContextBuilder expResult = null;
        ConfigurationContextBuilder result = instance.toBuilder();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
