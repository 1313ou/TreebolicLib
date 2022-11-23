/*
 * Copyright (c) 2019-2022. Bernard Bou
 */

package treebolic.component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.control.Controller;
import treebolic.glue.ActionListener;
import treebolic.model.INode;
import treebolic.model.MenuItem;
import treebolic.model.MenuItem.Action;
import treebolic.model.MountPoint;
import treebolic.model.Settings;
import treebolic.view.View;

/**
 * Popup context menu
 *
 * @author Bernard Bou
 */
public class PopupMenu extends treebolic.glue.component.PopupMenu
{
	/**
	 * Constructor
	 *
	 * @param view view
	 */
	@SuppressWarnings("WeakerAccess")
	public PopupMenu(@NonNull final View view)
	{
		super(view);
	}

	/**
	 * Make popup menu
	 *
	 * @param view       view
	 * @param controller controller
	 * @param value      input value
	 * @param node       target node
	 * @param settings   settings
	 * @return popup menu
	 */
	@NonNull
	static public PopupMenu makePopup(@NonNull final View view, @NonNull final Controller controller, final String value, @NonNull final INode node, @NonNull final Settings settings)
	{
		@NonNull final PopupMenu popupMenu = new PopupMenu(view);

		// info
		popupMenu.addItem(LabelIndices.LABEL_INFO.ordinal(), ImageIndices.IMAGE_INFO.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				controller.dispatch(Action.INFO, null, null, null, null, null, node);
				return true;
			}
		});

		// focus
		popupMenu.addItem(LabelIndices.LABEL_FOCUS.ordinal(), ImageIndices.IMAGE_FOCUS.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				controller.dispatch(Action.FOCUS, null, null, null, null, null, node);
				return true;
			}
		});

		// mount
		@Nullable final MountPoint mountPoint = node.getMountPoint();
		if (mountPoint != null)
		{
			@SuppressWarnings("InstanceofConcreteClass") final boolean isMounted = mountPoint instanceof MountPoint.Mounted;
			popupMenu.addItem(isMounted ? LabelIndices.LABEL_UNMOUNT.ordinal() : LabelIndices.LABEL_MOUNT.ordinal(), ImageIndices.IMAGE_MOUNT.ordinal(), new ActionListener()
			{
				@SuppressWarnings("SameReturnValue")
				@Override
				public boolean onAction(final Object... params)
				{
					controller.dispatch(Action.MOUNT, null, null, null, null, null, node);
					return true;
				}
			});
		}

		// link
		if (PopupMenu.isURL(node.getLink()))
		{
			popupMenu.addItem(LabelIndices.LABEL_LINKTO.ordinal(), ImageIndices.IMAGE_LINK.ordinal(), new ActionListener()
			{
				@SuppressWarnings("SameReturnValue")
				@Override
				public boolean onAction(final Object... params)
				{
					controller.dispatch(Action.LINK, null, null, null, null, null, node);
					return true;
				}
			});
		}

		// custom
		if (settings.menu != null)
		{
			for (@NonNull final MenuItem menuItem : settings.menu)
			{
				@Nullable String menuLabel = null;
				boolean prepend = menuItem.label != null && (menuItem.label.length() == 0 || Character.isLowerCase(menuItem.label.charAt(0)));
				assert menuItem.action != null;
				switch (menuItem.action)
				{
					case GOTO:
						// check
						if (controller.getGotoTarget(menuItem.link, node) == null)
						{
							// illegal combination
							continue;
						}
						menuLabel = prepend ? LabelIndices.LABEL_GOTO.ordinal() + ' ' + menuItem.label : menuItem.label;
						break;

					case SEARCH:
						if (controller.getSearchTarget(menuItem.target, node) == null)
						{
							// illegal combination
							continue;
						}
						menuLabel = prepend ? LabelIndices.LABEL_SEARCH.ordinal() + ' ' + menuItem.label : menuItem.label;
						break;

					default:
						break;
				}

				try
				{
					menuLabel = PopupMenu.expandMacro(menuLabel, value, node);

					// no item without label
					if (menuLabel == null)
					{
						continue;
					}

					// assume ImageIndices.ordinal() = control.Action.ordinal()
					popupMenu.addItem(menuLabel, menuItem.action.ordinal(), new ActionListener()
					{
						@SuppressWarnings("SameReturnValue")
						@Override
						public boolean onAction(final Object... params)
						{
							controller.dispatch(menuItem.action, menuItem.link, menuItem.target, menuItem.matchTarget, menuItem.matchScope, menuItem.matchMode, node);
							return true;
						}
					});
				}
				catch (@NonNull final ArrayIndexOutOfBoundsException ignored)
				{
					// do nothing
				}
			}
		}

		// cancel
		popupMenu.addItem(LabelIndices.LABEL_CANCEL.ordinal(), ImageIndices.IMAGE_CANCEL.ordinal(), new ActionListener()
		{
			@SuppressWarnings("SameReturnValue")
			@Override
			public boolean onAction(final Object... params)
			{
				return false;
			}
		});

		return popupMenu;
	}

	// U R L . V A L I D A T I O N

	static private boolean isURL(@Nullable final String link)
	{
		if (link != null && !link.isEmpty())
		{
			// well-formed URL
			try
			{
				/* URL url = */
				new URL(link);
				return true;
			}
			catch (@NonNull final MalformedURLException ignored)
			{
				// well-formed URI
				try
				{
					@NonNull final URI uri = new URI(link);

					// relative form not including scheme
					if (link.equals(uri.getPath()))
					{
						return true;
					}

					// fragment
					@NonNull final String fragment = '#' + uri.getFragment();
					//noinspection SimplifiableIfStatement
					if (link.equals(fragment))
					{
						return true;
					}

					// desperate attempt
					return uri.getScheme().matches("[a-z]*");
				}
				catch (@NonNull final URISyntaxException ignored2)
				{
					//
				}
			}
		}
		return false;
	}

	// S T R I N G . E X P A N D

	/**
	 * Expand string
	 *
	 * @param str   string to expand
	 * @param value interactively supplied value
	 * @param node  node node
	 * @return expanded string
	 */
	@Nullable
	static public String expandMacro(@Nullable final CharSequence str, @Nullable final String value, @NonNull final INode node)
	{
		if (str == null)
		{
			return null;
		}

		@NonNull final StringBuilder sb = new StringBuilder();
		final int n = str.length();
		for (int i = 0; i < n; i++)
		{
			final char c = str.charAt(i);
			if (c != '$')
			{
				sb.append(c);
			}
			else
			{
				if (i < n - 1)
				{
					// c is not last : peek at next
					final char c2 = str.charAt(++i);
					switch (c2)
					{

						// label
						case 'l':
							@Nullable final String label = node.getLabel();
							if (label != null)
							{
								sb.append(label.toCharArray());
							}
							break;

						// content
						case 'c':
							@Nullable final String content = node.getContent();
							if (content != null)
							{
								sb.append(content.toCharArray());
							}
							break;

						// link url
						case 'u':
							@Nullable final String link = node.getLink();
							if (link != null)
							{
								sb.append(link.toCharArray());
							}
							break;

						// id
						case 'i':
							@Nullable final String id = node.getId();
							if (id != null)
							{
								sb.append(id.toCharArray());
							}
							break;

						// parent
						case 'p':
							@Nullable final INode parent = node.getParent();
							if (parent != null)
							{
								@Nullable final String parentId = parent.getId();
								if (parentId != null)
								{
									sb.append(parentId.toCharArray());
								}
							}
							break;

						// element value
						case 'e':
							if (value != null)
							{
								sb.append(value.toCharArray());
							}
							break;

						// escaped $
						case '$':
							sb.append(c2);
							break;

						// unrecognized
						default:
							sb.append(c);
							sb.append(c2);
					}
				}
				else
				{
					// is last : copy
					sb.append(c);
				}
			}
		}
		@NonNull final String result = sb.toString();
		if (result.isEmpty())
		{
			return null;
		}
		return result;
	}
}
