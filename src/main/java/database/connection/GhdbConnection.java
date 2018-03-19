package database.connection;

import config.Config;

import java.sql.Connection;

/**
 * @author bsun
 */
public class GhdbConnection {
    private final static String CONNECTION_METHOD = "jdbc";
    private final static String DATABASE_TYPE = "postgresql";
    private final static String JDBC_DRIVER_NAME = "org.postgresql.Driver";

    private String sourceDatabaseUserName;
    private String sourceDatabasePassword;
    private String sourceDatabaseIp;
    private String sourceDatabaseSchema;

    private String targetDatabaseUserName;
    private String targetDatabasePassword;
    private String targetDatabaseIp;
    private String targetDatabaseSchema;


    private String sourceConnectionUrl;
    private String targetConnectionUrl;

    private Connection inputConn;
    private Connection outputConn;

    public GhdbConnection() {
        setupDataBaseParameter();
        createConnection();
    }

    private void setupDataBaseParameter() {
        Config postgresConfig = new Config("src/main/resources/configfile/AutomationTest.properties");

        sourceDatabaseUserName = postgresConfig.get("automationtest.database.dataduplication.sourcedatabase.username");
        sourceDatabasePassword = postgresConfig.getDecryption("automationtest.database.dataduplication.sourcedatabase.password");
        sourceDatabaseIp = postgresConfig.get("automationtest.database.dataduplication.sourcedatabase.ip");
        sourceDatabaseSchema = postgresConfig.get("automationtest.database.dataduplication.sourcedatabase.schema");
        sourceConnectionUrl = composeString(CONNECTION_METHOD, ":", DATABASE_TYPE, "://", sourceDatabaseIp, "/", sourceDatabaseSchema);

        targetDatabaseUserName = postgresConfig.get("automationtest.database.dataduplication.targetdatabase.username");
        targetDatabasePassword = postgresConfig.getDecryption("automationtest.database.dataduplication.targetdatabase.password");
        targetDatabaseIp = postgresConfig.get("automationtest.database.dataduplication.targetdatabase.ip");
        targetDatabaseSchema = postgresConfig.get("automationtest.database.dataduplication.targetdatabase.schema");
        targetConnectionUrl = composeString(CONNECTION_METHOD, ":", DATABASE_TYPE, "://", targetDatabaseIp, "/", targetDatabaseSchema);
    }

    private void createConnection() {
        try {
            inputConn = JDBCBase.createConnection(sourceDatabaseUserName, sourceDatabasePassword, JDBC_DRIVER_NAME, sourceConnectionUrl);
            outputConn = JDBCBase.createConnection(targetDatabaseUserName, targetDatabasePassword, JDBC_DRIVER_NAME, targetConnectionUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String composeString(String... inputs) {
        StringBuilder output = new StringBuilder();
        for (String s : inputs) {
            output.append(s);
        }
        return output.toString();
    }

    public Connection getInputConn() {
        return inputConn;
    }

    public Connection getOutputConn() {
        return outputConn;
    }
}
