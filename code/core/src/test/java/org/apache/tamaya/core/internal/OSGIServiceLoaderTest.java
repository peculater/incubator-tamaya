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
package org.apache.tamaya.core.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.Version;

/**
 *
 * @author William.Lieurance 2018-02-06
 */
public class OSGIServiceLoaderTest {

    /**
     * Test of getBundleContext method, of class OSGIServiceLoader.
     */
    @Test
    public void testGetBundleContext() {
        BundleContext mockBundleContext = new MockBundleContext();
        OSGIServiceLoader instance = new OSGIServiceLoader(mockBundleContext);
        BundleContext result = instance.getBundleContext();
        assertEquals(mockBundleContext, result);
    }

    /**
     * Test of getResourceBundles method, of class OSGIServiceLoader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetResourceBundles() throws Exception {
        MockBundleContext mockBundleContext = new MockBundleContext();
        MockBundle startedBundle = new MockBundle();
        startedBundle.setState(Bundle.ACTIVE);
        startedBundle.setBundleId(1);
        startedBundle.setBundleContext(mockBundleContext);
        mockBundleContext.installBundle(startedBundle);
        OSGIServiceLoader instance = new OSGIServiceLoader(mockBundleContext);
        Set<Bundle> result = instance.getResourceBundles();
        assertFalse(result.isEmpty());
    }

    /**
     * Test of bundleChanged method, of class OSGIServiceLoader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testBundleChanged() throws Exception {
        //Set up mocks
        Set<Bundle> resultBundles;
        MockBundleContext mockBundleContext = new MockBundleContext();
        MockBundle startedBundle = new MockBundle();
        startedBundle.setState(Bundle.ACTIVE);
        startedBundle.setBundleId(1);
        startedBundle.setBundleContext(mockBundleContext);
        MockBundle stoppedBundle = new MockBundle();
        stoppedBundle.setState(Bundle.INSTALLED);
        stoppedBundle.setBundleId(2);
        stoppedBundle.setBundleContext(mockBundleContext);
        MockBundle flipBundle = new MockBundle();
        flipBundle.setState(Bundle.INSTALLED);
        flipBundle.setBundleId(3);
        flipBundle.setBundleContext(mockBundleContext);
        mockBundleContext.installBundle(startedBundle);
        mockBundleContext.installBundle(stoppedBundle);
        mockBundleContext.installBundle(flipBundle);

        //Default case
        mockBundleContext.setServiceCount(0);
        OSGIServiceLoader instance = new OSGIServiceLoader(mockBundleContext);
        resultBundles = instance.getResourceBundles();
        assertEquals(1, resultBundles.size());
        assertEquals(2, mockBundleContext.getServiceCount());

        //After start
        mockBundleContext.setServiceCount(0);
        BundleEvent startedEvent = new BundleEvent(BundleEvent.STARTED, flipBundle);
        instance.bundleChanged(startedEvent);
        resultBundles = instance.getResourceBundles();
        assertEquals(2, resultBundles.size());
        assertEquals(2, mockBundleContext.getServiceCount());

        //After stop
        mockBundleContext.setServiceCount(0);
        BundleEvent stoppedEvent = new BundleEvent(BundleEvent.STOPPED, flipBundle);
        instance.bundleChanged(stoppedEvent);
        resultBundles = instance.getResourceBundles();
        assertEquals(1, resultBundles.size());
        assertEquals(0, mockBundleContext.getServiceCount());
    }

    /**
     * Mocked OSGi objects
     */
    private class MockBundleContext implements BundleContext {

        private ArrayList<Bundle> bundles = new ArrayList<>();

