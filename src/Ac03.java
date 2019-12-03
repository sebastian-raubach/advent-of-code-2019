import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sebastian Raubach
 */
public class Ac03
{
	public static void main(String[] args)
		throws IOException
	{
		Path input = new File("res/03.txt").toPath();
		List<Wire> wires = Files.readAllLines(input)
								.stream()
								.map(Wire::create)
								.collect(Collectors.toList());

		solvePartOne(wires);
		solvePartTwo(wires);
	}

	private static void solvePartOne(List<Wire> wires)
	{
		int first = getClosestCrossing(wires.get(0), wires.get(1));
		if (first != 159)
			throw new RuntimeException("Invalid distance");
		else
			System.out.println("First test correct");
		int second = getClosestCrossing(wires.get(2), wires.get(3));
		if (second != 135)
			throw new RuntimeException("Invalid distance");
		else
			System.out.println("Second test correct");
		int third = getClosestCrossing(wires.get(4), wires.get(5));
		System.out.println(third);
	}

	private static void solvePartTwo(List<Wire> wires)
	{
		int first = getShortestCrossing(wires.get(0), wires.get(1));
		if (first != 610)
			throw new RuntimeException("Invalid distance");
		else
			System.out.println("First test correct");
		int second = getShortestCrossing(wires.get(2), wires.get(3));
		if (second != 415)
			throw new RuntimeException("Invalid distance");
		else
			System.out.println("Second test correct");
		int third = getShortestCrossing(wires.get(4), wires.get(5));
		System.out.println(third);
	}

	private static int getShortestCrossing(Wire one, Wire two)
	{
		Set<Point> aPathPoints = getPointsOnPath(one);
		Set<Point> bPathPoints = getPointsOnPath(two);
		int minDistance = Integer.MAX_VALUE;

		int aCounter = 0;
		int bCounter = 0;
		for (Point a : aPathPoints)
		{
			aCounter++;
			for (Point b : bPathPoints)
			{
				bCounter++;
				if (a.x == b.x && a.y == b.y)
					minDistance = Math.min(minDistance, aCounter + bCounter);
			}
			bCounter = 0;
		}

		return minDistance;
	}

	private static int getClosestCrossing(Wire one, Wire two)
	{
		Set<Point> aPathPoints = getPointsOnPath(one);
		Set<Point> bPathPoints = getPointsOnPath(two);
		int minDistance = Integer.MAX_VALUE;

		for (Point a : aPathPoints)
		{
			for (Point b : bPathPoints)
			{
				if (a.x == b.x && a.y == b.y)
					minDistance = Math.min(minDistance, a.getManhattan());
			}
		}

		return minDistance;
	}

	private static Set<Point> getPointsOnPath(Wire wire)
	{
		Set<Point> points = new LinkedHashSet<>();

		Point prev = new Point(0, 0);
		for (Point p : wire)
		{
			if (prev.x == p.x)
			{
				int minY = Math.min(prev.y, p.y);
				int maxY = Math.max(prev.y, p.y);

				for (int y = minY + 1; y <= maxY; y++)
					points.add(new Point(prev.x, y));
			}
			else
			{
				int minX = Math.min(prev.x, p.x);
				int maxX = Math.max(prev.x, p.x);

				for (int x = minX + 1; x <= maxX; x++)
					points.add(new Point(x, prev.y));
			}
			prev = p;
		}

		return points;
	}

	private static class Wire extends ArrayList<Point>
	{
		public static Wire create(String input)
		{
			Wire result = new Wire();

			String[] parts = input.split(",");

			for (int i = 0; i < parts.length; i++)
			{
				int deltaX = 0;
				int deltaY = 0;

				int number = Integer.parseInt(parts[i].substring(1));
				switch (parts[i].charAt(0))
				{
					case 'U':
						deltaY = number;
						break;
					case 'R':
						deltaX = number;
						break;
					case 'D':
						deltaY = -number;
						break;
					case 'L':
						deltaX = -number;
						break;
					default:
						throw new RuntimeException("Invalid path part: " + parts[i]);
				}

				if (i != 0)
				{
					Point prev = result.get(i - 1);
					deltaX += prev.x;
					deltaY += prev.y;
				}

				result.add(new Point(deltaX, deltaY));
			}

			return result;
		}
	}

	private static class Point
	{
		int x;
		int y;

		public Point(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public int getManhattan()
		{
			return Math.abs(x) + Math.abs(y);
		}
	}
}
