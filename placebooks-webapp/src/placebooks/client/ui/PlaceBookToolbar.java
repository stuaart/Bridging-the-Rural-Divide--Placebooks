package placebooks.client.ui;

import placebooks.client.model.Shelf;
import placebooks.client.resources.Resources;
import placebooks.client.ui.PlaceBookToolbarLogin.ShelfListener;
import placebooks.client.ui.places.PlaceBookEditorNewPlace;
import placebooks.client.ui.places.PlaceBookHomePlace;
import placebooks.client.ui.places.PlaceBookLibraryPlace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.FlowPanel;

public class PlaceBookToolbar extends FlowPanel
{
	private final PlaceBookToolbarLogin login = new PlaceBookToolbarLogin();
	private PlaceController placeController;

	public PlaceBookToolbar()
	{
		super();
		setStyleName(Resources.INSTANCE.style().toolbar());

		add(new PlaceBookToolbarItem("HOME", null, new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				placeController.goTo(new PlaceBookHomePlace(login.getShelf()));

			}
		}));
		add(new PlaceBookToolbarItem("CREATE", Resources.INSTANCE.add(), new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				placeController.goTo(new PlaceBookEditorNewPlace(login.getShelf().getUser()));
			}
		}));
		add(new PlaceBookToolbarItem("MY LIBRARY", Resources.INSTANCE.book(), new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				placeController.goTo(new PlaceBookLibraryPlace(login.getShelf()));
			}
		}));
		add(login);
	}

	public void setShelfListener(ShelfListener shelfListener)
	{
		login.setShelfListener(shelfListener);
	}
	
	public PlaceController getPlaceController()
	{
		return placeController;
	}
	
	public void setPlaceController(final PlaceController placeController)
	{
		this.placeController = placeController;
	}

	public void setShelf(final Shelf shelf)
	{
		login.setShelf(shelf);
	}
}