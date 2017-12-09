import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Log records what happened in a single instance of this game.
 * Is uniquely determined by its String representation, and can thus be saved on the file system.
 * @author Nikolaj Ignatieff Schwartzbach
 * @version 1.0.0
 *
 */
public class Log {
    public static void main(String[] args) {
    testCG5strings();
    }

    /** A map of timestamps to city names */
    private Map<Integer, String> choices;

    /** The seed of this Log */
    private int seed;

    /** A reference to the current Settings object */
    private Settings settings;

    /**
     * Instantiates a new Log object with a given seed, and Settings object.
     * @param seed The seed of this log.
     * @param settings The Settings object.
     */
    public Log(int seed, Settings settings){
        choices = new HashMap<>();
        this.seed = seed;
        this.settings = settings;
    }

    /**
     * Instructs this Log that the Game was forcefully aborted.
     * @param time
     */
    public void abort(int time){
        choices.put(50-time, "Aborted");
    }

    /**
     * Instantiates a new Log object from its String representation.
     * @param s The string representation of this Log object.
     * @throws SettingsException May be thrown if the Settings is corrupted (too few lines, illegal format).
     * @throws LogException May be thrown if it is unable to properly create this log.
     */
    public Log(String log) throws SettingsException, LogException{
        String[] s = log.split("\r\n");
        this.seed = Integer.parseInt(s[0]);
        String[] settings = Arrays.copyOfRange(s,1,7);
        this.settings = new Settings(settings);
        HashMap<Integer,String> ch = new HashMap<>();
        for (int i = 7; i < s.length; i++) {
            String[] map = s[i].split(" ");
                ch.put(Integer.parseInt(map[0]),map[1]);
        }
        this.choices = ch;
    }

    /**
     * Gets the current Settings associated with this Log.
     * @return A Settings object.
     */
    public Settings getSettings(){
        return settings;
    }

    /**
     * Gets the seed of this log.
     * @return An integer representing the seed (passed to Random object) of this log.
     */
    public int getSeed(){
        return seed;
    }

    /**
     * Associates a given time with a given city.
     * Will then attempt to click on the same cities at the same time during playback to simulate the GUI player.
     * @param t The current time.
     * @param c The city which is clicked at the time 't'.
     */
    public void add(int t, City c){
        choices.put(c.getCountry().getGame().getTotalTimeLeft()-t, c.getName());
    }

    /**
     * Gets which city was clicked at a given time.
     * Will return 'null' if no city was clicked at the specified time.
     * @param t The time step.
     * @return A string representing the name of the city which was clicked at time 't'.
     */
    public String getChoice(int t){
        return choices.get(t);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(seed + "\r\n");
        sb.append(settings.toString());
        Object[] ch = choices.keySet().toArray();
        for (int i = 0; i < choices.keySet().size(); i++) {
            sb.append(ch[i] + " " + choices.get(ch[i])+"\r\n");
        }
        return sb.toString();
    }

    /**
     * Saves this Log to a given path.
     * @param path The path to save this log to.
     * @throws IOException May be thrown if various I/O-exceptions occur.
     */
    public void save(String path) throws IOException{
    }

