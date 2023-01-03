package uk.co.diyaccounting.util.ops;

import javax.jms.Message;

/**
 * A Message type that will never occur in the wild, abstract for extra assurance
 */
public abstract class UnknownMessage implements Message {

	private UnknownMessage(){
		throw new IllegalStateException("This class should not be constructed");
	}

}
