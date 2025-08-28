 import java.util.*;

public class PIEINPUT {

    // Method to calculate using Inclusion-Exclusion

    public static int inclusionExclusion(Map<String, Integer> data) {

        // Collect names of individual sets (no intersections)

        List<String> sets = new ArrayList<>();
        for (String name : data.keySet()) {
            if (!name.contains("∩")) {  // if it is not an intersection
                sets.add(name);
            }
        }

        int total = 0; // final result

        System.out.println("\nSolving Using the Principle of Inclusion and Exclusion");
        System.out.println("We have these sets: " + String.join(", ", sets) + "\n");

        // STEP 1: Add all individual sets

        int step1 = 0;
        for (String s : sets) {
            step1 += data.get(s);
        }
        System.out.println("Step 1: Add sizes of individual sets = " + step1);
        total = total + step1;

        // STEP 2: Subtract all pairwise intersections

        int step2 = 0;
        for (String name : data.keySet()) {

            // check if it is an intersection of exactly 2 sets
            
            long count = name.chars().filter(ch -> ch == '∩').count();
            if (count == 1) {
                step2 += data.get(name);
            }
        }
        total = total - step2;
        System.out.println("Step 2: Subtract pairwise intersections = -" + step2 + " → " + total);

        // STEP 3: Add all triple intersections
        
        int step3 = 0;
        for (String name : data.keySet()) {
            long count = name.chars().filter(ch -> ch == '∩').count();
            if (count == 2) {  // intersection of 3 sets
                step3 += data.get(name);
            }
        }
        total = total + step3;
        System.out.println("Step 3: Add triple intersections = +" + step3 + " → " + total);

        // STEP 4: Subtract 4-way intersections (if they exist)

        int step4 = 0;
        for (String name : data.keySet()) {
            long count = name.chars().filter(ch -> ch == '∩').count();
            if (count == 3) {  // intersection of 4 sets
                step4 += data.get(name);
            }
        }
        if (step4 > 0) { // only if it exists
            total = total - step4;
            System.out.println("Step 4: Subtract 4-way intersections = -" + step4 + " → " + total);
        }

        System.out.println("Final Answer: Total elements in union = " + total);
        return total;
    }

    // Example 1: Library Example (predefined data)

    public static void libraryExample() {
        Map<String, Integer> data = new LinkedHashMap<>();

        // Filling the data
        data.put("Maths", 50);
        data.put("Science", 45);
        data.put("Literature", 30);
        data.put("Maths∩Science", 12);
        data.put("Maths∩Literature", 8);
        data.put("Science∩Literature", 6);
        data.put("Maths∩Science∩Literature", 4);

        System.out.println("\n################ Example 1: Library Problem ################");
        System.out.println("Students borrowing Maths, Science, and Literature books.");
        inclusionExclusion(data);
    }

    // Example 2: Movie Example (user input)
    public static void movieExample(Scanner sc) {
        Map<String, Integer> data = new LinkedHashMap<>();

        System.out.println("\n################ Example 2: Movie Preferences ################");
        System.out.println("Enter number of students who like each genre:");

        System.out.print("Action: ");
        data.put("Action", sc.nextInt());

        System.out.print("Comedy: ");
        data.put("Comedy", sc.nextInt());

        System.out.print("Drama: ");
        data.put("Drama", sc.nextInt());

        System.out.println("Now enter the intersections:");
        System.out.print("Action∩Comedy: ");
        data.put("Action∩Comedy", sc.nextInt());

        System.out.print("Action∩Drama: ");
        data.put("Action∩Drama", sc.nextInt());

        System.out.print("Comedy∩Drama: ");
        data.put("Comedy∩Drama", sc.nextInt());

        System.out.print("Action∩Comedy∩Drama: ");
        data.put("Action∩Comedy∩Drama", sc.nextInt());

        inclusionExclusion(data);
    }

    // Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // First run fixed example
        libraryExample();

        // Then run interactive example
        movieExample(sc);

        sc.close();
    }
}