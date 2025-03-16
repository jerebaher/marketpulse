package org.marketpulse.stock.controller;

import org.marketpulse.stock.model.StockPriceResponse;
import org.marketpulse.stock.service.StockPriceService;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Path("/stock")
@Produces(MediaType.APPLICATION_JSON)
public class StockPriceController {

    private final StockPriceService stockPriceService;

    @GET
    @Path("/{symbol}")
    public StockPriceResponse getStockPrice(@PathParam("symbol") String symbol) {
        return stockPriceService.getStockPrice(symbol);
    }
}
