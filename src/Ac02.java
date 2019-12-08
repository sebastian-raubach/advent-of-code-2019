import java.io.IOException;
import java.util.Arrays;

public class Ac02
{
	public static void main(String[] args) throws IOException
	{
		int[] inputValues = TaskUtils.readAllInts("res/input/02.txt", ",");

		int[] localValues = Arrays.copyOf(inputValues, inputValues.length);
		localValues[1] = 12;
		localValues[2] = 2;

		IntCode first = new IntCode(localValues);
		System.out.println(first.computeOutput());

		outer:
		for (int noun = 0; noun < 100; noun++)
		{
			for (int verb = 0; verb < 100; verb++)
			{
				localValues = Arrays.copyOf(inputValues, inputValues.length);
				localValues[1] = noun;
				localValues[2] = verb;

				IntCode intCode = new IntCode(localValues);

				int output = intCode.computeOutput();

				if (output == 19690720)
				{
					System.out.println(String.format("%02d", noun) + String.format("%02d", verb));
					break outer;
				}
			}
		}
	}
}
