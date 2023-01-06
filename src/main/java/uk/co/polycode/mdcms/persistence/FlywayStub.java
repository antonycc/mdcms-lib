package uk.co.polycode.mdcms.persistence;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by antony on 26/06/2016.
 */
public class FlywayStub extends Flyway {

	private static final Logger logger = LoggerFactory.getLogger(FlywayStub.class);

	private DataSource dataSource = null;

	public FlywayStub(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public int migrate(){
		logger.warn("Flyway migration using no effect stub");
		return 0;
	}

	public ClassLoader getClassLoader() {
		return null;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
