import java.io.IOException;
import java.util.*;

public class Ac23 extends AbstractTask
{
	private long[] input;

	public Ac23(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		new Ac23(TaskUtils.readAllLongs("res/input/23.txt", ",")).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		IntCode[] machines = new IntCode[50];
		Map<Integer, Queue<Packet>> packetsInTransit = new HashMap<>();

		for (int i = 0; i < machines.length; i++)
		{
			machines[i] = new IntCode(Arrays.copyOf(input, input.length), i);

			packetsInTransit.put(i, new LinkedList<>());
		}

		long natX = 0;
		long natY = 0;

		Set<Long> yValues = new HashSet<>();

		while (true)
		{
			int idleCount = 0;
			for (int i = 0; i < machines.length; i++)
			{
				IntCode machine = machines[i];
				Queue<Packet> forMachine = packetsInTransit.get(i);

				if (forMachine.size() > 0)
				{
					Packet p = forMachine.poll();

					machine.addManualInput(p.x, p.y);
				}
				else
				{
					machine.addManualInput(-1);
				}

				int target = (int) machine.computeOutput();

				if (target != -Integer.MAX_VALUE)
				{
					long x = machine.computeOutput();
					long y = machine.computeOutput();

					if (target == 255)
					{
						natX = x;
						natY = y;
					}
					else
					{
						Packet newPacket = new Packet(x, y);
						packetsInTransit.get(target).add(newPacket);
					}
				}
				else
				{
					idleCount++;
				}
			}

			if (idleCount == machines.length)
			{
				packetsInTransit.get(0).add(new Packet(natX, natY));

				if (yValues.contains(natY))
				{
					System.out.println(natY);
					return;
				}

				yValues.add(natY);
			}
		}
	}

	@Override
	protected void solvePartTwo()
	{

	}

	private static class Packet
	{
		private long x;
		private long y;

		public Packet(long x, long y)
		{
			this.x = x;
			this.y = y;
		}
	}
}
