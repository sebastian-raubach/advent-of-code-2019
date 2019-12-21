import java.io.IOException;
import java.util.Arrays;

public class Ac21 extends AbstractTask
{
	private long[] input;

	public Ac21(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		new Ac21(TaskUtils.readAllLongs("res/input/21.txt", ",")).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		// Jump is D has land and at least one of A,B,C is a hole
		// !A&&D || !B&&D || !C&&D
		long[] instructions = {
				'N', 'O', 'T', ' ', 'A', ' ', 'J', '\n',
				'A', 'N', 'D', ' ', 'D', ' ', 'J', '\n',
				'N', 'O', 'T', ' ', 'B', ' ', 'T', '\n',
				'A', 'N', 'D', ' ', 'D', ' ', 'T', '\n',
				'O', 'R', ' ', 'T', ' ', 'J', '\n',
				'N', 'O', 'T', ' ', 'C', ' ', 'T', '\n',
				'A', 'N', 'D', ' ', 'D', ' ', 'T', '\n',
				'O', 'R', ' ', 'T', ' ', 'J', '\n',
				'W', 'A', 'L', 'K', '\n'
		};
		IntCode intCode = new IntCode(Arrays.copyOf(this.input, this.input.length), instructions);

		while (intCode.canContinue())
		{
			long value = intCode.computeOutput();
			if (value >= 128)
				System.out.println(value);
			else
				System.out.print((char) value);
		}
	}

	@Override
	protected void solvePartTwo()
	{
		// Jump:
		//    ABCD EFGHI
		// 1.  ███ █████
		// 2. ██ █   ███
		// 3. █  █  ████
		// 4.  █ █ █  ██
		// 5.    █ █████
		// 6. █ ██  ████
		//
		// Don't jump:
		//    ABCD EFGHI
		// 7. ██ █  ██
		// 8. ████  █  █
		long[] instructions = {
				// 1.
				'N', 'O', 'T', ' ', 'A', ' ', 'J', '\n',
				// 2-5 but not 7
				'N', 'O', 'T', ' ', 'C', ' ', 'T', '\n',
				'A', 'N', 'D', ' ', 'H', ' ', 'T', '\n',
				'O', 'R', ' ', 'T', ' ', 'J', '\n',
				// 6 but not 8
				'N', 'O', 'T', ' ', 'B', ' ', 'T', '\n',
				'A', 'N', 'D', ' ', 'A', ' ', 'T', '\n',
				'A', 'N', 'D', ' ', 'C', ' ', 'T', '\n',
				'O', 'R', ' ', 'T', ' ', 'J', '\n',
				// Jump only of D has land
				'A', 'N', 'D', ' ', 'D', ' ', 'J', '\n',
				'R', 'U', 'N', '\n'
		};
		IntCode intCode = new IntCode(Arrays.copyOf(this.input, this.input.length), instructions);

		while (intCode.canContinue())
		{
			long value = intCode.computeOutput();
			if (value >= 128)
				System.out.println(value);
			else
				System.out.print((char) value);
		}
	}
}
