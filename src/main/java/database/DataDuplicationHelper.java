package database;

import database.connection.GhdbConnection;
import database.table.*;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author bsun
 */
public class DataDuplicationHelper implements TestHelper, Closeable {
    private static final String TIME_PATTERN_POSTFIX = "yyMMDDHHmm";
    private static final String TIME_PATTERN_PREFIX = "yyMMDD";
    private static final String FLOWCELL_ID_PREFIX = "LIMS";


    private List<String> sampleList;

    Connection inputConn = null;
    Connection outputConn = null;

    private TableManager manager;
    private String flowcellId;
    private String newFlowcellId;

    public DataDuplicationHelper(String flowcellId, List<String> sampleList) {
        this.flowcellId = flowcellId;
        this.sampleList = sampleList;
        this.newFlowcellId = generateNewRunId();
        setupConnection();
        addTable();
    }

    private void addTable() {
        manager = new TableManager(inputConn, outputConn, flowcellId, sampleList, newFlowcellId);
        for (GhdbDataDuplicationTable g : GhdbDataDuplicationTable.values()) {
            manager.addTable(g);
        }
    }


    @Override
    public void run() {
        for (TestHelper t : nullSafe(manager.getHelperList())) {
            t.run();
        }
        close();
    }

    @Override
    public String getInfo() {
        return newFlowcellId;
    }

    @Override
    public void close() {
        closeConnection(inputConn);
        closeConnection(outputConn);
    }

    public void printAnumberMapping() {
        manager.printSampleIdMap();
    }
    private void setupConnection() {
        GhdbConnection connection = new GhdbConnection();
        inputConn = connection.getInputConn();
        outputConn = connection.getOutputConn();
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateNewRunId() {
        if (sampleList == null || sampleList.isEmpty()) {
            System.out.println("There is no new sample id provided, the program will copy the " + flowcellId + "data without any modification. ");
            return flowcellId;
        }
//        YYMMDD_LIMS_FLOWCELLID_YYMMDDHHMM
        String newRunId = new StringBuilder(getCurrentTime(TIME_PATTERN_PREFIX))
                                                            .append("_")
                                                            .append(FLOWCELL_ID_PREFIX)
                                                            .append("_")
                                                            .append(flowcellId)
                                                            .append("_")
                                                            .append(getCurrentTime(TIME_PATTERN_POSTFIX)).toString();
        System.out.println("New Run Id: " + newRunId);
        return newRunId;
    }
    private String getCurrentTime(String pattern) {
        java.util.Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
        return dateFormatter.format(now);
    }
    private <T extends Iterable> T nullSafe(T object) {
        return object == null ?  (T) Collections.EMPTY_LIST : object;
    }


}
