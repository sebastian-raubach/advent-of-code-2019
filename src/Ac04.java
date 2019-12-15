import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Ac04 extends AbstractTask
{
	private int start;
	private int end;
	private int length;

	public Ac04(String[] values)
	{
		this.start = Integer.parseInt(values[0]);
		this.end = Integer.parseInt(values[1]);
		this.length = values[0].length();
	}

	public static void main(String[] args) throws IOException
	{
		Path input = new File("res/input/04.txt").toPath();

		String[] values = new String(Files.readAllBytes(input)).split("-");

		new Ac04(values).run();
	}

	@Override
	protected boolean test()
	{
		try
		{
			assert isValidPartOne(TaskUtils.mapToInts("111111", ""));
			assert !isValidPartOne(TaskUtils.mapToInts("223450", ""));
			assert !isValidPartOne(TaskUtils.mapToInts("123789", ""));

			assert isValidPartTwo(TaskUtils.mapToInts("112233", ""));
			assert !isValidPartTwo(TaskUtils.mapToInts("123444", ""));
			assert isValidPartTwo(TaskUtils.mapToInts("111122", ""));
		}
		catch (AssertionError e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		int counter = 0;
		for (int i = start; i <= end; i++)
		{
			int[] number = new int[length];

			String current = String.format("%0" + length + "d", i);

			for (int j = 0; j < length; j++)
				number[j] = Integer.parseInt(current.charAt(j) + "");

			if (isValidPartOne(number))
				counter++;
		}

		System.out.println(counter);
	}

	@Override
	protected void solvePartTwo()
	{
		int counter = 0;
		for (int i = start; i <= end; i++)
		{
			int[] number = new int[length];

			String current = String.format("%0" + length + "d", i);

			for (int j = 0; j < length; j++)
				number[j] = Integer.parseInt(current.charAt(j) + "");

			if (isValidPartTwo(number))
				counter++;
		}

		System.out.println(counter);
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
