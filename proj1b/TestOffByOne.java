import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author fymas
 */
public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        Assert.assertFalse(offByOne.equalChars('A', 'Z'));
        Assert.assertFalse(offByOne.equalChars('{', 'z'));
        Assert.assertTrue(offByOne.equalChars('b', 'a'));
        Assert.assertFalse(offByOne.equalChars('A', 'a'));
    }
    // Uncomment this class once you've created your CharacterComparator interface and OffByOne class. *
}
