import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A Game object is an instance of NordicTraveller.
 * @author Nikolaj Ignatieff Schwartzbach
 * @version 1.0.0
 *
 */
public class Game {

	/** Set of all countries in the game */
	private List<Country> countries;
	
	/** List of all players */
	private List<Player>  players;
	
	/** Reference to the GUI Player */
	private Player guiPlayer;
	
	/** The random generator */
	private Random random;
	
	/** A Log object */
	private Log log;
	
	/** Whether or not to log this game */
	private boolean logging;
	
	/** Variables for time */
	private int totalTimeLeft = 50, timeLeft = getTotalTimeLeft();

	/** The seed of this Game instance (used for Random) */
	private int seed;

	/** Positions of the GUI for the various cities (in pixels) */
    private Map<City, Point> guiPosition;
    
    /** The settings for this Game */
    private Settings settings;
    
    /** Whether or not this Game is forcefully aborted */
    private boolean aborted=false;
    
    /**
     * Instantiates a new Game object with a random seed.
     */
    public Game(){
    	this((int)(Math.random()*Integer.MAX_VALUE));
    }
    
    /**
     * Instantiates a new Game object with a given seed.
     * @param seed
     */
	public Game(int seed){
		//Create random
		this.seed = seed;
		random = new Random(seed);
		
		//Instantiate collections
		countries = new ArrayList<Country>();
		players   = new ArrayList<Player>();
		guiPosition = new HashMap<City, Point>();
		
		//Try to load Settings from file, otherwise default to normal settings
		try{
			settings = new Settings(new String(Files.readAllBytes(Paths.get("settings.dat"))));
		} catch(IOException e){
			settings = new Settings();
		} catch(SettingsException e){
			settings = new Settings();
		}

		//Logging
		log = new Log(seed, settings);
		logging = false;
	}
	
	public void abort(){
		aborted = true;
	}
	
	/**
	 * Determines whether or not this game is still ongoing.
	 * @return True if this game is ongoing, false otherwise.
	 */
	public boolean ongoing(){
		return !aborted && timeLeft!=0;
	}
	
	/**
	 * Get the Log object.
	 * @return A reference to the current Log object.
	 */
	public Log getLog(){
		return log;
	}
	
	/**
	 * Plays a given Log object within this Game window.
	 * Will remove the normal GUI-controlled Player and replace with an instance of LogPlayer.
	 * Also resets the game to the seed specified in the log.
	 * @param log The Log to play
	 */
	public void playLog(Log log){
		LogPlayer lp = new LogPlayer(log, guiPlayer.getPosition());
		List<Player> newPlayers = new ArrayList<Player>();
		for(Player p : players){
			if(p instanceof RandomPlayer || p instanceof GreedyPlayer || p instanceof SmartPlayer)
				newPlayers.add(p);
		}
		newPlayers.add(lp);
		guiPlayer = lp;
		players = newPlayers;
		Collections.sort(players);
		this.seed = log.getSeed();
		this.settings = log.getSettings();
		reset(true, false);
		logging = true;
	}
	
	/**
	 * Removes the current LogPlayer instance, and replaces it with a regular Player object.
	 * Also resets this Game instance.
	 */
	private void removeLogPlayer(){
		List<Player> newPlayers = new ArrayList<Player>();
		boolean seenGUIPlayer = false;
		for(Player p : players){
			if(!(p instanceof LogPlayer))
				newPlayers.add(p);
			if(p.getClass() == Player.class)
				seenGUIPlayer = true;
		}
		if(!seenGUIPlayer){
			guiPlayer = new Player(guiPlayer.getPosition());
			newPlayers.add(guiPlayer);
		}
		players = newPlayers;
		Collections.sort(players);
	}
	
	/**
	 * Get the global Random object.
	 * @return A reference to the current Random generator.
	 */
	public Random getRandom(){
		return random;
	}
	
	/**
	 * Get the Settings object.
	 * @return A reference to the current Settings object.
	 */
	public Settings getSettings(){
		return settings;
	}
	
	/**
	 * Determines how badly a Player gets robbed.
	 * Returns a random integer in the interval [minRobbery, maxRobbery] using the current Settings object.
	 * Uses the global Random generator.
	 * @return An integer representing how many euroes the given player lost.
	 */
	public int getLoss(){
		return settings.getMinRobbery() + random.nextInt(settings.getMaxRobbery() - settings.getMinRobbery() + 1);
	}
	
	/**
	 * Associates a given City object with a position on the GUI.
	 * @param c The city.
	 * @param p The position (in pixels).
	 */
	public void putPosition(City c, Point p){
		guiPosition.put(c, p);
	}
	
