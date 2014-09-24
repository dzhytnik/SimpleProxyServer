package main.java.com.odnoklasniki.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Properties {
	
	Map <String, ConfiguredConnection> connectionMap = new HashMap<>();

	public void loadProperties() {
		PropertiesConfiguration config = new PropertiesConfiguration();

		try {
			config.load("res/configuration.properties");
			Iterator<String> iterator = config.getKeys();
			while (iterator.hasNext()) {
				String nextProperty = iterator.next();
				StringTokenizer tokenizer = new StringTokenizer(nextProperty,".");
				String firstToken = tokenizer.nextToken();
				String secondToken = tokenizer.nextToken();
				addNewProperty(firstToken, secondToken,
						config.getProperty(nextProperty).toString());
			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addNewProperty(String firstToken, String secondToken, String value) {
		ConfiguredConnection connection;
		if (connectionMap.containsKey(firstToken)) {
			connection = connectionMap.get(firstToken);
		}
		else {
			connection = new ConfiguredConnection();
			connectionMap.put(firstToken, connection);
		}
		connection.setProperty(secondToken, value);
	}

	public Map<String, ConfiguredConnection> getConnectionMap() {
		return connectionMap;
	}
	
	

}
