package no.cantara.csdb.settings;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import no.cantara.csdb.config.ConfigValue;

/**
 * @author Sindre Mehus
 */
@Deprecated
public class MapDbSettingsDao implements SettingsDao {

    private final DB db;
    private final Map<String, String> aliases;
    private final Set<String> ignoredClients;

    public MapDbSettingsDao() {
        File mapDbPathFile = new File(ConfigValue.MAPDB_PATH);
        mapDbPathFile.getParentFile().mkdirs();
        db = DBMaker.newFileDB(mapDbPathFile).make();
        aliases = db.getHashMap("aliasMap");
        ignoredClients = db.getTreeSet("ignoreClientList");
    }

    @Override
    public Map<String, String> getAliases() {
        return aliases;
    }

    @Override
    public boolean addAlias(String clientId, String alias) {
        aliases.put(clientId, alias);
        db.commit();
        return true;
    }

    @Override
    public Set<String> getIgnoredClients() {
        return ignoredClients;
    }

	@Override
	public boolean setIgnoredFlag(String clientId, boolean ignore) {
		if(ignore) {
			 ignoredClients.add(clientId);
		} else {
			ignoredClients.remove(clientId);
		}
		db.commit();
		return true;
	}
}
