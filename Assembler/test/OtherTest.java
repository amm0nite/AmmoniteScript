import fr.ambox.assembler.DataStore;
import fr.ambox.assembler.atoms.StringAtom;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.datas.TableData;
import org.junit.Assert;
import org.junit.Test;

public class OtherTest {

    @Test
    public void cleanStringTest() {
        String res = StringAtom.cleanString("'pou\\'zefzeffezfz\\'et'");
        Assert.assertEquals("pou'zefzeffezfz'et", res);
    }

    @Test
    public void dataStoreTest() {
        int id1;
        int id2;

        DataStore store = new DataStore();

        // because StringData are immutable they are merged
        StringData stringData1 = new StringData("Hello World");
        StringData stringData2 = new StringData("Hello World");
        id1 = store.add(stringData1);
        id2 = store.add(stringData2);
        Assert.assertEquals(id1, id2);
        Assert.assertEquals(stringData1, stringData2);
        Assert.assertTrue(stringData1.isEqualTo(stringData2));
        Assert.assertTrue(stringData1.isLike(stringData2));

        // but TableData are not
        TableData tableData1 = new TableData();
        tableData1.safePut("alice", new IntegerData(11));
        tableData1.safePut("bob", new IntegerData(22));
        TableData tableData2 = new TableData();
        tableData2.safePut("alice", new IntegerData(11));
        tableData2.safePut("bob", new IntegerData(22));
        id1 = store.add(tableData1);
        id2 = store.add(tableData2);
        Assert.assertNotEquals(id1, id2);
        Assert.assertNotEquals(tableData1, tableData2);
        Assert.assertFalse(tableData1.isEqualTo(tableData2));
        Assert.assertTrue(tableData1.isLike(tableData2));
    }
}
