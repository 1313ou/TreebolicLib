package treebolic.component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6316113839021843464L;

	/**
	 * Indexes to labels
	 */
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_CANCEL = 0;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_INFO = 1;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_FOCUS = 2;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_LINKTO = 3;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_MOUNT = 4;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_UNMOUNT = 5;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_GOTO = 6;
	@SuppressWarnings("WeakerAccess")
	static public final int LABEL_SEARCH = 7;

	// static String[] labels defined in glue for localization

	/**
	 * Constructor
	 */
	@SuppressWarnings("WeakerAccess")
	public PopupMenu(final View thisView)
	{
		super(thisView);
	}

	// public void show(final Component parent, final int x, final int y)

	/**
	 * Make popup menu
	 *
	 * @param thisView       view
	 * @param thisController controller
	 * @param thisValue      input value
	 * @param thisNode       target node
	 * @param theseSettings  settings
	 * @return popup menu
	 */
	static public PopupMenu makePopup(final View thisView, final Controller thisController, final String thisValue, final INode thisNode, final Settings theseSettings)
	{
		final PopupMenu thisPopupMenu = new PopupMenu(thisView);

		// info
		thisPopupMenu.addItem(labels[LABEL_INFO], ImageIndices.IMAGE_INFO.ordinal(), new ActionListener()
		{
			@Override
			public boolean onAction(final Object... theseParams)
			{
				thisController.dispatch(Action.INFO, null, null, null, null, null, thisNode);
				return true;
			}
		});

		// focus
		thisPopupMenu.addItem(labels[LABEL_FOCUS], ImageIndices.IMAGE_FOCUS.ordinal(), new ActionListener()
		{
			@Override
			public boolean onAction(final Object... theseParams)
			{
				thisController.dispatch(Action.FOCUS, null, null, null, null, null, thisNode);
				return true;
			}
		});

		// mount
		final MountPoint thisMountPoint = thisNode.getMountPoint();
		if (thisMountPoint != null)
		{
			final boolean isMounted = thisMountPoint instanceof MountPoint.Mounted;
			thisPopupMenu.addItem(labels[isMounted ? LABEL_UNMOUNT : LABEL_MOUNT], ImageIndices.IMAGE_MOUNT.ordinal(), new ActionListener()
			{
				@Override
				public boolean onAction(final Object... theseParams)
				{
					thisController.dispatch(Action.MOUNT, null, null, null, null, null, thisNode);
					return true;
				}
			});
		}

		// link
		if (PopupMenu.isURL(thisNode.getLink()))
		{
			thisPopupMenu.addItem(labels[LABEL_LINKTO], ImageIndices.IMAGE_LINK.ordinal(), new ActionListener()
			{
				@Override
				public boolean onAction(final Object... theseParams)
				{
					thisController.dispatch(Action.LINK, null, null, null, null, null, thisNode);
					return true;
				}
			});
		}

		// custom
		if (theseSettings.theMenu != null)
		{
			for (final MenuItem thatMenuItem : theseSettings.theMenu)
			{
				String thisMenuLabel = null;
				boolean prepend = thatMenuItem.theLabel != null && (thatMenuItem.theLabel.length() == 0 || Character.isLowerCase(thatMenuItem.theLabel.charAt(0)));
				switch (thatMenuItem.theAction)
				{
					case GOTO:
						// check
						if (thisController.getGotoTarget(thatMenuItem.theLink, thisNode) == null)
						{
							// illegal combination
							continue;
						}
						thisMenuLabel = prepend ? labels[LABEL_GOTO] + ' ' + thatMenuItem.theLabel : thatMenuItem.theLabel;
						break;
					case SEARCH:
						if (thisController.getSearchTarget(thatMenuItem.theTarget, thisNode) == null)
						{
							// illegal combination
							continue;
						}
						thisMenuLabel = prepend ? labels[LABEL_SEARCH] + ' ' + thatMenuItem.theLabel : thatMenuItem.theLabel;
						break;
					default:
						break;
				}

				try
				{
					thisMenuLabel = PopupMenu.expandMacro(thisMenuLabel, thisValue, thisNode);

					// assume ImageIndices.ordinal() = control.Action.ordinal()
					thisPopupMenu.addItem(thisMenuLabel, thatMenuItem.theAction.ordinal(), new ActionListener()
					{
						@Override
						public boolean onAction(final Object... theseParams)
						{
							thisController.dispatch(thatMenuItem.theAction, thatMenuItem.theLink, thatMenuItem.theTarget, thatMenuItem.theMatchTarget, thatMenuItem.theMatchScope, thatMenuItem.theMatchMode, thisNode);
							return true;
						}
					});
				}
				catch (final ArrayIndexOutOfBoundsException ignored)
				{
					// do nothing
				}
			}
		}

		// cancel
		thisPopupMenu.addItem(labels[LABEL_CANCEL], ImageIndices.IMAGE_CANCEL.ordinal(), new ActionListener()
		{
			@Override
			public boolean onAction(final Object... theseParams)
			{
				return false;
			}
		});

		return thisPopupMenu;
	}

	// U R L . V A L I D A T I O N

	static private boolean isURL(final String thisLink)
	{
		if (thisLink != null && !thisLink.isEmpty())
		{
			// well-formed URL
			try
			{
				/* URL thisUrl = */
				new URL(thisLink);
				return true;
			}
			catch (final MalformedURLException ignored)
			{
				// well-formed URI
				try
				{
					final URI thisUri = new URI(thisLink);

					// relative form not including scheme
					if (thisLink.equals(thisUri.getPath()))
					{
						return true;
					}

					// fragment
					final String thisFragment = '#' + thisUri.getFragment();
					if (thisLink.equals(thisFragment))
					{
						return true;
					}

					// desperate attempt
					return thisUri.getScheme().matches("[a-z]*");
				}
				catch (final URISyntaxException ignored2)
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
	 * @param thisString string to expand
	 * @param thisValue  interactively supplied value
	 * @param thisNode   node node
	 * @return expanded string
	 */
	static public String expandMacro(final String thisString, final String thisValue, final INode thisNode)
	{
		if (thisString == null)
		{
			return null;
		}

		final StringBuilder thisBuilder = new StringBuilder();
		final int n = thisString.length();
		for (int i = 0; i < n; i++)
		{
			final char c = thisString.charAt(i);
			if (c != '$')
			{
				thisBuilder.append(c);
			}
			else
			{
				if (i < n - 1)
				{
					// c is not last : peek at next
					final char c2 = thisString.charAt(++i);
					switch (c2)
					{

						// label
						case 'l':
							final String thisLabel = thisNode.getLabel();
							if (thisLabel != null)
							{
								thisBuilder.append(thisLabel.toCharArray());
							}
							break;

						// content
						case 'c':
							final String thisContent = thisNode.getContent();
							if (thisContent != null)
							{
								thisBuilder.append(thisContent.toCharArray());
							}
							break;

						// link url
						case 'u':
							final String thisLink = thisNode.getLink();
							if (thisLink != null)
							{
								thisBuilder.append(thisLink.toCharArray());
							}
							break;

						// id
						case 'i':
							final String thisId = thisNode.getId();
							if (thisId != null)
							{
								thisBuilder.append(thisId.toCharArray());
							}
							break;

						// parent
						case 'p':
							final INode thisParent = thisNode.getParent();
							if (thisParent != null)
							{
								final String thisParentId = thisParent.getId();
								if (thisParentId != null)
								{
									thisBuilder.append(thisParentId.toCharArray());
								}
							}
							break;

						// element value
						case 'e':
							if (thisValue != null)
							{
								thisBuilder.append(thisValue.toCharArray());
							}
							break;

						// escaped $
						case '$':
							thisBuilder.append(c2);
							break;

						// unrecognized
						default:
							thisBuilder.append(c);
							thisBuilder.append(c2);
					}
				}
				else
				{
					// is last : copy
					thisBuilder.append(c);
				}
			}
		}
		final String thisResult = thisBuilder.toString();
		if (thisResult.isEmpty())
		{
			return null;
		}
		return thisResult;
	}
}
