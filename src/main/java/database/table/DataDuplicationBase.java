package database.table;

import database.TestHelper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author bsun
 */
public class DataDuplicationBase implements TestHelper {
    protected String tableName;
    protected String sourceSql;
    private String newFlowcellId;
    protected String conditionValue;
    protected Connection inputConn;
    protected Connection outputConn;
    protected List<String> newSampleIdList;
    private Map<String, String> sampleIdMap;


    protected DataDuplicationBase(Connection inputConn, Connection outputConn, String conditionValue, List<String> newSampleIdList, String newFlowcellId) {
       this.inputConn = inputConn;
       this.outputConn = outputConn;
       this.conditionValue = conditionValue;
       this.newFlowcellId = newFlowcellId;
       this.newSampleIdList = new LinkedList<>(newSampleIdList);
       composeSourceSql();
    }

    protected DataDuplicationBase(Connection inputConn, Connection outputConn, String conditionValue,
                                  List<String> newSampleIdList, String tableName, Map<String, String> sampleIdMap, String newFlowcellId) {
        this.inputConn = inputConn;
        this.outputConn = outputConn;
        this.conditionValue = conditionValue;
        this.newFlowcellId = newFlowcellId;
        if (newSampleIdList != null) {
            this.newSampleIdList = new LinkedList<>(newSampleIdList);
        }
        this.tableName = tableName;
        this.sampleIdMap = sampleIdMap;
        composeSourceSql();
    }

    // all tables we are duplicating have runid column.
    public void composeSourceSql() {
        sourceSql = "Select * from " + tableName + " where runid like '%" + conditionValue + "'";
    }

    @Override
    public void run() {
        try (PreparedStatement s1 = inputConn.prepareStatement(sourceSql);
             ResultSet rs = s1.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();

            String insertionSql = prepareInsertionSql(meta);

            try (PreparedStatement s2 = outputConn.prepareStatement(insertionSql)) {
                while (rs.next()) {
//                    setUpdateColumn(rs, meta, s2, sampleIdMap);
                    setColumns(rs, meta, s2, sampleIdMap);
                    s2.addBatch();
                }
                s2.executeBatch();
            } catch (SQLException e) {
                while(e.getNextException() != null) {
                    System.out.println("-" + e.getNextException());
                    e = e.getNextException();
                }
                e.getNextException();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setColumns(ResultSet rs, ResultSetMetaData meta, PreparedStatement s2, Map<String, String> sampleIdMap) throws Exception {
        if (newSampleIdList == null || newSampleIdList.isEmpty()) {// If there is no new sample ids provided, the program will copy the data without any modification.
            directCopy(rs, meta, s2);
        } else {
            setUpdateColumn(rs, meta, s2, sampleIdMap);
        }
    }

    private void setUpdateColumn(ResultSet rs, ResultSetMetaData meta, PreparedStatement s2, Map<String, String> sampleIdMap) throws Exception {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            if (meta.getColumnName(i).equalsIgnoreCase("run_sample_id") ) {
                s2.setObject(i, getSampleIdFromMap(sampleIdMap, (String) rs.getObject(i)));
            } else if (meta.getColumnName(i).equalsIgnoreCase("runid") ) {
                s2.setObject(i, newFlowcellId);
            } else {
                    s2.setObject(i, rs.getObject(i));
            }
        }
    }
    private void directCopy(ResultSet rs, ResultSetMetaData meta, PreparedStatement s2) throws Exception {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            s2.setObject(i, rs.getObject(i));
        }
    }

    private String getSampleIdFromMap(Map<String, String> sampleIdMap, String sourceSampleId) throws Exception {
        if (sourceSampleId == null) {// sample coverage table , some records have null sampleId
            return null;
        }
        if (!sampleIdMap.containsKey(sourceSampleId)) {
            if (newSampleIdList == null || newSampleIdList.isEmpty()) {
                throw new Exception("DataDuplicationBase::getSampleIdFromMap::The number of source sampleids and the number of new sample ids doesn't match. " +
                        "E.g.: Source flowcell has 14 samples but the client only provides 13 sampleIds");
            }
            if (newSampleIdList.contains(sourceSampleId)) {
                sampleIdMap.put(sourceSampleId, sourceSampleId);
                newSampleIdList.remove(sourceSampleId);
            } else {
                sampleIdMap.put(sourceSampleId, newSampleIdList.get(newSampleIdList.size() - 1));
                newSampleIdList.remove(newSampleIdList.size() - 1);
            }
        }
        return sampleIdMap.get(sourceSampleId);
    }

    // This is a util function, since we simply copy everything that we queried,
    // the function should be used for all the cases.
    private String prepareInsertionSql(ResultSetMetaData meta) throws SQLException {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder bindVariables = new StringBuilder();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            if (i > 1) {
                columnNames.append(", ");
                bindVariables.append(", ");
            }

            columnNames.append(meta.getColumnName(i));
            bindVariables.append('?');
        }

        return "INSERT INTO " + tableName + " ("
                + columnNames
                + ") VALUES ("
                + bindVariables
                + ")";
    }




    @Override
    public String getInfo() {
        return null;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

}
