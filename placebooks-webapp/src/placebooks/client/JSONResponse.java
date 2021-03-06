package placebooks.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;

public abstract class JSONResponse<T extends JavaScriptObject> implements RequestCallback
{
	public void handleError(final Request request, final Response response, final Throwable throwable)
	{

	}

	public void handleOther(final Request request, final Response response)
	{

	}

	public abstract void handleResponse(T object);

	@Override
	public void onError(final Request request, final Throwable throwable)
	{
		GWT.log("Error: " + throwable.getMessage(), throwable);
		handleError(request, null, throwable);
	}

	@Override
	public void onResponseReceived(final Request request, final Response response)
	{
		GWT.log("Response: " + response.getStatusCode() + ": " + response.getText());
		try
		{
			if (response.getStatusCode() == 200)
			{
				T result = parse(response.getText());
				if(result == null)
				{
					GWT.log("Error: 'null' parsed from " + response.getText());
					handleError(request, response, new NullPointerException());
				}
				handleResponse(result);
			}
			else
			{
				handleOther(request, response);
			}
		}
		catch (final Exception e)
		{
			GWT.log("Error: " + response.getText(), e);
			handleError(request, response, e);
		}
	}

	private T parse(final String json)
	{
		return JSONParser.parseStrict(json).isObject().getJavaScriptObject().<T> cast();
	}
}
