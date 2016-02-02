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
package org.apache.tamaya.spi;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by atsticks on 02.02.16.
 */
public class PropertyValueBuilderTest {

    @Test
    public void testSetContextData() throws Exception {
        PropertyValueBuilder b = new PropertyValueBuilder("k", "v");
        Map<String,String> context = new HashMap<>();
        context.put("source", "testSetContextData");
        context.put("ts", String.valueOf(System.currentTimeMillis()));
        context.put("y", "yValue");
        b.setContextData(new HashMap<String, String>());
        b.setContextData(context);
        context.remove("y");
        b.setContextData(context);
        Map<String,String> contextData = b.build().getContextData();
        assertEquals(contextData.size(), context.size());
        assertEquals(contextData.get("_k.source"), "testSetContextData");
        assertNotNull(contextData.get("_k.ts"));
        assertNull(contextData.get("_k.y"));
    }

    @Test
    public void testAddContextData() throws Exception {
        PropertyValueBuilder b = new PropertyValueBuilder("k", "v");
        b.addContextData("source", "testAddContextData");
        b.addContextData("ts", System.currentTimeMillis());
        b.addContextData("y", "yValue");
        b.addContextData("y", "y2");
        Map<String,String> contextData = b.build().getContextData();
        assertEquals(contextData.size(), 3);
        assertEquals(contextData.get("_k.source"), "testAddContextData");
        assertNotNull(contextData.get("_k.ts"));
        assertEquals(contextData.get("_k.y"), "y2");
    }

}