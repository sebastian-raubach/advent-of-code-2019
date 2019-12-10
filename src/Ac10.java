import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

		// TODO: Need to calculate numbers between pairs instead!
		Map<Float, Long> map = points.stream()
				.map(Point::toFraction)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		System.out.println(map.values()
				.stream()
				.mapToLong(l -> l)
				.max());
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
			return x / (1f * y);
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
