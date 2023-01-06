package uk.co.polycode.mdcms.persistence;

import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by antony on 26/06/2016.
 */
public class FlywayConfigStub extends ClassicConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(FlywayConfigStub.class);

	private DataSource dataSource = null;

	public int migrate(){
		logger.warn("Flyway migration using no effect stub");
		return 0;
	}

	@Override
	public ClassLoader getClassLoader() {
		return null;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean isIgnoreMissingMigrations() {
		return true;
	}

	@Override
	public boolean isIgnoreFutureMigrations() {
		return true;
	}

	@Override
	public boolean isValidateOnMigrate() {
		return false;
	}

}
