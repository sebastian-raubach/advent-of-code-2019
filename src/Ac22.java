import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Ac22 extends AbstractTask
{
	private List<Instruction> instructions;
	private List<Long> stack;

	public Ac22(List<Instruction> instructions)
	{
		this.instructions = instructions;
	}

	public static void main(String[] args) throws IOException
	{
		List<String> lines = Files.readAllLines(new File("res/input/22.txt").toPath());
		List<Instruction> instructions = new ArrayList<>();

		Operation previous = null;
		for (String line : lines)
		{
			Instruction instruction = new Instruction();
			instruction.operation = Operation.parse(line);

			if (instruction.operation != Operation.DEAL_INTO_NEW_STACK)
				instruction.byHowMuch = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));

			if (previous == Operation.DEAL_INTO_NEW_STACK && instruction.operation == Operation.DEAL_INTO_NEW_STACK)
			{
				// Skip if there are two consecutive new stack operations
				previous = null;
			}
			else
			{
				previous = instruction.operation;
				instructions.add(instruction);
			}
		}

		new Ac22(instructions).run();
	}

	@Override
	protected boolean test()
	{
		try
		{
			List<Instruction> instructions = new ArrayList<>();
			instructions.add(new Instruction(Operation.INCREMENT, 7));
			instructions.add(new Instruction(Operation.DEAL_INTO_NEW_STACK, null));
			instructions.add(new Instruction(Operation.DEAL_INTO_NEW_STACK, null));
			System.out.println(shuffle(LongStream.range(0, 10).boxed().collect(Collectors.toList()), instructions));

			instructions = new ArrayList<>();
			instructions.add(new Instruction(Operation.CUT, 6));
			instructions.add(new Instruction(Operation.INCREMENT, 7));
			instructions.add(new Instruction(Operation.DEAL_INTO_NEW_STACK, null));
			System.out.println(shuffle(LongStream.range(0, 10).boxed().collect(Collectors.toList()), instructions));

			instructions = new ArrayList<>();
			instructions.add(new Instruction(Operation.INCREMENT, 7));
			instructions.add(new Instruction(Operation.INCREMENT, 9));
			instructions.add(new Instruction(Operation.CUT, -2));
			System.out.println(shuffle(LongStream.range(0, 10).boxed().collect(Collectors.toList()), instructions));
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
		this.stack = LongStream.range(0, 10007)
				.boxed()
				.collect(Collectors.toList());

		List<Long> result = shuffle(this.stack, this.instructions);

		System.out.println(result.indexOf(2019L));
	}

	private List<Long> shuffle(List<Long> stack, List<Instruction> instructions)
	{
		for (Instruction instruction : instructions)
		{
			switch (instruction.operation)
			{
				case DEAL_INTO_NEW_STACK:
					Collections.reverse(stack);
					break;
				case CUT:
					List<Long> newStack = new ArrayList<>();
					int offset = Math.floorMod(instruction.byHowMuch, stack.size());

					for (int i = 0; i < stack.size(); i++)
					{
						newStack.add(stack.get(Math.floorMod(offset + i, stack.size())));
					}
					stack = newStack;
					break;
				case INCREMENT:
					Long[] newStackArray = new Long[stack.size()];

					int index = 0;
					for (int i = 0; i < stack.size(); i++)
					{
						newStackArray[index] = stack.get(i);
						index = Math.floorMod(index + instruction.byHowMuch, stack.size());
					}
					stack = new ArrayList<>(Arrays.asList(newStackArray));
					break;
			}
		}

		return stack;
	}

	@Override
	protected void solvePartTwo()
	{
		List<Instruction> reverse = new ArrayList<>(this.instructions);
		Collections.reverse(reverse);

		BigInteger d = new BigInteger("119315717514047");
		BigInteger n = new BigInteger("101741582076661");

		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		BigInteger an = a.modPow(n, d);

		BigInteger B = b.multiply(an.subtract(BigInteger.ONE)).multiply(a.subtract(BigInteger.ONE).modInverse(d));

		BigInteger result = BigInteger.valueOf(2020).subtract(B).multiply(an.modInverse(d)).mod(d);

		System.out.println(result);
	}

	private enum Operation
	{
		DEAL_INTO_NEW_STACK("deal into new stack"),
		CUT("cut"),
		INCREMENT("deal with increment ");

		String name;

		Operation(String name)
		{
			this.name = name;
		}

		static Operation parse(String input)
		{
			for (Operation op : Operation.values())
			{
				if (input.startsWith(op.name))
					return op;
			}

			return null;
		}
	}

	private static class Instruction
	{
		private Operation operation;
		private Integer byHowMuch;

		public Instruction()
		{
		}

		public Instruction(Operation operation, Integer byHowMuch)
		{
			this.operation = operation;
			this.byHowMuch = byHowMuch;
		}

		@Override
		public String toString()
		{
			return "Instruction{" +
					"operation=" + operation +
					", byHowMuch=" + byHowMuch +
					'}';
		}
	}
}
