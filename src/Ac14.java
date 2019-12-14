import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ac14
{
	private Map<String, Reaction> reactions;
	private Map<String, Long> inStorage;

	public Ac14(Map<String, Reaction> reactions)
	{
		this.reactions = reactions;
		this.inStorage = new HashMap<>();
	}

	public static void main(String[] args) throws IOException
	{
		// Map material name to it's reaction formula
		Map<String, Reaction> reactions = Files.readAllLines(new File("res/input/14.txt").toPath())
				.stream()
				.map(Reaction::parse)
				.collect(Collectors.toMap(Reaction::getOutputMaterialName, Function.identity()));

		System.out.println(new Ac14(reactions).solvePartOne());
		System.out.println(new Ac14(reactions).solvePartTwo());
	}

	public long solvePartOne()
	{
		return produce(new Material("FUEL", 1L));
	}

	public long solvePartTwo()
	{
		long lower = 0L;
		long upper = 1L;
		long maxInput = 1000000000000L;

		while (produce(new Material("FUEL", upper)) <= maxInput)
		{
			upper *= 10;
		}

		// Binary search this.
		while (lower < upper)
		{
			long mid = (lower + upper) / 2;

			long produce = produce(new Material("FUEL", mid));

			if (produce < maxInput)
			{
				lower = mid + 1;
			}
			else if (produce > maxInput)
			{
				upper = mid - 1;
			}
		}

		return lower;
	}

	private long produce(Material material)
	{
		long ore = 0L;
		// Get the reaction that produces this
		Reaction reaction = reactions.get(material.material);
		// Calculate how often we need to run this equation to get the required output
		long multiplier = (long) Math.ceil(material.quantity / (1f * reaction.output.quantity));

		// For each of the inputs
		for (Material input : reaction.input)
		{
			// If it's ORE, sum it up
			if (input.material.equals("ORE"))
			{
				ore += multiplier * input.quantity;
			}
			else
			{
				// If it isn't check how much we have in inStorage
				Long storage = inStorage.get(input.material);
				if (storage == null)
					storage = 0L;
				inStorage.put(input.material, storage);

				// If there's less than we need, check how much it costs to get it
				if (storage < multiplier * input.quantity)
				{
					ore += produce(new Material(input.material, multiplier * input.quantity - storage));
				}

				// Substract what we used from storage
				storage = inStorage.get(input.material);
				storage -= multiplier * input.quantity;
				inStorage.put(input.material, storage);
			}
		}

		// Store the output amount
		Long output = inStorage.get(reaction.output.material);
		if (output == null)
			output = 0L;
		output += multiplier * reaction.output.quantity;
		inStorage.put(reaction.output.material, output);

		// Return how much ORE it cost to get here
		return ore;
	}


	private static class Reaction
	{
		private Set<Material> input;
		private Material output;

		private static Reaction parse(String input)
		{
			String[] parts = input.split("=>");

			String[] inputs = parts[0].trim().split(", ");

			Reaction result = new Reaction();
			result.input = Arrays.stream(inputs)
					.map(Material::parse)
					.collect(Collectors.toSet());
			result.output = Material.parse(parts[1]);

			return result;
		}

		public String getOutputMaterialName()
		{
			return output.material;
		}

		@Override
		public String toString()
		{
			return "Reaction{" +
					"input=" + input +
					", output=" + output +
					'}';
		}
	}

	private static class Material
	{
		private String material;
		private long quantity;

		public Material(String material, long quantity)
		{
			this.material = material;
			this.quantity = quantity;
		}

		private static Material parse(String input)
		{
			input = input.trim();

			String[] parts = input.split(" ");
			return new Material(parts[1], Long.parseLong(parts[0]));
		}

		@Override
		public String toString()
		{
			return "Material{" +
					"material='" + material + '\'' +
					", quantity=" + quantity +
					'}';
		}
	}
}
