package main.java.com.odnoklasniki.test;

import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class RuntimeConnection {
    private static int BUFFER_SIZE = 1000024;
	
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel clientChannel;
	private SocketChannel remoteChannel;
	private ConfiguredConnection configuredConnection;
	private ByteBuffer bufferIn;
	private ByteBuffer bufferOut;
	private boolean remote = false;
	private int nullLengthCount;
	
	
	
	public RuntimeConnection(ServerSocketChannel serverSocketChannel, SocketChannel clientChannel, SocketChannel remoteChannel,
			ConfiguredConnection configuredConnection) {
		this.serverSocketChannel = serverSocketChannel;
		this.clientChannel = clientChannel;
		this.remoteChannel = remoteChannel;
		this.configuredConnection = configuredConnection;
		
		bufferIn = ByteBuffer.allocate(BUFFER_SIZE);
	    bufferOut = ByteBuffer.allocate(BUFFER_SIZE);
	}
	
	public ServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}
	public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}
	public SocketChannel getClientChannel() {
		return clientChannel;
	}
	public void setClientChannel(SocketChannel clientChannel) {
		this.clientChannel = clientChannel;
	}
	public SocketChannel getRemoteChannel() {
		return remoteChannel;
	}
	public void setRemoteChannel(SocketChannel remoteChannel) {
		this.remoteChannel = remoteChannel;
	}
	public ConfiguredConnection getConfiguredConnection() {
		return configuredConnection;
	}
	public void setConfiguredConnection(ConfiguredConnection configuredConnection) {
		this.configuredConnection = configuredConnection;
	}
	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public ByteBuffer getBufferIn() {
		return bufferIn;
	}

	public ByteBuffer getBufferOut() {
		return bufferOut;
	}

	public int getNullLengthCount() {
		return nullLengthCount;
	}

	public void setNullLengthCount(int nullLengthCount) {
		this.nullLengthCount = nullLengthCount;
	}


}