	/**
	 * Get the GUI position of a given City.
	 * @param c The city.
	 * @return The GUI position (in pixels) of the city 'c'.
	 */
	public Point getPosition(City c){
		return guiPosition.get(c);
	}
	
	/**
	 * Returns a random position within this Game instance.
	 * First chooses a random country, and then a random city within that country.
	 * @return A randomly chosen Position.
	 */
	public Position getRandomStartingPosition(){
		Country c = getRandom(countries);
		return c.position(getRandom(c.getCities()));
	}
	
	/**
	 * Resets this given Game instance. This consists of the following actions:
	 *  * Reset the Log.
	 *  * Reset the Random object with some seed.
	 *  * Possibly removes the LogPlayer.
	 *  * Resets all countries.
	 *  * Assigns randomly positions to all players.
	 * @param repeat Whether or not to repeat the current seed. If not, a new seed is randomly chosen.
	 * @param removeLog Whether or not to remove the LogPlayer (if it exists).
	 */
	public void reset(boolean repeat, boolean removeLog){
		if(!repeat){
			seed = random.nextInt(Integer.MAX_VALUE);
		}
		log = new Log(seed, settings);
		random = new Random(seed);
		timeLeft = totalTimeLeft;
		aborted=false;
		
		if(removeLog){
			removeLogPlayer();
			logging = false;
		}
		
		for(Country c : countries)
			c.reset();
		Collections.sort(players);
		for(Player p : players){
			p.reset();
			p.setPosition(getRandomStartingPosition());
		}
	}
	
	/**
	 * Returns a random element in a given Set.
	 * @param set The set.
	 * @return A randomly chosen element from the given set.
	 */
	private <T> T getRandom(Collection<T> set){
		int r = random.nextInt(set.size());
		int i=0;
		for(T t : set)
			if(i++==r)
				return t;
		return null;
	}
	
	/**
	 * Associates the GUI player with some specific Player instance. 
	 * @param p The Player object representing the GUI Player.
	 */
	public void setGUIPlayer(Player p){
		this.guiPlayer = p;
		players.add(p);
	}
	
	/**
	 * Get a list of all countries within this Game instance.
	 * @return A list of all countries.
	 */
	public List<Country> getCountries(){
		return countries;
	}
	
	/**
	 * Adds a given Country to this Game instance.
	 * @param c The country to add.
	 */
	public void addCountry(Country c){
		countries.add(c);
		c.setGame(this);
		Collections.sort(countries, Comparator.comparing(k -> k.getName()));
	}
	
	/**
	 * Gets the GUI Player (the one controlled by the GUI).
	 * @return A reference to the GUI Player.
	 */
	public Player getGUIPlayer(){
		return guiPlayer;
	}
	
	/**
	 * Gets a list of all players associated with this Game instance.
	 * @return A list of all players.
	 */
	public List<Player> getPlayers(){
		return players;
	}
	
	/**
	 * Gets a specific City object, based on its name (findOne).
	 * Will traverse all countries, and if a city with the name 'search' is found, it is returned.
	 * If no such city exists, null is returned.
	 * @param search The name of the city to search for (case sensitive).
	 * @return A City object with the name 'search', or null.
	 */
	public City getCity(String search){
		City city = null;
		for(Country c : countries){
			city = c.getCity(search);
			if(city != null)
				return city;
		}
		return null;
	}
	
	/**
	 * Advances this Game instance one step.
	 * A step consists of moving all players once on the road they're currently travelling, as well as updating money.
	 * If this step was the last step (that is, getStepsLeft()==1 before invoking this method), the Log representing the game is saved to 'last.log'.
	 */
	public void step(){
		if(timeLeft==0 || aborted)
			return;
		Collections.sort(players);
		for(Player p : players){
			if(p.getClass()==RandomPlayer.class && !settings.isActive(0))
				continue;
			if(p.getClass()==GreedyPlayer.class && !settings.isActive(1))
				continue;
			if(p.getClass()==SmartPlayer.class && !settings.isActive(2))
				continue;
			p.step();
			if(p.getMoney()<0)p.reset();
		}
		if(--timeLeft == 0)
			try {
				log.save("last.log");
			} catch (IOException e) {
				System.out.println("Unable to save log");
			}
	}
	
	/**
	 * Gets the number of steps remaining in this Game instance.
	 * @return An integer representing how many steps this Game object can take before reaching the end.
	 */
	public int getStepsLeft(){
		return timeLeft;
	}
	
