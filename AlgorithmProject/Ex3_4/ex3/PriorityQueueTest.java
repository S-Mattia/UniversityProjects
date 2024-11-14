package ex3;

import java.beans.Transient;
import java.util.Comparator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 11/05/2024
 * @details Unit-testing for The implementation of PriorityQueue;
 **/

public class PriorityQueueTest {

    class ComparatorInt implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);
        }
    }

    class ComparatorStringt implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    class Student {
        public String name;
        public String surname;
        public Integer badge;

        public Student(String nameNew, String surnameNew, int badgeNew) {
            this.name = nameNew;
            this.surname = surnameNew;
            this.badge = badgeNew;
        }

    }

    class ComparatorStudent implements Comparator<Student> {
        @Override
        public int compare(Student a, Student b) {
            return (a.badge).compareTo(b.badge);
        }
    }

    private int i1, i2, i3;
    private String s1, s2, s3;
    private Student st1, st2, st3;
    PriorityQueue<Integer> PriorityQueueInt;
    PriorityQueue<Student> PriorityQueueStudent;
    PriorityQueue<String> PriorityQueueString;

    @Before
    public void createPriorityQueue() {
        i1 = 34;
        i2 = 69;
        i3 = -53;
        PriorityQueueInt = new PriorityQueue<>(new ComparatorInt());

        st1 = new Student("mattia", "giorgi", 3456);
        st2 = new Student("gabriele", "bianchi", 8317);
        st3 = new Student("andrea", "ricci", 4567);
        PriorityQueueStudent = new PriorityQueue<>(new ComparatorStudent());

        s1 = "albero";
        s2 = "cima";
        s3 = "zattera";
        PriorityQueueString = new PriorityQueue<>(new ComparatorStringt());
    }

    // TEST PRIORITY QUEUE INT
    @Test
    public void testIsEmpty_zeroEl_int() {
        assertTrue(PriorityQueueInt.empty());
    }

    @Test
    public void testIsEmpty_oneEl_int() {
        PriorityQueueInt.push(i1);
        assertFalse(PriorityQueueInt.empty());
    }

    @Test
    public void testContains_zeroEl_int() {
        assertFalse(PriorityQueueInt.contains(i1));
    }

    @Test
    public void testContains_oneEl_int() {
        PriorityQueueInt.push(i1);
        assertTrue(PriorityQueueInt.contains(i1));
    }

    @Test
    public void testContains_threeEl_int() {
        PriorityQueueInt.push(i1);
        PriorityQueueInt.push(i2);
        PriorityQueueInt.push(i3);
        assertTrue(PriorityQueueInt.contains(i1));
        assertTrue(PriorityQueueInt.contains(i2));
        assertTrue(PriorityQueueInt.contains(i3));
    }

    @Test
    public void testTop_oneEl_int() {
        PriorityQueueInt.push(i1);
        assertTrue(PriorityQueueInt.top() == i1);
    }

    @Test
    public void testTop_threeEl_int() {
        PriorityQueueInt.push(i1);
        PriorityQueueInt.push(i2);
        PriorityQueueInt.push(i3);
        assertTrue((PriorityQueueInt.top()) == i3);
        PriorityQueueInt.pop();
        assertTrue((PriorityQueueInt.top()) == i1);
        PriorityQueueInt.pop();
        assertTrue((PriorityQueueInt.top()) == i2);
        PriorityQueueInt.pop();
    }

    @Test
    public void testPop_oneEl_int() {
        PriorityQueueInt.push(i1);
        PriorityQueueInt.pop();
        assertFalse(PriorityQueueInt.contains(i1));
        assertTrue(PriorityQueueInt.empty());
    }

    @Test
    public void testPop_threeEl_int() {
        PriorityQueueInt.push(i1);
        PriorityQueueInt.push(i2);
        PriorityQueueInt.push(i3);
        PriorityQueueInt.pop();
        PriorityQueueInt.pop();
        PriorityQueueInt.pop();
        assertFalse(PriorityQueueInt.contains(i1) || PriorityQueueInt.contains(i2) || PriorityQueueInt.contains(i3));
        assertTrue(PriorityQueueInt.empty());
    }

    @Test
    public void testPush_oneEl_int() {
        PriorityQueueInt.push(i1);
        assertTrue(PriorityQueueInt.contains(i1));
        assertTrue(PriorityQueueInt.top() == i1);
    }

    @Test
    public void testPush_threeEl_int() {
        PriorityQueueInt.push(i1);
        PriorityQueueInt.push(i2);
        PriorityQueueInt.push(i3);
        assertTrue(PriorityQueueInt.contains(i1) && PriorityQueueInt.contains(i2) && PriorityQueueInt.contains(i3));
        assertFalse(PriorityQueueInt.empty());
    }

    // TEST PRIORITY QUEUE STRING
    @Test
    public void testIsEmpty_zeroEl_String() {
        assertTrue(PriorityQueueString.empty());
    }

    @Test
    public void testIsEmpty_oneEl_String() {
        PriorityQueueString.push(s1);
        assertFalse(PriorityQueueString.empty());
    }

    @Test
    public void testContains_zeroEl_String() {
        assertFalse(PriorityQueueString.contains(s1));
    }

    @Test
    public void testContains_oneEl_String() {
        PriorityQueueString.push(s1);
        assertTrue(PriorityQueueString.contains(s1));
    }

    @Test
    public void testContains_threeEl_String() {
        PriorityQueueString.push(s1);
        PriorityQueueString.push(s2);
        PriorityQueueString.push(s3);
        assertTrue(PriorityQueueString.contains(s1) && PriorityQueueString.contains(s2)
                && PriorityQueueString.contains(s3));
    }

    @Test
    public void testTop_oneEl_String() {
        PriorityQueueString.push(s1);
        assertTrue(PriorityQueueString.top().compareTo(s1) == 0);
    }

    @Test
    public void testTop_threeEl_String() {
        PriorityQueueString.push(s2);
        PriorityQueueString.push(s3);
        PriorityQueueString.push(s1);
        assertTrue((PriorityQueueString.top().compareTo(s1)) == 0);
        PriorityQueueString.pop();
        assertTrue((PriorityQueueString.top().compareTo(s2)) == 0);
        PriorityQueueString.pop();
        assertTrue((PriorityQueueString.top().compareTo(s3)) == 0);
        PriorityQueueString.pop();
    }

    @Test
    public void testPop_oneEl_String() {
        PriorityQueueString.push(s1);
        PriorityQueueString.pop();
        assertFalse(PriorityQueueString.contains(s1));
        assertTrue(PriorityQueueString.empty());
    }

    @Test
    public void testPop_threeEl_String() {
        PriorityQueueString.push(s1);
        PriorityQueueString.push(s2);
        PriorityQueueString.push(s3);
        PriorityQueueString.pop();
        PriorityQueueString.pop();
        PriorityQueueString.pop();
        assertFalse(PriorityQueueString.contains(s1) || PriorityQueueString.contains(s2)
                || PriorityQueueString.contains(s3));
        assertTrue(PriorityQueueString.empty());
    }

    @Test
    public void testPush_oneEl_String() {
        PriorityQueueString.push(s1);
        assertTrue(PriorityQueueString.contains(s1));
        assertTrue((PriorityQueueString.top().compareTo(s1)) == 0);
    }

    @Test
    public void testPush_threeEl_String() {
        PriorityQueueString.push(s1);
        PriorityQueueString.push(s2);
        PriorityQueueString.push(s3);
        assertTrue(PriorityQueueString.contains(s1) && PriorityQueueString.contains(s2)
                && PriorityQueueString.contains(s3));
        assertFalse(PriorityQueueString.empty());
    }

    // TEST PRIORITY QUEUE STUDENT
    @Test
    public void testIsEmpty_zeroEl_Student() {
        assertTrue(PriorityQueueStudent.empty());
    }

    @Test
    public void testIsEmpty_oneEl_Student() {
        PriorityQueueStudent.push(st1);
        assertFalse(PriorityQueueStudent.empty());
    }

    @Test
    public void testContains_zeroEl_Student() {
        assertFalse(PriorityQueueStudent.contains(st1));
    }

    @Test
    public void testContains_oneEl_Student() {
        PriorityQueueStudent.push(st1);
        assertTrue(PriorityQueueStudent.contains(st1));
    }

    @Test
    public void testContains_threeEl_Student() {
        PriorityQueueStudent.push(st1);
        PriorityQueueStudent.push(st2);
        PriorityQueueStudent.push(st3);
        assertTrue(PriorityQueueStudent.contains(st1) && PriorityQueueStudent.contains(st2)
                && PriorityQueueStudent.contains(st3));
    }

    @Test
    public void testTop_oneEl_Student() {
        PriorityQueueStudent.push(st1);
        assertTrue((new ComparatorStudent()).compare((PriorityQueueStudent.top()), st1) == 0);
    }

    @Test
    public void testTop_threeEl_Student() {
        PriorityQueueStudent.push(st2);
        PriorityQueueStudent.push(st3);
        PriorityQueueStudent.push(st1);
        assertTrue((new ComparatorStudent()).compare((PriorityQueueStudent.top()), st1) == 0);
        PriorityQueueStudent.pop();
        assertTrue((new ComparatorStudent()).compare((PriorityQueueStudent.top()), st3) == 0);
        PriorityQueueStudent.pop();
        assertTrue((new ComparatorStudent()).compare((PriorityQueueStudent.top()), st2) == 0);
        PriorityQueueStudent.pop();
    }

    @Test
    public void testPop_oneEl_Student() {
        PriorityQueueStudent.push(st1);
        PriorityQueueStudent.pop();
        assertFalse(PriorityQueueStudent.contains(st1));
        assertTrue(PriorityQueueStudent.empty());
    }

    @Test
    public void testPop_threeEl_Student() {
        PriorityQueueStudent.push(st1);
        PriorityQueueStudent.push(st2);
        PriorityQueueStudent.push(st3);
        PriorityQueueStudent.pop();
        PriorityQueueStudent.pop();
        PriorityQueueStudent.pop();
        assertFalse(PriorityQueueStudent.contains(st1) || PriorityQueueStudent.contains(st2)
                || PriorityQueueStudent.contains(st3));
        assertTrue(PriorityQueueStudent.empty());
    }

    @Test
    public void testPush_oneEl_Student() {
        PriorityQueueStudent.push(st1);
        assertTrue(PriorityQueueStudent.contains(st1));
        assertTrue((new ComparatorStudent()).compare((PriorityQueueStudent.top()), st1) == 0);
    }

    @Test
    public void testPush_threeEl_Student() {
        PriorityQueueStudent.push(st1);
        PriorityQueueStudent.push(st2);
        PriorityQueueStudent.push(st3);
        assertTrue(PriorityQueueStudent.contains(st1) && PriorityQueueStudent.contains(st2)
                && PriorityQueueStudent.contains(st3));
        assertFalse(PriorityQueueStudent.empty());
    }

}
