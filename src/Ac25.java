import java.io.IOException;
import java.util.Scanner;

public class Ac25 extends AbstractTask
{
	private long[] input;

	public Ac25(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		new Ac25(TaskUtils.readAllLongs("res/input/25.txt", ",")).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		// You'll need: hypercube, mouse, antenna, semiconductor
		IntCode intCode = new IntCode(input);
		Scanner scanner = new Scanner(System.in);

		String output = "";
		while (intCode.canContinue())
		{
			long character = intCode.computeOutput();

			if (character < 256)
				output += (char) character;
			else
				output += character;

			if (output.endsWith("Command?"))
			{
				System.out.println(output);
				output = "";

				String newInput = scanner.nextLine();

				char[] chars = newInput.toCharArray();
				for (int i = 0; i < chars.length; i++)
					intCode.addManualInput(chars[i]);

				intCode.addManualInput('\n');
			}
		}

		System.out.println(output);
	}

	@Override
	protected void solvePartTwo()
	{

	}
}
