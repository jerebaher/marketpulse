package org.marketpulse.config.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.marketpulse.stock.controller.StockPriceController;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(StockPriceController.class);
    }
}
