import java.util.LinkedList;
import java.util.Queue;

public class IntCode
{
	private long[] inputSequence;
	private int operatorPosition = 0;
	private int relativeBase = 0;
	private int[] currentOperatorSequence;
	private Queue<Long> manualInput = new LinkedList<>();
	private int increment;

	public IntCode(long[] inputSequence, long... globalManualInput)
	{
		// Copy the array onto a much larger array. This was necessary because some tasks needed more memory.
		this.inputSequence = new long[inputSequence.length * 1000];
		System.arraycopy(inputSequence, 0, this.inputSequence, 0, inputSequence.length);

		addManualInput(globalManualInput);
	}

	public IntCode(long[] inputSequence)
	{
		this(inputSequence, null);
	}

	private static int[] instructionToParts(long input)
	{
		int[] result = new int[5];
		char[] parts = Long.toString(input).toCharArray();

		int delta = result.length - parts.length;

		for (int i = result.length - 1; i >= delta; i--)
			result[i] = Integer.parseInt(parts[i - delta] + "");

		return result;
	}

	public boolean canContinue()
	{
		return inputSequence[operatorPosition] != 99;
	}

	public void addManualInput(long... manualInput)
	{
		if (manualInput != null)
		{
			for (int i = 0; i < manualInput.length; i++)
			{
				this.manualInput.add(manualInput[i]);
			}
		}
	}

	public long computeOutput(long... manualInput)
	{
		addManualInput(manualInput);

		loop:
		while (true)
		{
			long value = inputSequence[operatorPosition];
			increment = 0;

			currentOperatorSequence = instructionToParts(value);
			int currentOperation = currentOperatorSequence[3] * 10 + currentOperatorSequence[4];
			switch (currentOperation)
			{
				case 99:
					break loop;
				case 1:
					add();
					increment = 4;
					break;
				case 2:
					multiply();
					increment = 4;
					break;
				case 3:
					if (this.manualInput.size() < 1)
					{
						return -Integer.MAX_VALUE;
					}
					else
					{
						inputSequence[getAddress(1, currentOperatorSequence[2])] = this.manualInput.poll();
						increment = 2;
					}
					break;
				case 4:
					int index = getIndex(1, currentOperatorSequence[2]);
					long result = inputSequence[index];
					operatorPosition += 2;
					return result;
				case 5:
					jumpIfTrue();
					break;
				case 6:
					jumpIfFalse();
					break;
				case 7:
					lessThan();
					increment = 4;
					break;
				case 8:
					isEqual();
					increment = 4;
					break;
				case 9:
					relativeBase += inputSequence[getIndex(1, currentOperatorSequence[2])];
					increment = 2;
					break;
			}

			operatorPosition += increment;
		}

		return inputSequence[0];
	}

	private int getIndex(int offset, int mode)
	{
		switch (mode)
		{
			case 0:
				return (int) inputSequence[operatorPosition + offset];
			case 1:
				return operatorPosition + offset;
			case 2:
				return (int) inputSequence[operatorPosition + offset] + relativeBase;
			default:
				return operatorPosition;
		}
	}

	private int getAddress(int offset, int mode)
	{
		switch (mode)
		{
			case 0:
			case 1:
				return (int) inputSequence[operatorPosition + offset];
			case 2:
				return (int) inputSequence[operatorPosition + offset] + relativeBase;
			default:
				return operatorPosition;
		}
	}

	private void lessThan()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);
		int target = getAddress(3, currentOperatorSequence[0]);

		if (inputSequence[left] < inputSequence[right])
			inputSequence[target] = 1;
		else
			inputSequence[target] = 0;
	}

	private void isEqual()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);
		int target = getAddress(3, currentOperatorSequence[0]);

		if (inputSequence[left] == inputSequence[right])
			inputSequence[target] = 1;
		else
			inputSequence[target] = 0;
	}

	private void jumpIfTrue()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);

		if (inputSequence[left] != 0)
			operatorPosition = (int) inputSequence[right];
		else
			increment = 3;
	}

	private void jumpIfFalse()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);

		if (inputSequence[left] == 0)
			operatorPosition = (int) inputSequence[right];
		else
			increment = 3;
	}

	private void add()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);
		int target = getAddress(3, currentOperatorSequence[0]);

		inputSequence[target] = inputSequence[left] + inputSequence[right];
	}

	private void multiply()
	{
		int left = getIndex(1, currentOperatorSequence[2]);
		int right = getIndex(2, currentOperatorSequence[1]);
		int target = getAddress(3, currentOperatorSequence[0]);

		inputSequence[target] = inputSequence[left] * inputSequence[right];
	}
}
