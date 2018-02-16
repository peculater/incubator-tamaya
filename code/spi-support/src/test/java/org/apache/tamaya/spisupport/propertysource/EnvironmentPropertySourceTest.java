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
package org.apache.tamaya.spisupport.propertysource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.tamaya.spisupport.propertysource.EnvironmentPropertySource;
import org.apache.tamaya.spi.PropertyValue;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Tests for {@link EnvironmentPropertySource}.
 */
public class EnvironmentPropertySourceTest {

    private final EnvironmentPropertySource envPropertySource = new EnvironmentPropertySource();

    @Test
    public void testConstructionProperties() throws IOException {
        StringWriter stringBufferWriter = new StringWriter();
        System.getProperties().store(stringBufferWriter, null);
        String before = stringBufferWriter.toString();
        
        try{
            EnvironmentPropertySource environmentSource = new EnvironmentPropertySource();
            assertFalse(environmentSource.isDisabled());
            
            System.setProperty("tamaya.envprops.prefix", "fakeprefix");
            System.setProperty("tamaya.envprops.disable", "true");
            environmentSource = new EnvironmentPropertySource();
            //assertEquals("fakeprefix", environmentSource.getPrefix());
            assertTrue(environmentSource.isDisabled());
            assertNull(environmentSource.get(System.getenv().entrySet().iterator().next().getKey()));
            assertTrue(environmentSource.getName().contains("(disabled)"));
            assertTrue(environmentSource.getProperties().isEmpty());
            assertTrue(environmentSource.toString().contains("disabled=true"));
            
            System.getProperties().clear();
            System.getProperties().load(new StringReader(before));
            System.setProperty("tamaya.defaults.disable", "true");
            environmentSource = new EnvironmentPropertySource();
            assertTrue(environmentSource.isDisabled());
            
            System.getProperties().clear();
            System.getProperties().load(new StringReader(before));
            System.setProperty("tamaya.envprops.disable", "");
            environmentSource = new EnvironmentPropertySource();
            assertFalse(environmentSource.isDisabled());
            
            System.getProperties().clear();
            System.getProperties().load(new StringReader(before));
            System.setProperty("tamaya.defaults.disable", "");
            environmentSource = new EnvironmentPropertySource();
            assertFalse(environmentSource.isDisabled());
            
        }finally{
            System.getProperties().clear();
            System.getProperties().load(new StringReader(before));
        }
    }
    
    @Test
    public void testDisabled() {
        
    }

    @Test
    public void testGetOrdinal() throws Exception {
        assertEquals(EnvironmentPropertySource.DEFAULT_ORDINAL, envPropertySource.getOrdinal());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("environment-properties", envPropertySource.getName());
    }

    @Test
    public void testGet() throws Exception {
        for (Map.Entry<String, String> envEntry : System.getenv().entrySet()) {
            assertEquals(envPropertySource.get(envEntry.getKey()).getValue(), envEntry.getValue());
        }
    }

    @Test
    public void testGetProperties() throws Exception {
        Map<String, PropertyValue> props = envPropertySource.getProperties();
        for (Map.Entry<String, PropertyValue> en : props.entrySet()) {
            if (!en.getKey().startsWith("_")) {
                assertEquals(System.getenv(en.getKey()), en.getValue().getValue());
            }
        }
    }

    @Test
    public void testIsScannable() throws Exception {
        assertTrue(envPropertySource.isScannable());
    }
}
