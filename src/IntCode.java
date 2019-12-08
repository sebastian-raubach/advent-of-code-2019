public class IntCode
{
	private int[] inputSequence;
	private int operatorPosition = 0;
	private int currentOperation = 0;
	private int[] currentOperatorSequence;

	public IntCode(int[] inputSequence)
	{
		this.inputSequence = inputSequence;
	}

	private static int[] instructionToParts(int input)
	{
		int[] result = new int[5];
		char[] parts = Integer.toString(input).toCharArray();

		int delta = result.length - parts.length;

		for (int i = result.length - 1; i >= delta; i--)
			result[i] = Integer.parseInt(parts[i - delta] + "");

		return result;
	}

	public boolean canContinue()
	{
		return currentOperation != 99;
	}

	public int computeOutput(int... manualInput)
	{
		int manualInputPointer = 0;

		loop:
		while (operatorPosition < inputSequence.length)
		{
			Integer value = inputSequence[operatorPosition];
			int increment = 0;

			currentOperatorSequence = instructionToParts(value);
			currentOperation = currentOperatorSequence[3] * 10 + currentOperatorSequence[4];
			int delta;
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
					inputSequence[inputSequence[operatorPosition + 1]] = manualInput[manualInputPointer++];
					increment = 2;
					break;
				case 4:
					int result = inputSequence[inputSequence[operatorPosition + 1]];
					operatorPosition += 2;
					return result;
				case 5:
					delta = jumpIfTrue();
					if (delta == 0)
						increment = 3;
					else
						operatorPosition = delta;
					break;
				case 6:
					delta = jumpIfFalse();
					if (delta == 0)
						increment = 3;
					else
						operatorPosition = delta;
					break;
				case 7:
					lessThan();
					increment = 4;
					break;
				case 8:
					isEqual();
					increment = 4;
					break;
			}

			operatorPosition += increment;
		}

		return inputSequence[0];
	}

	private void lessThan()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;
		int target = inputSequence[operatorPosition + 3];

		if (inputSequence[left] < inputSequence[right])
			inputSequence[target] = 1;
		else
			inputSequence[target] = 0;
	}

	private void isEqual()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;
		int target = inputSequence[operatorPosition + 3];

		if (inputSequence[left] == inputSequence[right])
			inputSequence[target] = 1;
		else
			inputSequence[target] = 0;
	}

	private int jumpIfTrue()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;

		if (inputSequence[left] != 0)
			return inputSequence[right];
		else
			return 0;
	}

	private int jumpIfFalse()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;

		if (inputSequence[left] == 0)
			return inputSequence[right];
		else
			return 0;
	}

	private void add()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;
		int target = inputSequence[operatorPosition + 3];

		inputSequence[target] = inputSequence[left] + inputSequence[right];
	}

	private void multiply()
	{
		int first = currentOperatorSequence[2];
		int second = currentOperatorSequence[1];

		int left = first == 0 ? inputSequence[operatorPosition + 1] : operatorPosition + 1;
		int right = second == 0 ? inputSequence[operatorPosition + 2] : operatorPosition + 2;
		int target = inputSequence[operatorPosition + 3];

		inputSequence[target] = inputSequence[left] * inputSequence[right];
	}
}
