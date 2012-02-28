/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.metrics;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.nesscomputing.config.ConfigProvider;
import com.yammer.metrics.guice.InstrumentationModule;

public class MetricsModule extends AbstractModule {

    @Override
    protected void configure() {
        install (new InstrumentationModule());

        bind (MetricsConfiguration.class).toProvider(ConfigProvider.of(MetricsConfiguration.class)).in(Scopes.SINGLETON);
        bind (MetricsExporter.class).asEagerSingleton();
    }

}
