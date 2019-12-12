import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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

	private static void solvePartTwo() throws IOException
	{
		List<Moon> moons = Files.readAllLines(new File("res/input/12.txt").toPath())
				.stream()
				.map((Moon::parse))
				.collect(Collectors.toList());

		long counter = 0;
		List<String> states = new ArrayList<>();

		int matchIndex = -1;
		while (matchIndex == -1)
		{
			applyRound(moons);

			matchIndex = states.indexOf(getState(moons));
			counter++;

			if (counter % 100000 == 0)
				System.out.println(counter);
		}

		System.out.println(counter);
	}

	private static String getState(List<Moon> moons)
	{
		String result = "";

		for (Moon moon : moons)
		{
			result += "." + moon.position[0] + "." + moon.position[1] + "." + moon.position[2];
		}

		return result;
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
			{
				velocity[i]--;
			}
			else if (position[i] < other)
			{
				velocity[i]++;
			}
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
