package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import encodeDecode.*;
import services.*;
import result.*;

/**
 * Implementation class that overrides HttpHandler for an EventService.
 */
public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                if (exchange.getRequestHeaders().containsKey("Authorization")) {
                    EventResult resultOne = new EventResult();
                    EventService serviceOne = new EventService();
                    AllEventResult resultAll = new AllEventResult();
                    AllEventService serviceAll = new AllEventService();
                    String authtoken = exchange.getRequestHeaders().getFirst("Authorization");
                    String uri = exchange.getRequestURI().toString();
                    StringBuilder url = new StringBuilder(uri);
                    url.deleteCharAt(0);
                    String[] paths = url.toString().split("/");

                    if (paths.length == 1) {
                        resultAll = serviceAll.allEvents(authtoken);

                        if (resultAll.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        String json = Encoder.encode(resultAll);
                        OutputStream os = exchange.getResponseBody();
                        IOHandler.writeToString(json, os);
                    } else if (paths.length == 2){
                        String eventID = paths[1];
                        resultOne = serviceOne.oneEvent(eventID, authtoken);

                        if (resultOne.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        String json = Encoder.encode(resultOne);
                        OutputStream os = exchange.getResponseBody();
                        IOHandler.writeToString(json, os);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
        }
    }
}