	/**
	 * This method is called whenever a City is clicked.
	 * Is used mainly by the GUI instance to invoke player commands.
	 * It is also used by the LogPlayer to simulate mouse clicks.
	 * @param c The city to click.
	 */
	public void clickCity(City c){
		log.add(timeLeft-(logging?1:0), c);
		
		guiPlayer.travelTo(c);
	}
	
	/**
	 * Adds roads between 'a' and 'b' (if they exist) with a given length.
	 * Adds a road from a->b, as well as a road from b->a (it is a symmetrical operator).
	 * @param a A string representing the first city.
	 * @param b A string representing the second city.
	 * @param len The length of the road to construct.
	 */
	public void addRoads(String a, String b, int len) {
		City A = null, B = null;
		for(Country country : countries){
			for(City city : country.getCities()){
				if(city.getName().equals(a))
					A = city;
				if(city.getName().equals(b))
					B = city;
			}
		}
		if(A==null)
			throw new RuntimeException("No such city: '"+a+"'.");
		if(B==null)
			throw new RuntimeException("No such city: '"+b+"'.");
		addRoads(A,B,len);
	}

	/**
	 * Adds roads between 'a' and 'b' (if they exist) with a given length.
	 * Adds a road from a->b, as well as a road from b->a (it is a symmetrical operator).
	 * @param a The first City object.
	 * @param b The second City object.
	 * @param i The length of the road to construct.
	 */
	public void addRoads(City a, City b, int i) {
		for(Country country : countries){
			country.addRoads(a, b, i);
			if(country.getNetwork().containsKey(a)){
				Collections.sort(country.getNetwork().get(a), Comparator.comparing(r -> r.getFrom().getName()));
				Collections.sort(country.getNetwork().get(a), Comparator.comparing(r -> r.getTo().getName()));
			}
			if(country.getNetwork().containsKey(b)){
				Collections.sort(country.getNetwork().get(b), Comparator.comparing(r -> r.getFrom().getName()));
				Collections.sort(country.getNetwork().get(b), Comparator.comparing(r -> r.getTo().getName()));
			}
		}
	}

	/**
	 * Gets how much time this game had at its conception.
	 * @return An integer representing how many steps this Game had available when it was created.
	 */
	public int getTotalTimeLeft() {
		return totalTimeLeft;
	}

	/**
	 * Changes the total time left of this Game instance.
	 * @param totalTimeLeft The new total time left.
	 */
	public void setTotalTimeLeft(int totalTimeLeft) {
		this.totalTimeLeft = totalTimeLeft;
	}
	
