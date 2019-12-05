import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class Ac05
{
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args)
		throws IOException
	{
		computeOutput();
	}

	private static int computeOutput()
		throws IOException
	{
		Path input = new File("res/input/05.txt").toPath();
		Integer[] values = Arrays.stream(new String(Files.readAllBytes(input)).split(","))
								 .mapToInt(Integer::parseInt)
								 .boxed()
								 .toArray(Integer[]::new);

		int i = 0;
		loop:
		while (i < values.length)
		{
			Integer value = values[i];
			int increment = 0;

			int[] instruction = instructionToParts(value);

			int op = instruction[3] * 10 + instruction[4];
			switch (op)
			{
				case 99:
					break loop;
				case 1:
					add(instruction, values, i);
					increment = 4;
					break;
				case 2:
					multiply(instruction, values, i);
					increment = 4;
					break;
				case 3:
					values[values[i + 1]] = in.nextInt();
					increment = 2;
					break;
				case 4:
					System.out.println(values[values[i + 1]]);
					increment = 2;
					break;
				case 5:
					i = jumpIfTrue(instruction, values, i);
					break;
				case 6:
					i = jumpIfFalse(instruction, values, i);
					break;
				case 7:
					lessThan(instruction, values, i);
					increment = 4;
					break;
				case  8:
					isEqual(instruction, values, i);
					increment = 4;
					break;
			}

			i += increment;
		}

		return values[0];
	}

	private static void lessThan(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		if (left < right)
			input[target] = 1;
		else
			input[target] = 0;
	}

	private static void isEqual(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		if (left == right)
			input[target] = 1;
		else
			input[target] = 0;
	}

	private static int jumpIfTrue(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		if (left != 0)
			return right;
		else
			return index;
	}

	private static int jumpIfFalse(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		if (left == 0)
			return right;
		else
			return index;
	}

	private static void add(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		input[target] = input[left] + input[right];
	}

	private static void multiply(int[] instruction, Integer[] input, int index)
	{
		int first = instruction[2];
		int second = instruction[1];
		int third = instruction[0];

		int left = first == 0 ? input[index + 1] : index + 1;
		int right = second == 0 ? input[index + 2] : index + 2;
		int target = input[index + 3];

		input[target] = input[left] * input[right];
	}

	private static int[] instructionToParts(Integer input)
	{
		int[] result = new int[5];
		char[] parts = Integer.toString(input).toCharArray();

		int delta = result.length - parts.length;

		for (int i = result.length - 1; i >= delta; i--)
			result[i] = Integer.parseInt(parts[i - delta] + "");

		return result;
	}
}
