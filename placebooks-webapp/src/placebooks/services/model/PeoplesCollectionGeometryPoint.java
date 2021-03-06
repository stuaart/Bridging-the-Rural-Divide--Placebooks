package placebooks.services.model;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PeoplesCollectionGeometryPoint
{
	private static final Logger log = Logger.getLogger(PeoplesCollectionGeometryPoint.class);
	
	private String type;
	private float[] coordinates;

	public PeoplesCollectionGeometryPoint() {
		
	}

	public PeoplesCollectionGeometryPoint(String type, float[] coordinates) {
		this.type = type;
		this.coordinates = coordinates;
	}

	
	public String GetType() {
		return type;
	}

	public void SetType(String type) {
		this.type = type;
	}

	public float[] GetCoordinates() {
		return coordinates;
	}

	public void SetCoordinates(float[] coordinates) {
		this.coordinates = coordinates;
	}

	public Geometry GetGeometry() throws IOException {
		try
		{
			String wkt = type.toUpperCase() + " ( " + coordinates[0] + " " + coordinates[1] + " )";
			log.debug("Generated WKT: "+ wkt);
			return new WKTReader().read(wkt);
		}
		catch (ParseException e)
		{
			throw new IOException("Parse Error", e);
		}
	}
}