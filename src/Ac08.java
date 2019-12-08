import java.io.IOException;

public class Ac08
{
	public static void main(String[] args) throws IOException
	{
		int[] input = TaskUtils.readAllInts("res/input/08.txt");

		int width = 25;
		int height = 6;
		int layers = input.length / (width * height);
		int[][][] image = new int[layers][height][width];
		int[][] numberCount = new int[image.length][3];

		int counter = 0;
		// For each layer
		for (int layer = 0; layer < image.length; layer++)
		{
			// Each y position
			for (int y = 0; y < height; y++)
			{
				// Each x position
				for (int x = 0; x < width; x++)
				{
					{
						// Fill in the pixel
						image[layer][y][x] = input[counter++];
						// And count the digit count per layer
						numberCount[layer][image[layer][y][x]]++;
					}
				}
			}
		}

		// Determine min zero-count and one-count * two-count
		int zeroCount = Integer.MAX_VALUE;
		int multiplication = 0;
		for (int layer = 0; layer < numberCount.length; layer++)
		{
			if (numberCount[layer][0] < zeroCount)
			{
				zeroCount = numberCount[layer][0];
				multiplication = numberCount[layer][1] * numberCount[layer][2];
			}
		}

		// Output part one
		System.out.println(multiplication);


		// Generate image
		char[][] resultingImage = new char[height][width];

		// For each pixel
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				// Go through all layers and get the first non-transparent pixel
				for (int layer = 0; layer < image.length; layer++)
				{
					if (image[layer][y][x] != 2)
					{
						resultingImage[y][x] = (char) (image[layer][y][x] + '0');
						break;
					}
				}

				// Output them using unicode block characters
				if (resultingImage[y][x] == '0')
					System.out.print('█');
				else
					System.out.print('░');
			}
			System.out.println();
		}
	}
}
