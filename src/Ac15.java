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

	/**
	 * The IntCode instance
	 */
	private IntCode intCode;
	/**
	 * The labyrinth used as we walk through it
	 */
	private int[][] labyrinth = new int[44][44];
	/**
	 * After part 1, this contains the labyrinth structure, part 2 then fills this with oxygen
	 */
	private int[][] oxygen = new int[44][44];

	public Ac15() throws IOException
	{
		intCode = new IntCode(TaskUtils.readAllLongs("res/input/15.txt", ","));
	}

	private void solve()
	{
		System.out.println(solvePartOne());
		System.out.println(solvePartTwo());
	}

	public static void main(String[] args) throws IOException
	{
		new Ac15().solve();
	}

	private int solvePartOne()
	{
		// Fill the grids
		for (int y = 0; y < labyrinth.length; y++)
		{
			Arrays.fill(labyrinth[y], NOT_VISITED);
			Arrays.fill(oxygen[y], NOT_VISITED);
		}

		// Set the start position
		labyrinth[22][22] = VISITED;

		// Find the shortest path
		return findShortestPath(22, 22);
	}

	private boolean hasOxygen(int x, int y)
	{
		return oxygen[y][x] == VISITED || oxygen[y][x] == GOAL;
	}

	private int solvePartTwo()
	{
		int steps = 0;
		// Remember the new cells to fill in this step
		Set<int[]> toFillNext = new HashSet<>();
		do
		{
			toFillNext.clear();

			// Go through the whole grid
			for (int y = 1; y < oxygen.length - 1; y++)
			{
				for (int x = 1; x < oxygen[y].length - 1; x++)
				{
					// Check if this cell has oxygen
					if (hasOxygen(x, y))
					{
						// Check all neighbors, if they don't have oxygen, add them to the set
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

			// Fill all items in the set with oxygen
			for (int[] position : toFillNext)
				oxygen[position[1]][position[0]] = VISITED;

			// Increase step counter
			steps++;
		} while (toFillNext.size() > 0);

		// Fix the off-by-one-error, because we run one loop at the end that isn't necessary
		return steps - 1;
	}

	private int findShortestPath(int x, int y)
	{
		// From the start position, find the shortest path in all 4 directions
		int minDistance = findShortestPath(x, y, 1, Integer.MAX_VALUE, 0);
		minDistance = Math.min(minDistance, findShortestPath(x, y, 2, minDistance, 0));
		minDistance = Math.min(minDistance, findShortestPath(x, y, 3, minDistance, 0));
		minDistance = Math.min(minDistance, findShortestPath(x, y, 4, minDistance, 0));

		return minDistance;
	}

	private int findShortestPath(int x, int y, int direction, int minDistance, int distanceSoFar)
	{
		// Get the status from the IntCode
		int status = (int) intCode.computeOutput(direction);

		// Set as visited
		labyrinth[y][x] = VISITED;

		switch (status)
		{
			case GOAL:
				// If it's the goal, update the min distance and remember that this cell has oxygen
				minDistance = Math.min(minDistance, distanceSoFar + 1);
				oxygen[y][x] = GOAL;
				break;
			case VISITED:
				// If we've been here before, nothing changes (don't think this case ever happens, because we check for it before calling this methog
				minDistance = distanceSoFar;
				break;
			case WALL:
				// If it's a wall, remember for step 2 and return max int
				minDistance = Integer.MAX_VALUE;
				oxygen[y][x] = WALL;
				break;
			case EMPTY:
				// If we haven't been here before, check all 4 directions and get the minimum from each
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

		// Set it back to unvisited, because there might be abother way to get here that's shorter
		labyrinth[y][x] = NOT_VISITED;

		// If we didn't hit a wall, remember to walk back by calling the IntCode with the opposite direction
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

		// Return the min distance from this grid cell
		return minDistance;
	}

	private void print()
	{
		// Print the spread of oxygen to the console
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
			// Wait a bit to slow down the visual output
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
