package no.cantara.csdb.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.cantara.csdb.util.Configuration.getBoolean;

/**
 * @author Sindre Mehus
 */
@Configuration
public class SettingsDaoFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsDaoFactory.class);

    @Bean
    public SettingsDao settingsRepository() {
        if (getBoolean("dynamodb.enabled")) {
            LOG.info("Using DynamoDB");
            return new DynamoDbSettingsDao();
        }
        LOG.info("Using MapDB");
        return new MapDbSettingsDao();
    }
}