        @Override
        public String getProperty(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Bundle getBundle() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Bundle installBundle(String string, InputStream in) throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Bundle installBundle(String string) throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        public Bundle installBundle(Bundle bundle) {
            bundles.add(bundle);
            return bundle;
        }

        @Override
        public Bundle getBundle(long l) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Bundle[] getBundles() {
            return bundles.toArray(new Bundle[bundles.size()]);
        }

        @Override
        public void addServiceListener(ServiceListener sl, String string) throws InvalidSyntaxException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void addServiceListener(ServiceListener sl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void removeServiceListener(ServiceListener sl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void addBundleListener(BundleListener bl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void removeBundleListener(BundleListener bl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void addFrameworkListener(FrameworkListener fl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void removeFrameworkListener(FrameworkListener fl) {
            throw new UnsupportedOperationException("Not supported.");
        }

        int serviceCount = 0;

        public int getServiceCount() {
            return serviceCount;
        }

        public void setServiceCount(int serviceCount) {
            this.serviceCount = serviceCount;
        }
        
        @Override
        public ServiceRegistration<?> registerService(String[] strings, Object o, Dictionary<String, ?> dctnr) {
            serviceCount++;
            return null;
        }

        @Override
        public ServiceRegistration<?> registerService(String string, Object o, Dictionary<String, ?> dctnr) {
            serviceCount++;
            return null;
        }

        @Override
        public <S> ServiceRegistration<S> registerService(Class<S> type, S s, Dictionary<String, ?> dctnr) {
            serviceCount++;
            return null;
        }

        @Override
        public ServiceReference<?>[] getServiceReferences(String string, String string1) throws InvalidSyntaxException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public ServiceReference<?>[] getAllServiceReferences(String string, String string1) throws InvalidSyntaxException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public ServiceReference<?> getServiceReference(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public <S> ServiceReference<S> getServiceReference(Class<S> type) {
            return null;
        }

        @Override
        public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> type, String string) throws InvalidSyntaxException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public <S> S getService(ServiceReference<S> sr) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean ungetService(ServiceReference<?> sr) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public File getDataFile(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Filter createFilter(String string) throws InvalidSyntaxException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Bundle getBundle(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }
    };

    private class MockBundle implements Bundle {

        private int state = Bundle.ACTIVE;

        @Override
        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        @Override
        public void start(int i) throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void start() throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void stop(int i) throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void stop() throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void update(InputStream in) throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void update() throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void uninstall() throws BundleException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Dictionary<String, String> getHeaders() {
            return new Hashtable<>();
        }

        private long bundleId = 1L;

        @Override
        public long getBundleId() {
            return bundleId;
        }

        public void setBundleId(long bundleId) {
            this.bundleId = bundleId;
        }

        @Override
        public String getLocation() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public ServiceReference<?>[] getRegisteredServices() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public ServiceReference<?>[] getServicesInUse() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean hasPermission(Object o) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public URL getResource(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Dictionary<String, String> getHeaders(String string) {
            return new Hashtable<>();
        }

        @Override
        public String getSymbolicName() {
            return "MockBundle";
        }

        @Override
        public Class<?> loadClass(String string) throws ClassNotFoundException {
            if (string.contains("org.something.else") || string.endsWith("/")) {
                throw new UnsupportedOperationException("Requested class that should not be requested: " + string);
            }
            return String.class;
        }

        @Override
        public Enumeration<URL> getResources(String string) throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Enumeration<String> getEntryPaths(String string) {
            Vector<String> v = new Vector<>();
            v.add("META-INF/services/" + "someslash/");
            v.add("META-INF/services/" + "org.apache.tamaya");
            v.add("META-INF/services/" + "org.something.else");
            return v.elements();
        }

        @Override
        public URL getEntry(String string) {
            if (string.equals("META-INF/services/")) {
                try {
                    return new URL("file:///");
                } catch (MalformedURLException ex) {
                    return null;
                }
            }
            if (string.contains("org.something.else") || string.endsWith("/")) {
                throw new UnsupportedOperationException("Requested entry that should not be requested: " + string);
            }
            return getClass().getClassLoader().getResource("osgi-entry.txt");
        }

        @Override
        public long getLastModified() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Enumeration<URL> findEntries(String string, String string1, boolean bln) {
            throw new UnsupportedOperationException("Not supported.");
        }

        private BundleContext bundleContext = new MockBundleContext();

        @Override
        public BundleContext getBundleContext() {
            return bundleContext;
        }

        public void setBundleContext(BundleContext bundleContext) {
            this.bundleContext = bundleContext;
        }

        @Override
        public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int i) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Version getVersion() {
            return new Version(0, 0, 1);
        }

        @Override
        public <A> A adapt(Class<A> type) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public File getDataFile(String string) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int compareTo(Bundle o) {
            return Long.compare(this.getBundleId(), o.getBundleId());
        }

    }
}
