package no.cantara.csdb.settings;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.cantara.csdb.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.util.Collections.singletonList;

/**
 * @author Sindre Mehus
 */
public class DynamoDbSettingsDao implements SettingsDao {

    private static final Logger LOG = LoggerFactory.getLogger(DynamoDbSettingsDao.class);
    public static final String KEY_ALIASES = "aliases";
    public static final String KEY_IGNORED_CLIENTS = "ignoredClients";
    public static final String ATTRIBUTE_KEY = "key";
    public static final String ATTRIBUTE_VALUE = "value";

    private final String tableName;
    private final DynamoDB dynamo;
    private final Table table;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, String> aliases = new TreeMap<>();
    private final Set<String> ignoredClients = new TreeSet<>();


    public DynamoDbSettingsDao() {
        tableName = Configuration.getString("dynamodb.table");
        String region = Configuration.getString("dynamodb.region");
        boolean autocreateTable = Configuration.getBoolean("dynamodb.createTableIfNecessary");

        AmazonDynamoDBClient client = new AmazonDynamoDBClient().withRegion(Regions.fromName(region));
        dynamo = new DynamoDB(client);

        if (autocreateTable) {
            createTableIfNecessary();
        }
        table = dynamo.getTable(tableName);

        readTable();
    }

    private void readTable() {
        try {
            Item item = table.getItem(ATTRIBUTE_KEY, KEY_ALIASES);
            if (item != null) {
                String aliasesJson = item.getString(ATTRIBUTE_VALUE);
                TypeReference<Map<String, String>> aliasesRef = new TypeReference<Map<String, String>>() {
                };
                Map<String, String> tmpMap = objectMapper.readValue(aliasesJson, aliasesRef);
                aliases.putAll(tmpMap);
            }

            item = table.getItem(ATTRIBUTE_KEY, KEY_IGNORED_CLIENTS);
            if (item != null) {
                String ignoredClientsJson = item.getString(ATTRIBUTE_VALUE);
                TypeReference<Set<String>> ignoredClientsRef = new TypeReference<Set<String>>() {
                };
                Set<String> tmpSet = objectMapper.readValue(ignoredClientsJson, ignoredClientsRef);
                ignoredClients.addAll(tmpSet);
            }
        } catch (Exception e) {
            LOG.error("Failed to read from DynamoDB", e);
        }
    }

    @Override
    public Map<String, String> getAliases() {
        return aliases;
    }

    @Override
    public boolean addAlias(String clientId, String alias) {
        aliases.put(clientId, alias);
        try {
            table.putItem(new Item().withPrimaryKey(ATTRIBUTE_KEY, KEY_ALIASES)
                                    .withString(ATTRIBUTE_VALUE, objectMapper.writeValueAsString(aliases)));
            return true;
        } catch (Exception e) {
            LOG.error("Failed to persist aliases to DynamoDB", e);
            return false;
        }
    }

    @Override
    public Set<String> getIgnoredClients() {
        return ignoredClients;
    }

    private void createTableIfNecessary() {
        for (Table table : dynamo.listTables()) {
            if (tableName.equals(table.getTableName())) {
                LOG.info("Table {} already exists.", tableName);
                return;
            }
        }

        LOG.info("Creating table {}", tableName);
        Table table = dynamo.createTable(tableName,
                                         singletonList(new KeySchemaElement(ATTRIBUTE_KEY, KeyType.HASH)),
                                         singletonList(new AttributeDefinition(ATTRIBUTE_KEY, ScalarAttributeType.S)),
                                         new ProvisionedThroughput(1L, 1L));
        try {
            table.waitForActive();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOG.info("Table {} created with status {}", tableName, table.getDescription().getTableStatus());
    }

	@Override
	public boolean setIgnoredFlag(String clientId, boolean ignore) {
		if(ignore) {
			if(!ignoredClients.contains(clientId)) {
				ignoredClients.add(clientId);
			}
		} else {
			ignoredClients.remove(clientId);
		}
		
		try {
			table.putItem(new Item().withPrimaryKey(ATTRIBUTE_KEY, KEY_IGNORED_CLIENTS)
					.withString(ATTRIBUTE_VALUE, objectMapper.writeValueAsString(ignoredClients)));
			return true;
		} catch (Exception e) {
			LOG.error("Failed to persist ignored clients to DynamoDB", e);
			return false;
		}
		
	}
}
