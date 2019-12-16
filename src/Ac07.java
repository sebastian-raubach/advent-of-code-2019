import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ac07 extends AbstractTask
{
	private long[] input;

	public Ac07(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args)
			throws IOException
	{
		long[] inputValues = TaskUtils.readAllLongs("res/input/07.txt", ",");

		new Ac07(inputValues).run();
	}

	@Override
	protected boolean test()
	{
		try
		{
			assert computePartOne(TaskUtils.mapToLongs("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0", ","), new long[]{4, 3, 2, 1, 0}) == 43210;
			assert computePartOne(TaskUtils.mapToLongs("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0", ","), new long[]{0, 1, 2, 3, 4}) == 54321;
			assert computePartOne(TaskUtils.mapToLongs("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0", ","), new long[]{1, 0, 4, 3, 2}) == 65210;

			assert computePartTwo(TaskUtils.mapToLongs("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5", ","), new long[]{9, 8, 7, 6, 5}) == 139629729;
//			assert computePartOne(TaskUtils.mapToLongs("3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10", ","), new long[]{9,7,8,5,6}) == 18216;
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
		System.out.println(computePartOne(Arrays.copyOf(this.input, this.input.length), new long[]{0, 1, 2, 3, 4}));
	}

	private long computePartOne(long[] inputValues, long[] values)
	{
		List<long[]> permutations = new ArrayList<>();
		permute(permutations, values, 0);

		long max = -Long.MAX_VALUE;

		for (long[] order : permutations)
		{
			long intermediate = new IntCode(inputValues).computeOutput(order[0], 0);
			intermediate = new IntCode(inputValues).computeOutput(order[1], intermediate);
			intermediate = new IntCode(inputValues).computeOutput(order[2], intermediate);
			intermediate = new IntCode(inputValues).computeOutput(order[3], intermediate);
			intermediate = new IntCode(inputValues).computeOutput(order[4], intermediate);

			max = Math.max(max, intermediate);
		}

		return max;
	}

	@Override
	protected void solvePartTwo()
	{
		System.out.println(computePartTwo(Arrays.copyOf(this.input, this.input.length), new long[]{5, 6, 7, 8, 9}));
	}

	private long computePartTwo(long[] inputValues, long[] values)
	{
		List<long[]> result = new ArrayList<>();
		permute(result, values, 0);

		long max = -Long.MAX_VALUE;

		for (long[] order : result)
		{
			IntCode one = new IntCode(inputValues);
			IntCode two = new IntCode(inputValues);
			IntCode three = new IntCode(inputValues);
			IntCode four = new IntCode(inputValues);
			IntCode five = new IntCode(inputValues);

			// Do an initial round with the phase settings
			long intermediate = one.computeOutput(order[0], 0);
			intermediate = two.computeOutput(order[1], intermediate);
			intermediate = three.computeOutput(order[2], intermediate);
			intermediate = four.computeOutput(order[3], intermediate);
			intermediate = five.computeOutput(order[4], intermediate);
			max = Math.max(max, intermediate);

			// Then iterate as long as it doesn't terminate
			while (five.canContinue())
			{
				intermediate = one.computeOutput(intermediate);
				intermediate = two.computeOutput(intermediate);
				intermediate = three.computeOutput(intermediate);
				intermediate = four.computeOutput(intermediate);
				intermediate = five.computeOutput(intermediate);
				max = Math.max(max, intermediate);
			}
		}

		return max;
	}

	private static void permute(List<long[]> result, long[] arr, int index)
	{
		for (int i = index; i < arr.length; i++)
		{
			swap(arr, i, index);
			permute(result, arr, index + 1);
			swap(arr, index, i);
		}

		if (index == arr.length - 1)
			result.add(Arrays.copyOf(arr, arr.length));
	}

	private static void swap(long[] a, int i, int j)
	{
		long t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
}
