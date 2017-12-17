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
package org.apache.tamaya.base.configsource;

import org.apache.tamaya.base.configsource.EnvironmentConfigSource;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link EnvironmentConfigSource}.
 */
public class EnvironmentPropertySourceTest {

    private final EnvironmentConfigSource envPropertySource = new EnvironmentConfigSource();

    @Test
    public void testGetOrdinal() throws Exception {
        assertEquals(EnvironmentConfigSource.DEFAULT_ORDINAL, envPropertySource.getOrdinal());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("environment-properties", envPropertySource.getName());
    }

    @Test
    public void testGet() throws Exception {
        for (Map.Entry<String, String> envEntry : System.getenv().entrySet()) {
            assertEquals(envPropertySource.getValue(envEntry.getKey()), envEntry.getValue());
        }
    }

    @Test
    public void testGetProperties() throws Exception {
        Map<String, String> props = envPropertySource.getProperties();
        for(Map.Entry<String,String> en: props.entrySet()){
            if(!en.getKey().endsWith("[meta]")){
                assertEquals(System.getenv(en.getKey()), en.getValue());
            }
        }
    }

}