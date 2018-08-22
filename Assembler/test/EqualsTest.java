import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.VariableAtom;
import fr.ambox.assembler.datas.*;
import org.junit.Assert;
import org.junit.Test;

public class EqualsTest {

    @Test
    public void doubleData() {
        IntegerData integerOne = new IntegerData(1);
        DecimalData decimalOne = new DecimalData(1);

        Assert.assertNotEquals(integerOne, decimalOne);
        Assert.assertTrue(integerOne.isEqualTo(decimalOne));
        Assert.assertTrue(integerOne.isLike(decimalOne));
    }

    @Test
    public void stringDataTest() {
        // Only immutable data have equivalent equals and isEqualTo

        StringData str1;
        StringData str2;

        str1 = new StringData("Hello world!");
        str2 = new StringData("Hello world!");

        Assert.assertEquals(str1, str2);
        Assert.assertTrue(str1.isEqualTo(str2));
        Assert.assertEquals(str1.hashCode(), str2.hashCode());

        str1 = new StringData("Hello");
        str2 = new StringData("World");

        Assert.assertNotEquals(str1, str2);
        Assert.assertFalse(str1.isEqualTo(str2));
        Assert.assertNotEquals(str1.hashCode(), str2.hashCode());
    }
}
