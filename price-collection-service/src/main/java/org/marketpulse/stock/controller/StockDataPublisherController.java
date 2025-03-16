package org.marketpulse.stock.controller;

import lombok.AllArgsConstructor;
import org.marketpulse.stock.publisher.StockDataPublisher;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@AllArgsConstructor
@Path("/stock-data")
@Produces(MediaType.APPLICATION_JSON)
public class StockDataPublisherController {

    private final StockDataPublisher stockDataPublisher;

    @POST
    @Path("/publish/{symbol}")
    public Response publishStockData(@PathParam("symbol") String symbol) {
        stockDataPublisher.publishStockData(symbol);
        return Response.noContent().build();
    }

    @POST
    @Path("/publish/test")
    public Response publishTestStockData() {
        stockDataPublisher.publishTestStockData();
        return Response.noContent().build();
    }
}
