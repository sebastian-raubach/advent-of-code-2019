import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Sebastian Raubach
 */
public class Ac06 extends AbstractTask
{
	private Map<String, Node> nodes;

	public Ac06(Map<String, Node> nodes)
	{
		this.nodes = nodes;
	}

	public static void main(String[] args)
			throws IOException
	{
		Path input = new File("res/input/06.txt").toPath();

		new Ac06(getMapping(new String(Files.readAllBytes(input)))).run();
	}

	private static Map<String, Node> getMapping(String input)
	{
		Map<String, Node> nodes = new HashMap<>();
		Arrays.stream(input.split("\r\n"))
				.forEach(s ->
				{
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

		return nodes;
	}

	@Override
	protected boolean test()
	{
		try
		{
			assert computeOne(getMapping("COM)B\r\nB)C\r\nC)D\r\nD)E\r\nE)F\r\nB)G\r\nG)H\r\nD)I\r\nE)J\r\nJ)K\r\nK)L")) == 42;
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
		System.out.println(computeOne(nodes));
	}

	private int computeOne(Map<String, Node> nodes)
	{
		return nodes.values().stream()
				.mapToInt(n -> getDistanceToRoot(n, 0))
				.sum();
	}

	@Override
	protected void solvePartTwo()
	{
		Node you = nodes.get("YOU");
		Node san = nodes.get("SAN");

		System.out.println(minimalDistanceBetween(you.parent, san.parent));
	}

	private int minimalDistanceBetween(Node start, Node end)
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

	private List<String> getOrderedParents(Node start)
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

	private int getDistanceToRoot(Node node, int soFar)
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
