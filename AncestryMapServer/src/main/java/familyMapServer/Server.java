package familyMapServer;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handlers.*;

/*
	This example demonstrates the basic structure of the Family Map Server
	(although it is for a fictitious "Ticket to Ride" game, not Family Map).
	The example is greatly simplfied to help you more easily understand the
	basic elements of the server.

	The Server class is the "main" class for the server (i.e., it contains the
		"main" method for the server program).
	When the server runs, all command-line arguments are passed in to Server.main.
	For this server, the only command-line argument is the port number on which
		the server should accept incoming client connections.
*/
public class Server {
    // Java provides an HttpServer class that can be used to embed
    // an HTTP server in any Java program.
    // Using the HttpServer class, you can easily make a Java
    // program that can receive incoming HTTP requests, and respond
    // with appropriate HTTP responses.
    // HttpServer is the class that actually implements the HTTP network
    // protocol (be glad you don't have to).
    // The "server" field contains the HttpServer instance for this program,
    // which is initialized in the "run" method below.
    private HttpServer server;

    public Server() {}

    public void start(int numPort) throws IOException {
        InetSocketAddress address = new InetSocketAddress(numPort);
        HttpServer httpServer = HttpServer.create(address, 12);
        makeHandlers(httpServer);
        httpServer.start();
        System.out.println("Server is listening on port " + numPort);
    }

    public void makeHandlers(HttpServer httpServer) {
        httpServer.createContext("/clear", new ClearHandler());
        httpServer.createContext("/event", new EventHandler());
        httpServer.createContext("/", new FileHandler());
        httpServer.createContext("/fill", new FillHandler());
        httpServer.createContext("/load", new LoadHandler());
        httpServer.createContext("/user/login", new LoginHandler());
        httpServer.createContext("/person", new PersonHandler());
        httpServer.createContext("/user/register", new RegisterHandler());
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        int portNumber = Integer.parseInt(args[0]);
        server.start(portNumber);
    }
}

