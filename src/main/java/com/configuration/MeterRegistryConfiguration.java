package com.configuration;

import io.micrometer.azuremonitor.AzureMonitorConfig;
import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Duration;

/**
 * This class is responsible for configuring Micrometer Azure Monitor
 * Registry to collect aggregated metrics.
 */
@WebListener
public class MeterRegistryConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        MeterRegistry azureMeterRegistry = new AzureMonitorMeterRegistry(new AzureMonitorConfig() {
            @Override
            public String get(String key) {
                return null;
            }
            @Override
            public Duration step() {
                // set frequency of how fast metrics will be sent. This can be done to 1 second also.
                return Duration.ofSeconds(5);}

        }, Clock.SYSTEM);

        // bind JvmThreadMetrics to AzureMonitor Registry
        new JvmThreadMetrics().bindTo(azureMeterRegistry);

        //bind JvmMemory Metrics to AzureMonitor Registry
        new JvmMemoryMetrics().bindTo(azureMeterRegistry);

        //bind JvmGC Memory metrics to AzureMonitor Registry
        new JvmGcMetrics().bindTo(azureMeterRegistry);

        //This will help capturing PostGres metrics.
        //new PostgreSQLDatabaseMetrics(DataSource postgresDataSource, String database).bindTo(azureMeterRegistry);


        // Multiple meter binders can be attached here as above example. A complete list of MeterBinders available
        // can be found https://github.com/micrometer-metrics/micrometer/tree/master/micrometer-core/src/main/java/io/micrometer/core/instrument/binder
        // So there are a wide range of metrics you can capture.


        // set the config to be used elsewhere
        servletContextEvent.getServletContext().setAttribute("AzureMonitorMeterRegistry", azureMeterRegistry);
    }
}
