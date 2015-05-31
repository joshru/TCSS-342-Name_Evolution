/**
 * TCSS 342 - Data Structures
 * Assignment 2 - String Evolution
 * Genome Class
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * The Genome Class.
 * Responsible for holding a target string and a collection of chars.
 * Genome has the ability to mutate towards the target string and gauge it's fitness along the way
 */
public class Genome {

	/** The array of possible characters. */
	public static final char[] POOL = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' ','-','’'};
	
	/** The target genome string. */
	public static final String TARGET = "JOSHUA DAVID RUESCHENBERG";
	
	/** The random object used throughout program. */
	private static final Random R = new Random();
	
	/** The list of possible characters. */
	public ArrayList<Character> charPool;
	
	/** The list of characters to represent genome. */
	public ArrayList<Character> myChars; 
	
	/** The mutation rate. */
	public double mutationRate;
	
	/** My fitness. */
	public Integer myFitness;
	
	
	/**
	 * Instantiates a new genome with set mutation rate.
	 *
	 * @param theRate the mutation rate.
	 * O(n) - because the populate pool method has runtime O(n) and the fitness method also has runtime O(n).
	 */
	public Genome(double theRate) {
		populatePool();
		myChars = new ArrayList<Character>();
		myChars.add('A');
		mutationRate = theRate;
		myFitness = fitness();
	}
	
	/**
	 * Copy constructor that accepts a genome.
	 *
	 * @param theGene the gene to be copied.
	 * O(n) - because the populate pool method has runtime O(n).
	 */
	public Genome(Genome theGene) {
		populatePool();
		myChars = theGene.getValue();
		mutationRate = theGene.getMuteRate();
		myFitness = theGene.getFitness();
	}
	
	/**
	 * Populates the array list with available mutation characters. 
	 * O(n) - iterates through my array of characters and creates an array list from it.
	 */
	private void populatePool() {
		charPool = new ArrayList<>();
		for (char c : POOL) {
			charPool.add(c);
		}
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value.
	 * O(1)
	 */
	public ArrayList<Character> getValue() {
		return myChars;
	}
	
	/**
	 * Gets the index.
	 *
	 * @param theIndex the index.
	 * @return the character at parameter index.
	 * O(n)
	 */
	public char get(int theIndex) {
		return myChars.get(theIndex);
	}
	
	/**
	 * Gets the mutation rate.
	 *
	 * @return the mutation rate.
	 * O(1)
	 */
	public double getMuteRate() {
		return mutationRate;
	}
	
	/**
	 * Gets the fitness.
	 *
	 * @return the fitness.
	 * O(1)
	 */
	public Integer getFitness() {
		return myFitness;
	}
	
	/**
	 * Mutates the genome based on the mutation rate. 
	 * Has a mutation rate based chance to mutate the gene and all its characters.
	 * Also has a chance to either remove a random index or add a random char to the end based on the rate. 
	 * O(n) - the method itself has runtime O(n) but the fitness call at the end makes it O(2n) = O(n).
	 */
	public void mutate() {
		double randChance = R.nextDouble();;	
		
		if (randChance <= mutationRate) {

			for (int i = 0; i < myChars.size(); i++) {
				
				randChance = R.nextDouble();
				if (randChance <= mutationRate) {
					char mutated = mutateChar(myChars.get(i));
					myChars.set(i, mutated);
				}
			}

		}
		
		randChance = R.nextDouble();
		if (myChars.size() > 1 && randChance <= mutationRate) {
			int randIndex = R.nextInt(myChars.size());
			myChars.remove(randIndex);
			
		}
		
		randChance = R.nextDouble();
		if(randChance <= mutationRate) {
			myChars.add(pickRandomChar());
		}
		
		myFitness = fitness();
	}
	
	/**
	 * Crosses over this gene with another by randomly selecting characters from each.
	 * Changes this gene to the value of the crossover gene.
	 *
	 * @param theOther the other genome to cross with.
	 * O(n) - iterates through two lists touching on one character per index between the two. 
	 */
	public void crossover(Genome theOther) {
		ArrayList<Character> newGene = new ArrayList<>();
		ArrayList<Character> otherList = theOther.getValue();
		Iterator<Character> itr1 = myChars.iterator();
		Iterator<Character> itr2 = otherList.iterator();
		
		while (itr1.hasNext() || itr2.hasNext()) {
			boolean pickGene = R.nextBoolean();
			if (pickGene) {
				if (itr1.hasNext()) {
					newGene.add(itr1.next());
				} else {
					break;
				}
				if (itr2.hasNext()) itr2.next();
				
			} else if (!pickGene) {
				if (itr2.hasNext()) {
					newGene.add(itr2.next());
				} else {
					break;
				}
				if (itr1.hasNext()) itr1.next();
			}
		}
		
		myChars = newGene;
		myFitness = fitness();
	}
	
	/**
	 * Calculates the fitness of this gene when compared to the target.
	 *
	 * @return the fitness score of this gene.
	 * O(n) - makes one full iteration through the characters.
	 */
	public Integer fitness() {
		Integer result = 0;
		int gLength = myChars.size();
		int tLength = TARGET.length();
		int tests;
		
		result = result - Math.abs(tLength - gLength);
		
		if (gLength < tLength) tests = gLength;
		else tests = tLength;
		
		for (int i = 0; i < tests; i++) {
			Integer temp = getSteps(myChars.get(i), TARGET.charAt(i));
			result += temp;
		}
		myFitness = result;
		return result;
	}
	
	/**
	 * Gets the number steps between two parameter characters.
	 *
	 * @param theFirst the first character, from the genome.
	 * @param theSecond the second character, from the target.
	 * @return the steps between the two in the character pool.
	 * O(n) - does some simple operations based on 2 indexOf calls on 2 characters.
	 */
	private Integer getSteps(char theFirst, char theSecond) {
		int temp;
		int steps = 0;
		int firstIndex = charPool.indexOf(theFirst);
		int secondIndex = charPool.indexOf(theSecond);
		
		if (firstIndex == secondIndex) {
			steps += 14;
			
		} else if (firstIndex < secondIndex) {
			if(firstIndex == 0 && secondIndex == 28) temp = 1;
			else temp = secondIndex - firstIndex;
			steps += (14 - temp);
			
		} else if (firstIndex > secondIndex) {
			if (firstIndex == 28 && secondIndex == 0) temp = 1;
			else temp = firstIndex - secondIndex;
			steps += 14 - temp;
			
		}
		
		return (Integer) steps;
	}
	
	/**
	 * Picks random char.
	 *
	 * @return the random char.
	 * O(n)
	 */
	private char pickRandomChar() {
		int rand = R.nextInt(charPool.size());
	
		return charPool.get(rand);
	}
	
	/**
	 * Mutates a character up or down by one value.
	 *
	 * @param theChar the character.
	 * @return the new character after mutation.
	 * O(n) - does some simple operations with an iterator on a single character.
	 */
	private char mutateChar(char theChar) {
		boolean upDownRand = R.nextBoolean();
		
		int replaceIndex;
		
		if (upDownRand) {
			
			int charIndex = charPool.indexOf(theChar);
			
			if (charIndex == charPool.size() - 1) {
				replaceIndex = 0;
			} else {
				replaceIndex = charIndex + 1;
			}
		} else {
			int charIndex = charPool.indexOf(theChar);
			if (charIndex == 0) {
				replaceIndex = charPool.size() - 1;
			} else {
				replaceIndex = charIndex - 1;
			}
		}

		return charPool.get(replaceIndex);
	}
	
	/**
	 * Outputs the gene and its fitness
	 * O(n) - has to iterate through all chars and add them to the string builder.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (char c : myChars) {
			sb.append(c);
		}
		sb.append(" Fitness: " + myFitness);
		return sb.toString();
	}
	
}
