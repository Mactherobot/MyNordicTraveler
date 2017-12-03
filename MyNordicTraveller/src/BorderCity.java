/**
 * A subclass of the City class, that is on the borders between countries.
 * They take a toll for traveling to them from other countries.
 *
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class BorderCity extends City {
    /**
     * Creates a BorderCity object as a subclass of City.
     *
     * @param name    //The name of the BorderCity
     * @param value   //The value that the BorderCity has
     * @param country //The country that the BorderCity is in
     */
    public BorderCity(String name, int value, Country country) {
        super(name, value, country);
    }

    /**
     * Overrides the arrive method from the superclass City
     *
     * @param p //The player that is arriving at the city
     * @return int
     */
    @Override
    public int arrive(Player p) {
        if (!p.getCountryFrom().equals(getCountry())) {
            int toll = p.getMoney() * getCountry().getGame().getSettings().getTollToBePaid() / 100;
            int bonus = super.arrive(p);
            changeValue(toll);
            return bonus - toll;
        }
        return super.arrive(p);
    }
}
