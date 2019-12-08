import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ac04
{
	public static void main(String[] args) throws IOException
	{
		Path input = new File("res/input/04.txt").toPath();

		String[] values = new String(Files.readAllBytes(input)).split("-");

		System.out.println(Arrays.toString(getNrOfPasswords(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[0].length())));
	}

	private static int[] getNrOfPasswords(int start, int end, int length)
	{
		int partOneCounter = 0;
		int partTwoCounter = 0;

		for (int i = start; i <= end; i++)
		{
			int[] number = new int[length];

			String current = String.format("%0" + length + "d", i);

			for (int j = 0; j < length; j++)
				number[j] = Integer.parseInt(current.charAt(j) + "");

			if (isValidPartOne(number))
				partOneCounter++;
			if (isValidPartTwo(number))
				partTwoCounter++;
		}

		return new int[]{partOneCounter, partTwoCounter};
	}

	private static boolean isValidPartOne(int[] number)
	{
		boolean hasAdjacentMatch = false;
		boolean isNonDecreasing = true;

		for (int i = 1; i < number.length; i++)
		{
			hasAdjacentMatch |= number[i] == number[i - 1];
			isNonDecreasing &= number[i] >= number[i - 1];
		}

		return hasAdjacentMatch && isNonDecreasing;
	}

	private static boolean isValidPartTwo(int[] number)
	{
		Map<Integer, Integer> digitCount = new HashMap<>();
		boolean isNonDecreasing = true;

		digitCount.put(number[0], 1);
		for (int i = 1; i < number.length; i++)
		{
			Integer value = digitCount.get(number[i]);

			if (value == null)
				value = 0;

			digitCount.put(number[i], value + 1);

			isNonDecreasing &= number[i] >= number[i - 1];
		}

		boolean hasPair = false;

		for (Integer value : digitCount.values())
			hasPair |= value == 2;

		return hasPair && isNonDecreasing;
	}
}
