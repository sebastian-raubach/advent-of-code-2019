import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Ac16 extends AbstractTask
{
	private int[] input;
	private int[] base = {0, 1, 0, -1};

	public Ac16(int[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		int[] input = TaskUtils.readAllInts("res/input/16.txt", "");

		new Ac16(input).run();
	}

	private int getMultiplier(int index, int factor) {
		// Calculate the multiplier by scaling the index into the target domain by dividing it through the factor (mind the +1 for the offset) and then moduloing it into range again
		return base[((index + 1) / factor) % base.length];
	}

	@Override
	protected boolean test()
	{
		try
		{
			assert calculateLoop(TaskUtils.mapToInts("80871224585914546619083218645595", "")) == 24176176;
			assert calculateLoop(TaskUtils.mapToInts("19617804207202209144916044189917", "")) == 73745418;
			assert calculateLoop(TaskUtils.mapToInts("69317163492948606335995924319873", "")) == 52432133;

			assert calculateLongLoop(TaskUtils.mapToInts("03036732577212944063491565474664", "")) == 84462026;
			assert calculateLongLoop(TaskUtils.mapToInts("02935109699940807407585447034323", "")) == 78725270;
			assert calculateLongLoop(TaskUtils.mapToInts("03081770884921959731165446850517", "")) == 53553731;
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
		System.out.println(calculateLoop(this.input));
	}

	private int calculateLoop(int[] input) {
		int[] current = Arrays.copyOf(input, input.length);
		// Run 100 phases
		for (int phase = 0; phase < 100; phase++)
			current = runPhase(current);

		// Then get the first 8 values as a number
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 8; i++)
			result.append(current[i]);

		return Integer.parseInt(result.toString());
	}

	private int[] runPhase(int[] input)
	{
		// The output array
		int[] result = new int[input.length];

		for (int i = 0; i < input.length; i++)
		{
			int value = 0;

			// Sum up over each position calculation
			for (int j = 0; j < input.length; j++)
				// Where each one takes the input and multiplies it with the multiplier
				value += input[j] * getMultiplier(j, i + 1);

			// Then we get the last digit of the output
			String v = Integer.toString(value);
			result[i] = v.charAt(v.length() - 1) - '0';
		}

		return result;
	}

	@Override
	protected void solvePartTwo()
	{
		System.out.println(calculateLongLoop(this.input));
	}

	private int calculateLongLoop(int[] input) {
		// Create a huge new array
		int[] current = new int[input.length * 10000];

		// Copy the old values across
		for (int i = 0; i < current.length; i++)
			current[i] = input[i % input.length];

		// Then get the answer offset from the first 7 digits
		int answerOffset = Integer.parseInt(Arrays.stream(Arrays.copyOf(current, 7))
				.mapToObj(Integer::toString)
				.collect(Collectors.joining()));

		// Again, run 100 phases
		for (int phase = 0; phase < 100; phase++) {
			// But this time, we're exploiting a repetition in the data
			long sum = 0;

			for (int j = answerOffset; j < current.length; j++)
				sum += current[j];

			for (int j = answerOffset; j < current.length; j++)
			{
				long t = sum;

				sum -= current[j];

				if (t >= 0)
					current[j] = Math.floorMod(t, 10);
				else
					current[j] = Math.floorMod(-t, 10);
			}
		}

		// Get the 8 values at the offset and return them
		StringBuilder result = new StringBuilder();
		for (int i = answerOffset; i < answerOffset + 8; i++)
			result.append(current[i]);

		return Integer.parseInt(result.toString());
	}
}
