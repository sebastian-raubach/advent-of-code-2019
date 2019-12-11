import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Ac11
{
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	public static void main(String[] args) throws IOException
	{
		solvePartOne();
		solvePartTwo();
	}

	private static void solvePartOne() throws IOException
	{
		IntCode intCode = new IntCode(TaskUtils.readAllLongs("res/input/11.txt", ","));

		boolean[][] grid = new boolean[100][100];
		int x = 50;
		int y = 50;
		int heading = UP;

		Set<String> paintedCells = new HashSet<>();
		while (intCode.canContinue())
		{
			long input = grid[y][x] ? 1 : 0;
			long newColor = intCode.computeOutput(input);
			grid[y][x] = newColor == 1L;

			paintedCells.add(x + "-" + y);

			long direction = intCode.computeOutput(input);
			heading = heading + (direction == 1L ? 1 : -1);

			if (heading < 0)
				heading += 4;

			heading = Math.floorMod(heading, 4);

			switch (heading)
			{
				case UP:
					y++;
					break;
				case RIGHT:
					x++;
					break;
				case DOWN:
					y--;
					break;
				case LEFT:
					x--;
					break;
			}
		}
		System.out.println(paintedCells.size());
	}

	private static void solvePartTwo() throws IOException
	{
		IntCode intCode = new IntCode(TaskUtils.readAllLongs("res/input/11.txt", ","));

		boolean[][] grid = new boolean[100][100];
		int x = 50;
		int y = 50;
		int heading = UP;
		grid[y][x] = true;

		Set<String> paintedCells = new HashSet<>();
		while (intCode.canContinue())
		{
			long input = grid[y][x] ? 1 : 0;
			long newColor = intCode.computeOutput(input);
			grid[y][x] = newColor == 1L;

			paintedCells.add(x + "-" + y);

			long direction = intCode.computeOutput(input);
			heading = heading + (direction == 1L ? 1 : -1);

			if (heading < 0)
				heading += 4;

			heading = Math.floorMod(heading, 4);

			switch (heading)
			{
				case UP:
					y++;
					break;
				case RIGHT:
					x++;
					break;
				case DOWN:
					y--;
					break;
				case LEFT:
					x--;
					break;
			}
		}

		for (int b = grid.length - 1; b >= 0; b--)
		{
			for (int a = 0; a < grid[b].length; a++)
			{
				System.out.print(grid[b][a] ? '░' : '█');
			}
			System.out.println();
		}
	}
}
