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
        Deque<Character> deque = wordToDeque(word);
        return helpIsPalindrome(deque);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        return helpIsPalindrome(deque, cc);
    }

    private boolean helpIsPalindrome(Deque<Character> deque) {
        if (deque == null || deque.size() <= 1) {
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (first != last) {
            return false;
        }
        return helpIsPalindrome(deque);
    }

    private boolean helpIsPalindrome(Deque<Character> deque, CharacterComparator characterComparator) {
        if (deque == null || deque.size() <= 1) {
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (!characterComparator.equalChars(first, last)) {
            return false;
        }
        return helpIsPalindrome(deque, characterComparator);
    }
}
