import java.io.IOException;

public class Ac13
{
	public static void main(String[] args) throws IOException
	{
		solvePartOne();
		solvePartTwo();
	}

	private static void solvePartOne() throws IOException
	{
		int[][] gameBoard = new int[21][40];
		long[] input = TaskUtils.readAllLongs("res/input/13.txt", ",");
		IntCode intCode = new IntCode(input);

		int blocks = 0;

		while (intCode.canContinue())
		{
			int x = (int) intCode.computeOutput();
			int y = (int) intCode.computeOutput();
			int value = (int) intCode.computeOutput();

			gameBoard[y][x] = value;

			if (value == 2)
				blocks++;
		}

		System.out.println(blocks);
	}

	private static void solvePartTwo() throws IOException
	{
		int[][] gameBoard = new int[21][40];
		long[] input = TaskUtils.readAllLongs("res/input/13.txt", ",");
		input[0] = 2;
		IntCode intCode = new IntCode(input);

		int score = 0;
		int joyStick = 0;
		int paddleX = -1;
		int ballX = -1;
		while (intCode.canContinue())
		{
			int x = (int) intCode.computeOutput(joyStick);
			int y = (int) intCode.computeOutput(joyStick);
			int value = (int) intCode.computeOutput(joyStick);

			if (x == 39 && y == 20)
			{
				int a = 0;
			}

			if (x == -1 && y == 0)
				score = value;
			else
				gameBoard[y][x] = value;

			if (value == 3)
				paddleX = x;
			if (value == 4)
				ballX = x;

			joyStick = Integer.compare(ballX, paddleX);

			print(gameBoard);
		}

		System.out.println(score);
	}

	private static void print(int[][] grid)
	{
		for (int y = 0; y < grid.length; y++)
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				switch (grid[y][x])
				{
					case 0:
						System.out.print(' ');
						break;
					case 1:
						System.out.print('█');
						break;
					case 2:
						System.out.print('░');
						break;
					case 3:
						System.out.print('▁');
						break;
					case 4:
						System.out.print('●');
						break;
				}
			}
			System.out.println();
		}

		System.out.println();
		System.out.println();
	}
}
