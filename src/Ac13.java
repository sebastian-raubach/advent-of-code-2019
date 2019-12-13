import javax.swing.*;
import java.awt.*;
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
		SwingUtilities.invokeLater(() -> {
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
			{
				ex.printStackTrace();
			}

			try
			{
				int[][] gameBoard = new int[21][40];
				long[] input = TaskUtils.readAllLongs("res/input/13.txt", ",");
				input[0] = 2;
				IntCode intCode = new IntCode(input);

				Visualization viz = new Visualization(gameBoard);

				JFrame frame = new JFrame("Ac13");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new FlowLayout());
				frame.add(viz);
				frame.pack();
				frame.validate();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

				new Thread(() -> {
					int score = 0;
					int joyStick = 0;
					int paddleX = -1;
					int ballX = -1;
					while (intCode.canContinue())
					{
						int x = (int) intCode.computeOutput(joyStick);
						int y = (int) intCode.computeOutput(joyStick);
						int value = (int) intCode.computeOutput(joyStick);

						if (x == -1 && y == 0)
							score = value;
						else
						{
							viz.updateGrid(x, y, value);
							gameBoard[y][x] = value;
						}

						if (value == 3)
							paddleX = x;
						if (value == 4)
							ballX = x;

						joyStick = Integer.compare(ballX, paddleX);

						try
						{
							Thread.sleep(10);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}

					System.out.println(score);
				}).start();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}

	private static class Visualization extends JPanel
	{
		private int[][] grid;

		public Visualization(int[][] grid) throws HeadlessException
		{
			super();
			this.grid = grid;
		}

		public void updateGrid(int x, int y, int value)
		{
			grid[y][x] = value;
			repaint();
		}

		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(400, 210);
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int width = getWidth() / grid[0].length;
			int height = getHeight() / grid.length;

			for (int y = 0; y < grid.length; y++)
			{
				for (int x = 0; x < grid[y].length; x++)
				{
					switch (grid[y][x])
					{
						case 1:
							drawWall(g2d, x, y, width, height);
							break;
						case 2:
							drawBlock(g2d, x, y, width, height);
							break;
						case 3:
							drawPaddle(g2d, x, y, width, height);
							break;
						case 4:
							drawCircle(g2d, x, y, width, height);
							break;
					}
				}
			}

			g2d.dispose();
		}

		private void drawWall(Graphics2D g, int x, int y, int width, int height)
		{
			g.setColor(Color.BLACK);
			g.fillRect(x * width, y * height, width, height);
		}

		private void drawBlock(Graphics2D g, int x, int y, int width, int height)
		{
			g.setColor(Color.BLUE);
			g.fillRect(x * width, y * height, width, height);
		}

		private void drawPaddle(Graphics2D g, int x, int y, int width, int height)
		{
			g.setColor(Color.GRAY);
			g.fillRect(x * width, y * height, width, height);
		}

		private void drawCircle(Graphics2D g, int x, int y, int width, int height)
		{
			g.setColor(Color.RED);
			g.fillOval(x * width, y * height, width, height);
		}
	}
}
