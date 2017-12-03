import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class RoadTest
{
    private Country country1;
    private City cityA, cityB, cityC;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);
    }
    @Test
    public void constructor() {
        //Checks if the constructor works
        Road road = new Road(cityA, cityB, 4);
        assertEquals(road.getFrom(), cityA);
        assertEquals(road.getTo(),cityB);
        assertEquals(road.getLength(),4);
        //Checks for another road
        Road road2 = new Road(cityB, cityA, 5);
        assertEquals(road2.getFrom(), cityB);
        assertEquals(road2.getTo(),cityA);
        assertEquals(road2.getLength(),5);
    }
    @Test
    public void compareTo() {
        //Checks if the compareTo returns the right values
        Road road1 = new Road(cityA, cityB, 0);
        Road road2 = new Road(cityA, cityC, 0);
        Road road3 = new Road(cityB, cityA, 0);
        assertTrue(road1.compareTo(road2) < 0);
        assertTrue(road1.compareTo(road3) < 0);
        assertTrue(road2.compareTo(road1) > 0);
        assertTrue(road2.compareTo(road3) < 0);
        assertTrue(road3.compareTo(road1) > 0);
        assertTrue(road3.compareTo(road2) > 0);
        assertEquals(road1.compareTo(road1), 0);
        assertEquals(road2.compareTo(road2),0);
        assertEquals(road3.compareTo(road3),0);
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
}