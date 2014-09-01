package nz.co.neo4j.sample.bookshop.data;

import nz.co.neo4j.sample.bookshop.data.model.Publisher;

import org.neo4j.graphdb.Node;

public class PublisherNode implements Publisher {

	final Node node;

	public static final String Name = "name";

	public PublisherNode(Node node) {
		this.node = node;
	}

	@Override
	public String getName() {
		return (String) node.getProperty(Name);
	}

	@Override
	public void setName(String name) {
		node.setProperty(Name, name);
	}
}
