/**
 * @author fymas
 */
public class OffByN implements CharacterComparator {
    private final int diff;

    public OffByN(int n) {
        this.diff = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        if (!Character.isAlphabetic(x) || !Character.isAlphabetic(y)) {
            return false;
        }
        return Math.abs(x - y) == this.diff;
    }
}
