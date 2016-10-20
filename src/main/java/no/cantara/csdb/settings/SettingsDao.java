package no.cantara.csdb.settings;

import java.util.Map;
import java.util.Set;

/**
 * @author Sindre Mehus
 */
public interface SettingsDao {

    Map<String, String> getAliases();

    void addAlias(String clientId, String alias);

    Set<String> getIgnoredClients();

    void addIgnoredClient(String clientId);

}
