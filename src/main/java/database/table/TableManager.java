package database.table;


import database.TestHelper;

import java.sql.Connection;
import java.util.*;

/**
 * @author bsun
 */
public class TableManager {
    protected Connection inputConn;
    protected Connection outputConn;
    protected String conditionValue;
    private String newFlowcellId;
    private List<String> newSampleIdList;
    private List<TestHelper> helperList;
    private Map<String, String> sampleIdMap;

    public TableManager(Connection inputConn, Connection outputConn, String conditionValue, List<String> newSampleIdList, String newFlowcellId) {
        this.inputConn = inputConn;
        this.outputConn = outputConn;
        this.conditionValue = conditionValue;
        this.newFlowcellId = newFlowcellId;
        this.newSampleIdList = newSampleIdList;
        if (newSampleIdList != null) {
            Collections.sort(this.newSampleIdList);
            Collections.reverse(this.newSampleIdList);
        }
        this.sampleIdMap = new HashMap<>();

        helperList = new LinkedList<>();
    }

    public void addTable(GhdbDataDuplicationTable table) {
        helperList.add(new DataDuplicationBase(inputConn, outputConn, conditionValue, newSampleIdList, table.getTableName(), sampleIdMap, newFlowcellId));
    }

    public void addTable(GhdbDataDuplicationTable tableName, String sql) {
         DataDuplicationBase dp = new DataDuplicationBase(inputConn, outputConn, conditionValue, newSampleIdList, tableName.getTableName(), sampleIdMap, newFlowcellId);
         dp.setSourceSql(sql);
    }

    public List<TestHelper> getHelperList() {
        return helperList;
    }
    public void printSampleIdMap() {
        System.out.println("A# mapping: ");
        for (Map.Entry e : sampleIdMap.entrySet() ) {
            System.out.println("Old A#: " + e.getKey() + "; New A# " + e.getValue());
        }
    }
}
