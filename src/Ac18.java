import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class Ac18 extends AbstractTask
{

	private List<Key> keys;
	private List<Door> doors;
	private char[][] grid;
	private int height = 0;
	private int width = 0;
	public Ac18(char[][] grid)
	{
		this.grid = grid;

		this.height = grid.length;
		this.width = grid[0].length;

		keys = new ArrayList<>();
		doors = new ArrayList<>();

		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				if (Character.isLowerCase(grid[y][x]))
				{
					keys.add(new Key(x, y, grid[y][x]));
				}
				else if (Character.isUpperCase(grid[y][x]))
				{
					doors.add(new Door(x, y, grid[y][x]));
				}
			}
		}
	}

	public static void main(String[] args) throws IOException
	{
		List<String> lines = Files.readAllLines(new File("res/input/18.txt").toPath());

		char[][] grid = new char[lines.size()][];
		for (int i = 0; i < lines.size(); i++)
			grid[i] = lines.get(i).toCharArray();

		new Ac18(grid).run();
	}

	@Override
	protected boolean test()
	{
		return true;
	}

	@Override
	protected void solvePartOne()
	{
		char[][] localGrid = new char[grid.length][];

		for (int i = 0; i < grid.length; i++)
			localGrid[i] = Arrays.copyOf(grid[i], grid[i].length);

		Queue<Position> queue = new LinkedList<>();

		for (int y = 0; y < localGrid.length; y++)
		{
			for (int x = 0; x < localGrid[y].length; x++)
			{
				if (localGrid[y][x] == '@')
					queue.add(new Position(x, y));
			}
		}

		System.out.println(findShortestRoute(localGrid, queue));
	}

	@Override
	protected void solvePartTwo()
	{
		char[][] localGrid = new char[grid.length][];

		for (int i = 0; i < grid.length; i++)
			localGrid[i] = Arrays.copyOf(grid[i], grid[i].length);

		int initialStartX = 0;
		int initialStartY = 0;
		for (int y = 0; y < localGrid.length; y++) {
			for (int x = 0; x < localGrid[y].length; x++) {
				if (localGrid[y][x] == '@') {
					initialStartX = x;
					initialStartY = y;
					break;
				}
			}
		}

		localGrid[initialStartY][initialStartX] = '#';
		localGrid[initialStartY - 1][initialStartX] = '#';
		localGrid[initialStartY - 1][initialStartX + 1] = '@';
		localGrid[initialStartY][initialStartX + 1] = '#';
		localGrid[initialStartY + 1][initialStartX + 1] = '@';
		localGrid[initialStartY + 1][initialStartX] = '#';
		localGrid[initialStartY + 1][initialStartX - 1] = '@';
		localGrid[initialStartY][initialStartX - 1] = '#';
		localGrid[initialStartY - 1][initialStartX - 1] = '@';

		System.out.println(TaskUtils.toString(localGrid));

		LinkedList<Position> queue = new LinkedList<>();

		for (int y = 0; y < localGrid.length; y++)
		{
			for (int x = 0; x < localGrid[y].length; x++)
			{
				if (localGrid[y][x] == '@')
					queue.add(new Position(x, y));
			}
		}

		Collections.shuffle(queue);

		System.out.println(findShortestRoute(localGrid, queue));
	}

	private int findShortestRoute(char[][] grid, Queue<Position> queue) {
		Set<String> visited = new HashSet<>();

		while (queue.size() > 0) {
			Position current = queue.poll();

			List<Character> sortedKeys = new ArrayList<>(current.keys);
			Collections.sort(sortedKeys);
			String key = current.x + " " + current.y + " " + sortedKeys.toString();

			if (visited.contains(key))
				continue;

			visited.add(key);

			if (current.x < 0 || current.x >= width || current.y < 0 || current.y >= height || grid[current.y][current.x] == '#')
				continue;

			char cell = grid[current.y][current.x];

			if (Character.isUpperCase(cell) && !current.keys.contains(Character.toLowerCase(cell)))
				continue;

			Set<Character> newKeys = new HashSet<>(current.keys);

			if (Character.isLowerCase(cell)){
				newKeys.add(cell);

				if (newKeys.size() == keys.size())
				{
					return current.distance;
				}
			}

			queue.add(new Position(current.x + 1, current.y, current.distance + 1, newKeys));
			queue.add(new Position(current.x - 1, current.y, current.distance + 1, newKeys));
			queue.add(new Position(current.x, current.y + 1, current.distance + 1, newKeys));
			queue.add(new Position(current.x, current.y - 1, current.distance + 1, newKeys));
		}

		return -1;
	}

	private static class Position {
		private int x;
		private int y;
		private int distance;
		private Set<Character> keys = new HashSet<>();

		public Position(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public Position(int x, int y, int distance, Set<Character> keys)
		{
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.keys = keys;
		}
	}

	private static class Door {
		private Position position;
		private char name;

		public Door(int x, int y, char name)
		{
			this.position = new Position(x, y);
			this.name = name;
		}
	}

	private static class Key
	{
		private Position position;
		private char name;

		public Key(int x, int y, char name)
		{
			this.position = new Position(x, y);
			this.name = name;
		}
	}
}
