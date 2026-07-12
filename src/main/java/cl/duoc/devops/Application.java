package cl.duoc.devops;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class Application {
  static final String RESPONSE = "Hol Mundo\n";
  private static HttpServer runningServer;

  private Application() {
  }

  public static void main(String[] args) throws IOException {
    runningServer = start("0.0.0.0", port());
  }

  static void stop() {
    if (runningServer != null) {
      runningServer.stop(0);
      runningServer = null;
    }
  }

  static int port() {
    return Integer.parseInt(System.getProperty("PORT", System.getenv().getOrDefault("PORT", "8082")));
  }

  static HttpServer start(String host, int port) throws IOException {
    HttpServer server = createServer(new InetSocketAddress(host, port));
    server.start();
    return server;
  }

  static HttpServer createServer(InetSocketAddress address) throws IOException {
    HttpServer server = HttpServer.create(address, 0);
    server.createContext("/", Application::handle);
    return server;
  }

  private static void handle(HttpExchange exchange) throws IOException {
    byte[] bytes = RESPONSE.getBytes(StandardCharsets.UTF_8);

    exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
    exchange.sendResponseHeaders(200, bytes.length);
    try (OutputStream stream = exchange.getResponseBody()) {
      stream.write(bytes);
    }
  }
}
