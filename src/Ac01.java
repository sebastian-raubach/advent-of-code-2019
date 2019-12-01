import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Ac01 {
    public static void main(String[] args) throws IOException {
        Path input = new File("res/01.txt").toPath();

        List<Integer> values = Files.readAllLines(input)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        long sum = values.stream()
                .mapToInt(Ac01::getFuelRequirement)
                .sum();
        System.out.println(sum);

        sum = values.stream()
                .mapToInt(Ac01::getFuelRequirementRecursive)
                .sum();
        System.out.println(sum);
    }

    private static int getFuelRequirement(int input) {
        return input / 3 - 2;
    }

    private static int getFuelRequirementRecursive(int input) {
        int value = getFuelRequirement(input);

        if (value > 0) {
            value += getFuelRequirementRecursive(value);
            return value;
        } else {
            return 0;
        }
    }
}
