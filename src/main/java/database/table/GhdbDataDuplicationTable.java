package database.table;

/**
 * @author bsun
 */
public enum GhdbDataDuplicationTable {
    BOARD("gh_board"),
    CNV_CALL("cnv_call"),
    FLOWCELL("gh_flowcell"),
    FUSION_CALL("fusion_call"),
    GH_FUSION("gh_fusion"),
    INDEL_CALL("indel_call"),
    QC_ON_TARGET("qc_on_target"),
    QC_SEQ("qc_seq"),
    SAMPLE_COVERAGE("sample_coverage"),
    SAMPLE_QC("sample_qc"),
    SNV("snv_call"),
    TMB("tmb_call"),
    VARIANT("gh_variant");

    private String tableName;

    GhdbDataDuplicationTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
