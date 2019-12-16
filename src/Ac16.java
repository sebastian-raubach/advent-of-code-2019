import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ac16 extends AbstractTask
{
	private int[] input;
	private int[][] multipliers;

	public Ac16(int[] input)
	{
		this.input = input;

		calculateMultipliers();
	}

	public static void main(String[] args) throws IOException
	{
		int[] input = TaskUtils.readAllInts("res/input/16.txt", "");

		new Ac16(input).run();
	}

	private void calculateMultipliers()
	{
		int[] base = {0, 1, 0, -1};
		multipliers = new int[input.length][];

		for (int i = 0; i < multipliers.length; i++)
		{
			multipliers[i] = getMultipliers(base, i + 1, input.length);
		}
	}

	private int[] getMultipliers(int[] base, int factor, int targetLength)
	{
		List<Integer> result = new ArrayList<>();

		for (int i = 0; i < base.length; i++)
		{
			for (int j = 0; j < factor; j++)
			{
				result.add(base[i]);
			}
		}

		int counter = 0;
		while (result.size() <= targetLength)
		{
			result.add(result.get(counter));

			counter = (counter + 1) % result.size();
		}

		result.remove(0);

		return result.stream()
				.mapToInt(i -> i)
				.toArray();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		int[] current = Arrays.copyOf(input, input.length);
		for (int phase = 0; phase < 100; phase++)
		{
			current = runPhase(current);
		}

		for (int i = 0; i < 8; i++)
			System.out.print(current[i]);
	}

	private int[] runPhase(int[] input)
	{
		int[] result = new int[input.length];

		for (int i = 0; i < input.length; i++)
		{
			int value = 0;

			for (int j = 0; j < input.length; j++)
			{
				value += input[j] * multipliers[i][j];
			}

			String v = Integer.toString(value);
			result[i] = v.charAt(v.length() - 1) - '0';
		}

		return result;
	}

	@Override
	protected void solvePartTwo()
	{

	}
}
