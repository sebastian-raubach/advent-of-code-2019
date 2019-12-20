import java.io.IOException;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class Ac20 extends AbstractTask
{
	private char[][] grid;
	private Map<String, Point> portals = new HashMap<>();
	private Point start;
	private Point end;
	public Ac20(char[][] grid)
	{
		this.grid = grid;
	}

	public static void main(String[] args) throws IOException
	{
		char[][] grid = TaskUtils.readGrid("res/input/20.txt");

		new Ac20(grid).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartTwo()
	{
		parsePortals(true);
		bfs();
	}

	@Override
	protected void solvePartOne()
	{
		parsePortals(false);
		bfs();
	}

	private int bfs() {
		// BFS queue
		Queue<Point> queue = new LinkedList<>();
		// State of visited nodes
		Set<String> visited = new HashSet<>();
		queue.add(start);

		while (!queue.isEmpty())
		{
			Point current = queue.poll();

			// Get the visited portals, we don't want to go through the same one again
			List<String> visitedPortals = new ArrayList<>(current.visitedPortals);
			Collections.sort(visitedPortals);
			// Compute the key, remember to include the level and the portals
			String key = current.x + " " + current.y + " " + current.level + " " + visitedPortals.toString();

			if (visited.contains(key))
				continue;

			visited.add(key);

			// If we found the goal and we're on level 0, return
			if (current.x == end.x && current.y == end.y && current.level == 0)
				return current.distance;

			// Get cell value
			char cell = grid[current.y][current.x];

			// Check if it's within the labyrinth bounds and if it's an empty field
			if (current.x < 2 || current.y < 2 || current.x >= grid[0].length - 2 || current.y >= grid.length - 2 || cell != '.')
				continue;

			// Check if we're next to a portal (this actually returns the target portal / exit)
			Point portal = getPortal(current.x, current.y, current.level);

			// If we're next to a portal and we haven't visited it before
			if (portal != null && !current.visitedPortals.contains((portal.portalName)))
			{
				// Clone the portal exit with a distance of  +1
				Point next = portal.clone(current.distance + 1, current.level);
				next.visitedPortals.add(portal.portalName);
				queue.add(next);
			}
			else
			{
				// Else, check all directions
				queue.add(new Point(current.x - 1, current.y, current.distance + 1, current.level, current.visitedPortals));
				queue.add(new Point(current.x + 1, current.y, current.distance + 1, current.level, current.visitedPortals));
				queue.add(new Point(current.x, current.y - 1, current.distance + 1, current.level, current.visitedPortals));
				queue.add(new Point(current.x, current.y + 1, current.distance + 1, current.level, current.visitedPortals));
			}
		}

		return -1;
	}

	private void parsePortals(boolean recursive)
	{
		for (int y = 0; y < grid.length - 1; y++)
		{
			for (int x = 0; x < grid[y].length - 1; x++)
			{
				// Check if this cell is a letter
				if (Character.isAlphabetic(grid[y][x]))
				{
					// Check if the one to the right is a letter
					if (Character.isAlphabetic(grid[y][x + 1]))
					{
						// If the portal point is to the right
						if (x + 2 < grid[y].length && grid[y][x + 2] == '.')
							addPortal(grid[y][x], grid[y][x + 1], x + 2, y, recursive);
						// Otherwise, check if it's to the left
						else if (grid[y][x - 1] == '.')
							addPortal(grid[y][x], grid[y][x + 1], x - 1, y, recursive);
					}

					// Check if the one below is a letter
					if (Character.isAlphabetic(grid[y + 1][x]))
					{
						// If the portal point is below
						if (y + 2 < grid.length && grid[y + 2][x] == '.')
							addPortal(grid[y][x], grid[y + 1][x], x, y + 2, recursive);
						// Otherwise, check if it's above
						else if (grid[y - 1][x] == '.')
							addPortal(grid[y][x], grid[y + 1][x], x, y - 1, recursive);
					}
				}
			}
		}
	}

	private Point getPortal(int x, int y, int level)
	{
		// Get the key
		String key = x + " " + y;

		// Check if there's a portal here
		Point result = portals.get(key);

		if (result != null)
		{
			// If there is, try and see if we know where the output is
			return portals.values()
					.stream()
					.filter(point ->
					{
						// If we're on level 0 and the portal has a level delta, return false
						if (level == 0 && point.levelDelta > 0)
							return false;
						// Else check if it's got the same name, but different location
						else
							return point.portalName.equals(result.portalName) &&
									point.x != result.x &&
									point.y != result.y;
					})
					.findAny()
					.orElse(null);
		}

		return null;
	}

	private void addPortal(char first, char second, int x, int y, boolean recursive)
	{
		// Get the key
		String p = x + " " + y;

		Point portal = portals.get(p);

		if (portal == null)
			portal = new Point(x, y);

		// If we're on part 2, set the level delta
		if (recursive)
			portal.levelDelta = (x > 3 && x < grid[y].length - 3 && y > 3 && y < grid.length - 3) ? 1 : -1;

		// Set the name
		portal.portalName = first + " " + second;

		// Remember it
		portals.put(p, portal);

		// Check if it's start or finish
		if (first == 'A' && second == 'A')
			this.start = new Point(x, y);
		if (first == 'Z' && second == 'Z')
			this.end = new Point(x, y);
	}

	private static class Point
	{
		int x;
		int y;
		int distance = 0;
		int levelDelta = 0;
		int level = 0;
		Set<String> visitedPortals = new HashSet<>();

		String portalName;

		public Point(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public Point(int x, int y, int distance, int level, Set<String> visitedPortals)
		{
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.level = level;
			this.visitedPortals = visitedPortals;
		}

		public Point clone(int distance, int level)
		{
			Point result = new Point(x, y);
			result.portalName = this.portalName;
			result.distance = distance;
			result.visitedPortals = this.visitedPortals;
			result.level = level + this.levelDelta;
			return result;
		}

		@Override
		public String toString()
		{
			return "Point{" +
					"x=" + x +
					", y=" + y +
					", portalName='" + portalName + '\'' +
					'}';
		}
	}
}
