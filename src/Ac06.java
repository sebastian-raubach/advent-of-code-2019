import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sebastian Raubach
 */
public class Ac06
{
	private static Map<String, Node> nodes = new HashMap<>();

	public static void main(String[] args)
			throws IOException
	{
		Path input = new File("res/input/06.txt").toPath();

		Files.readAllLines(input)
				.forEach(s -> {
					String[] parts = s.split("\\)");

					Node first = nodes.get(parts[0]);
					if (first == null)
						first = new Node(parts[0]);
					Node second = nodes.get(parts[1]);
					if (second == null)
						second = new Node(parts[1]);

					second.parent = first;

					nodes.put(first.name, first);
					nodes.put(second.name, second);
				});

		System.out.println(solvePartOne());
		System.out.println(solvePartTwo());
	}

	private static int solvePartOne()
	{
		return nodes.values().stream()
				.mapToInt(n -> getDistanceToRoot(n, 0))
				.sum();
	}

	private static int solvePartTwo()
	{
		Node you = nodes.get("YOU");
		Node san = nodes.get("SAN");

		return minimalDistanceBetween(you.parent, san.parent);
	}

	private static int minimalDistanceBetween(Node start, Node end)
	{
		List<String> orderedFromStart = getOrderedParents(start);
		List<String> orderedFromEnd = getOrderedParents(end);

		int min = Integer.MAX_VALUE;
		for (int i = 0; i < orderedFromStart.size(); i++)
		{
			int j = orderedFromEnd.indexOf(orderedFromStart.get(i));

			if (j != -1)
				min = Math.min(i + j + 2, min);
		}

		return min;
	}

	private static List<String> getOrderedParents(Node start)
	{
		List<String> result = new ArrayList<>();

		Node parent = start.parent;

		while (parent != null)
		{
			result.add(parent.name);
			parent = parent.parent;
		}

		return result;
	}

	private static int getDistanceToRoot(Node node, int soFar)
	{
		if (node.parent == null)
			return soFar;
		else
			return getDistanceToRoot(node.parent, soFar + 1);
	}

	private static class Node
	{
		private String name;
		private Node parent;

		public Node(String name)
		{
			this.name = name;
		}
	}
}
