import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Sebastian Raubach
 */
public class Ac05
{
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args)
		throws IOException
	{
        int[] inputValues = TaskUtils.readAllInts("res/input/05.txt");

        IntCode one = new IntCode(Arrays.copyOf(inputValues, inputValues.length));
        while (one.canContinue())
            System.out.println(one.computeOutput(1));

        IntCode two = new IntCode(Arrays.copyOf(inputValues, inputValues.length));
        while (two.canContinue())
            System.out.println(two.computeOutput(5));
	}
}
