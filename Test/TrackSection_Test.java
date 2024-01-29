import org.junit.Test;

import static org.junit.Assert.*;

public class TrackSection_Test {

    //Test add train to section
    @Test
    public void testAddTrain() {
        TrackSection s1 = new TrackSection(1);
        assertFalse(s1.isOccupied());
        Train t1 = new Train("t1", 1, 8);
        s1.addTrain(t1);
        assertTrue(s1.isOccupied());
        assertEquals(s1.getTrain(), t1.trainName);
        s1.addTrain(t1);
    }

    //Test add train IllegalStateException
    @Test(expected = IllegalStateException.class)
    public void testAddException() {
        TrackSection s1 = new TrackSection(1);
        Train t1 = new Train("a1", 1, 8);
        Train t2 = new Train("a2", 1, 8);
        s1.addTrain(t1);
        s1.addTrain(t2);
    }

    //Test move train:
    @Test
    public void testMove(){
        TrackSection s1 = new TrackSection(1);
        TrackSection s5 = new TrackSection(5);
        Train t1 = new Train("b1", 1, 8);
        Train t2 = new Train("b2", 1, 8);
        s1.addTrain(t1);
        s1.moveTrain();
        s5.addTrain(t1);
        assertEquals(s1.getTrain(),null);
        assertFalse(s1.isOccupied());
        assertEquals(s5.getTrain(), t1.trainName);
        assertEquals(t1.getSection(), s5.sectionID);
        s1.addTrain(t2);
        assertEquals(t2.getSection(),s1.sectionID);
        assertEquals(s1.getTrain(), t2.trainName);
        assertTrue(s1.isOccupied());
    }


}