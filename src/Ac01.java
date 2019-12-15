import java.io.IOException;
import java.util.Arrays;

public class Ac01 extends AbstractTask
{
	private int[] input;

	public Ac01(int[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		int[] values = TaskUtils.readAllInts("res/input/01.txt", "\n");

		new Ac01(values).run();
	}

	@Override
	public boolean test()
	{
		try
		{
			assert getFuelRequirement(12) == 2;
			assert getFuelRequirement(14) == 2;
			assert getFuelRequirement(1969) == 654;
			assert getFuelRequirement(100756) == 33583;

			assert getFuelRequirementRecursive(14) == 2;
			assert getFuelRequirementRecursive(1969) == 966;
			assert getFuelRequirementRecursive(100756) == 50346;
		}
		catch (AssertionError e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public void solvePartOne()
	{
		long sum = Arrays.stream(input)
				.boxed()
				.mapToInt(this::getFuelRequirement)
				.sum();
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo()
	{
		int sum = Arrays.stream(input)
				.boxed()
				.mapToInt(this::getFuelRequirementRecursive)
				.sum();
		System.out.println(sum);
	}

	private int getFuelRequirement(int input)
	{
		return input / 3 - 2;
	}

	private int getFuelRequirementRecursive(int input)
	{
		int value = getFuelRequirement(input);

		if (value > 0)
		{
			value += getFuelRequirementRecursive(value);
			return value;
		}
		else
		{
			return 0;
		}
	}
}
