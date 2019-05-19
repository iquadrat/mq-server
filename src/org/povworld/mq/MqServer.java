package org.povworld.mq;

import java.io.IOException;

import io.prometheus.client.exporter.HTTPServer;

public class MqServer {
    
    public static void main(String[] args) throws IOException {
        BitcoinMetrics.registerMetrics();
        new HTTPServer("localhost", 3333);
    }
}
