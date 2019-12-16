public abstract class AbstractTask
{
	protected final void run()
	{
		if (test())
		{
			solvePartOne();
			solvePartTwo();
		}
	}

	protected abstract boolean test();

	protected abstract void solvePartOne();

	protected abstract void solvePartTwo();
}
