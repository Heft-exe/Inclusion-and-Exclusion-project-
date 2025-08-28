import java.util.*;

public class pied2 {

    // Function to generate all subsets of given set indexes
    public static void generateSubsets(int n, List<List<Integer>> subsets) {
        int total = 1 << n; // 2^n subsets
        for (int mask = 1; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(i);
                }
            }
            subsets.add(subset);
        }
    }

    // PIE Calculation
    public static int inclusionExclusion(Map<String, Integer> data, String[] setNames) {
        int n = setNames.length;
        int total = 0;

        System.out.println("\nSolving using Principle of Inclusion-Exclusion...");
        System.out.println("Sets: " + String.join(", ", setNames) + "\n");

        // Generate all non-empty subsets
        List<List<Integer>> subsets = new ArrayList<>();
        generateSubsets(n, subsets);

        // Apply PIE
        for (List<Integer> subset : subsets) {
            // Build intersection key name
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < subset.size(); i++) {
                if (i > 0) key.append("∩");
                key.append(setNames[subset.get(i)]);
            }

            if (data.containsKey(key.toString())) {
                int value = data.get(key.toString());
                int sign = (subset.size() % 2 == 1) ? +1 : -1;
                total += sign * value;

                // Print step
                if (sign == 1) {
                    System.out.println("Add " + key + " = +" + value + " -> " + total);
                } else {
                                System.out.println("Subtract " + key + " = -" + value + " -> " + total);
                }
}
        }

        System.out.println("\nFinal Answer: Total elements in union = " + total);
        return total;
    }

    // Dynamic Example (interactive with manual/auto intersections)
    public static void runDynamicExample(Scanner sc) {
        System.out.print("Enter number of sets: ");
        int n = sc.nextInt();
        sc.nextLine();

        String[] setNames = new String[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter name of set " + (i + 1) + ": ");
            setNames[i] = sc.nextLine();
        }

        Map<String, Integer> data = new LinkedHashMap<>();

        // Enter individual set sizes
        for (String set : setNames) {
            System.out.print("Enter size of " + set + ": ");
            data.put(set, sc.nextInt());
        }

        System.out.print("\nDo you want to enter intersections manually? (yes/no): ");
        String choice = sc.next();

        List<List<Integer>> subsets = new ArrayList<>();
        generateSubsets(n, subsets);

        if (choice.equalsIgnoreCase("yes")) {
            System.out.println("\nNow enter intersections (if not applicable, enter 0):");
            for (List<Integer> subset : subsets) {
                if (subset.size() >= 2) {
                    StringBuilder key = new StringBuilder();
                    for (int i = 0; i < subset.size(); i++) {
                        if (i > 0) key.append("∩");
                        key.append(setNames[subset.get(i)]);
                    }
                    System.out.print("Enter size of " + key + ": ");
                    data.put(key.toString(), sc.nextInt());
                }
            }
        } else {
            System.out.println("\nProgram will auto-calculate approximate intersections:");
            for (List<Integer> subset : subsets) {
                if (subset.size() >= 2) {
                    StringBuilder key = new StringBuilder();
                    for (int i = 0; i < subset.size(); i++) {
                        if (i > 0) key.append("∩");
                        key.append(setNames[subset.get(i)]);
                    }

                    int approx = Integer.MAX_VALUE;
                    for (int idx : subset) {
                        approx = Math.min(approx, data.get(setNames[idx]));
                    }
                    approx = approx / 2;

                    System.out.println("Approximating size of " + key + " as " + approx);
                    data.put(key.toString(), approx);
                }
            }
        }

        inclusionExclusion(data, setNames);
    }

    // Predefined Example 1: Library
    public static void libraryExample() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String[] sets = {"Maths", "Science", "Literature"};

        data.put("Maths", 50);
        data.put("Science", 45);
        data.put("Literature", 30);
        data.put("Maths∩Science", 12);
        data.put("Maths∩Literature", 8);
        data.put("Science∩Literature", 6);
        data.put("Maths∩Science∩Literature", 4);

        inclusionExclusion(data, sets);
    }

    // Predefined Example 2: Movie
    public static void movieExample(Scanner sc) {
        Map<String, Integer> data = new LinkedHashMap<>();
        String[] sets = {"Action", "Comedy", "Drama"};

        System.out.print("Action: ");
        data.put("Action", sc.nextInt());
        System.out.print("Comedy: ");
        data.put("Comedy", sc.nextInt());
        System.out.print("Drama: ");
        data.put("Drama", sc.nextInt());

        System.out.print("Action∩Comedy: ");
        data.put("Action∩Comedy", sc.nextInt());
        System.out.print("Action∩Drama: ");
        data.put("Action∩Drama", sc.nextInt());
        System.out.print("Comedy∩Drama: ");
        data.put("Comedy∩Drama", sc.nextInt());
        System.out.print("Action∩Comedy∩Drama: ");
        data.put("Action∩Comedy∩Drama", sc.nextInt());

        inclusionExclusion(data, sets);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== PIE Calculator ==========");
            System.out.println("1. Run Dynamic Example");
            System.out.println("2. Library Example (predefined)");
            System.out.println("3. Movie Example (predefined)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> runDynamicExample(sc);
                case 2 -> libraryExample();
                case 3 -> movieExample(sc);
                case 4 -> {
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}