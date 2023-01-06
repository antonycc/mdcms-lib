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
public class SomeOtherJsonMessage extends JsonMessageImpl implements JsonMessage {

	@JsonIgnore
	@XmlTransient
	private transient ObjectMapper mapper = new ObjectMapper();

	private String notName;

	private int notCount;

	public SomeOtherJsonMessage() {
	}

	public SomeOtherJsonMessage(final TextMessage textMessage) {
		super(textMessage);
		try {
			this.parseJsonString(textMessage.getText());
		} catch (JMSException e) {
			throw new InvalidMessageException("Exception getting text body from message", e);
		}
	}

	//@Override
	public void parseJsonString(final String s) {
		ObjectReader or = this.mapper.readerForUpdating(this);
		try {
			or.readValue(s);
		} catch (IOException e) {
			throw new InvalidMessageException("Could not create object from: " + s, e);
		}
	}

	//@Override
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
		buf.append(this.notName);
		buf.append(", count: ");
		buf.append(this.notCount);
		return buf.toString();
	}

	public String getNotName() {
		return notName;
	}

	public void setNotName(final String notName) {
		this.notName = notName;
	}

	public int getNotCount() {
		return notCount;
	}

	public void setNotCount(final int notCount) {
		this.notCount = notCount;
	}
}
