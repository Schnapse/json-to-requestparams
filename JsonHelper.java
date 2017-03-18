import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonHelper
{
	/**
	 * Convert JSONObject to RequestParams
	 * @param JSONObject jsonObject
	 * @return RequestParams params
	*/
	public static RequestParams toRequestParams(JSONObject jsonObject)
	{
		return mapToRequestParams(objectToHashMap(jsonObject, ""));
	}

	/**
	 * Convert JSONObject to HashMap<String, String>
	 * @param JSONObject jsonObject
	 * @param String prefix
	 * @return HashMap<String, String> params
	*/
	private static HashMap<String, String> objectToHashMap(JSONObject jsonObject, String prefix)
	{
		HashMap<String, String> params = new HashMap<>();

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext())
		{
			String key = iterator.next();

			String tmpPrefix = prefix.isEmpty() ? key : prefix + "[" + key + "]";

			try
			{
				Object value = jsonObject.get(key);

				if(value instanceof JSONObject)
				{
					JSONObject obj = (JSONObject) value;

					params.putAll(objectToHashMap(obj, tmpPrefix));
				}
				else if(value instanceof  JSONArray)
				{
					JSONArray array = (JSONArray) value;

					params.putAll(arrayToHashMap(array, tmpPrefix));
				}
				else
				{
					params.put(tmpPrefix, value.toString());
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		return params;
	}

	/**
	 * Convert JSONArray to HashMap<String, String>
	 * @param JSONArray jsonArray
	 * @param String prefix
	 * @return HashMap<String, String> params
	*/
	private static HashMap<String, String> arrayToHashMap(JSONArray jsonArray, String prefix)
	{
		HashMap<String, String> map = new HashMap<>();

		for(int i = 0; i < jsonArray.length(); i++)
		{
			String tmpPrefix = prefix + "[" + i + "]";

			try
			{
				Object value = jsonArray.get(i);

				if(value instanceof JSONObject)
				{
					JSONObject obj = (JSONObject) value;

					map.putAll(objectToHashMap(obj, tmpPrefix));
				}
				else if(value instanceof JSONArray)
				{
					JSONArray array = (JSONArray) value;

					map.putAll(arrayToHashMap(array, tmpPrefix));
				}
				else
				{
					map.put(tmpPrefix, value.toString());
				}
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * Convert HashMap<String, String> to RequestParams
	 * @param HashMap<String, String> map
	 * @return RequestParams params
	 */
	private static RequestParams mapToRequestParams(HashMap<String, String> map)
	{
		RequestParams params = new RequestParams();

		for(Map.Entry<String, String> entry : map.entrySet())
		{
			params.add(entry.getKey(), entry.getValue());
		}

		return params;
	}
}
