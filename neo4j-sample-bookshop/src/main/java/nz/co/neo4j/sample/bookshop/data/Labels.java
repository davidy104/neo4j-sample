package nz.co.neo4j.sample.bookshop.data;

import org.neo4j.graphdb.Label;

public enum Labels implements Label {
	Book, Person, Publisher;
}