    /**
     * Tests whether or not converting between a Log and its string representation is working correctly.
     * Will load a Log from 'test.log' (input) using the constructor new Log(String s). 
     * Will then convert this Log object to its string representation (output), and compare input and output.
     * If string conversion is wrong, an appropriate error message will be printed to the console.
     * @return Returns 'true' if this Log is working correctly, and false otherwise.
     */
    public static boolean testCG5files(){

        try{
            String sampleLog = new String(Files.readAllBytes(Paths.get("test.log"))),
                   copyLog = new String(Files.readAllBytes(Paths.get("copy.log")));

            String[] in  = sampleLog.replace("\r","").split("\n"),
                     out = copyLog.replace("\r","").split("\n");

            //Check that the two logs have the same number of lines
            if(in.length != out.length){
                System.out.println("Verification error: The output has "+out.length+" lines, but the input had "+in.length+" lines.\n\tExpected: "+in.length+"\n\tReceived: "+out.length);
                return false;
            }

            //Check that the seed is identical
            if(!in[0].equals(out[0])){
                System.out.println("Verification error: The seed value for the output is "+out[0]+", but the input had a seed value of "+in[0]+".\n\tExpected: "+in[0]+"\n\tReceived: "+out[0]);
                return false;
            }

            //Check that the settings lines (seed + settings) are identical
            for(int i=1; i<=5; i++){
                if(!in[i].equals(out[i])){
                    System.out.println("Verification error: Settings are changed in the output. Line "+(i+1)+" in the output does not match line "+(i+1)+" in the input.\n\tExpected: "+in[i]+"\n\tReceived: "+out[i]);
                    return false;
                }
            }

            //Sort the remaining n-7 lines and compare them piecewise
            String[] inChoices  = Arrays.copyOfRange(in, 7, in.length),
                     outChoices = Arrays.copyOfRange(out, 7, out.length);
            Arrays.sort(inChoices);
            Arrays.sort(outChoices);
            for(int i=0; i<inChoices.length; i++){
                if(!inChoices[i].equals(outChoices[i])){
                    System.out.println("Verification error: The output is missing the choice "+inChoices[i]+" at line "+(i+7)+" (choice no. "+(i+1)+").\n\tExpected: "+inChoices[i]+"\n\tReceived: "+outChoices[i]);
                    return false;
                }
            }

        } catch(IOException e){
            System.out.println("Verification error: Unable to load sample.log:\n"+e.getMessage());
            return false;
        }

        System.out.println("Verification success!");
        return true;
    }

    /**
     * Tests whether or not converting between a Log and its string representation is working correctly.
     * Will load a Log from 'test.log' (input) using the constructor new Log(String s). 
     * Will then convert this Log object to its string representation (output), and compare input and output.
     * If string conversion is wrong, an appropriate error message will be printed to the console.
     * @return Returns 'true' if this Log is working correctly, and false otherwise.
     */
    public static boolean testCG5strings(){

        try{
            String sampleLog = new String(Files.readAllBytes(Paths.get("test.log")), "windows-1252");
            Log log = new Log(sampleLog);

            String[] in  = sampleLog.replace("\r","").split("\n"),
                     out = log.toString().replace("\r","").split("\n");

            //Check that the two logs have the same number of lines
            if(in.length != out.length){
                System.out.println("Verification error: The output has "+out.length+" lines, but the input had "+in.length+" lines.\n\tExpected: "+in.length+"\n\tReceived: "+out.length);
                return false;
            }

            //Check that the seed is identical
            if(!in[0].equals(out[0])){
                System.out.println("Verification error: The seed value for the output is '"+out[0]+"', but the input had a seed value of '"+in[0]+"'.\n\tExpected: "+in[0]+"\n\tReceived: "+out[0]);
                return false;
            }

            //Check that the settings lines (seed + settings) are identical
            for(int i=1; i<=5; i++){
                if(!in[i].equals(out[i])){
                    System.out.println("Verification error: Settings are changed in the output. Line "+(i+1)+" in the output does not match line "+(i+1)+" in the input.\n\tExpected: "+in[i]+"\n\tReceived: "+out[i]);
                    return false;
                }
            }

            //Sort the remaining n-7 lines and compare them piecewise
            String[] inChoices  = Arrays.copyOfRange(in, 7, in.length),
                     outChoices = Arrays.copyOfRange(out, 7, out.length);
            Arrays.sort(inChoices);
            Arrays.sort(outChoices);
            for(int i=0; i<inChoices.length; i++){
                if(!inChoices[i].equals(outChoices[i])){
                    System.out.println("Verification error: The output is missing the choice "+inChoices[i]+" at line "+(i+7)+" (choice no. "+(i+1)+").\n\tExpected: "+inChoices[i]+"\n\tReceived: "+outChoices[i]);
                    return false;
                }
            }

        } catch(IOException e){
            System.out.println("Verification error: Unable to load test.log:\n"+e);
            return false;
        } catch(LogException e){
            System.out.println("Verification error: "+e.getMessage());
            return false;
        } catch(SettingsException e){
            System.out.println("Verification error: The settings are corrupted:\n"+e.getMessage());
            return false;
        }

        System.out.println("Verification success!");
        return true;
    }
}

class LogException extends Exception {

    private static final long serialVersionUID = 3544439369388631324L;

    public LogException(String s){
        super(s);
    }
}
