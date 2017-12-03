/**
 * A road object that goes form one city to another, and has a certain length.
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class Road implements Comparable<Road> {
    /** The city which the road starts in. */
    private City from;
    /** The city which the road is ending in. */
    private City to;
    /** The length of the road. */
    private int length;

    /**
     * Creates a road object.
     * @param from      //The city which the road starts in.
     * @param to        //The city which the road ends in.
     * @param length    //The length of the road.
     */
    public Road(City from,City to,int length){
        this.from = from;
        this.to = to;
        this.length = length;

    }

    /**
     * Returns the city object to where the road goes
     * @return City
     */
    public City getTo() {
        return to;
    }

    /**
     * Returns the city object from where the road starts.
     * @return City
     */
    public City getFrom() {
        return from;
    }

    /**
     * Returns the length of the road
     * @return int
     */
    public int getLength() {
        return length;
    }

    /**
     * Compares a road lexicographically to another, in the order "from" where the city starts.
     * If they start in the same city it compares in order of where they are going "to".
     * @param other Other road
     * @return int
     */
    public int compareTo(Road other){
        if (!from.equals(other.from)){
            return from.compareTo(other.from);
        }
        else {
            return to.compareTo(other.to);
        }
    }
}
