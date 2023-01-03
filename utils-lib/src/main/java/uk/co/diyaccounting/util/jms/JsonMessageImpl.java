package uk.co.diyaccounting.util.jms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.diyaccounting.util.lang.ComparableUsingString;

import javax.jms.TextMessage;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Message for testing JMS
 */
public abstract class JsonMessageImpl extends ComparableUsingString implements JsonMessage {

	@JsonIgnore
	@XmlTransient
	private TextMessage textMessage;

	public JsonMessageImpl() {
	}

	public JsonMessageImpl(final TextMessage textMessage) {
		this();
		this.textMessage = textMessage;
	}

	public String toString(){
		return this.toJsonString();
	}

	public abstract void parseJsonString(String s);

	public abstract String toJsonString();

	public TextMessage getTextMessage() {
		return textMessage;
	}

}
