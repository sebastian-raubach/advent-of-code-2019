import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ac24 extends AbstractTask
{
	private boolean[][] grid;
	private int[][] neighbors;
	private boolean[][][] gridPartTwo;
	private int[][][] neighborsPartTwo;

	public Ac24(boolean[][] grid)
	{
		this.grid = grid;
		this.gridPartTwo = new boolean[201][grid.length][grid[0].length];

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				gridPartTwo[100][y][x] = grid[y][x];
			}
		}

		this.neighbors = new int[grid.length][];
		this.neighborsPartTwo = new int[201][gridPartTwo[0].length][gridPartTwo[0][0].length];

		for (int i = 0; i < neighbors.length; i++)
		{
			neighbors[i] = new int[grid[i].length];
			neighborsPartTwo[i] = new int[grid.length][grid[i].length];
		}
	}

	public static void main(String[] args) throws IOException
	{
		List<String> lines = Files.readAllLines(new File("res/input/24.txt").toPath());
		boolean[][] grid = new boolean[lines.size()][];

		for (int i = 0; i < lines.size(); i++)
		{
			String[] parts = lines.get(i).split("");

			grid[i] = new boolean[parts.length];

			for (int j = 0; j < parts.length; j++)
			{
				grid[i][j] = "#".equals(parts[j]);
			}
		}

		new Ac24(grid).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		Set<String> hashes = new HashSet<>();
		while (true)
		{
			String hash = computeHash();

			if (hashes.contains(hash))
			{
				System.out.println(computeBiodiversity());
				return;
			}
			else
			{
				hashes.add(hash);
			}

			updateNeighbors();
			updateGrid();
		}
	}

	private long computeBiodiversity()
	{
		long result = 0;
		int position = 0;

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (grid[y][x])
					result += Math.pow(2, position);

				position++;
			}
		}

		return result;
	}

	private void updateNeighbors()
	{
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				neighbors[y][x] = getNeighbors(x, y);
			}
		}
	}

	private void updateGrid()
	{
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (grid[y][x])
				{
					grid[y][x] = neighbors[y][x] == 1;
				}
				else
				{
					grid[y][x] = neighbors[y][x] == 1 || neighbors[y][x] == 2;
				}
			}
		}
	}

	private String computeHash()
	{
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				builder.append(grid[y][x] ? '#' : '.');
			}
		}

		return builder.toString();
	}

	private int getNeighbors(int x, int y)
	{
		int neighbors = 0;

		if (x > 0 && grid[y][x - 1])
			neighbors++;
		if (x < grid[y].length - 1 && grid[y][x + 1])
			neighbors++;
		if (y > 0 && grid[y - 1][x])
			neighbors++;
		if (y < grid.length - 1 && grid[y + 1][x])
			neighbors++;

		return neighbors;
	}

	@Override
	protected void solvePartTwo()
	{
		int bugs = 0;
		for (int m = 0; m < 200; m++)
		{
			System.out.println(TaskUtils.toString(gridPartTwo[98]));
			System.out.println(TaskUtils.toString(gridPartTwo[99]));
			System.out.println(TaskUtils.toString(gridPartTwo[100]));
			System.out.println(TaskUtils.toString(gridPartTwo[101]));
			System.out.println(TaskUtils.toString(gridPartTwo[102]));
//			System.out.println(TaskUtils.toString(gridPartTwo[100]));
			updateNeighborsPartTwo();
			bugs = updateGridPartTwo();
			System.out.println(TaskUtils.toStringGrid(neighborsPartTwo[100]));
			System.out.println(bugs);
		}

		System.out.println(bugs);
	}

	private void updateNeighborsPartTwo()
	{
		for (int l = 0; l < gridPartTwo.length; l++)
		{
			for (int y = 0; y < gridPartTwo[l].length; y++)
			{
				for (int x = 0; x < gridPartTwo[l][y].length; x++)
				{
					neighborsPartTwo[l][y][x] = getNeighbors(x, y, l);
				}
			}
		}
	}

	private int updateGridPartTwo()
	{
		int counter = 0;

		for (int l = 0; l < gridPartTwo.length; l++)
		{
			for (int y = 0; y < gridPartTwo[l].length; y++)
			{
				for (int x = 0; x < gridPartTwo[l][y].length; x++)
				{
					if (y == 2 && x == 2)
						continue;

					if (gridPartTwo[l][y][x])
					{
						gridPartTwo[l][y][x] = neighborsPartTwo[l][y][x] == 1;
					}
					else
					{
						gridPartTwo[l][y][x] = neighborsPartTwo[l][y][x] == 1 || neighborsPartTwo[l][y][x] == 2;
					}

					if (gridPartTwo[l][y][x])
						counter++;
				}
			}
		}

		return counter;
	}

	// This is horrendously ugly, but couldn't think of a neater solution just now
	private int getNeighbors(int x, int y, int l)
	{
		int neighbors = 0;

		// Check X

		switch (x)
		{
			// We're on the left edge
			case 0:
				// To the left
				if (l > 0 && gridPartTwo[l - 1][2][1])
					neighbors++;
				// To the right
				if (gridPartTwo[l][y][x + 1])
					neighbors++;
				break;
			case 1:
				if (y != 2)
				{
					if (gridPartTwo[l][y][x + 1])
						neighbors++;
					if (gridPartTwo[l][y][x - 1])
						neighbors++;
				}
				else
				{
					if (gridPartTwo[l][y][x - 1])
						neighbors++;
					if (l < gridPartTwo.length - 1)
					{
						if (gridPartTwo[l + 1][0][0])
							neighbors++;
						if (gridPartTwo[l + 1][1][0])
							neighbors++;
						if (gridPartTwo[l + 1][2][0])
							neighbors++;
						if (gridPartTwo[l + 1][3][0])
							neighbors++;
						if (gridPartTwo[l + 1][4][0])
							neighbors++;
					}
				}
				break;
			case 2:
				if (y != 2)
				{
					if (gridPartTwo[l][y][x + 1])
						neighbors++;
					if (gridPartTwo[l][y][x - 1])
						neighbors++;
				}
				break;
			case 3:
				if (y != 2)
				{
					if (gridPartTwo[l][y][x + 1])
						neighbors++;
					if (gridPartTwo[l][y][x - 1])
						neighbors++;
				}
				else
				{
					if (gridPartTwo[l][y][x + 1])
						neighbors++;
					if (l < gridPartTwo.length - 1)
					{
						if (gridPartTwo[l + 1][0][4])
							neighbors++;
						if (gridPartTwo[l + 1][1][4])
							neighbors++;
						if (gridPartTwo[l + 1][2][4])
							neighbors++;
						if (gridPartTwo[l + 1][3][4])
							neighbors++;
						if (gridPartTwo[l + 1][4][4])
							neighbors++;
					}
				}
				break;
			case 4:
				// To the left
				if (gridPartTwo[l][y][x - 1])
					neighbors++;
				// To the right
				if (l > 0 && gridPartTwo[l - 1][2][3])
					neighbors++;
				break;
		}


		switch (y)
		{
			// We're on the top edge
			case 0:
				// To the top
				if (l > 0 && gridPartTwo[l - 1][1][2])
					neighbors++;
				// To the bottom
				if (gridPartTwo[l][y + 1][x])
					neighbors++;
				break;
			case 1:
				if (x != 2)
				{
					if (gridPartTwo[l][y + 1][x])
						neighbors++;
					if (gridPartTwo[l][y - 1][x])
						neighbors++;
				}
				else
				{
					if (gridPartTwo[l][y - 1][x])
						neighbors++;
					if (l < gridPartTwo.length - 1)
					{
						if (gridPartTwo[l + 1][0][0])
							neighbors++;
						if (gridPartTwo[l + 1][0][1])
							neighbors++;
						if (gridPartTwo[l + 1][0][2])
							neighbors++;
						if (gridPartTwo[l + 1][0][3])
							neighbors++;
						if (gridPartTwo[l + 1][0][4])
							neighbors++;
					}
				}
				break;
			case 2:
				if (x != 2)
				{
					if (gridPartTwo[l][y + 1][x])
						neighbors++;
					if (gridPartTwo[l][y - 1][x])
						neighbors++;
				}
				break;
			case 3:
				if (x != 2)
				{
					if (gridPartTwo[l][y + 1][x])
						neighbors++;
					if (gridPartTwo[l][y - 1][x])
						neighbors++;
				}
				else
				{
					if (gridPartTwo[l][y + 1][x])
						neighbors++;
					if (l < gridPartTwo.length - 1)
					{
						if (gridPartTwo[l + 1][4][0])
							neighbors++;
						if (gridPartTwo[l + 1][4][1])
							neighbors++;
						if (gridPartTwo[l + 1][4][2])
							neighbors++;
						if (gridPartTwo[l + 1][4][3])
							neighbors++;
						if (gridPartTwo[l + 1][4][4])
							neighbors++;
					}
				}
				break;
			case 4:
				// To the top
				if (gridPartTwo[l][y - 1][x])
					neighbors++;
				if (l > 0 && gridPartTwo[l - 1][3][2])
					neighbors++;
				break;
		}

		return neighbors;
	}
}
