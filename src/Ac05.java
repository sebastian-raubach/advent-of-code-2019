import java.io.IOException;
import java.util.Arrays;

/**
 * @author Sebastian Raubach
 */
public class Ac05 extends AbstractTask
{
	private long[] input;

	public Ac05(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args)
			throws IOException
	{
		long[] input = TaskUtils.readAllLongs("res/input/05.txt", ",");

		new Ac05(input).run();
	}

	@Override
	protected boolean test()
	{
		try
		{
			assert compute(TaskUtils.mapToLongs("3,9,8,9,10,9,4,9,99,-1,8", ","), 8) == 1;
			assert compute(TaskUtils.mapToLongs("3,9,8,9,10,9,4,9,99,-1,8", ","), 7) == 0;

			assert compute(TaskUtils.mapToLongs("3,9,7,9,10,9,4,9,99,-1,8", ","), 7) == 1;
			assert compute(TaskUtils.mapToLongs("3,9,7,9,10,9,4,9,99,-1,8", ","), 8) == 0;

			assert compute(TaskUtils.mapToLongs("3,3,1108,-1,8,3,4,3,99", ","), 8) == 1;
			assert compute(TaskUtils.mapToLongs("3,3,1108,-1,8,3,4,3,99", ","), 7) == 0;

			assert compute(TaskUtils.mapToLongs("3,3,1107,-1,8,3,4,3,99", ","), 7) == 1;
			assert compute(TaskUtils.mapToLongs("3,3,1107,-1,8,3,4,3,99", ","), 8) == 0;
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
		System.out.println(compute(this.input, 1));
	}

	@Override
	protected void solvePartTwo()
	{
		System.out.println(compute(this.input, 5));
	}

	private long compute(long[] input, int manual)
	{
		IntCode two = new IntCode(Arrays.copyOf(input, input.length));

		long result = 0;
		while (two.canContinue())
			result = two.computeOutput(manual);

		return result;
	}
}
