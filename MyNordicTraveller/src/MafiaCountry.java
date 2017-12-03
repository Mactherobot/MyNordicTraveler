import java.util.List;
import java.util.Map;

/**
 * A subclass of the Country class, that robs you of you'r money. Sometimes!!
 *
 * @author Magnus Niels Jensen
 * @version v1.0
 */
public class MafiaCountry extends Country {
    /**
     * Creates a MafiaCountry object.
     *
     * @param name    //The name of the country.
     * @param network //The map over what cities and roads there's in the country.
     */
    public MafiaCountry(String name, Map<City, List<Road>> network) {
        super(name, network);
    }

    /**
     * Overrides the bonus method from the superclass Country
     *
     * @param value //The value that a given city has
     * @return int
     */
    @Override
    public int bonus(int value) {
        if (getGame().getRandom().nextInt(100) + 1 > getGame().getSettings().getRisk()) {
            return super.bonus(value);
        }
        return -getGame().getLoss();
    }
}
