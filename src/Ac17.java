import java.io.IOException;

public class Ac17
{
	public static void main(String[] args) throws IOException
	{
		long[] input = TaskUtils.readAllLongs("res/input/17.txt", ",");

		IntCode intCode = new IntCode(input);

		char[][] grid = new char[39][45];

		int x = 0;
		int y = 0;
		while (intCode.canContinue())
		{
			long value = intCode.computeOutput();

			if (((int) value) == 10)
			{
				y++;
				x = 0;
			}
			else
			{
				grid[y][x++] = (char) value;
			}
		}

		StringBuilder builder = new StringBuilder();

		int result = 0;
		for (int a = 0; a < grid.length; a++)
		{
			for (int b = 0; b < grid[a].length; b++)
			{
				builder.append(grid[a][b]);

				if (a > 0 && b > 0)
				{
					if (grid[a][b] == '#' && grid[a - 1][b] == '#' && grid[a + 1][b] == '#' && grid[a][b - 1] == '#' && grid[a][b + 1] == '#')
					{
						System.out.println(a + " " + b);
						result += a * b;
					}
				}
			}
			builder.append("\n");
		}

		System.out.println(builder.toString());

		System.out.println(result);
	}
}
