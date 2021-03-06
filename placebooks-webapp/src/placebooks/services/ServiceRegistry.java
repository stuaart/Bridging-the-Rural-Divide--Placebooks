package placebooks.services;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import placebooks.controller.CommunicationHelper;
import placebooks.controller.PropertiesSingleton;
import placebooks.model.LoginDetails;
import placebooks.model.User;

public class ServiceRegistry
{
	private static final Map<String, Service> services = new HashMap<String, Service>();
	
	static
	{
		addService(new EverytrailService());
		addService(new PeoplesCollectionService());
	}
	
	public static Service getService(final String serviceName)
	{
		return services.get(serviceName);
	}
	
	public static void addService(final Service service)
	{
		services.put(service.getName(), service);
	}
	
	public static void updateServices(final EntityManager manager, final User user)
	{
		for(LoginDetails details: user.getLoginDetails())
		{
			Service service = services.get(details.getService());
			if(service != null)
			{
				service.sync(manager, user, false, Double.parseDouble(PropertiesSingleton.get(CommunicationHelper.class.getClassLoader()).getProperty(PropertiesSingleton.IDEN_SEARCH_LON, "0")),
						Double.parseDouble(PropertiesSingleton.get(CommunicationHelper.class.getClassLoader()).getProperty(PropertiesSingleton.IDEN_SEARCH_LAT, "0")),
						Double.parseDouble(PropertiesSingleton.get(CommunicationHelper.class.getClassLoader()).getProperty(PropertiesSingleton.IDEN_SEARCH_RADIUS, "0"))
				);
			}
		}
	}	
}
