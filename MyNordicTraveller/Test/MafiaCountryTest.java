import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class MafiaCountryTest {
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;
    private Player player;
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

        //Create a player
        player = new Player(new Position(cityF, cityC, 0), 250);

        // Create countries
        country1 = new Country("Country 1", network1);
        country2 = new MafiaCountry("Country 2", network2);
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
    public void bonus() {
        for (int seed = 0; seed < 1000; seed++) {
            game.getRandom().setSeed(seed);
            int robs = 0;
            int loss = 0;
            Set<Integer> values = new HashSet<>();
            for (int i = 0; i < 50000; i++) {
                int bonus = country2.bonus(80);
                if (bonus < 0) {
                    robs++;
                    assertTrue(bonus <= -10 && bonus >= -50);
                    loss -= bonus;
                    values.add(-bonus);
                }
            }
            assertTrue(loss >= 250000 && loss <= 350000);
            assertTrue(robs > 9000 && robs < 11000);
            assertEquals(values.size(), 41);
        }
        //Checks that the mafia gets allllll the money!!
        for (int seed = 0; seed < 1000; seed++) {
            game.getRandom().setSeed(seed);
            int bonus = country2.bonus(90);
            game.getRandom().setSeed(seed);
            if (bonus < 0) {
                cityF.arrive(player);
                assertEquals(cityF.getValue(), 90);
            } else {
                cityF.arrive(player);
                assertEquals(cityF.getValue(), 90 - bonus);
            }
            cityF.reset();
        }
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