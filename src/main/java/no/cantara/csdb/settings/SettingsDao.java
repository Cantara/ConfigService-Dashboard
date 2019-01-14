package no.cantara.csdb.settings;

import java.util.Map;
import java.util.Set;

import no.cantara.csdb.errorhandling.AppException;

/**
 * @author Sindre Mehus
 */
public interface SettingsDao {

    Map<String, String> getAliases();

    boolean addAlias(String clientId, String alias) throws Exception;

    Set<String> getIgnoredClients();

    boolean setIgnoredFlag(String clientId, boolean ignore);

}
