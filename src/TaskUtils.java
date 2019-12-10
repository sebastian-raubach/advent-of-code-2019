import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TaskUtils
{
	public static int[] readAllInts(String path, String separator) throws IOException
	{
		Path input = new File(path).toPath();
		return Arrays.stream(new String(Files.readAllBytes(input)).split(separator))
				.mapToInt(Integer::parseInt)
				.toArray();
	}

	public static long[] readAllLongs(String path, String separator) throws IOException
	{
		Path input = new File(path).toPath();
		return Arrays.stream(new String(Files.readAllBytes(input)).split(separator))
				.mapToLong(Long::parseLong)
				.toArray();
	}

	public static int[] readAllInts(String path) throws IOException
	{
		Path input = new File(path).toPath();

		char[] chars = new String(Files.readAllBytes(input)).toCharArray();
		return IntStream.range(0, chars.length)
				.map(i -> chars[i] - '0')
				.toArray();
	}
}
