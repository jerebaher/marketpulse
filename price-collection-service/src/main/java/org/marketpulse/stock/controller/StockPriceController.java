package org.marketpulse.stock.controller;

import org.marketpulse.stock.model.StockPriceResponse;
import org.marketpulse.stock.service.StockPriceService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Path("/stocks")
@Produces(MediaType.APPLICATION_JSON)
public class StockPriceController {

    private final StockPriceService stockPriceService;

    @GET
    @Path("/{symbol}")
    public StockPriceResponse getStockPrice(@PathParam("symbol") String symbol) {
        return stockPriceService.getStockPrice(symbol);
    }
}
