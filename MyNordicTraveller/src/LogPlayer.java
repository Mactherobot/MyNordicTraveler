import java.awt.Color;
/**
 * An AI player which chooses its path based on a log. Simulates a GUI player.
 * @author Nikolaj Ignatieff Schwartzbach
 * @version 1.0.0
 */
public class LogPlayer extends Player {

    private Log log;
    private int t;
    
    /**
     * Instantiates a new LogPlayer from a given log at a given position.
     * @param log The log to use.
     * @param pos The position of this LogPlayer.
     */
    public LogPlayer(Log log, Position pos) {
        super(pos);
        t = 0;
        this.log = log;
    }
    
    @Override
    public void step(){
        super.step();
        String choice = log.getChoice(++t);
        
        if(choice==null)
            return;
        
        if(choice.equals("Aborted")){
            getCountry().getGame().abort();
            return;
        }
        
        getCountry().getGame().clickCity(getCountry().getGame().getCity(choice));
    }

    @Override
    public String getName(){
        return "GUI Player (log)";
    }
    
    @Override
    public Color getColor(){
        return Color.RED;
    }
}
