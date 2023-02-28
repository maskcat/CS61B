/**
 * @author fymas
 */
public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        char[] characters = word.toCharArray();
        for (char character : characters) {
            deque.addLast(character);
        }
        return deque;
    }
    public boolean isPalindrome(String word) {
        return false;
    }
}
