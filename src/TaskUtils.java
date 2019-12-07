import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class TaskUtils {
    public static int[] readAllInts(String path) throws IOException {
        Path input = new File(path).toPath();
        return Arrays.stream(new String(Files.readAllBytes(input)).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
