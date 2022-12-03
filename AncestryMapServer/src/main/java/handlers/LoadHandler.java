package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import encodeDecode.*;
import request.LoadRequest;
import services.*;
import result.*;

/**
 * Implementation class that overrides HttpHandler for a LoadService.
 */
public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {

            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                LoadService service = new LoadService();
                LoadResult result = new LoadResult();
                InputStream requestBody = exchange.getRequestBody();
                String requestData = IOHandler.readFromString(requestBody);
                LoadRequest request = Decoder.decode(requestData, LoadRequest.class);
                result = service.load(request);

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
