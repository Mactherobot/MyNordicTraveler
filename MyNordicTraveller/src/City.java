import java.util.Objects;

/**
 * A City object which contains the value of the city, and which country the city is in.
 *
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class City implements Comparable<City> {
    /**
     * The name of the City.
     */
    private String name;
    /**
     * The value the city has.
     */
    private int value;
    /**
     * The initial value of the City.
     */
    private int initialValue;
    /**
     * The country the City is in.
     */
    private Country country;

    /**
     * This is the constructor which creates the City object.
     *
     * @param name    //This represents the name of the City
     * @param value   //This is the value that the City has.
     * @param country //This represents the country the City is in.
     */
    public City(String name, int value, Country country) {
        this.name = name;
        this.value = value;
        this.initialValue = value;
        this.country = country;

    }

    /**
     * Returns the name of the city.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the city.
     *
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Adds the amount to the value of the city.
     *
     * @param amount The amount to be added.
     */
    public void changeValue(int amount) {
        this.value += amount;
    }

    /**
     * This resets the value of the city back to the initial value.
     */
    public void reset() {
        this.value = initialValue;
    }

    /**
     * Returns 0 if the city object has the same name as the one that is being compared.
     * Returns n>0 if the first city is lexicographically greater then the second one else it would return n<0.
     *
     * @param other The other city object.
     * @return int
     */
    public int compareTo(City other) {
        return name.compareTo(other.name);
    }

    /**
     * Returns the country of the city object.
     *
     * @return Country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Reduces the value of the city by the bonus from country class, and then returns the bonus value.
     *
     * @return int
     */
    public int arrive() {
        int v = country.bonus(value);
        if (v > 0) {
            value -= v;
            return v;
        } else {
            return v;
        }
    }

    /**
     * New city method that calls the other one.
     *
     * @param p //The player that is at the city
     * @return int
     */
    public int arrive(Player p) {
        return arrive();
    }

    /**
     * Looks to see if the city objects are the same.
     *
     * @param city //The city that you are comparing
     * @return boolean
     */
    public boolean equals(City city) {
        if (this == city) {
            return true;
        }
        if (city == null) {
            return false;
        }
        if (getClass() != city.getClass()) {
            return false;
        }
        City other = (City) city;
        return name.equals(city.getName()) && country.equals(city.getCountry());
    }

    /**
     * Makes a hashcode of the city.
     *
     * @return int
     */
    public int hashCode() {
        return Objects.hash(this.name, this.country);
    }
}
