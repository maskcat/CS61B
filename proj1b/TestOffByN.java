import org.junit.Assert;
import org.junit.Test;

/**
 * @author fymas
 */
public class TestOffByN {
    @Test
    public void testEqualChars() {
        CharacterComparator offByN = new OffByN(3);
        Assert.assertFalse(offByN.equalChars('a', 'a'));
        Assert.assertTrue(offByN.equalChars('a', 'd'));
        Assert.assertTrue(offByN.equalChars('d', 'a'));
    }
}
