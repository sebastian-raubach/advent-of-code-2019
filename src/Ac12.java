import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ac12
{
	public static void main(String[] args) throws IOException
	{
		solvePartOne();
		solvePartTwo();
	}

	private static void solvePartOne() throws IOException
	{
		List<Moon> moons = Files.readAllLines(new File("res/input/12.txt").toPath())
				.stream()
				.map((Moon::parse))
				.collect(Collectors.toList());

		System.out.println(moons);

		for (int i = 0; i < 1000; i++)
		{
			applyRound(moons);
		}

		int total = 0;
		for (Moon m : moons)
		{
			total += m.getTotalEnergy();
		}

		System.out.println(total);
	}

	private static void solvePartTwo() throws IOException
	{
		List<Moon> moons = Files.readAllLines(new File("res/input/12.txt").toPath())
				.stream()
				.map((Moon::parse))
				.collect(Collectors.toList());

		int[] xPos = getPositions(moons, 0);
		int[] xVel = getVelocities(moons, 1);
		int[] yPos = getPositions(moons, 1);
		int[] yVel = getVelocities(moons, 1);
		int[] zPos = getPositions(moons, 2);
		int[] zVel = getVelocities(moons, 2);

		int counter = 0;
		int xIndex = -1;
		int yIndex = -1;
		int zIndex = -1;

		boolean found = false;
		while (!found)
		{
			counter++;

			applyRound(moons);

			if (xIndex == -1)
				xIndex = Arrays.equals(xPos, getPositions(moons, 0)) && Arrays.equals(xVel, getVelocities(moons, 0)) ? counter : -1;
			if (yIndex == -1)
				yIndex = Arrays.equals(yPos, getPositions(moons, 1)) && Arrays.equals(yVel, getVelocities(moons, 1)) ? counter : -1;
			if (zIndex == -1)
				zIndex = Arrays.equals(zPos, getPositions(moons, 2)) && Arrays.equals(zVel, getVelocities(moons, 2)) ? counter : -1;

			found = xIndex != -1 && yIndex != -1 && zIndex != -1;
		}

		System.out.println(lcm(xIndex, yIndex, zIndex));
	}

	private static void applyRound(List<Moon> moons)
	{
		for (Moon first : moons)
		{
			for (Moon second : moons)
			{
				first.adjustVelocity(second);
			}
		}

		for (Moon m : moons)
		{
			m.applyVelocity();
		}
	}

	private static int[] getPositions(List<Moon> moons, int dim)
	{
		return moons.stream()
				.mapToInt(m -> m.position[dim])
				.toArray();
	}

	private static int[] getVelocities(List<Moon> moons, int dim)
	{
		return moons.stream()
				.mapToInt(m -> m.velocity[dim])
				.toArray();
	}

	private static long gcd(long a, long b)
	{
		if (a == 0)
			return b;
		return gcd(b % a, a);
	}

	private static long lcm(long... inputs)
	{
		if (inputs.length == 0) return 0;
		long rVal = inputs[0];
		for (int i = 0; i < inputs.length; i++)
		{
			rVal = (rVal * inputs[i]) / gcd(rVal, inputs[i]);
		}
		return rVal;
	}

	private static class Moon
	{
		private int[] position = new int[3];
		private int[] velocity = new int[3];

		private static Moon parse(String input)
		{
			String[] parts = input.replaceAll("[^-0-9\\s]", "").split(" ");

			Moon result = new Moon();
			result.position[0] = Integer.parseInt(parts[0]);
			result.position[1] = Integer.parseInt(parts[1]);
			result.position[2] = Integer.parseInt(parts[2]);

			return result;
		}

		private int getTotalEnergy()
		{
			int p = 0;
			int v = 0;

			for (int i = 0; i < position.length; i++)
			{
				p += Math.abs(position[i]);
				v += Math.abs(velocity[i]);
			}

			return p * v;
		}

		private void applyVelocity()
		{
			for (int i = 0; i < position.length; i++)
			{
				position[i] += velocity[i];
			}
		}

		private void adjustVelocity(Moon other)
		{
			for (int i = 0; i < position.length; i++)
			{
				adjustVelocityPosition(i, other.position[i]);
			}
		}

		private void adjustVelocityPosition(int i, int other)
		{
			if (position[i] > other)
				velocity[i]--;
			else if (position[i] < other)
				velocity[i]++;
		}

		@Override
		public String toString()
		{
			return "Moon{" +
					"position=" + Arrays.toString(position) +
					", velocity=" + Arrays.toString(velocity) +
					'}';
		}
	}
}
