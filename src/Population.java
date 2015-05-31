/**
 * TCSS 342 - Data Structures
 * Assignment 2 - String Evolution
 * Population Class
 */

import java.util.ArrayList;
import java.util.Random;


/**
 * The Population class.
 * Responsible for holding a collection of genomes and evolving them. 
 */
public class Population {
	
	/** The random object. */
	private static final Random R = new Random();
	
	/** The collection of genome objects. */
	public ArrayList<Genome> myGenes;
	
	/** The max size of this genome population. */
	public Integer myPopSize;
	
	/** The target string to mutate toward. */
	public String target;
	
	/** The most fit genome. */
	public Genome mostFit;
	
	
	/**
	 * Instantiates a new population.
	 *
	 * @param numAgents the num agents
	 * @param mutationRate the mutation rate
	 * O(n) - where n is the number of agents, genome constructor call can be considered a constant and ignored
	 */
	public Population(Integer numAgents, Double mutationRate) {
		myGenes = new ArrayList<>();
		myPopSize = numAgents;
		for (int i = 0; i < numAgents; i++) {
			myGenes.add(new Genome(mutationRate));
		}
		target = Genome.TARGET;
		mostFit = myGenes.get(myGenes.size() - 1);
	}
	
	/**
	 * Performs a day of evolution on the gene population.
	 * Sorts genes by fitness from lowest to highest, removes the bottom half, and repopulates the population.
	 * 
	 * O(n) - performs many O(n) operations so they may be treated as constants and ignored. 
	 */
	public void day() {
		
		sortByFitness();
		removeLowerHalf();
		
		while (myGenes.size() < myPopSize) {
			boolean chance = R.nextBoolean();
			int randIndex = R.nextInt(myGenes.size());
			int randIndex2 = R.nextInt(myGenes.size());
			
			if (chance) {
				Genome temp = myGenes.get(randIndex);
				Genome newGene = new Genome(temp);
				newGene.mutate();
				myGenes.add(newGene);
			} else {
				Genome crossTemp = myGenes.get(randIndex);
				Genome newCross = new Genome(crossTemp);
				newCross.crossover(myGenes.get(randIndex2));
				newCross.mutate();
				myGenes.add(newCross);
			}
			
		}
		
		sortByFitness();
		mostFit = myGenes.get(myGenes.size() - 1);

	}
	
	/**
	 * Bubble sort method pirated from in-class Sorter we made.
	 * Sorts by fitness, ascending. 
	 * O(n^2)
	 */
	private void sortByFitness() {
		for(int i = 0; i < myGenes.size() - 1; i++){
			for(int j = 0; j < myGenes.size() - 1 - i; j++){
				if(myGenes.get(j).getFitness() > myGenes.get(j+1).getFitness()) {
					swapGenes(j, j + 1);
				}
			}
		}
	}
	
	/**
	 * Swap genes, used by sortByFitness().
	 *
	 * @param index1 the first index
	 * @param index2 the second index
	 * O(n)
	 */
	private void swapGenes(int index1, int index2) {
		Genome temp = myGenes.get(index1);
		myGenes.set(index1, myGenes.get(index2));
		myGenes.set(index2, temp);
	}
	
	/**
	 * Removes the lower half of the population, contains the least fit genes.
	 * O(n)
	 */
	private void removeLowerHalf() {
		int halfList = myPopSize / 2;
		for (int i = 0; i <= halfList; i++) {
			myGenes.remove(0);
		}
	}
	
	/**
	 * Formats the collection of genomes into an easy to read format
	 * 
	 * O(n^2) - I could also see it being O(n) because after a certain point,
	 * all the genomes will be the same length and could be treated as a constant
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Genome g : myGenes) {
			sb.append(g + "\n");
		}
		return sb.toString();
	}
	
}
