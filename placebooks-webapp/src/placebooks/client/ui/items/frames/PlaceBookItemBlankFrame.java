package placebooks.client.ui.items.frames;

import placebooks.client.Resources;
import placebooks.client.ui.elements.PlaceBookController;

public class PlaceBookItemBlankFrame extends PlaceBookItemFrame
{
	public static PlaceBookItemFrameFactory FACTORY = new PlaceBookItemFrameFactory()
	{
		@Override
		public PlaceBookItemFrame createFrame(final PlaceBookController handler)
		{
			return new PlaceBookItemBlankFrame();
		}
	};

	public PlaceBookItemBlankFrame()
	{
		rootPanel = widgetPanel;
		widgetPanel.setStyleName(Resources.STYLES.style().widgetPanel());
	}
}
