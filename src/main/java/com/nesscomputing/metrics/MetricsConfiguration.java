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

import org.skife.config.Config;
import org.skife.config.Default;
import org.skife.config.DefaultNull;
import org.skife.config.TimeSpan;

interface MetricsConfiguration {

    /**
     * Whether to enable anything at all.  If false, nothing happens
     */
    @Config("ness.metrics.export")
    @Default("false")
    boolean isExportingEnabled();

    /**
     * How frequently to collect and report statistics
     */
    @Config("ness.metrics.report-interval")
    @Default("10s")
    TimeSpan getReportingInterval();

    /**
     * Whether to compress package names (i.e. com.nesscomputing.metrics.SomeMetric becomse c.n.m.SomeMetric)
     */
    @Config("ness.metrics.compress-package-names")
    @Default("true")
    boolean isCompressPackageNamesEnabled();

    /**
     * Whether to export metrics data to Ganglia over UDP
     */
    @Config("ness.metrics.ganglia.enabled")
    @Default("false")
    boolean isGangliaReportingEnabled();

    /**
     * Prefix for the Ganglia groupname
     */
    @Config("ness.metrics.ganglia.group-prefix")
    @DefaultNull
    String getGangliaGroupPrefix();

    /**
     * Hostname to send Ganglia packets to
     */
    @Config("ness.metrics.ganglia.host")
    @Default("localhost")
    String getGangliaHostname();

    /**
     * Port to send Ganglia packets to
     */
    @Config("ness.metrics.ganglia.port")
    @Default("8649")
    int getGangliaPort();

    /**
     * Whether to export metrics data to Graphite's Carbon database server
     */
    @Config("ness.metrics.graphite.enabled")
    @Default("false")
    boolean isGraphiteReportingEnabled();

    /**
     * Prefix for the Graphite data
     */
    @Config("ness.metrics.graphite.group-prefix")
    @DefaultNull
    String getGraphiteGroupPrefix();

    /**
     * Graphite hostname
     */
    @Config("ness.metrics.graphite.host")
    @Default("localhost")
    String getGraphiteHostname();

    /**
     * Graphite port
     */
    @Config("ness.metrics.graphite.port")
    @Default("2003")
    int getGraphitePort();
}
