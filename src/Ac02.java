import java.io.IOException;
import java.util.Arrays;

public class Ac02 extends AbstractTask
{
	private long[] input;

	public Ac02(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		long[] inputValues = TaskUtils.readAllLongs("res/input/02.txt", ",");

		new Ac02(inputValues).run();
	}

	@Override
	protected boolean test()
	{
		long[] localValues = Arrays.stream("1,9,10,3,2,3,11,0,99,30,40,50".split(","))
				.mapToLong(Long::parseLong)
				.toArray();
		IntCode intCode = new IntCode(localValues);

		try
		{
			assert intCode.computeOutput() == 3500;
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
		long[] localValues = Arrays.copyOf(input, input.length);
		localValues[1] = 12;
		localValues[2] = 2;

		IntCode first = new IntCode(localValues);
		System.out.println(first.computeOutput());
	}

	@Override
	protected void solvePartTwo()
	{
		outer:
		for (int noun = 0; noun < 100; noun++)
		{
			for (int verb = 0; verb < 100; verb++)
			{
				long[] localValues = Arrays.copyOf(input, input.length);
				localValues[1] = noun;
				localValues[2] = verb;

				IntCode intCode = new IntCode(localValues);

				long output = intCode.computeOutput();

				if (output == 19690720)
				{
					System.out.println(String.format("%02d", noun) + String.format("%02d", verb));
					break outer;
				}
			}
		}
	}
}
