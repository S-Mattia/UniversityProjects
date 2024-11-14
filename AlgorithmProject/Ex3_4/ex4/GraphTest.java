package ex4;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 19/05/2024
 * @details Unit-testing for The implementation of GRAPH;
 **/

public class GraphTest {

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

    private int i1, i2, i3;
    private String s1, s2, s3;
    private Student st1, st2, st3;
    Graph<Integer, Integer> GraphInteger;
    Graph<Student, Integer> GraphStudent;
    Graph<String, Integer> GraphString;
    ComparatorEdge compE;

    @Before
    public void createGraphs() {
        i1 = 34;
        i2 = 69;
        i3 = -53;
        GraphInteger = new Graph<>(false, false);

        st1 = new Student("mattia", "giorgi", 3456);
        st2 = new Student("gabriele", "bianchi", 8317);
        st3 = new Student("andrea", "ricci", 4567);
        GraphStudent = new Graph<>(true, true);

        s1 = "albero";
        s2 = "cima";
        s3 = "zattera";
        GraphString = new Graph<>(false, false);

        compE = new ComparatorEdge();
    }

    // TEST PRIORITY QUEUE INT
    @Test
    public void testIsDirected_Int() {
        assertFalse(GraphInteger.isDirected());
    }

    @Test
    public void testIsLabelled_Int() {
        assertFalse(GraphInteger.isLabelled());
    }

    @Test
    public void testNumNode_zeroEl_int() {
        assertEquals(GraphInteger.numNodes(), 0);
    }

    @Test
    public void testNumEdges_zeroEl_int() {
        assertEquals(GraphInteger.numEdges(), 0);
    }

    @Test
    public void testNumNode_oneEl_int() {
        GraphInteger.addNode(i1);
        assertEquals(GraphInteger.numNodes(), 1);
    }

    @Test
    public void testNumEdges_oneEl_int() {
        GraphInteger.addEdge(i3, i2, null);
        assertEquals(GraphInteger.numEdges(), 1);
    }

    @Test
    public void testNumNode_threeEl_int() {
        GraphInteger.addNode(i1);
        GraphInteger.addEdge(i2, i3, null);
        assertEquals(GraphInteger.numNodes(), 3);
    }

    @Test
    public void testNumEdges_threeEl_int() {
        GraphInteger.addEdge(i1, i2, null);
        GraphInteger.addEdge(i2, i3, null);
        GraphInteger.addEdge(i3, i1, null);
        assertEquals(GraphInteger.numEdges(), 3);
    }

    @Test
    public void testAddNode_oneEl_int() {
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(i1);
        GraphInteger.addNode(i1);
        assertEquals(GraphInteger.getNodes(), expected);
    }

    @Test
    public void testAddNode_threeEl_int() {
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(i1);
        GraphInteger.addNode(i1);
        GraphInteger.addEdge(i2, i3, null);
        expected.add(i2);
        expected.add(i3);
        assertEquals(GraphInteger.getNodes(), expected);
    }

    @Test
    public void testAddEdges_oneEl_int() {
        GraphInteger.addEdge(i2, i3, null);
        assertTrue(GraphInteger.containsEdge(i2, i3));
    }

    @Test
    public void testAddEdges_threeEl_int() {
        GraphInteger.addEdge(i2, i3, null);
        GraphInteger.addEdge(i1, i3, null);
        GraphInteger.addEdge(i2, i1, null);
        assertTrue(GraphInteger.containsEdge(i2, i3));
        assertTrue(GraphInteger.containsEdge(i3, i2));
        assertTrue(GraphInteger.containsEdge(i1, i3));
        assertTrue(GraphInteger.containsEdge(i3, i1));
        assertTrue(GraphInteger.containsEdge(i2, i1));
        assertTrue(GraphInteger.containsEdge(i1, i2));
    }

    @Test
    public void testContainsNode_oneEl_int() {
        GraphInteger.addNode(i1);
        assertTrue(GraphInteger.containsNode(i1));
    }

    @Test
    public void testContainsNode_threeEl_int() {
        ;
        GraphInteger.addNode(i1);
        GraphInteger.addEdge(i2, i3, null);
        assertTrue(GraphInteger.containsNode(i1));
        assertTrue(GraphInteger.containsNode(i2));
        assertTrue(GraphInteger.containsNode(i3));
    }

    // TEST PRIORITY QUEUE STRING
    @Test
    public void testIsDirected_String() {
        assertFalse(GraphString.isDirected());
    }

    @Test
    public void testIsLabelled_String() {
        assertFalse(GraphString.isLabelled());
    }

    // TEST PRIORITY QUEUE Student
    @Test
    public void testIsDirected_Student() {
        assertTrue(GraphStudent.isDirected());
    }

    @Test
    public void testIsLabelled_Student() {
        assertTrue(GraphStudent.isLabelled());
    }

}