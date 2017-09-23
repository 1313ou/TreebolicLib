package treebolic.control;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class allows specifying Python generator-like sequences. For examples, see the JUnit test case. The implementation uses a separate Thread to produce the
 * sequence items. This is certainly not as fast as eg. a for-loop, but not horribly slow either. On a machine with a dual core i5 CPU @ 2.67 GHz, 1000 items
 * can be produced in &lt; 0.03s. By overriding finalize(), the class takes care not to leave any Threads running longer than necessary.
 *
 * @param <T> type of objects
 * @author Herrmann
 */
@SuppressWarnings("WeakerAccess")
public abstract class Generator<T> implements Iterable<T>
{
	/**
	 * Condition
	 */
	private class Condition
	{
		private boolean isSet;

		public synchronized void set()
		{
			this.isSet = true;
			notify();
		}

		public synchronized void await() throws InterruptedException
		{
			try
			{
				if (this.isSet)
				{
					return;
				}
				wait();
			}
			finally
			{
				this.isSet = false;
			}
		}
	}

	@SuppressWarnings("WeakerAccess")
	static ThreadGroup THREAD_GROUP;

	@SuppressWarnings("WeakerAccess")
	Thread producer;

	private boolean hasFinished;

	/**
	 * Object to wait on : item available or producer has finished
	 */
	@SuppressWarnings("synthetic-access")
	private final Condition itemAvailableOrHasFinished = new Condition();

	/**
	 * Object to wait on : item requested
	 */
	@SuppressWarnings("synthetic-access")
	private final Condition itemRequested = new Condition();

	private T nextItem;

	private boolean nextItemAvailable;

	private RuntimeException exceptionRaisedByProducer;

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return waitForNext();
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public T next()
			{
				if (!waitForNext())
				{
					throw new NoSuchElementException();
				}
				Generator.this.nextItemAvailable = false;
				return Generator.this.nextItem;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}

			@SuppressWarnings("synthetic-access")
			private boolean waitForNext()
			{
				if (Generator.this.nextItemAvailable)
				{
					return true;
				}
				if (Generator.this.hasFinished)
				{
					return false;
				}
				if (Generator.this.producer == null)
				{
					startProducer();
				}
				Generator.this.itemRequested.set();
				try
				{
					Generator.this.itemAvailableOrHasFinished.await();
				}
				catch (InterruptedException e)
				{
					Generator.this.hasFinished = true;
				}
				if (Generator.this.exceptionRaisedByProducer != null)
				{
					throw Generator.this.exceptionRaisedByProducer;
				}
				return !Generator.this.hasFinished;
			}
		};
	}

	/**
	 * Run generator. Each element is generated with a yield
	 *
	 * @throws InterruptedException interrupted exception
	 */
	protected abstract void run() throws InterruptedException;

	/**
	 * Yield element
	 *
	 * @param element element
	 * @throws InterruptedException interrupted exception
	 */
	@SuppressWarnings("WeakerAccess")
	protected void yield(T element) throws InterruptedException
	{
		this.nextItem = element;
		this.nextItemAvailable = true;
		this.itemAvailableOrHasFinished.set();
		this.itemRequested.await();
	}

	/**
	 * Start producer thread
	 */
	private void startProducer()
	{
		assert this.producer == null;

		// thread group
		if (THREAD_GROUP == null)
		{
			THREAD_GROUP = new ThreadGroup("generatorfunctions");
		}

		// new thread definition
		this.producer = new Thread(THREAD_GROUP, new Runnable()
		{
			@SuppressWarnings("synthetic-access")
			@Override
			public void run()
			{
				try
				{
					// wait for request before starting generation
					Generator.this.itemRequested.await();

					// generate
					Generator.this.run();
				}
				catch (InterruptedException e)
				{
					// No need to do anything here
					// Remaining steps in run() will cleanly shut down the thread.
				}
				catch (RuntimeException e)
				{
					Generator.this.exceptionRaisedByProducer = e;
				}

				// generation has terminated
				Generator.this.hasFinished = true;

				// signal finish
				Generator.this.itemAvailableOrHasFinished.set();

				// System.out.println("FINISHED");
			}
		});

		// start
		this.producer.setDaemon(true);
		this.producer.start();
	}

	@Override
	protected void finalize() throws Throwable
	{
		terminate();
		super.finalize();
	}

	/**
	 * Terminate generator
	 *
	 * @throws InterruptedException interrupted exception
	 */
	public void terminate() throws InterruptedException
	{
		if (this.producer.isAlive())
		{
			if (!this.producer.isInterrupted())
			{
				this.producer.interrupt();
				// System.out.println("INTERRUPTED");
			}
			this.producer.join();
			// System.out.println("DIED");
		}
	}
}
