import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ac10
{
	public static void main(String[] args) throws IOException
	{
		List<Point> points = new ArrayList<>();

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

		System.out.println(points.size());

		Point maxPoint = null;
		int maxStations = 0;
		for (int i = 0; i < points.size(); i++)
		{
			Point p = points.get(i);
			Set<Float> fractions = new HashSet<>();
			points.stream()
					.filter(q -> !(p.x == q.x && p.y == q.y))
					.forEach(q -> {
						Point delta = p.delta(q);
						fractions.add(delta.toFraction());
					});

			if (fractions.size() > maxStations)
			{
				maxStations = fractions.size();
				maxPoint = p;
			}
		}
		;

		System.out.println(maxPoint + " -> " + maxStations);
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

		public Float toFraction()
		{
			return (float) Math.atan2(x, y);
		}

		public Point delta(Point other)
		{
			return new Point(other.x - x, other.y - y);
		}

		@Override
		public String toString()
		{
			return "Point{" +
					"x=" + x +
					", y=" + y +
					'}';
		}
	}
}