	/**
	 * Tests whether or not this computer game (probably) works.
	 * Runs a game with a predetermined seed, and checks if all values match with the expected values.
	 * Will print errors to System.out.
	 * @return Returns true if the implementation passes the test, and false otherwise.
	 */
	public static boolean testCG3(){
		
		if(!Files.exists(Paths.get("network.dat"))){
			System.out.println("Cannot perform test without 'network.dat'. Aborting...");
			return false;
		}
		
		Game g = Generator.generateGame(1, "network.dat");
		
		for(int i=0; i<50; i++)
			g.step();
		
		for(Player p : g.getPlayers()){
			int expectedMoney = -1,
				expectedRemaining = -1;
			String expectedFrom = "",
				   expectedTo = "";
			
			if(p.getClass() == Player.class){
				expectedMoney = 0;
				expectedRemaining = 0;
				expectedFrom = "Malmö";
				expectedTo = "Malmö";
			}
			if(p.getClass() == RandomPlayer.class){
				expectedMoney = 159;
				expectedRemaining = 2;
				expectedFrom = "Aarhus";
				expectedTo = "Odense";
			}
			if(p.getClass() == GreedyPlayer.class){
				expectedMoney = 358;
				expectedRemaining = 7;
				expectedFrom = "Helsinki";
				expectedTo = "Stockholm";
			}
			if(p.getClass() == SmartPlayer.class){
				expectedMoney = 174;
				expectedRemaining = 0;
				expectedFrom = "Nykøbing";
				expectedTo = "Nykøbing";
			}
			
			if(p.getMoney()!=expectedMoney){
				System.out.println("Test failed: incorrect amount of money for "+p.getName()+"\n\tExpected: "+expectedMoney+"€\n\tReceived: "+p.getMoney()+"€");
				return false;
			}
			if(p.getPosition().getDistance()!=expectedRemaining){
				System.out.println("Test failed: incorrect remaining distance for "+p.getName()+"\n\tExpected: "+expectedRemaining+"\n\tReceived: "+p.getPosition().getDistance());
				return false;
			}
			if(!p.getPosition().getFrom().getName().equals(expectedFrom)){
				System.out.println("Test failed: incorrect departing city (from) for "+p.getName()+"\n\tExpected: "+expectedFrom+"\n\tReceived: "+p.getPosition().getFrom().getName());
				return false;
			}
			if(!p.getPosition().getTo().getName().equals(expectedTo)){
				System.out.println("Test failed: incorrect arriving city (to) for "+p.getName()+"\n\tExpected: "+expectedTo+"\n\tReceived: "+p.getPosition().getTo().getName());
				return false;
			}
		}
		
		for(Country c : g.getCountries())
			for(City cc : c.getCities()){
				int expectedValue = cc.getValue();
				
				switch(cc.getName()){
					case "Tromsø":
						expectedValue = 180;
						break;
					case "Struer":
						expectedValue = 3;
						break;
					case "Kokkola":
						expectedValue = 85;
						break;
					case "Pohjanmaa":
						expectedValue = 100;
						break;
					case "Dombås":
						expectedValue = 110;
						break;
					case "Lillehammer":
						expectedValue = 110;
						break;
					case "Myrdal":
						expectedValue = 70;
						break;
					case "Narvik":
						expectedValue = 150;
						break;
					case "Åndalsnes":
						expectedValue = 150;
						break;
					case "Falun":
						expectedValue = 21;
						break;
					case "Gällivare":
						expectedValue = 100;
						break;
					case "Helsingborg":
						expectedValue = 100;
						break;
					case "Karlskrona":
						expectedValue = 18;
						break;
					case "Kiruna":
						expectedValue = 80;
						break;
					case "Kvillsfors":
						expectedValue = 100;
						break;
					case "Linköping":
						expectedValue = 41;
						break;
					case "Luleå":
						expectedValue = 95;
						break;
					case "Mora":
						expectedValue = 50;
						break;
					case "Strömstad":
						expectedValue = 120;
						break;
					case "Torsby":
						expectedValue = 40;
						break;
					case "Umeå":
						expectedValue = 29;
						break;
					case "Uppsala":
						expectedValue = 24;
						break;
					case "Örebro":
						expectedValue = 32;
						break;
					case "Nykøbing":
						expectedValue = 3;
						break;
					case "Herning":
						expectedValue = 0;
						break;
					case "Trondheim":
						expectedValue = 140;
						break;
					case "Oslo":
						expectedValue = 125;
						break;
					case "Bergen":
						expectedValue = 175;
						break;
					case "Kristiansand":
						expectedValue = 48;
						break;
					case "Stavanger":
						expectedValue = 200;
						break;
					case "Bodø":
						expectedValue = 40;
						break;
					case "Kirkenes":
						expectedValue = 200;
						break;
					case "Kuopio":
						expectedValue = 50;
						break;
					case "Tampere":
						expectedValue = 48;
						break;
					case "Kermi":
						expectedValue = 40;
						break;
					case "Pori":
						expectedValue = 30;
						break;
					case "Vaasa":
						expectedValue = 50;
						break;
					case "Oulu":
						expectedValue = 70;
						break;
					case "Turku":
						expectedValue = 70;
						break;
					case "Helsinki":
						expectedValue = 160;
						break;
					case "Rovaniemi":
						expectedValue = 30;
						break;
					case "Odense":
						expectedValue = 0;
						break;
					case "Esbjerg":
						expectedValue = 5;
						break;
					case "Copenhagen":
						expectedValue = 598;
						break;
					case "Aarhus":
						expectedValue = 0;
						break;
					case "Aalborg":
						expectedValue = 39;
						break;
					case "Gävle":
						expectedValue = 79;
						break;
					case "Göteborg":
						expectedValue = 90;
						break;
					case "Kalmar":
						expectedValue = 90;
						break;
					case "Malmö":
						expectedValue = 13;
						break;
					case "Stockholm":
						expectedValue = 319;
						break;
					case "Jönköping":
						expectedValue = 110;
						break;
					case "Sundsvall":
						expectedValue = 18;
						break;
					case "Norrköping":
						expectedValue = 80;
						break;
					case "Karlstad":
						expectedValue = 85;
						break;
					case "Visby":
						expectedValue = 90;
						break;
					case "Östersund":
						expectedValue = 50;
						break;
				}
				
				if(cc.getValue() != expectedValue){
					System.out.println("Test failed: invalid value for city "+cc.getName()+":\n\tExpected: "+expectedValue+"€\n\tReceived: "+cc.getValue()+"€");
					return false;
				}
			}
		System.out.println("Test successful!");
		return true;
	}
}
