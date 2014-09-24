package main.java.com.odnoklasniki.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleProxyServer {
	
	private static Selector selector;

    public static void main(String[] args) throws IOException {
		try {
			Properties config = new Properties();

			initServer();

			config.loadProperties();

			Iterator<Entry<String, ConfiguredConnection>> iterator = config.getConnectionMap().entrySet().iterator();

			while (iterator.hasNext()) {
				ConfiguredConnection connection = iterator.next().getValue();
				runServer(connection);
			}
			
			handleEvents();

		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	private static void initServer() throws IOException {
		selector = Selector.open();
	}

	
 
	private static void runServer(ConfiguredConnection connection)
			throws IOException {
		final int localPort = new Integer(connection.getLocalPort());

		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.socket().bind(new InetSocketAddress(localPort));
		ssChannel.configureBlocking(false);
		SelectionKey serverKey = ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		

        SocketChannel remote = SocketChannel.open();
        remote.configureBlocking(false);

        RuntimeConnection runtimeConnection = new RuntimeConnection(ssChannel, null, remote, connection);
        serverKey.attach(runtimeConnection);
        
	}
	
	private static void handleEvents() throws IOException {
		 while (true) {
        	selector.select(500);
			Set keys = selector.selectedKeys();
			Iterator iterator = keys.iterator();

			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				iterator.remove();
				
				if (key.isAcceptable()) {
					ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
					SocketChannel client = ssChannel.accept();
					client.configureBlocking(false);
					
					RuntimeConnection runtimeConnection = (RuntimeConnection) key.attachment();
					runtimeConnection.setClientChannel(client);
					SelectionKey selectionKey = client.register(selector, SelectionKey.OP_READ);
					selectionKey.attach(runtimeConnection);
					continue;
				}

				if (key.isReadable()) {
					RuntimeConnection runtimeConnection = (RuntimeConnection) key.attachment();
					try {
						
						if (!runtimeConnection.isRemote()) {
							SocketChannel channel = runtimeConnection.getClientChannel();
							if (channel.read(runtimeConnection.getBufferIn()) == 0) {
								SelectionKey selectionKey = channel.register(selector, 0);
								selectionKey.attach(runtimeConnection);
							}
							else {
								SelectionKey selectionKey;
								if (!runtimeConnection.getRemoteChannel().isConnected()) {
									runtimeConnection.getRemoteChannel().connect(new java.net.InetSocketAddress(runtimeConnection.getConfiguredConnection().getRemoteHost(), new Integer(runtimeConnection.getConfiguredConnection().getRemotePort())));
									selectionKey = runtimeConnection.getRemoteChannel().register(selector, SelectionKey.OP_CONNECT);
								}
								else {
									selectionKey = runtimeConnection.getRemoteChannel().register(selector, SelectionKey.OP_WRITE);
								}
								selectionKey.attach(runtimeConnection);
							}
						}
						else {
							SocketChannel channel = runtimeConnection.getRemoteChannel();
							int bytesRead = channel.read(runtimeConnection.getBufferOut()); 
							if (bytesRead == 0) {
								SelectionKey selectionKey = channel.register(selector, 0);
								runtimeConnection.setRemote(false);
								selectionKey.attach(runtimeConnection);
							}
							else if (bytesRead > 0) {
								SelectionKey selectionKey = runtimeConnection.getClientChannel().register(selector, SelectionKey.OP_WRITE);
								selectionKey.attach(runtimeConnection);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						continue; 
					}
					continue;
				}
				
				if (key.isConnectable()) {
					SocketChannel channel = (SocketChannel) key.channel();
					channel.finishConnect();
					RuntimeConnection runtimeConnection = (RuntimeConnection) key.attachment();
					SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_WRITE);
					selectionKey.attach(runtimeConnection);
					
					continue;
				}
				
				if (key.isWritable()) {
					RuntimeConnection runtimeConnection = (RuntimeConnection) key.attachment();
					if (!runtimeConnection.isRemote()) {
						SocketChannel channel = runtimeConnection.getRemoteChannel();
						runtimeConnection.getBufferIn().flip();
						if (channel.write(runtimeConnection.getBufferIn()) != -1) {
							SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
							runtimeConnection.getBufferIn().flip();
							runtimeConnection.setRemote(true);
							selectionKey.attach(runtimeConnection);
						}
					}
					else {
						SocketChannel channel = runtimeConnection.getClientChannel();
						runtimeConnection.getBufferOut().flip();
						if (channel.write(runtimeConnection.getBufferOut()) != -1) {
							runtimeConnection.getBufferOut().flip();
							channel.register(selector, 0);
							SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
							selectionKey.attach(runtimeConnection);
						}
					}
					continue;
				}
			}
		 }
	}
}
           
         
  