import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class CountryTest {
    private Game game;
    private Country country1, country2, country3, country4;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;
    Map<City, List<Road>> network1;

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
        Map<City, List<Road>> network2 = new HashMap<>();

        // Create countries
        country1 = new Country("Country 1", network1);
        country2 = new Country("Country 2", network2);
        country1.setGame(game);
        country2.setGame(game);

        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);
        cityD = new City("City D", 100, country1);
        cityE = new City("City E", 50, country2);
        cityF = new City("City F", 90, country2);
        cityG = new City("City G", 70, country2);

        // Create road lists
        List<Road>
                roadsA = new ArrayList<Road>(),
                roadsB = new ArrayList<>(),
                roadsC = new ArrayList<>(),
                roadsD = new ArrayList<>(),
                roadsE = new ArrayList<>(),
                roadsF = new ArrayList<>(),
                roadsG = new ArrayList<>();

        network1.put(cityA, roadsA);
        network1.put(cityB, roadsB);
        network1.put(cityC, roadsC);
        network1.put(cityD, roadsD);
        network2.put(cityE, roadsE);
        network2.put(cityF, roadsF);
        network2.put(cityG, roadsG);

        // Create roads
        country1.addRoads(cityA, cityB, 4);
        country1.addRoads(cityA, cityC, 3);
        country1.addRoads(cityA, cityD, 5);
        country1.addRoads(cityB, cityD, 2);
        country1.addRoads(cityC, cityD, 2);
        country1.addRoads(cityC, cityE, 4);
        country1.addRoads(cityD, cityF, 3);
        country2.addRoads(cityE, cityC, 4);
        country2.addRoads(cityE, cityF, 2);
        country2.addRoads(cityE, cityG, 5);
        country2.addRoads(cityF, cityD, 3);
        country2.addRoads(cityF, cityG, 6);
    }

    @Test
    public void constructor() {
        //Checks if the constructor works
        assertEquals(country1.getName(), "Country 1");
        assertEquals(country2.getName(), "Country 2");
        assertTrue(country1.getNetwork() != null);
        assertTrue(country1.getNetwork().keySet().size() == 4);
        assertTrue(country1.getNetwork().containsKey(cityA));
        assertTrue(country1.getNetwork().containsKey(cityB));
    }

    @Test
    public void addRoad() {
        //Test to see if the roads are added correctly, by first looking at a city form a to e.
        country1.addRoads(cityA, cityE, 4);
        for (City c : country1.getCities()) {
            for (Road r : country1.getNetwork().get(c)) {
                assertTrue(!r.getFrom().equals(cityE));
            }
        }

        Road newRoad = null;
        for (Road r : country1.getNetwork().get(cityA)) {
            if (r.getTo().equals(cityE)) {
                newRoad = r;
            }
        }
        assertTrue(newRoad != null);
        assertTrue(newRoad.getFrom().equals(cityA));
        assertTrue(newRoad.getLength() == 4);

        //Test if the road is added correctly when it originates in different countries
        country1.addRoads(cityE, cityB, 4);
        for (City c : country1.getNetwork().keySet()) {
            for (Road r : country1.getNetwork().get(c)) {
                assertTrue(!r.getFrom().equals(cityE));
            }
        }

        for (Road r : country1.getNetwork().get(cityB)) {
            if (r.getTo().equals(cityE)) {
                newRoad = r;
            }
        }
        assertTrue(newRoad != null);
        assertTrue(newRoad.getFrom().equals(cityB));
        assertTrue(newRoad.getLength() == 4);
    }

    @Test
    public void reset() {
        cityA.arrive();
        cityA.arrive();
        cityA.arrive();
        cityE.arrive();
        cityE.arrive();
        cityE.arrive();
        int valueE = cityE.getValue();

        country1.reset();
        //Test if the reset did anything
        assertEquals(cityA.getValue(), 80);
        //Test if it resets other countries
        assertEquals(cityE.getValue(), valueE);
    }

    @Test
    public void bonus() {
        for (int seed = 0; seed < 1000; seed++) { // Try 1000 different seeds
            game.getRandom().setSeed(seed);
            int sum = 0;
            int sum1 = 0;
            Set<Integer> values = new HashSet<>();
            Set<Integer> values1 = new HashSet<>();
            for (int i = 0; i < 10000; i++) { // Call method 10000 times
                int bonus = country1.bonus(80);
                int bonus1 = country1.bonus(1);
                assertTrue(0 <= bonus1 && bonus1 <= 1);
                assertTrue(0 <= bonus && bonus <= 80);
                sum += bonus;
                sum1 += bonus1;
                values.add(bonus);
                values1.add(bonus1);
            }
            assertTrue(390000 < sum && sum < 420000);
            assertEquals(values.size(), 81);
            assertEquals(values1.size(), 2);
        }
        assertEquals(country1.bonus(0), 0);
        assertEquals(country1.bonus(-10), 0);

    }

    @Test
    public void getCity() {
        //Checks if we can find a city with null
        assertEquals(country1.getCity(null), null);
        //Checks if it works
        assertEquals(country1.getCity("City A"), cityA);
        //Checks if there's no city wih that name
        assertEquals(country1.getCity("Test"), null);
    }

    @Test
    public void getCities() {
        //Creates and shuffles a list
        List<City> cityList = new ArrayList<>(country1.getNetwork().keySet());
        Collections.shuffle(cityList);

        //Checks if we get a sorted list
        assertFalse(country1.getCities().equals(cityList));
        Collections.sort(cityList);
        assertEquals(country1.getCities(), cityList);

        //Checks if the list is empty
        country1.getNetwork().clear();
        assertTrue(country1.getCities().isEmpty());
    }

    @Test
    public void getRoads() {
        //Checks if we get null
        assertTrue(country1.getRoads(cityA) != null);
        assertTrue(country1.getRoads(cityE) != null);

        //Checks if it works
        List<Road> test = new ArrayList<>(country1.getNetwork().get(cityA));
        assertEquals(country1.getRoads(cityA), test);

        //Checks if the city is not in the country
        assertTrue(country1.getRoads(cityG).isEmpty());

        //Checks that we get a empty list if we use null
        assertTrue(country1.getRoads(null).isEmpty());
    }

    @Test
    public void readyToTravel() {
        // Verifies travel of a bidirectional route fra different cities and constellations.
        Position newPosTest = country1.readyToTravel(cityA, cityB);
        assertEquals(newPosTest.getTo(), cityB);
        assertEquals(newPosTest.getFrom(), cityA);
        assertEquals(newPosTest.getDistance(), 4);

        // Checks with international travel.
        newPosTest = country1.readyToTravel(cityC, cityE);
        assertEquals(newPosTest.getTo(), cityE);
        assertEquals(newPosTest.getFrom(), cityC);
        assertEquals(newPosTest.getDistance(), 4);

        // Checks a travel distance of 0
        newPosTest = country1.readyToTravel(cityA, cityA);
        assertEquals(newPosTest.getTo(), cityA);
        assertEquals(newPosTest.getFrom(), cityA);
        assertEquals(newPosTest.getDistance(), 0);


        // Checks if there's no road between the countries
        newPosTest = country1.readyToTravel(cityF, cityA);
        assertEquals(newPosTest.getTo(), cityF);
        assertEquals(newPosTest.getFrom(), cityF);
        assertEquals(newPosTest.getDistance(), 0);
    }

    @Test
    public void Position() {
        //Checks if position sets and returns correctly
        Position pos = country1.position(cityA);
        assertEquals(pos.getTo(), cityA);
        assertEquals(pos.getFrom(), cityA);
        assertEquals(pos.getDistance(), 0);
    }

    @Test
    public void equals() {
        country3 = new Country("Country 1", network1);
        assertTrue(country1.equals(country1));
        assertFalse(country1.equals("Country 1"));
        assertFalse(country1.equals(country2));
        assertTrue(country1.equals(country3));
        assertFalse(country1.equals(null));
    }

    @Test
    public void hashCodeTest() {
        country4 = new Country("Country 1", null);
        assertFalse(country1.hashCode() == country2.hashCode());
        assertEquals(country1.hashCode(), country1.hashCode());
        assertTrue(country4.hashCode() == country1.hashCode());
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
