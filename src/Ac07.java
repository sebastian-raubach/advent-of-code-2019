import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ac07 {
    public static void main(String[] args)
            throws IOException {
        solvePartOne();
        solvePartTwo();
    }

    private static void solvePartOne() throws IOException {
        Path input = new File("res/input/07.txt").toPath();
        int[] inputValues = Arrays.stream(new String(Files.readAllBytes(input)).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        int[] values = new int[]{0, 1, 2, 3, 4};
        List<int[]> permutations = new ArrayList<>();
        permute(permutations, values, 0);

        int max = -Integer.MAX_VALUE;

        for (int[] order : permutations) {
            int intermediate = new IntCode(inputValues).computeOutput(order[0], 0);
            intermediate = new IntCode(inputValues).computeOutput(order[1], intermediate);
            intermediate = new IntCode(inputValues).computeOutput(order[2], intermediate);
            intermediate = new IntCode(inputValues).computeOutput(order[3], intermediate);
            intermediate = new IntCode(inputValues).computeOutput(order[4], intermediate);

            max = Math.max(max, intermediate);
        }

        System.out.println(max);
    }

    private static void solvePartTwo() throws IOException {
        Path input = new File("res/input/07.txt").toPath();
        int[] inputValues = Arrays.stream(new String(Files.readAllBytes(input)).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        int[] values = new int[]{5, 6, 7, 8, 9};
        List<int[]> result = new ArrayList<>();
        permute(result, values, 0);

        int max = -Integer.MAX_VALUE;

        for (int[] order : result) {
            IntCode one = new IntCode(inputValues);
            IntCode two = new IntCode(inputValues);
            IntCode three = new IntCode(inputValues);
            IntCode four = new IntCode(inputValues);
            IntCode five = new IntCode(inputValues);

            // Do an initial round with the phase settings
            int intermediate = one.computeOutput(order[0], 0);
            intermediate = two.computeOutput(order[1], intermediate);
            intermediate = three.computeOutput(order[2], intermediate);
            intermediate = four.computeOutput(order[3], intermediate);
            intermediate = five.computeOutput(order[4], intermediate);
            max = Math.max(max, intermediate);

            // Then iterate as long as it doesn't terminate
            while (five.canContinue()) {
                intermediate = one.computeOutput(intermediate);
                intermediate = two.computeOutput(intermediate);
                intermediate = three.computeOutput(intermediate);
                intermediate = four.computeOutput(intermediate);
                intermediate = five.computeOutput(intermediate);
                max = Math.max(max, intermediate);
            }
        }

        System.out.println(max);
    }

    private static void permute(List<int[]> result, int[] arr, int k) {
        for (int i = k; i < arr.length; i++) {
            swap(arr, i, k);
            permute(result, arr, k + 1);
            swap(arr, k, i);
        }
        if (k == arr.length - 1) {
            result.add(Arrays.copyOf(arr, arr.length));
        }
    }

    private static int[] swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        return a;
    }
}
