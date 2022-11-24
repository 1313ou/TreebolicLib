/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.control;

import java.util.List;
import java.util.Locale;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
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
		@SuppressWarnings("WeakerAccess")
		boolean match(final INode node);
	}

	/**
	 * All matcher
	 */
	@SuppressWarnings("WeakerAccess")
	static public class AllMatcher implements Matcher
	{
		@SuppressWarnings("SameReturnValue")
		@Override
		public boolean match(final INode node)
		{
			return true;
		}
	}

	/**
	 * All matcher
	 */
	@NonNull
	static public Matcher ALLMATCHER = new AllMatcher();

	/**
	 * Selective matcher
	 */
	@SuppressWarnings("WeakerAccess")
	static abstract public class SelectiveMatcher implements Matcher
	{
		final String target;

		final MatchScope scope;

		final MatchMode mode;

		/**
		 * Constructor
		 *
		 * @param target target
		 * @param scope  scope
		 * @param mode   mode
		 */
		@SuppressWarnings("WeakerAccess")
		public SelectiveMatcher(final String target, final MatchScope scope, final MatchMode mode)
		{
			this.target = target;
			this.scope = scope;
			this.mode = mode;
		}
	}

	/**
	 * Case-sensitive matcher
	 */
	@SuppressWarnings("WeakerAccess")
	static public class CaseMatcher extends SelectiveMatcher
	{
		/**
		 * Constructor
		 *
		 * @param target target
		 * @param scope  scope
		 * @param mode   mode
		 */
		public CaseMatcher(String target, MatchScope scope, MatchMode mode)
		{
			super(target, scope, mode);
		}

		@Override
		public boolean match(@NonNull final INode node)
		{
			if (this.target == null || this.target.isEmpty())
			{
				return false;
			}

			@Nullable String nodeScope;
			switch (this.scope)
			{
				case CONTENT:
					nodeScope = node.getContent();
					break;

				case LINK:
					nodeScope = node.getLink();
					break;

				case ID:
					nodeScope = node.getId();
					break;

				case LABEL:
				default:
					nodeScope = node.getLabel();
					break;
			}

			// try to match this node
			if (nodeScope != null)
			{
				switch (this.mode)
				{
					case EQUALS:
						if (nodeScope.equals(this.target))
						{
							return true;
						}
						break;

					case INCLUDES:
						if (nodeScope.contains(this.target))
						{
							return true;
						}
						break;

					case STARTSWITH:
					default:
						if (nodeScope.startsWith(this.target))
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
	 * Case-ignore matcher
	 */
	static public class NoCaseMatcher extends SelectiveMatcher
	{
		/**
		 * Constructor
		 *
		 * @param target target
		 * @param scope  scope
		 * @param mode   mode
		 */
		public NoCaseMatcher(@NonNull String target, MatchScope scope, MatchMode mode)
		{
			super(target.toLowerCase(Locale.getDefault()), scope, mode);
		}

		@Override
		public boolean match(@NonNull final INode node)
		{
			if (this.target == null || this.target.isEmpty())
			{
				return false;
			}

			@Nullable String nodeScope;
			switch (this.scope)
			{
				case CONTENT:
					nodeScope = node.getContent();
					break;

				case LINK:
					nodeScope = node.getLink();
					break;

				case ID:
					nodeScope = node.getId();
					break;

				case LABEL:
				default:
					nodeScope = node.getLabel();
					break;
			}

			// try to match this node
			if (nodeScope != null)
			{
				nodeScope = nodeScope.toLowerCase(Locale.getDefault());
				switch (this.mode)
				{
					case EQUALS:
						if (nodeScope.equals(this.target))
						{
							return true;
						}
						break;

					case INCLUDES:
						if (nodeScope.contains(this.target))
						{
							return true;
						}
						break;

					case STARTSWITH:
					default:
						if (nodeScope.startsWith(this.target))
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
	final Matcher matcher;

	/**
	 * Node
	 */
	@SuppressWarnings("WeakerAccess")
	final INode node;

	/**
	 * Constructor
	 *
	 * @param matcher matcher
	 * @param node    start node
	 */
	public Traverser(final Matcher matcher, final INode node)
	{
		super();
		this.matcher = matcher;
		this.node = node;
	}

	@Override
	protected void run() throws InterruptedException
	{
		traverse(this.node);
	}

	private void traverse(@NonNull final INode node) throws InterruptedException
	{
		// match
		if (this.matcher.match(node))
		{
			this.yield(node);
		}

		// try to match match this node's children
		@Nullable final List<INode> children = node.getChildren();
		if (children != null)
		{
			for (@NonNull final INode child : node.getChildren())
			{
				traverse(child);
			}
		}
	}
}
