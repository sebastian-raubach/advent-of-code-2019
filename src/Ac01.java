import java.io.IOException;
import java.util.Arrays;

public class Ac01
{
	public static void main(String[] args) throws IOException
	{
		int[] values = TaskUtils.readAllInts("res/input/01.txt", ",");

		long sum = Arrays.stream(values)
				.boxed()
				.mapToInt(Ac01::getFuelRequirement)
				.sum();
		System.out.println(sum);

		sum = Arrays.stream(values)
				.boxed()
				.mapToInt(Ac01::getFuelRequirementRecursive)
				.sum();
		System.out.println(sum);
	}

	private static int getFuelRequirement(int input)
	{
		return input / 3 - 2;
	}

	private static int getFuelRequirementRecursive(int input)
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
