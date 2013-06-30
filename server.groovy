import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.eclipse.jetty.websocket.WebSocket.Connection;

try {
	// 1) Create a Jetty server with the 8091 port.
	Server server = new Server(8081);
	// 2) Register ChatWebSocketHandler in the Jetty server instance.
	WebSocketHandler chatWebSocketHandler = new WebSocketHandler() {

		// private final Set<WebSocket.OnTextMessage> webSockets = new
		// CopyOnWriteArraySet<WebSocket.OnTextMessage>();
		Connection connection2;

		public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
			return new WebSocket.OnTextMessage() {

				@Override
				public void onOpen(Connection connection) {
					// Client (Browser) WebSockets has opened a
					// connection.
					// 1) Store the opened connection
					connection2 = connection;
					// 2) Add ChatWebSocket in the global list of
					// ChatWebSocket
					// instances
					// instance.
					// webSockets.add(this);

				}

				@Override
				public void onClose(int closeCode, String message) {
					// Remove ChatWebSocket in the global list of
					// ChatWebSocket
					// instance.
					// webSockets.remove(this);
					connection2 = null;

				}

				@Override
				public void onMessage(String data) {
					System.out.println("onMessage() - data: " + data);
					// Loop for each instance of ChatWebSocket to send
					// message server to
					// each client WebSockets.
					try {
						connection2.sendMessage("I think I heard you say \"" + data+"\". Well, my name is ChatWebSocketServer.java");
					} catch (IOException x) {
						// Error was detected, close the ChatWebSocket
						// client side
						connection2.close();
					}

				}
			};
		}
	};

	chatWebSocketHandler.setHandler(new DefaultHandler());
	server.setHandler(chatWebSocketHandler);
	// 2) Start the Jetty server.
	server.start();
	// Jetty server is stopped when the Thread is interruped.
	server.join();
} catch (Throwable e) {
	e.printStackTrace();
}