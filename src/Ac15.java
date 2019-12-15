import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Ac15
{
	private static final int NOT_VISITED = -1;
	private static final int VISITED = 3;
	private static final int WALL = 0;
	private static final int EMPTY = 1;
	private static final int GOAL = 2;

	private IntCode intCode;
	private int[][] labyrinth = new int[44][44];
	private int[][] oxygen = new int[44][44];

	public Ac15() throws IOException
	{
		intCode = new IntCode(TaskUtils.readAllLongs("res/input/15.txt", ","));
	}

	public static void main(String[] args) throws IOException
	{
		new Ac15().solve();
	}

	private void solve()
	{
		System.out.println(solvePartOne());
		System.out.println(solvePartTwo());
	}

	private int solvePartOne()
	{
		for (int y = 0; y < labyrinth.length; y++)
		{
			Arrays.fill(labyrinth[y], NOT_VISITED);
			Arrays.fill(oxygen[y], NOT_VISITED);
		}

		labyrinth[22][22] = VISITED; // Visited

		return findShortestPath(22, 22);
	}

	private int solvePartTwo()
	{
		int steps = 0;
		Set<int[]> toFillNext = new HashSet<>();
		do
		{
			toFillNext.clear();
			for (int y = 1; y < oxygen.length - 1; y++)
			{
				for (int x = 1; x < oxygen[y].length - 1; x++)
				{
					if (hasOxygen(x, y))
					{
						if (oxygen[y][x + 1] == NOT_VISITED)
							toFillNext.add(new int[]{x + 1, y});
						if (oxygen[y][x - 1] == NOT_VISITED)
							toFillNext.add(new int[]{x - 1, y});
						if (oxygen[y - 1][x] == NOT_VISITED)
							toFillNext.add(new int[]{x, y - 1});
						if (oxygen[y + 1][x] == NOT_VISITED)
							toFillNext.add(new int[]{x, y + 1});
					}
				}
			}

//			print();

			for (int[] position : toFillNext)
				oxygen[position[1]][position[0]] = VISITED;

			steps++;
		} while (toFillNext.size() > 0);

		return steps - 1;
	}

	private boolean hasOxygen(int x, int y)
	{
		return oxygen[y][x] == VISITED || oxygen[y][x] == GOAL;
	}

	private int findShortestPath(int x, int y)
	{
		int minDistance = findShortestPath(x, y, 1, Integer.MAX_VALUE, 0);
		minDistance = Math.min(minDistance, findShortestPath(x, y, 2, minDistance, 0));
		minDistance = Math.min(minDistance, findShortestPath(x, y, 3, minDistance, 0));
		minDistance = Math.min(minDistance, findShortestPath(x, y, 4, minDistance, 0));

		print();

		return minDistance;
	}

	private int findShortestPath(int x, int y, int direction, int minDistance, int distanceSoFar)
	{
		int status = (int) intCode.computeOutput(direction);

		labyrinth[y][x] = VISITED;

		switch (status)
		{
			case GOAL:
				minDistance = Math.min(minDistance, distanceSoFar + 1);
				oxygen[y][x] = GOAL;
				break;
			case VISITED:
				minDistance = distanceSoFar;
				break;
			case WALL:
				minDistance = Integer.MAX_VALUE;
				oxygen[y][x] = WALL;
				break;
			case EMPTY:
				if (labyrinth[y - 1][x] == NOT_VISITED)
					minDistance = Math.min(minDistance, findShortestPath(x, y - 1, 1, minDistance, distanceSoFar + 1));
				if (labyrinth[y + 1][x] == NOT_VISITED)
					minDistance = Math.min(minDistance, findShortestPath(x, y + 1, 2, minDistance, distanceSoFar + 1));
				if (labyrinth[y][x - 1] == NOT_VISITED)
					minDistance = Math.min(minDistance, findShortestPath(x - 1, y, 3, minDistance, distanceSoFar + 1));
				if (labyrinth[y][x + 1] == NOT_VISITED)
					minDistance = Math.min(minDistance, findShortestPath(x + 1, y, 4, minDistance, distanceSoFar + 1));
				break;
		}

		// Set it back
		labyrinth[y][x] = NOT_VISITED;

		if (status != WALL)
		{
			switch (direction)
			{
				case 1:
					intCode.computeOutput(2);
					break;
				case 2:
					intCode.computeOutput(1);
					break;
				case 3:
					intCode.computeOutput(4);
					break;
				case 4:
					intCode.computeOutput(3);
					break;
			}
		}

		return minDistance;
	}

	private void print()
	{
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < oxygen.length; y++)
		{
			for (int x = 0; x < oxygen[y].length; x++)
			{
				switch (oxygen[y][x])
				{
					case NOT_VISITED:
						builder.append(' ');
						break;
					case WALL:
						builder.append('█');
						break;
					case VISITED:
						builder.append('░');
						break;
					case GOAL:
						builder.append('▓');
						break;
				}
			}
			builder.append('\n');
		}

		System.out.println(builder.toString());

		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
