/**
 * A subclass of the BorderCity subclass, that takes payment for the desires that people have.
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class CapitalCity extends BorderCity {
    /**
     * Creates a CapitalCity object as a subclass of BorderCity.
     * @param name //The name of the CapitalCity
     * @param value //The value that the CapitalCity has
     * @param country //The country that the CapitalCity is in
     */
    public CapitalCity(String name, int value, Country country) {
        super(name, value, country);
    }

    /**
     * Overrides the arrive method from the superclass BorderCity
     *
     * @param p //The player that is arriving at the city
     * @return int
     */
    @Override
    public int arrive(Player p){
            int bonus = super.arrive(p);
            int pMoney = p.getMoney() + bonus;
            int desires = getCountry().getGame().getRandom().nextInt(pMoney+1);
            changeValue(desires);
            return bonus - desires;
    }

}