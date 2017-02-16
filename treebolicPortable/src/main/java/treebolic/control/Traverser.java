/**
 * Title : Treebolic
 * Description: Treebolic
 * Version: 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 * Update : Jan 6, 2016
 */

package treebolic.control;

import treebolic.control.Controller.MatchMode;
import treebolic.control.Controller.MatchScope;
import treebolic.model.INode;

/**
 * Node finder
 * 
 * @author Bernard Bou
 */
public class Traverser extends Generator<INode>
{
	static public interface Matcher
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

	static public Matcher ALLMATCHER = new AllMatcher();

	static abstract public class SelectiveMatcher implements Matcher
	{
		final String theTarget;

		final MatchScope theScope;

		final MatchMode theMode;

		/**
		 * Constructor
		 * 
		 * @param thisTarget
		 *        target
		 * @param thisScope
		 *        scope
		 * @param thisMode
		 *        mode
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
		 * @param thisTarget
		 *        target
		 * @param thisScope
		 *        scope
		 * @param thisMode
		 *        mode
		 */
		public CaseMatcher(String thisTarget, MatchScope thisScope, MatchMode thisMode)
		{
			super(thisTarget, thisScope, thisMode);
		}

		@Override
		public boolean match(final INode thisNode)
		{
			if (this.theTarget == null || this.theTarget.isEmpty())
				return false;

			String thisNodeScope = null;
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
						return true;
					break;

				case INCLUDES:
					if (thisNodeScope.indexOf(this.theTarget) != -1)
						return true;
					break;

				case STARTSWITH:
				default:
					if (thisNodeScope.startsWith(this.theTarget))
						return true;
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
		 * @param thisTarget
		 *        target
		 * @param thisScope
		 *        scope
		 * @param thisMode
		 *        mode
		 */
		public NoCaseMatcher(String thisTarget, MatchScope thisScope, MatchMode thisMode)
		{
			super(thisTarget.toLowerCase(), thisScope, thisMode);
		}

		@Override
		public boolean match(final INode thisNode)
		{
			if (this.theTarget == null || this.theTarget.isEmpty())
				return false;

			String thisNodeScope = null;
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
				thisNodeScope = thisNodeScope.toLowerCase();
				switch (this.theMode)
				{
				case EQUALS:
					if (thisNodeScope.equals(this.theTarget))
						return true;
					break;

				case INCLUDES:
					if (thisNodeScope.indexOf(this.theTarget) != -1)
						return true;
					break;

				case STARTSWITH:
				default:
					if (thisNodeScope.startsWith(this.theTarget))
						return true;
					break;
				}
			}
			return false;
		}
	}

	/**
	 * Matcher
	 */
	final Matcher theMatcher;

	/**
	 * Node
	 */
	final INode theNode;

	/**
	 * Constructor
	 * 
	 * @param thisNode
	 *        start node
	 */
	public Traverser(final Matcher thisMatcher, final INode thisNode)
	{
		super();
		this.theMatcher = thisMatcher;
		this.theNode = thisNode;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Generator#run()
	 */
	@Override
	protected void run() throws InterruptedException
	{
		traverse(this.theNode);
	}

	private void traverse(final INode thisNode) throws InterruptedException
	{
		// match
		if (this.theMatcher.match(thisNode))
			yield(thisNode);

		// try to match match this node's children
		for (final INode thisChild : thisNode.getChildren())
		{
			traverse(thisChild);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.control.Generator#finalize()
	 */
	@Override
	public void finalize() throws Throwable
	{
		super.finalize();
	}
}
