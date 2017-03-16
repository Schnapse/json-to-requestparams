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
	 * Convert JSONObject or JSONArray to RequestParams
	 * @param json
	 * @return params
	 * @throws JSONException
     */
	public static RequestParams toRequestParams(Object json) throws JSONException
	{
		RequestParams params;

		if(json instanceof JSONObject)
		{
			params = objectToRequestParams((JSONObject) json);
		}
		else if(json instanceof JSONArray)
		{
			params = arrayToRequestParams((JSONArray) json);
		}
		else
		{
			throw new JSONException("Object is not a JSONObject or JSONArray.");
		}

		return params;
	}

	/**
	 * Convert JSONObject to RequestParams
	 * @param jsonObject
	 * @return
     */
	public static RequestParams objectToRequestParams(JSONObject jsonObject)
	{
		HashMap<String, String> map = objectToHashMap(jsonObject, "");

		return mapToRequestParams(map);
	}

	/**
	 * Convert JSONArray to RequestParams
	 * @param jsonArray
	 * @return
     */
	public static RequestParams arrayToRequestParams(JSONArray jsonArray)
	{
		HashMap<String, String> map = arrayToHashMap(jsonArray, "");

		return mapToRequestParams(map);
	}

	/**
	 * Convert JSON object to HashMap
	 * @param jsonObject
	 * @param prefix
     * @return
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
	 * Convert JSON array to RequestParams
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
	 * Convert HasMap to RequestParams
	 * @param map
	 * @return
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
