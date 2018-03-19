package database;


import org.testng.annotations.Test;
import java.util.LinkedList;
import java.util.List;

/**
 * @author bsun
 */
public class DataDuplicationHelperTest {
    @Test
    public void test() {
//        String runId = "170322_D00769_0326_AHJMTYBCXY";
        String runId = "AHNGMJBCXY";
        List<String> sampleIdList = prepareSampleIds();

        // If there is no new sample id then the program will copy the data without
        // any data duplications.
//        List<String> sampleIdList = null;
        DataDuplicationHelper test = new DataDuplicationHelper(runId, sampleIdList);
        test.run();
        System.out.println(test.getInfo());
        test.printAnumberMapping();
    }

    private List<String> prepareSampleIds() {
        List<String> list = new LinkedList<>();
        boolean forReal = true;
        if (forReal) {
            list.add("CC00T0130");
            list.add("A010004001");
            list.add("A010004101");
            list.add("A010004201");
            list.add("A010004301");
            list.add("A010004401");
            list.add("A010004501");
            list.add("A010004601");
            list.add("A010004701");
            list.add("A010004801");
            list.add("A010004901");
            list.add("A010005001");
            list.add("A010005101");
            list.add("A010005201");
            list.add("A010005301");
        } else {
            list.add("A01_test");
            list.add("A02_test");
            list.add("A03_test");
            list.add("A04_test");
            list.add("A05_test");
            list.add("A06_test");
            list.add("A07_test");
            list.add("A08_test");
            list.add("A09_test");
            list.add("A10_test");
            list.add("A11_test");
            list.add("A12_test");
            list.add("A13_test");
            list.add("A14_test");
            list.add("A15_test");
        }
        return list;
    }
}
