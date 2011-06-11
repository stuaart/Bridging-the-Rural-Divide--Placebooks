package placebooks.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import placebooks.controller.PropertiesSingleton;

import com.vividsolutions.jts.geom.Geometry;

@Entity
public class WebBundleItem extends PlaceBookItem
{

	private static final Logger log = 	
		Logger.getLogger(WebBundleItem.class.getName());

	@Transient
	private BufferedImage thumbnail;

	private String webBundle;

	public WebBundleItem(final User owner, final Geometry geom, 
						 final URL sourceURL, final String webBundle)
	{
		super(owner, geom, sourceURL);
		this.webBundle = webBundle;
		thumbnail = null;
	}

	WebBundleItem()
	{
	}

	public WebBundleItem(final WebBundleItem w)
	{
		super(w);
		this.webBundle = new String(w.getWebBundle());
		thumbnail = null;
	}

	@Override
	public WebBundleItem deepCopy()
	{
		return new WebBundleItem(this);
	}

	@Override
	public void appendConfiguration(final Document config, final Element root)
	{
		final Element item = getConfigurationHeader(config);

		try
		{
			final File from = new File(getWebBundlePath());
			final File to = new File(getPlaceBook().getPackagePath() 
									 + "/" + getKey());

			FileUtils.copyDirectory(from, to);

			final Element filename = config.createElement("filename");
			filename.appendChild(config.createTextNode(webBundle));
			item.appendChild(filename);
		}
		catch (final IOException e)
		{
			log.error(e.toString());
		}

		root.appendChild(item);
	}

	@Override
	public void deleteItemData()
	{
		try
		{
			FileUtils.deleteDirectory(new File(webBundle));
		}
		catch (final IOException e)
		{
			log.error(e.toString());
		}
	}

	@Override
	public String getEntityName()
	{
		return WebBundleItem.class.getName();
	}

	// A thumbnail preview image of the webpage - rendered somehow and stored
	// here
	public BufferedImage getPreview()
	{
		if (thumbnail == null)
		{
			// Thumbnail rendering functionality TODO
		}
		return thumbnail;
	}

	public String getWebBundle()
	{
		return webBundle;
	}

	public void setWebBundle(final String filepath)
	{
		webBundle = filepath;
	}

	public String getWebBundlePath()
	{
		return PropertiesSingleton.get(this.getClass().getClassLoader())
				.getProperty(PropertiesSingleton.IDEN_WEBBUNDLE, "") + getKey();
	}


}
