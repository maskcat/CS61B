import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testDeque() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        StringBuilder message = new StringBuilder("\n");
        while (true) {
            switch (StdRandom.uniform(3)) {
                case 0:
                    int case0first1 = StdRandom.uniform(100);
                    student.addFirst(case0first1);
                    solution.addFirst(case0first1);
                    message.append("addFirst(").append(case0first1).append(")\n");
                    break;
                case 1:
                    int case1last1 = StdRandom.uniform(100);
                    student.addLast(case1last1);
                    solution.addLast(case1last1);
                    message.append("addLast(").append(case1last1).append(")\n");

                    break;
                case 2:
                    if (student.isEmpty()) {
                        break;
                    }
                    Integer case2actual = student.removeLast();
                    Integer case2expect = solution.removeLast();
                    message.append("removeLast()\n");
                    assertEquals(message.toString(), case2expect, case2actual);
                    break;
                default:
                    if (student.isEmpty()) {
                        break;
                    }
                    Integer case3actual = student.removeFirst();
                    Integer case3expect = solution.removeFirst();
                    message.append("removeFirst()\n");
                    assertEquals(message.toString(), case3expect, case3actual);
                    break;
            }
        }
    }
}
