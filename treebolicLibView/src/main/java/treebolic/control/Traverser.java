package treebolic.control;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import treebolic.model.INode;
import treebolic.model.Types.MatchMode;
import treebolic.model.Types.MatchScope;

/**
 * Node finder
 *
 * @author Bernard Bou
 */
public class Traverser extends Generator<INode>
{
	interface Matcher
	{
		boolean match(final INode thisNode);
	}

	static public class AllMatcher implements Matcher
	{
		@Override
		public boolean match(final INode thisNode)
		{
			return true;
		}
	}

	@NonNull
	static public Matcher ALLMATCHER = new AllMatcher();

	static abstract public class SelectiveMatcher implements Matcher
	{
		final String theTarget;

		final MatchScope theScope;

		final MatchMode theMode;

		/**
		 * Constructor
		 *
		 * @param thisTarget target
		 * @param thisScope  scope
		 * @param thisMode   mode
		 */
		public SelectiveMatcher(final String thisTarget, final MatchScope thisScope, final MatchMode thisMode)
		{
			this.theTarget = thisTarget;
			this.theScope = thisScope;
			this.theMode = thisMode;
		}
	}

	static public class CaseMatcher extends SelectiveMatcher
	{
		/**
		 * Constructor
		 *
		 * @param thisTarget target
		 * @param thisScope  scope
		 * @param thisMode   mode
		 */
		public CaseMatcher(String thisTarget, MatchScope thisScope, MatchMode thisMode)
		{
			super(thisTarget, thisScope, thisMode);
		}

		@Override
		public boolean match(@NonNull final INode thisNode)
		{
			if (this.theTarget == null || this.theTarget.isEmpty())
			{
				return false;
			}

			String thisNodeScope;
			switch (this.theScope)
			{
				case CONTENT:
					thisNodeScope = thisNode.getContent();
					break;

				case LINK:
					thisNodeScope = thisNode.getLink();
					break;

				case ID:
					thisNodeScope = thisNode.getId();
					break;

				case LABEL:
				default:
					thisNodeScope = thisNode.getLabel();
					break;
			}

			// try to match this node
			if (thisNodeScope != null)
			{
				switch (this.theMode)
				{
					case EQUALS:
						if (thisNodeScope.equals(this.theTarget))
						{
							return true;
						}
						break;

					case INCLUDES:
						if (thisNodeScope.contains(this.theTarget))
						{
							return true;
						}
						break;

					case STARTSWITH:
					default:
						if (thisNodeScope.startsWith(this.theTarget))
						{
							return true;
						}
						break;
				}
			}
			return false;
		}
	}

	static public class NoCaseMatcher extends SelectiveMatcher
	{
		/**
		 * Constructor
		 *
		 * @param thisTarget target
		 * @param thisScope  scope
		 * @param thisMode   mode
		 */
		public NoCaseMatcher(@NonNull String thisTarget, MatchScope thisScope, MatchMode thisMode)
		{
			super(thisTarget.toLowerCase(Locale.getDefault()), thisScope, thisMode);
		}

		@Override
		public boolean match(@NonNull final INode thisNode)
		{
			if (this.theTarget == null || this.theTarget.isEmpty())
			{
				return false;
			}

			String thisNodeScope;
			switch (this.theScope)
			{
				case CONTENT:
					thisNodeScope = thisNode.getContent();
					break;

				case LINK:
					thisNodeScope = thisNode.getLink();
					break;

				case ID:
					thisNodeScope = thisNode.getId();
					break;

				case LABEL:
				default:
					thisNodeScope = thisNode.getLabel();
					break;
			}

			// try to match this node
			if (thisNodeScope != null)
			{
				thisNodeScope = thisNodeScope.toLowerCase(Locale.getDefault());
				switch (this.theMode)
				{
					case EQUALS:
						if (thisNodeScope.equals(this.theTarget))
						{
							return true;
						}
						break;

					case INCLUDES:
						if (thisNodeScope.contains(this.theTarget))
						{
							return true;
						}
						break;

					case STARTSWITH:
					default:
						if (thisNodeScope.startsWith(this.theTarget))
						{
							return true;
						}
						break;
				}
			}
			return false;
		}
	}

	/**
	 * Matcher
	 */
	@SuppressWarnings("WeakerAccess")
	final Matcher theMatcher;

	/**
	 * Node
	 */
	@SuppressWarnings("WeakerAccess")
	final INode theNode;

	/**
	 * Constructor
	 *
	 * @param thisNode start node
	 */
	public Traverser(final Matcher thisMatcher, final INode thisNode)
	{
		super();
		this.theMatcher = thisMatcher;
		this.theNode = thisNode;
	}

	@Override
	protected void run() throws InterruptedException
	{
		traverse(this.theNode);
	}

	private void traverse(@NonNull final INode thisNode) throws InterruptedException
	{
		// match
		if (this.theMatcher.match(thisNode))
		{
			yield(thisNode);
		}

		// try to match match this node's children
		final List<INode> theseChildren = thisNode.getChildren();
		if (theseChildren != null)
		{
			for (final INode thisChild : thisNode.getChildren())
			{
				traverse(thisChild);
			}
		}
	}

	@Override
	public void finalize() throws Throwable
	{
		super.finalize();
	}
}
