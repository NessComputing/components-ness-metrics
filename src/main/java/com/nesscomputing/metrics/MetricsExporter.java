package com.nesscomputing.metrics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nesscomputing.lifecycle.LifecycleStage;
import com.nesscomputing.lifecycle.guice.OnStage;
import com.nesscomputing.logging.Log;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.reporting.GangliaReporter;

@Singleton
public class MetricsExporter {
    private static final Log LOG = Log.findLog();

    private final MetricsRegistry registry;
    private final MetricsConfiguration config;
    private GangliaReporter gangliaReporter;

    @Inject
    MetricsExporter(MetricsRegistry registry, MetricsConfiguration config) {
        this.registry = registry;
        this.config = config;
    }

    @OnStage(LifecycleStage.START)
    public void exportMetrics() throws IOException {

        if (!config.isExportingEnabled()) {
            LOG.info("Metrics collection and reporting disabled by configuration.");
            return; // Nothing to do
        }

        LOG.info("Metrics will be collected and reported every %s and package names will%s be exported",
                config.getReportingInterval(),
                config.isCompressPackageNamesEnabled() ? "" : " not");

        if (config.isGangliaReportingEnabled()) {
            LOG.info("Reporting collected metrics to Ganglia at %s:%s with prefix %s",
                    config.getGangliaHostname(),
                    config.getGangliaPort(),
                    config.getGangliaGroupPrefix());

            gangliaReporter = new GangliaReporter(
                    registry,
                    config.getGangliaHostname(),
                    config.getGangliaPort(),
                    config.getGangliaGroupPrefix(),
                    MetricPredicate.ALL,
                    config.isCompressPackageNamesEnabled());

            gangliaReporter.start(config.getReportingInterval().getMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @OnStage(LifecycleStage.STOP)
    public void unexportMetrics() {
        LOG.info("Shutting down metrics");

        if (gangliaReporter != null) {
            gangliaReporter.shutdown();
            gangliaReporter = null;
        }
    }

}
