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
public class CapitalCityTest {
    private Game game;
    private Country country1, country2, country3;
    private City cityA, cityB, cityC, cityE, cityD;
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
        country3 = new MafiaCountry("Country 3", network1);
        country3.setGame(game);
        country2.setGame(game);
        country1.setGame(game);

        // Create Cities
        cityA = new CapitalCity("City A", 80, country3);
        cityB = new City("City B", 60, country2);
        cityC = new City("City C", 40, country1);
        cityD = new CapitalCity("City D", 100, country1);
        cityE = new CapitalCity("City E", 50, country2);

    }

    @Test
    public void arriveFromOtherCountry() {
        //Checks if you arrive at a capital from outside the country
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(cityC,cityE,0),250);
            game.getRandom().setSeed(i);
            int bonus = country2.bonus(50);
            int toll = (player.getMoney() * game.getSettings().getTollToBePaid()) / 100;
            int desires = game.getRandom().nextInt(player.getMoney() + bonus - toll + 1);
            game.getRandom().setSeed(i);
            int arrive = cityE.arrive(player);
            assertEquals(arrive, bonus - toll - desires);
            assertEquals(cityE.getValue(), 50 - bonus + desires + toll);
            cityE.reset(); }

        //Checks if you arrive at a capital from a city within the country
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(cityB,cityE,0),250);
            game.getRandom().setSeed(i);
            int bonus = country2.bonus(50);
            int desires = cityE.getCountry().getGame().getRandom().nextInt(player.getMoney()+bonus + 1);
            game.getRandom().setSeed(i);
            int arrive = cityE.arrive(player);
            assertEquals(arrive,bonus - desires);
            assertEquals(cityE.getValue(), 50 - bonus + desires);
            cityE.reset(); }

        //Checks if you arrive at a MafiaCapital
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(cityC,cityA,0),250);
            game.getRandom().setSeed(i);
            int bonus = country3.bonus(80);
            int toll = (player.getMoney() * game.getSettings().getTollToBePaid()) / 100;
            int desires = game.getRandom().nextInt(player.getMoney() + bonus - toll + 1);
            game.getRandom().setSeed(i);
            if (bonus < 0) {
                assertEquals(cityA.arrive(player), bonus - toll - desires);
                assertEquals(cityA.getValue(), 80 + desires + toll);
            }else{
                assertEquals(cityA.arrive(player), bonus - toll - desires);
                assertEquals(cityA.getValue(), 80 - bonus + desires + toll);
            }
            cityA.reset(); }
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