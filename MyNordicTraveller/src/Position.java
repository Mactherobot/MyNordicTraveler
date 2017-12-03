/**
 * A position object, that keeps track of where the player is, and are going.
 *
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class Position {
    /**
     * The city of which the traveler was in.
     */
    private City from;
    /**
     * The city where the traveller is going.
     */
    private City to;
    /**
     * The distance the player has to travel.
     */
    private int distance;
    /**
     * The total distance of the road he is on.
     */
    private int total;

    /**
     * Creates a position object that keeps track of where the player is going, and how far he has moved.
     *
     * @param from     //The city the player originated from
     * @param to       //The city the player is going to.
     * @param distance //The distance the player has yet to travel.
     */
    public Position(City from, City to, int distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.total = distance;
    }

    /**
     * Returns the city from where you are going
     *
     * @return City
     */
    public City getFrom() {
        return from;
    }

    /**
     * Returns the city you're going to.
     *
     * @return City
     */
    public City getTo() {
        return to;
    }

    /**
     * Returns the distance you have left to travel.
     *
     * @return int
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns the total amount of distance there is between 2 cities.
     *
     * @return int
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns true if the distance is 0, and you have arrived at the "to" city
     *
     * @return boolean
     */
    public boolean hasArrived() {
        if (distance == 0) {
            return true;
        }
        return false;
    }

    /**
     * If the distance is greater than 0 you subtract 1 from the distance
     *
     * @return boolean
     */
    public boolean move() {
        if (distance > 0) {
            distance--;
            return true;
        }
        return false;

    }

    /**
     * Turns the player around, so that the from city is now the to, and vice versa.
     * The distance is then the total distance minus the distance already travelled.
     */
    public void turnAround() {
        City c1 = from;
        City c2 = to;
        this.to = c1;
        this.from = c2;
        distance = total - distance;
    }


}
