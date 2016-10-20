package no.cantara.csdb.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sindre Mehus
 */
@Configuration
public class SettingsDaoFactory {

    @Bean
    public SettingsDao settingsRepository() {
        return new MapDbSettingsDao();
    }
}
