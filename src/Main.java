
public class Main {

	public static void main(String[] args) {
		Population p = new Population(25, 0.05);
		int i = 0;
		while(p.mostFit.fitness() < p.target.length()*14) {
			p.day();
			System.out.println(p.mostFit.toString());
		}
	}
	

}
