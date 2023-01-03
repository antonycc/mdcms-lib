package uk.co.diyaccounting.persistence;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Identifier generation for unique transactions.
 */
public class TransactionService {

	public byte[] getTransactionUuidAsByteArray() {
		int bytes = (Long.SIZE / Byte.SIZE) + (Long.SIZE / Byte.SIZE);
		UUID transactionUuid = this.getTransactionUuid();
		ByteBuffer bb = ByteBuffer.wrap(new byte[bytes]);
		bb.putLong(transactionUuid.getMostSignificantBits());
		bb.putLong(transactionUuid.getLeastSignificantBits());
		return bb.array();
	}

	public UUID getTransactionUuid() {
		UUID transactionUuid = UUID.randomUUID();
		return transactionUuid;
	}
}