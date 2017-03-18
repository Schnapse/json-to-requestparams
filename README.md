# JSON to RequestParams

## Introduction

In one of my project, in which I'm using [Loopj library](http://loopj.com/android-async-http/), I had to send a complex JSON Object to a web-service over POST HTTP request.

I tried to send data thanks to `StringEntity` with a application/json content type. Unfortunately, data was only accessible through `php://input` and not directly in `$_POST`. It's the reason why, I analyzed how jQuery library send JSON object then we can read in posted data. Simply, convert JSON object to pairs of key/value in order to POST them "normally".

## Example

### Code

    JSONObject params = new JSONObject();
    JSONArray users = new JSONArray();
    JSONObject user = new JSONObject();

    try
    {
        user.put("name", "Jean");
        user.put("age", "24");
        user.put("city", "Strasbourg");
        users.put(user);
        params.put("users", users);
    }
    catch(JSONException e)
    {
        // ...
    }

    RequestParams requestParams = JsonHelper.toRequestParams(params);

    AsyncHttpClient client = new AsyncHttpClient();

    client.post(context, url, requestParams, "application/x-www-form-urlencoded", new JsonHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response)
        {
            //...       
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse)
        {
            // ...
        }
    })

### Results

It would create three pairs of key value :

- users[0][name] : Jean
- users[0][age] : 24
- users[0][city] : Strasbourg

If you dump `$_POST` on server-side, you would retrieve data properly formed :

    {users : [{name : "Jean", age : "24", city : "Strasbourg"}]}

## Evolutions

I'll probably adapt this class to a real Loopj library compatible object and later, share it on jcenter / maven repos in order to allow people to use it thanks to gradle.
