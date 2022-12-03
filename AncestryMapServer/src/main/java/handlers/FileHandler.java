package handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.file.*;
import java.net.HttpURLConnection;

/**
 * Implementation class that overrides HttpHandler for a FileService.
 */
public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
            String urlPath = exchange.getRequestURI().toString();

            if (exchange.getRequestURI() == null ||
                    exchange.getRequestURI().toString().equals("/")) {
                urlPath = "/index.html";
            }
            String filePath = "web" + urlPath;
            File fileFound = new File(filePath);
            File fileNotFound = new File("web/HTML/404.html");

            if (fileFound.exists()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream responseBody = exchange.getResponseBody();
                Files.copy(fileFound.toPath(), responseBody);
                exchange.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream responseBody = exchange.getResponseBody();
                Files.copy(fileNotFound.toPath(), responseBody);
                exchange.close();
            }
            exchange.getResponseBody().close();
        }
    }
}
