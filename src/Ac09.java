import java.io.IOException;

public class Ac09
{
	public static void main(String[] args) throws IOException
	{
		long[] input = TaskUtils.readAllLongs("res/input/09.txt", ",");

		IntCode intCode = new IntCode(input);

		while (intCode.canContinue())
			System.out.println(intCode.computeOutput(1L));

		intCode = new IntCode(input);

		while (intCode.canContinue())
			System.out.println(intCode.computeOutput(2L));
	}
}
