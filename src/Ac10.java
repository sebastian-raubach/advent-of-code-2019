import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Ac10
{
	private static List<Point> points = new ArrayList<>();

	public static void main(String[] args) throws IOException
	{
		readInput();
		Point station = solvePartOne();
		solvePartTwo(station);
	}

	private static void readInput() throws IOException
	{
		List<String> rows = Files.readAllLines(new File("res/input/10.txt").toPath());
		for (int i = 0; i < rows.size(); i++)
		{
			String[] parts = rows.get(i).split("");

			for (int j = 0; j < parts.length; j++)
			{
				if (parts[j].equals("#"))
					points.add(new Point(j, i));
			}
		}
	}

	private static Point solvePartOne()
	{
		Point maxPoint = null;
		int maxStations = 0;

		for (int i = 0; i < points.size(); i++)
		{
			Point p = points.get(i);
			Set<Double> angles = new HashSet<>();
			points.stream()
					.filter(q -> !(p.x == q.x && p.y == q.y))
					.forEach(q -> {
						// For each point that isn't p, calculate the angle from p
						Point delta = p.delta(q);
						angles.add(delta.toFraction());
					});

			// Count the number of distinct angles
			if (angles.size() > maxStations)
			{
				maxStations = angles.size();
				maxPoint = p;
			}
		}

		System.out.println(maxStations);

		return maxPoint;
	}

	private static void solvePartTwo(Point baseStation)
	{
		// Calculate angle and distance from base
		points.forEach(p -> p.toBase(baseStation));

		// Sort them by angle
		List<Point> targets = points.stream()
				.filter(p -> !(p.x == baseStation.x && p.y == baseStation.y))
				.sorted((a, b) -> (int) Math.signum(a.angleToBase - b.angleToBase))
				.collect(Collectors.toList());

		// Get all angles sorted
		List<Double> targetDegrees = targets.stream()
				.map(p -> p.angleToBase)
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		// Find the index of angle -90
		int currentIndex = findClosestAngleTo(targetDegrees, -90);
		AtomicInteger counter = new AtomicInteger(0);

		// While targets are still left
		while (targets.size() > 0)
		{
			// Get the new angle
			Double degree = targetDegrees.get(currentIndex);
			targets.stream()
					.filter(t -> t.angleToBase == degree) // Get all targets with that angle
					.min((a, b) -> (int) Math.signum(a.distanceToBase - b.distanceToBase)) // Get the closest one
					.ifPresent(f -> {
						// Blast it
						targets.remove(f);

						// Increment counter and check if it's 200
						int c = counter.incrementAndGet();
						if (c == 200)
							System.out.println(f.x * 100 + f.y);
					});

			// Go to the next degree
			currentIndex = (currentIndex + 1) % targetDegrees.size();
		}
	}

	private static int findClosestAngleTo(List<Double> degrees, int target)
	{
		for (int i = 0; i < degrees.size(); i++)
		{
			if (degrees.get(i) >= target)
				return i;
		}

		return -1;
	}

	private static class Point
	{
		int x;
		int y;
		double angleToBase;
		double distanceToBase;

		public Point(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public void toBase(Point base)
		{
			angleToBase = Math.atan2(y - base.y, x - base.x) * (180 / Math.PI);
			distanceToBase = Math.hypot(base.x - x, base.y - y);
		}

		public Double toFraction()
		{
			return Math.atan2(y, x);
		}

		public Point delta(Point other)
		{
			return new Point(other.x - x, other.y - y);
		}
	}
}
