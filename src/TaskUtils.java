import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class TaskUtils
{
	/**
	 * Reads the input at the given location and uses the separator to split it into parts. Each part is then parsed into an int and the resulting array returned.
	 *
	 * @param path      The location of the input file
	 * @param separator The separator between the individual numbers
	 * @return An array containing the integers parsed from the input
	 * @throws IOException Thrown if any file interaction fails.
	 */
	public static int[] readAllInts(String path, String separator) throws IOException
	{
		Path input = new File(path).toPath();
		return Arrays.stream(new String(Files.readAllBytes(input)).split(separator))
				.mapToInt(Integer::parseInt)
				.toArray();
	}

	/**
	 * Reads the input at the given location and uses the separator to split it into parts. Each part is then parsed into a long and the resulting array returned.
	 *
	 * @param path      The location of the input file
	 * @param separator The separator between the individual numbers
	 * @return An array containing the longs parsed from the input
	 * @throws IOException Thrown if any file interaction fails.
	 */
	public static long[] readAllLongs(String path, String separator) throws IOException
	{
		Path input = new File(path).toPath();
		return Arrays.stream(new String(Files.readAllBytes(input)).split(separator))
				.mapToLong(Long::parseLong)
				.toArray();
	}

	/**
	 * Reads the input at the given location and splits it into individual characters. Each part is then parsed into an int and the resulting array returned.
	 *
	 * @param path The location of the input file
	 * @return An array containing the integers parsed from the input
	 * @throws IOException Thrown if any file interaction fails.
	 */
	public static int[] readAllInts(String path) throws IOException
	{
		Path input = new File(path).toPath();

		char[] chars = new String(Files.readAllBytes(input)).toCharArray();
		return IntStream.range(0, chars.length)
				.map(i -> chars[i] - '0')
				.toArray();
	}

	public static char[][] readGrid(String path) throws IOException
	{
		Path input = new File(path).toPath();

		List<String> lines = Files.readAllLines(input);
		char[][] result = new char[lines.size()][];

		for (int i = 0; i < lines.size(); i++)
			result[i] = lines.get(i).toCharArray();

		return result;
	}

	public static int[] mapToInts(String input, String separator)
	{
		return Arrays.stream(input.split(separator))
				.mapToInt(Integer::parseInt)
				.toArray();
	}

	public static long[] mapToLongs(String input, String separator)
	{
		return Arrays.stream(input.split(separator))
				.mapToLong(Long::parseLong)
				.toArray();
	}

	public static String toString(long[][] grid)
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				builder.append(grid[y][x]);
			}
			builder.append('\n');
		}
		builder.append('\n');

		return builder.toString();
	}


	public static String toString(int[][] grid)
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (grid[y][x] == 0)
					builder.append("#");
				else
					builder.append((char)('0' + grid[y][x]) + "");
			}
			builder.append('\n');
		}
		builder.append('\n');

		return builder.toString();
	}

	public static String toString(boolean[][] grid)
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (x == 2 && y == 2)
					builder.append(" ");
				else if (grid[y][x])
					builder.append("#");
				else
					builder.append(".");
			}
			builder.append('\n');
		}
		builder.append('\n');

		return builder.toString();
	}

	public static String toString(boolean[][] outer, boolean[][] middle, boolean[][] inner)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(outer[0][0] ? '#' : '.')
				.append(outer[0][1] ? '#' : '.')
				.append("    ")
				.append(outer[0][2] ? '#' : '.')
				.append("    ")
				.append(outer[0][3] ? '#' : '.')
				.append(outer[0][4] ? '#' : '.')
				.append("\n");

		builder.append(outer[1][0] ? '#' : '.')
				.append(outer[1][1] ? '#' : '.')
				.append("    ")
				.append(outer[1][2] ? '#' : '.')
				.append("    ")
				.append(outer[1][3] ? '#' : '.')
				.append(outer[1][4] ? '#' : '.')
				.append("\n");

		builder.append("  ")
				.append(middle[0][0] ? '#' : '.')
				.append(middle[0][1] ? '#' : '.')
				.append("  ")
				.append(middle[0][2] ? '#' : '.')
				.append("  ")
				.append(middle[0][3] ? '#' : '.')
				.append(middle[0][4] ? '#' : '.')
				.append("  ")
				.append("\n");

		builder.append("  ")
				.append(middle[1][0] ? '#' : '.')
				.append(middle[1][1] ? '#' : '.')
				.append("  ")
				.append(middle[1][2] ? '#' : '.')
				.append("  ")
				.append(middle[1][3] ? '#' : '.')
				.append(middle[1][4] ? '#' : '.')
				.append("  ")
				.append("\n");

		builder.append("    ")
				.append(inner[0][0] ? '#' : '.')
				.append(inner[0][1] ? '#' : '.')
				.append(inner[0][2] ? '#' : '.')
				.append(inner[0][3] ? '#' : '.')
				.append(inner[0][4] ? '#' : '.')
				.append("    ")
				.append("\n");

		builder.append("    ")
				.append(inner[1][0] ? '#' : '.')
				.append(inner[1][1] ? '#' : '.')
				.append(inner[1][2] ? '#' : '.')
				.append(inner[1][3] ? '#' : '.')
				.append(inner[1][4] ? '#' : '.')
				.append("    ")
				.append("\n");

		builder.append(outer[2][0] ? '#' : '.')
				.append(outer[2][1] ? '#' : '.')
				.append(middle[2][0] ? '#' : '.')
				.append(middle[2][1] ? '#' : '.')
				.append(inner[2][0] ? '#' : '.')
				.append(inner[2][1] ? '#' : '.')
				.append("?")
				.append(inner[2][3] ? '#' : '.')
				.append(inner[2][4] ? '#' : '.')
				.append(middle[2][3] ? '#' : '.')
				.append(middle[2][4] ? '#' : '.')
				.append(outer[2][3] ? '#' : '.')
				.append(outer[2][4] ? '#' : '.')
				.append("\n");

		builder.append("    ")
				.append(inner[3][0] ? '#' : '.')
				.append(inner[3][1] ? '#' : '.')
				.append(inner[3][2] ? '#' : '.')
				.append(inner[3][3] ? '#' : '.')
				.append(inner[3][4] ? '#' : '.')
				.append("    ")
				.append("\n");

		builder.append("    ")
				.append(inner[4][0] ? '#' : '.')
				.append(inner[4][1] ? '#' : '.')
				.append(inner[4][2] ? '#' : '.')
				.append(inner[4][3] ? '#' : '.')
				.append(inner[4][4] ? '#' : '.')
				.append("    ")
				.append("\n");

		builder.append("  ")
				.append(middle[3][0] ? '#' : '.')
				.append(middle[3][1] ? '#' : '.')
				.append("  ")
				.append(middle[3][2] ? '#' : '.')
				.append("  ")
				.append(middle[3][3] ? '#' : '.')
				.append(middle[3][4] ? '#' : '.')
				.append("  ")
				.append("\n");

		builder.append("  ")
				.append(middle[4][0] ? '#' : '.')
				.append(middle[4][1] ? '#' : '.')
				.append("  ")
				.append(middle[4][2] ? '#' : '.')
				.append("  ")
				.append(middle[4][3] ? '#' : '.')
				.append(middle[4][4] ? '#' : '.')
				.append("  ")
				.append("\n");

		builder.append(outer[3][0] ? '#' : '.')
				.append(outer[3][1] ? '#' : '.')
				.append("    ")
				.append(outer[3][2] ? '#' : '.')
				.append("    ")
				.append(outer[3][3] ? '#' : '.')
				.append(outer[3][4] ? '#' : '.')
				.append("\n");

		builder.append(outer[4][0] ? '#' : '.')
				.append(outer[4][1] ? '#' : '.')
				.append("    ")
				.append(outer[4][2] ? '#' : '.')
				.append("    ")
				.append(outer[4][3] ? '#' : '.')
				.append(outer[4][4] ? '#' : '.')
				.append("\n");

		return builder.toString();
	}

	public static String toStringGrid(int[][] grid)
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (x == 2 && y == 2)
					builder.append(" ");
				else
					builder.append(grid[y][x]);
			}
			builder.append('\n');
		}
		builder.append('\n');

		return builder.toString();
	}

	public static String toString(char[][] grid)
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				builder.append(grid[y][x]);
			}
			builder.append('\n');
		}
		builder.append('\n');

		return builder.toString();
	}
}
