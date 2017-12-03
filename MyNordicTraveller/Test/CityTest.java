import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class CityTest {
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC;

    /**
     * Sets up the test fixture.
     * <p>
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        game = new Game(0);
        game.getRandom().setSeed(0);
        Map<City, List<Road>> network1 = new HashMap<>();

        // Create countries
        country1 = new Country("Country 1", network1);
        country2 = new Country("Country 2", network1);
        country2.setGame(game);
        country1.setGame(game);

        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);

    }

    @Test
    public void constructor() {
        //Checks for 3 cities that the constructor works
        assertEquals(cityA.getName(), "City A");
        assertEquals(cityA.getCountry(), country1);
        assertEquals(cityA.getValue(), 80);

        assertEquals(cityB.getName(), "City B");
        assertEquals(cityB.getCountry(), country1);
        assertEquals(cityB.getValue(), 60);

        assertEquals(cityC.getName(), "City C");
        assertEquals(cityC.getCountry(), country1);
        assertEquals(cityC.getValue(), 40);

    }


    @Test
    public void compareTo() {
        City imposter = new City("City A", 80, country1);
        //Checks to see if the value you get are correct
        assertTrue(cityA.compareTo(cityB) < 0);
        assertTrue(cityA.compareTo(cityC) < 0);
        assertTrue(cityB.compareTo(cityA) > 0);
        assertTrue(cityB.compareTo(cityC) < 0);
        assertTrue(cityC.compareTo(cityA) > 0);
        assertTrue(cityC.compareTo(cityB) > 0);
        assertEquals(cityA.compareTo(imposter), 0);
        //Checks if they are the same you get the right values
        assertEquals(cityA.compareTo(cityA), 0);
        assertEquals(cityB.compareTo(cityB), 0);
        assertEquals(cityC.compareTo(cityC), 0);
    }

    @Test
    public void arrive() {
        // Run checks on arrive 1000 times with different seeds, and checks to see that both values
        // change correctly and gives the right in return.
        for (int i = 0; i < 1000; i++) {
            game.getRandom().setSeed(i);
            int bonus = country1.bonus(80);
            game.getRandom().setSeed(i);
            int arrive = cityA.arrive();
            assertEquals(arrive, bonus);
            assertEquals(cityA.getValue(), 80 - arrive);
            cityA.reset();
        }
        //Checks if the city can be reduce to 0 in value
        for (int i = 0; i < 1000; i++) {
            cityA.arrive();
        }
        assertEquals(cityA.getValue(), 0);
        //Checks if the value is negative you get 0
        cityA.changeValue(-20);
        assertEquals(cityA.arrive(), 0);

    }

    @Test
    public void reset() {
        // Change the value
        cityA.changeValue(40);
        cityB.changeValue(30);
        // Resets the values
        cityA.reset();
        // Checks if the reset is done
        assertEquals(cityA.getValue(), 80);
        assertEquals(cityB.getValue(), 90);
    }

    @Test
    public void equals() {
        //Checks if the cities are have the same name.
        assertTrue(cityA.equals(cityA));
        assertFalse(cityA.equals(cityB));
        // Now they have same name but not country
        cityB = new City("City A", 60, country2);
        assertFalse(cityA.equals(cityB));
        assertFalse(cityA.equals(null));
        // Now they have same name and country
        cityB = new City("City A",60,country1);
        assertTrue(cityA.equals(cityB));
    }

    @Test
    public void hashCodetest() {
        assertFalse(cityA.hashCode() == cityB.hashCode());
        assertEquals(cityA.hashCode(), cityA.hashCode());
        country2 = new Country("Sweeden", null);
        cityB = new City("City A", 60, country2);
        assertFalse(cityA.hashCode() == cityB.hashCode());
        assertNotEquals(cityA.hashCode(),null);
    }

    /**
     * Tears down the test fixture.
     * <p>
     * Called after every test case method.
     */
    @After
    public void tearDown() {
    }
}