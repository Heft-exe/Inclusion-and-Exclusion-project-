package src;

import java.util.*;
import java.util.logging.*;


/**
 * PIEv4 - Principle of Inclusion-Exclusion Calculator & Set Tool
 * A professional, SonarQube-compliant implementation.
 */
public final class piev4 {

    private static final Logger logger = Logger.getLogger(piev4.class.getName());

    // Prevent instantiation
    private piev4() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Configure logger once
    static {
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return record.getMessage() + System.lineSeparator();
            }
        });
        rootLogger.addHandler(handler);
        rootLogger.setLevel(Level.ALL);
    }

    // ----------- Generate all non-empty subsets as bitmasks -----------
    private static List<int[]> generateSubsets(int n) {
        List<int[]> subsets = new ArrayList<>();
        for (int mask = 1; mask < (1 << n); mask++) {
            int[] subset = new int[Integer.bitCount(mask)];
            int idx = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) subset[idx++] = i;
            }
            subsets.add(subset);
        }
        return subsets;
    }

    // ----------- Generate key like A∩B∩C from subset indices -----------
    private static String subsetKey(int[] subset, String[] setNames) {
        StringJoiner sj = new StringJoiner("∩");
        for (int idx : subset) sj.add(setNames[idx]);
        return sj.toString();
    }

    // ----------- Inclusion-Exclusion Formula -----------
    public static int inclusionExclusion(Map<String, Integer> data, String[] setNames) {
        int n = setNames.length;
        int total = 0;

        logger.info("Solving using Principle of Inclusion-Exclusion...");
        logger.info("Sets: " + String.join(", ", setNames));

        List<int[]> subsets = generateSubsets(n);

        for (int[] subset : subsets) {
            String key = subsetKey(subset, setNames);
            if (data.containsKey(key)) {
                int value = data.get(key);
                int sign = ((subset.length & 1) == 1) ? 1 : -1;
                total += sign * value;
                logger.info((sign == 1 ? "Add " : "Subtract ") + key + " = " + value + " -> " + total);
            }
        }

        if (total < 0) {
            logger.warning("⚠ Invalid result! Union size cannot be negative.");
        } else {
            logger.info("Final Answer: Total elements in union = " + total);
        }

        return total;
    }

    // ----------- Dynamic PIE with optional manual intersections -----------
    public static void runDynamicExample(Scanner sc) {
        logger.info("Enter number of sets: ");
        int n = sc.nextInt();
        sc.nextLine();

        String[] setNames = new String[n];
        for (int i = 0; i < n; i++) {
            logger.info("Enter name of set " + (i + 1) + ": ");
            setNames[i] = sc.nextLine();
        }

        Map<String, Integer> data = new LinkedHashMap<>();
        for (String set : setNames) {
            logger.info("Enter size of " + set + ": ");
            data.put(set, sc.nextInt());
        }

        logger.info("Do you want to enter intersections manually? (yes/no): ");
        String choice = sc.next();

        List<int[]> subsets = generateSubsets(n);

        for (int[] subset : subsets) {
            if (subset.length >= 2) {
                String key = subsetKey(subset, setNames);
                if (choice.equalsIgnoreCase("yes")) {
                    logger.info("Enter size of " + key + ": ");
                    data.put(key, sc.nextInt());
                } else {
                    int approx = Arrays.stream(subset)
                            .map(idx -> data.get(setNames[idx]))
                            .min().orElse(0) / 2;
                    logger.info("Approximating size of " + key + " as " + approx);
                    data.put(key, approx);
                }
            }
        }

        inclusionExclusion(data, setNames);
    }

    // ----------- PIE with Real Sets -----------
    public static void runSetBasedPIE(Scanner sc) {
        logger.info("Enter number of sets: ");
        int n = sc.nextInt();
        sc.nextLine();

        String[] setNames = new String[n];
        List<Set<Integer>> setsList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            logger.info("Enter name of set " + (i + 1) + ": ");
            setNames[i] = sc.nextLine();

            logger.info("Enter number of elements in " + setNames[i] + ": ");
            int m = sc.nextInt();
            Set<Integer> temp = new HashSet<>();
            logger.info("Enter elements:");
            for (int j = 0; j < m; j++) temp.add(sc.nextInt());
            setsList.add(temp);
            sc.nextLine();
        }

        Map<String, Integer> data = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) data.put(setNames[i], setsList.get(i).size());

        for (int[] subset : generateSubsets(n)) {
            if (subset.length >= 2) {
                Set<Integer> inter = new HashSet<>(setsList.get(subset[0]));
                for (int i = 1; i < subset.length; i++) inter.retainAll(setsList.get(subset[i]));
                data.put(subsetKey(subset, setNames), inter.size());
            }
        }

        inclusionExclusion(data, setNames);
    }

    // ----------- Predefined Examples -----------
    public static void libraryExample() {
        Map<String, Integer> data = Map.of(
                "Maths", 50, "Science", 45, "Literature", 30,
                "Maths∩Science", 12, "Maths∩Literature", 8,
                "Science∩Literature", 6, "Maths∩Science∩Literature", 4
        );
        inclusionExclusion(new LinkedHashMap<>(data),
                new String[]{"Maths", "Science", "Literature"});
    }

    public static void movieExample(Scanner sc) {
        String[] sets = {"Action", "Comedy", "Drama"};
        Map<String, Integer> data = new LinkedHashMap<>();
        for (String set : sets) {
            logger.info(set + ": ");
            data.put(set, sc.nextInt());
        }
        data.put("Action∩Comedy", sc.nextInt());
        data.put("Action∩Drama", sc.nextInt());
        data.put("Comedy∩Drama", sc.nextInt());
        data.put("Action∩Comedy∩Drama", sc.nextInt());

        inclusionExclusion(data, sets);
    }

    // ----------- Set Operations -----------
    public static void setOperations(Scanner sc) {
        logger.info("Enter number of sets: ");
        int n = sc.nextInt();
        sc.nextLine();

        String[] setNames = new String[n];
        List<Set<Integer>> setsList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            logger.info("Enter name of set " + (i + 1) + ": ");
            setNames[i] = sc.nextLine();
            logger.info("Enter number of elements in " + setNames[i] + ": ");
            int m = sc.nextInt();
            Set<Integer> temp = new HashSet<>();
            for (int j = 0; j < m; j++) temp.add(sc.nextInt());
            setsList.add(temp);
            sc.nextLine();
        }

        Set<Integer> universal = new HashSet<>();
        setsList.forEach(universal::addAll);

        while (true) {
            logger.info("Choose Set Operation:");
            logger.info("1. Union");
            logger.info("2. Intersection");
            logger.info("3. Difference");
            logger.info("4. Complement");
            logger.info("5. Exit");

            int op = sc.nextInt();

            switch (op) {
                case 1 -> {
                    Set<Integer> uni = new HashSet<>();
                    setsList.forEach(uni::addAll);
                    logger.info("Union = " + uni);
                }
                case 2 -> {
                    Set<Integer> inter = new HashSet<>(setsList.get(0));
                    for (int i = 1; i < n; i++) inter.retainAll(setsList.get(i));
                    logger.info("Intersection = " + inter);
                }
                case 3 -> {
                    logger.info("Choose first set (1-" + n + "): ");
                    int a = sc.nextInt() - 1;
                    logger.info("Choose second set (1-" + n + "): ");
                    int b = sc.nextInt() - 1;
                    Set<Integer> diff = new HashSet<>(setsList.get(a));
                    diff.removeAll(setsList.get(b));
                    logger.info("Difference = " + diff);
                }
                case 4 -> {
                    logger.info("Choose set index (1-" + n + "): ");
                    int a = sc.nextInt() - 1;
                    Set<Integer> comp = new HashSet<>(universal);
                    comp.removeAll(setsList.get(a));
                    logger.info("Complement = " + comp);
                }
                case 5 -> {
                    return;
                }
                default -> logger.warning("Invalid choice!");
            }
        }
    }

    // ----------- Main Menu -----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            logger.info("========== PIE Calculator & Set Tool ==========");
            logger.info("1. Dynamic PIE (numbers)");
            logger.info("2. PIE with Real Sets");
            logger.info("3. Library Example");
            logger.info("4. Movie Example");
            logger.info("5. Set Operations");
            logger.info("6. Exit");
            logger.info("Choice: ");

            switch (sc.nextInt()) {
                case 1 -> runDynamicExample(sc);
                case 2 -> runSetBasedPIE(sc);
                case 3 -> libraryExample();
                case 4 -> movieExample(sc);
                case 5 -> setOperations(sc);
                case 6 -> {
                    logger.info("Exiting...");
                    return;
                }
                default -> logger.warning("Invalid choice!");
            }
        }
    }
}
