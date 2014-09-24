package main.java.com.odnoklasniki.test;

public class ConfiguredConnection {
	public static final String LOCAL_PORT = "localPort";
	public static final String REMOTE_HOST = "remoteHost";
	public static final String REMOTE_PORT = "remotePort";

	private String localPort;
	private String remoteHost;
	private String remotePort;
	
	
	public String getLocalPort() {
		return localPort;
	}
	public void setLocalPort(String localPort) {
		this.localPort = localPort;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public String getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}
	
	public void setProperty(String key, String value) {
		switch(key) {
		case LOCAL_PORT:
			setLocalPort(value);
			break;
		case REMOTE_HOST:
			setRemoteHost(value);
			break;
		case REMOTE_PORT:
			setRemotePort(value);
			break;
		}
	}
	
	

}
