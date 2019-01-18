package com.configuration;

import com.microsoft.applicationinsights.extensibility.TelemetryInitializer;
import com.microsoft.applicationinsights.telemetry.Telemetry;

/**
 * Telemetry processor that will help in capturing the cloudRoleInstance as custom dimension(a.k.a tags) for slicing
 * the metrics.
 */
public class CloudRoleInstanceTelemetryInitializer implements TelemetryInitializer {

    @Override
    public void initialize(Telemetry telemetry) {
        // copy the Instance Name from CloudContext to metrics tag.
        telemetry.getProperties().putIfAbsent("role_instance", telemetry.getContext().getCloud().getRoleInstance());
    }
}
