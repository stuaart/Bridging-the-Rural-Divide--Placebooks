package placebooks.client.ui.items;

import placebooks.client.model.PlaceBookItem;
import placebooks.client.ui.elements.PlaceBookController;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;

public class ImageItem extends PlaceBookItemWidget
{
	private final Image image = new Image();
	
	private final Timer loadTimer = new Timer()
	{
		@Override
		public void run()
		{
			checkSize();
		}
	};

	ImageItem(final PlaceBookItem item, final PlaceBookController handler)
	{
		super(item, handler);
		image.setUrl(item.getURL());
		initWidget(image);
		image.getElement().getStyle().setProperty("margin", "0 auto");
		image.getElement().getStyle().setDisplay(Display.BLOCK);

		image.addLoadHandler(new LoadHandler()
		{
			@Override
			public void onLoad(final LoadEvent event)
			{
				checkSize();
			}
		});

		image.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				fireFocusChanged(true);
				event.stopPropagation();
			}
		});
	}

	private void checkSize()
	{
		if (image.getHeight() == 0)
		{
			loadTimer.schedule(1000);
		}
		else
		{
			loadTimer.cancel();
			fireResized();
		}
	}

	@Override
	public void refresh()
	{
		if (getItem().hasParameter("height"))
		{
			image.setWidth("auto");
			image.setHeight("100%");
		}
		else
		{
			image.setWidth("100%");
			image.setHeight("auto");
		}
		image.setUrl(getItem().getURL());
	}
}
