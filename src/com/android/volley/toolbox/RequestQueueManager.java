package com.android.volley.toolbox;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RequestQueueManager {
	private final String TAG = "RequestQueueManager";
	/**
	 * the queue :-)
	 */
	private static RequestQueueManager _instance;

	public static RequestQueueManager getInstance() {
		if (_instance == null) {
			_instance = new RequestQueueManager();
		}
		return _instance;
	}

	private RequestQueue mRequestQueue;
	Context mContext;

	/**
	 * Nothing to see here.
	 */
	protected RequestQueueManager() {
		// no instances
	}

	/**
	 * @param context
	 *            application context
	 */
	String cookie;

	public void init(Context context, String cookie) {
		mContext = context;
		mRequestQueue = Volley.newRequestQueue(context, new SSLHurlStack());
		this.cookie = cookie;
	}

	/**
	 * @return instance of the queue
	 * @throws IllegalStatException
	 *             if init has not yet been called
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("Not initialized");
		}
	}

	// 发送一个Post请求
	public void postStringReqeust(final Object Tag, final String url, final Map<String, String> vparams, final Listener<String> listener, final ErrorListener errorListener) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				Map<String, String> headers = addSessionCookie(super.getHeaders());
				return headers;
				// return super.getHeaders();
			}
		};
		stringRequest.setTag(Tag);
		mRequestQueue.add(stringRequest);
	}

	public void postJsonRequest(final Object Tag, final String url, final JSONObject vParams, final Listener<JSONObject> listener, final ErrorListener errorListener) {
		JsonObjectRequest jsonRequest = new JsonObjectRequest(url, vParams, listener, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				Map<String, String> headers = addSessionCookie(super.getHeaders());
				return headers;
				// return super.getHeaders();
			}
		};
		jsonRequest.setTag(Tag);
		mRequestQueue.add(jsonRequest);
	}

	// 清空一个Post请求
	public void clearRequest(final Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	private static final String COOKIE_KEY = "Cookie";

	public Map<String, String> addSessionCookie(Map<String, String> headers) {
		String sessionId = cookie;
		if (sessionId.length() > 0) {
			Map<String, String> newHeader = new HashMap<String, String>(headers);
			// StringBuilder builder = new StringBuilder();
			// builder.append(SESSION_COOKIE);
			// builder.append("=");
			// builder.append(sessionId);
			newHeader.put(COOKIE_KEY, sessionId);
			return newHeader;
		}
		return headers;
	}
}
