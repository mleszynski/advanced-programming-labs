package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import encodeDecode.*;
import services.*;
import result.*;

/**
 * Implementation class that overrides HttpHandler for a FillService.
 */
public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                FillService service = new FillService();
                FillResult result = new FillResult();
                String uri = exchange.getRequestURI().toString();
                StringBuilder url = new StringBuilder(uri);
                url.deleteCharAt(0);
                String[] path = url.toString().split("/");
                String username = path[1];
                int generations = 101;

                if (path.length == 3) {
                    generations = Integer.parseInt(path[2]);
                }
                result = service.fill(username, generations);

                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                String json = Encoder.encode(result);
                OutputStream os = exchange.getResponseBody();
                IOHandler.writeToString(json, os);
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
