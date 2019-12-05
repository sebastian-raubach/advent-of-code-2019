import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Ac02 {
    public static void main(String[] args) throws IOException {
        System.out.println(computeOutput(12, 2));

        outer:
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int output = computeOutput(noun, verb);

                if (output == 19690720) {
                    System.out.println(String.format("%02d", noun) + String.format("%02d", verb));
                    break outer;
                }
            }
        }
    }

    private static int computeOutput(int noun, int verb) throws IOException {
        Path input = new File("res/input/02.txt").toPath();
        Integer[] values = Arrays.stream(new String(Files.readAllBytes(input)).split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toArray(Integer[]::new);

        values[1] = noun;
        values[2] = verb;

        loop:
        for (int i = 0; i < values.length; i += 4) {
            Integer value = values[i];

            int left = values[values[i + 1]];
            int right = values[values[i + 2]];
            int target = values[i + 3];

            switch (value) {
                case 99:
                    break loop;
                case 1:
                    values[target] = left + right;
                    break;
                case 2:
                    values[target] = left * right;
                    break;
                default:
                    throw new IOException("Invalid opcode");
            }
        }

        return values[0];
    }
}
