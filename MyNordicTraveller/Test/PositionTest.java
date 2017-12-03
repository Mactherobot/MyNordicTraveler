import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class PositionTest
{
    private Country country1;
    private City cityA, cityB;
    private Position pos;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        // Create countries
        country1 = new Country("Standard.Standard.Country 1", null);
        // Create Cities
        cityA = new City("Standard.Standard.City A", 80, country1);
        cityB = new City("Standard.Standard.City B", 60, country1);
        pos = new Position(cityA, cityB, 3);
    }

    @Test
    public void constructor(){
        //Checks if the constructor works
        assertEquals(pos.getFrom(), cityA);
        assertEquals(pos.getTo(), cityB);
        assertFalse(pos.getDistance() != 3);
        assertFalse(pos.getTotal() != 3);
    }
    @Test
    public void hasArrived() {
        //Checks for when it has arrived
        assertFalse(pos.hasArrived());
        pos.move();
        assertFalse(pos.hasArrived());
        pos.move();
        assertFalse(pos.hasArrived());
        pos.move();
        assertTrue(pos.hasArrived());
        pos.move();
        assertTrue(pos.hasArrived());
    }
    @Test
    public void move() {
        //Checks for every move that it subtracts the right amount
        assertEquals(pos.getDistance(), 3);
        assertTrue(pos.move());
        assertEquals(pos.getDistance(), 2);
        assertTrue(pos.move());
        assertEquals(pos.getDistance(),1);
        assertTrue(pos.move());
        assertEquals(pos.getDistance(),0);
        assertFalse(pos.move());
        assertEquals(pos.getDistance(),0);
    }
    @Test
    public void turnAround() {
        //First it moves then turns around and then checks if the values are correct. Does this twice. And turns around 3 timers
        pos.move();
        pos.turnAround();
        assertEquals(pos.getFrom(), cityB);
        assertEquals(pos.getTo(),cityA);
        assertEquals(pos.getDistance(),1);
        assertEquals(pos.getTotal(),3);
        pos.move();
        pos.turnAround();
        assertEquals(pos.getFrom(),cityA);
        assertEquals(pos.getTo(),cityB);
        assertEquals(pos.getDistance(),3);
        assertEquals(pos.getTotal(),3);
        pos.turnAround();
        assertEquals(pos.getFrom(),cityB);
        assertEquals(pos.getTo(),cityA);
        assertEquals(pos.getDistance(),0);
        assertEquals(pos.getTotal(),3);
    }
    @After
    public void tearDown()
    {
    }
}