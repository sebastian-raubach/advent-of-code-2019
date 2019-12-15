import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sebastian Raubach
 */
public class Ac03 extends AbstractTask
{
	private List<Wire> wires;

	public Ac03(List<Wire> wires)
	{
		this.wires = wires;
	}

	public static void main(String[] args)
			throws IOException
	{
		Path input = new File("res/input/03.txt").toPath();
		List<Wire> wires = Files.readAllLines(input)
				.stream()
				.map(Wire::create)
				.collect(Collectors.toList());

		new Ac03(wires).run();
	}

	@Override
	protected boolean test()
	{
		try
		{
			List<Wire> wires = Arrays.stream("R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83".split("\n"))
					.map(Wire::create)
					.collect(Collectors.toList());
			assert calculateClosestCrossing(wires.get(0), wires.get(1)) == 159;
			assert calculateShortestCrossing(wires.get(0), wires.get(1)) == 610;

			wires = Arrays.stream("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7".split("\n"))
					.map(Wire::create)
					.collect(Collectors.toList());
			assert calculateClosestCrossing(wires.get(0), wires.get(1)) == 135;
			assert calculateShortestCrossing(wires.get(0), wires.get(1)) == 410;
		}
		catch (AssertionError e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	protected void solvePartOne()
	{
		System.out.println(calculateClosestCrossing(wires.get(0), wires.get(0)));
	}

	private static int calculateClosestCrossing(Wire one, Wire two)
	{
		int min = Integer.MAX_VALUE;
		// For each segment on ONE, check each segment on TWO
		for (int a = 1; a < one.size(); a++)
		{
			for (int b = 1; b < two.size(); b++)
			{
				// If they intersect, get the distance of the crossing point
				Point intersection = intersect(one.get(a - 1), one.get(a), two.get(b - 1), two.get(b));

				if (intersection != null)
					min = Math.min(min, intersection.getManhattan());
			}
		}

		return min;
	}

	@Override
	protected void solvePartTwo()
	{
		System.out.println(calculateShortestCrossing(wires.get(0), wires.get(0)));
	}

	private static int calculateShortestCrossing(Wire one, Wire two)
	{
		int min = Integer.MAX_VALUE;
		// Total distance from start to crossing along both wires
		int distance = 0;
		for (int a = 1; a < one.size(); a++)
		{
			// Local distance from the previous starting point to the closest crossing
			int localDistance = distance;

			Point aStart = one.get(a - 1);
			Point aEnd = one.get(a);

			// Check all segments on TWO for this segment of ONE
			for (int b = 1; b < two.size(); b++)
			{
				Point bStart = two.get(b - 1);
				Point bEnd = two.get(b);

				// Check if they intersect
				Point intersection = intersect(aStart, aEnd, bStart, bEnd);

				if (intersection != null)
				{
					// If they do, add the distance from both starting points to the crossing point
					localDistance += aStart.getManhattan(intersection) + bStart.getManhattan(intersection);
					// Check if it's smaller
					min = Math.min(min, localDistance);
					// Then return, because there cannot be a closer point
					break;
				}
				else
				{
					// If they don't, add the distance on TWO
					localDistance += bStart.getManhattan(bEnd);
				}
			}
			// Advance on ONE by one segment, so add the distance and then check again for the next segment
			distance += aStart.getManhattan(aEnd);
		}

		return min;
	}

	private static Point getMin(Point a, Point b)
	{
		return new Point(Math.min(a.x, b.x), Math.min(a.y, b.y));
	}

	private static Point getMax(Point a, Point b)
	{
		return new Point(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}

	private static Point intersect(Point aStart, Point aEnd, Point bStart, Point bEnd)
	{
		Point aMin = getMin(aStart, aEnd);
		Point aMax = getMax(aStart, aEnd);
		Point bMin = getMin(bStart, bEnd);
		Point bMax = getMax(bStart, bEnd);

		// Ignore the crossing at the origin of the very first path segments
		if (aStart.x == 0 && aStart.y == 0 && bStart.x == 0 && bStart.y == 0)
			return null;

		// Check both options where the paths could cross
		if (aMin.x >= bMin.x && aMin.x <= bMax.x && bMin.y >= aMin.y && bMin.y <= aMax.y)
			return new Point(aMin.x, bMin.y);
		else if (bMin.x >= aMin.x && bMin.x <= aMax.x && aMin.y >= bMin.y && aMin.y <= bMax.y)
			return new Point(bMin.x, aMin.y);
		else
			return null;
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

			result.add(0, new Point(0, 0));
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

		public int getManhattan(Point other)
		{
			return Math.abs(x - other.x) + Math.abs(y - other.y);
		}
	}
}
