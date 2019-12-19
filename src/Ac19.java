import java.io.IOException;
import java.util.Arrays;

/**
 * @author Sebastian Raubach
 */
public class Ac19 extends AbstractTask
{
	private long[] input;

	public Ac19(long[] input)
	{
		this.input = input;
	}

	public static void main(String[] args) throws IOException
	{
		new Ac19(TaskUtils.readAllLongs("res/input/19.txt", ",")).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		char[][] values = new char[50][50];

		int counter = 0;
		for (int y = 0; y < values.length; y++)
		{
			for (int x = 0; x < values[y].length; x++)
			{
				IntCode code = new IntCode(Arrays.copyOf(input, input.length));
				long result = code.computeOutput(x, y);
				values[y][x] = result > 0 ? '#' : '.';

				if (result > 0)
					counter++;
			}
		}

		System.out.println(counter);
		System.out.println(TaskUtils.toString(values));
	}

	@Override
	protected void solvePartTwo()
	{
		// Use an array that's twice as big as the ship in each dimension
		char[][] values = new char[200][200];

		// Guess the right area based on extrapolation from part one and a ship size of 10x10
		int yOffset = 900;
		int xOffset = 750;

		// Fill the grid for the offset area
		for (int y = 0; y < values.length; y++)
		{
			for (int x = 0; x < values[y].length; x++)
			{
				IntCode code = new IntCode(Arrays.copyOf(input, input.length));
				long result = code.computeOutput(x + xOffset, y + yOffset);
				values[y][x] = result > 0 ? '#' : '.';
			}
		}

		// Print it
		System.out.println(TaskUtils.toString(values));
		// Find the closest point for santa's ship
		System.out.println(findClosestPointForSantasShip(values, xOffset, yOffset, 99, 99));
	}

	private int findClosestPointForSantasShip(char[][] values, int xOffset, int yOffset, int santaWidth, int santaHeight)
	{
		for (int y = 0; y < values.length - santaHeight; y++)
		{
			for (int x = 0; x < values[y].length - santaWidth; x++)
			{
				// If this cell is affected by the beam and the one
				if (values[y][x] == '#' && values[y + santaHeight][x] == '#' && values[y][x + santaWidth] == '#')
					return (x + xOffset) * 10000 + (y + yOffset);
			}
		}

		return -1;
	}
}
