import java.util.Arrays;

public class Example {
	
	public static void main(String[] args) {
	
		
		// Get the commandline parameters
		// Feel free to add error and sanity checks
		if(args.length != 2) {
			System.out.println("Start program with two arguments <n> and <seed>");
			return;
		}
		
		int n = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		
		// Get the array to sort
		int[] arr = Oblig4Precode.generateArray(n, seed);
		
		
		// Sort it (replace this with Radix)
		Arrays.sort(arr);
		
		
		// Save the result
		Oblig4Precode.saveResults(Oblig4Precode.Algorithm.SEQ, seed, arr);
		
	}
	
}

