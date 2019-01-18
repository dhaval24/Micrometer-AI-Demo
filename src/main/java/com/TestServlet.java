package com;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class TestServlet extends HttpServlet {

    // Initialize a TelemetryClient for tracking custom dependencies.
    private TelemetryClient client = new TelemetryClient();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello world");

        // Simulate a DB Insert call
        insert();
    }

    private void insert() {
        long start = System.currentTimeMillis();

        try {
            // simulate an insert call to Postgres
            Thread.sleep(1000);
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            client.trackDependency("SQL", "INSERT", new Duration(end-start), false);
            e.printStackTrace();
        }


        long end = System.currentTimeMillis();
        client.trackDependency("SQL", "INSERT", new Duration(end-start), true);
    }

}
