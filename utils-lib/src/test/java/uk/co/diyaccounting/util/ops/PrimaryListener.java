package uk.co.diyaccounting.util.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.diyaccounting.util.jms.JsonMessageListener;

import javax.jms.TextMessage;

/**
 * Name count message listener
 */
public class PrimaryListener extends JsonMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(PrimaryListener.class);

	public static int received = Integer.MIN_VALUE;

	public static long processingTime = 1000L;

	@Override
	public void onJsonMessage(TextMessage textMessage) {
		logger.debug("Processing [{}]", textMessage.toString());
		NameCountJsonMessage nameCountJsonMessage = new NameCountJsonMessage(textMessage);
		this.emulateProcessingTime();
		logger.debug("Processed, name is [{}]", nameCountJsonMessage.getName());
		PrimaryListener.received++;
	}

	private void emulateProcessingTime(){
		try {
			Thread.sleep(PrimaryListener.processingTime);
			Thread.yield();
		}catch(InterruptedException e){
			logger.error("Listener did not sleep", e);
		}
	}

}