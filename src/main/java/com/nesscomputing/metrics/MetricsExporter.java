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
import com.yammer.metrics.reporting.GraphiteReporter;

@Singleton
class MetricsExporter {
    private static final Log LOG = Log.findLog();

    private final MetricsRegistry registry;
    private final MetricsConfiguration config;
    private GangliaReporter gangliaReporter;
    private GraphiteReporter graphiteReporter;

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

        LOG.info("Metrics will be collected and reported every %s and package names will%s be shortened",
                config.getReportingInterval(),
                config.isCompressPackageNamesEnabled() ? "" : " not");

        exportGanglia();
        exportGraphite();
    }

    private void exportGanglia() throws IOException {
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

    private void exportGraphite() throws IOException {
        if (config.isGraphiteReportingEnabled()) {
            LOG.info("Reporting collected metrics to Graphite at %s:%s with prefix %s",
                    config.getGraphiteHostname(),
                    config.getGraphitePort(),
                    config.getGraphiteGroupPrefix());

            graphiteReporter = new GraphiteReporter(
                    registry,
                    config.getGraphiteHostname(),
                    config.getGraphitePort(),
                    config.getGraphiteGroupPrefix());

            graphiteReporter.start(config.getReportingInterval().getMillis(), TimeUnit.MILLISECONDS);
        }
    }

    @OnStage(LifecycleStage.STOP)
    public void unexportMetrics() {
        LOG.info("Shutting down metrics");

        if (gangliaReporter != null) {
            gangliaReporter.shutdown();
            gangliaReporter = null;
        }

        if (graphiteReporter != null) {
            graphiteReporter.shutdown();
            graphiteReporter = null;
        }
    }

}
