import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class BorderCityTest {
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityE;
    private Position pos;

    /**
     * Sets up the test fixture.
     *
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
        cityA = new BorderCity("City A", 80, country1);
        cityB = new BorderCity("City B", 60, country1);
        cityC = new BorderCity("City C", 40, country1);
        cityE = new BorderCity("City E", 30, country2);

    }

    @Test
    public void arriveFromOtherCountry() {
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(cityE,cityC,0),250);
            game.getRandom().setSeed(i); // Set seed
            int bonus = country1.bonus(40); // Remember bonus
            game.getRandom().setSeed(i); // Reset seed
            int toll = player.getMoney() * cityC.getCountry().getGame().getSettings().getTollToBePaid()  /100; // Calculate toll
            int arrive = cityC.arrive(player); // Same bonus
            assertEquals(arrive,bonus - toll);
            assertEquals(cityC.getValue(), 40 - bonus + toll);
            cityC.reset(); }

        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(cityB,cityC,0),250);
            game.getRandom().setSeed(i); // Set seed
            int bonus = country1.bonus(40); // Remember bonus
            game.getRandom().setSeed(i); // Reset seed
            int arrive = cityC.arrive(player); // Same bonus
            assertEquals(arrive,bonus);
            assertEquals(cityC.getValue(), 40 - bonus);
            cityC.reset(); }
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