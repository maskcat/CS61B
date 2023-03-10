package byog.lab6;

import org.junit.Test;

public class TestMemoryGame {
    @Test
    public void testString(){
        MemoryGame memoryGame = new MemoryGame(20,30,30);
        String s = memoryGame.generateRandomString(5);
        System.out.println(s);
    }
}
