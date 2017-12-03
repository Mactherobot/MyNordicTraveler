import java.util.*;

/**
 * A country object that holds a dictionary of the city and road objects that there is in it.
 *
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class Country {
    /**
     * The name of the country.
     */
    private String name;
    /**
     * A map of all the cities and roads there's in the country.
     */
    private Map<City, List<Road>> network;
    /**
     * The game that the country is connected to.
     */
    private Game game;

    /**
     * Creates a country object.
     *
     * @param name    //The name of the country.
     * @param network //The map over what cities and roads there's in the country.
     */
    public Country(String name, Map<City, List<Road>> network) {
        this.name = name;
        this.network = network;
    }

    /**
     * Returns the name of the country.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the map that contains the cities and roads in the country.
     *
     * @return The network of the cities and roads
     */
    public Map<City, List<Road>> getNetwork() {
        return network;
    }

    /**
     * Returns all the roads that is connected to a given city c, if there is none it returns null.
     *
     * @param c The given city
     * @return List<Road>
     */
    public List<Road> getRoads(City c) {
        List<Road> roadsOfCityC = new ArrayList<>();
        if (network.containsKey(c)) {
            roadsOfCityC.addAll(network.get(c));
        }
        return roadsOfCityC;
    }

    /**
     * Returns a list of the cities there's in the country.
     *
     * @return List<>city
     */
    public List<City> getCities() {
        List<City> cInCountry = new ArrayList<>(network.keySet());
        Collections.sort(cInCountry);
        return cInCountry;
    }

    /**
     * Looks through all the cities in the country and returns a given city if it's in the country
     * else it returns null
     *
     * @param name //The name of the city you're looking for.
     * @return City
     */
    public City getCity(String name) {
        List<City> citiesOfCountry = new ArrayList<>(network.keySet());
        return citiesOfCountry.stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    /**
     * Resets te value of all the cities in the country.
     */
    public void reset() {
        List<City> cInCountry = new ArrayList<>(network.keySet());
        cInCountry.stream().forEach(c -> c.reset());
    }

    /**
     * Returns a bonus that is randomly generated if and only if the of a city is greater than 0.
     *
     * @param value //The value of the city
     * @return int
     */
    public int bonus(int value) {
        if (value > 0) {
            return game.getRandom().nextInt(value + 1);
        }
        return 0;
    }

    /**
     * Creates a road from two cities if they are in the same country, with a given length.
     * If none of the cities are in the country nothing will be done.
     *
     * @param a      //The first city
     * @param b      //The second city
     * @param length //The length between the cities
     */
    public void addRoads(City a, City b, int length) {
        if (network.containsKey(a)) {
            network.get(a).add(new Road(a, b, length));
        }
        if (network.containsKey(b)) {
            network.get(b).add(new Road(b, a, length));
        }
    }

    /**
     * Creates a position object with the same city as to and from, and with a distance of 0.
     *
     * @param city //THe city you're currently in
     * @return Position
     */
    public Position position(City city) {
        return new Position(city, city, 0);
    }

    /**
     * Creates a position between two cities, if it's the same city it will create a position
     * where it will go use the from parameter as the to and a length of 0.
     * And if there's no road between the two cities, it just creates a position in from with a distance of 0.
     *
     * @param from //The city you're traveling from
     * @param to   //The city you're traveling to
     * @return Position
     */
    public Position readyToTravel(City from, City to) {
        if (from.equals(to)) {
            return position(from);
        }

        List<Road> roadFrom = getRoads(from);

        for (Road r : roadFrom
                ) {
            if (r.getTo().equals(to)) {
                return new Position(from, to, r.getLength());
            }
        }
        return position(from);
    }

    /**
     * Returns the game that country is connected to
     *
     * @return Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Set's the field
     *
     * @param game //A game instance
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Returns true if the countries have the same name.
     *
     * @param otherObject // The country that you want to test.
     * @return boolean
     */
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        Country other = (Country) otherObject;
        return name.equals(other.getName());
    }

    /**
     * Creates a hashcode of a country.
     *
     * @return int
     */
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
