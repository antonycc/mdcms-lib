package uk.co.polycode.mdcms.ops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import uk.co.polycode.mdcms.util.jms.InvalidMessageException;
import uk.co.polycode.mdcms.util.jms.JsonMessage;
import uk.co.polycode.mdcms.util.jms.JsonMessageImpl;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;

/**
 * Message for testing JMS
 */
public class NameCountJsonMessage extends JsonMessageImpl implements JsonMessage {

	@JsonIgnore
	@XmlTransient
	private transient ObjectMapper mapper = new ObjectMapper();

	private String name;

	private int count;

	public NameCountJsonMessage() {
	}

	public NameCountJsonMessage(final TextMessage textMessage) {
		super(textMessage);
		try {
			this.parseJsonString(textMessage.getText());
		} catch (JMSException e) {
			throw new InvalidMessageException("Exception getting text body from message", e);
		}
	}

	@Override
	public void parseJsonString(final String s) {
		ObjectReader or = this.mapper.readerForUpdating(this);
		try {
			or.readValue(s);
		} catch (IOException e) {
			throw new InvalidMessageException("Could not create object from: " + s, e);
		}
	}

	@Override
	public String toJsonString() {
		try {
			ObjectWriter ow = this.mapper.writer();
			return ow.writeValueAsString(this);
		} catch (IOException e) {
			throw new InvalidMessageException("Could not create JSON version of this object", e);
		}
	}

	public String toString(){
		StringBuilder buf = new StringBuilder();
		buf.append("name: ");
		buf.append(this.name);
		buf.append(", count: ");
		buf.append(this.count);
		return buf.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		this.count = count;
	}
}
