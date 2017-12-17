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

import javax.config.spi.ConfigSource;
import javax.config.spi.ConfigSourceProvider;
import java.util.*;

/**
 * A Buildable property source.
 */
public class BuildableConfigSourceProvider implements ConfigSourceProvider{

    private List<ConfigSource> sources = new ArrayList<>();

    @Override
    public Collection<ConfigSource> getConfigSources(ClassLoader cl) {
        return Collections.unmodifiableCollection(sources);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildableConfigSourceProvider that = (BuildableConfigSourceProvider) o;

        return sources.equals(that.sources);
    }

    @Override
    public int hashCode() {
        return sources.hashCode();
    }

    @Override
    public String toString() {
        return "BuildablePropertySourceProvider{" +
                "sources=" + sources +
                '}';
    }

    /**
     * Builder builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }




    /**
     * The type Builder.
     */
    public static final class Builder {
        private List<ConfigSource> sources = new ArrayList<>();

        private Builder() {
        }

        /**
         * With propertySources.
         *
         * @param propertySources the propertySources
         * @return the builder
         */
        public Builder withPropertySourcs(ConfigSource... propertySources) {
            this.sources.addAll(Arrays.asList(propertySources));
            return this;
        }

        /**
         * With property sources builder.
         *
         * @param sources the property sources
         * @return the builder
         */
        public Builder withPropertySourcs(Collection<ConfigSource> sources) {
            this.sources.addAll(sources);
            return this;
        }

        /**
         * Build buildable property source.
         *
         * @return the buildable property source
         */
        public BuildableConfigSourceProvider build() {
            BuildableConfigSourceProvider buildablePropertySource = new BuildableConfigSourceProvider();
            buildablePropertySource.sources.addAll(this.sources);
            return buildablePropertySource;
        }
    }
}
