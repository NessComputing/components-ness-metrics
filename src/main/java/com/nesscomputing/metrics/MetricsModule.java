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
