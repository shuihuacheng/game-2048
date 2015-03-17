package com.android.volley.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Handler;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;

public class FileRequest extends Request<File> {
	/**
	 * Decoding lock so that we don't decode more than one file at a time (to
	 * avoid OOM's)
	 */
	private final Response.Listener<File> mListener;

	private static final Object sDecodeLock = new Object();
	private String fileName;
	private Handler mHandler;

	public FileRequest(String url, String fileLocalPath, Response.Listener<File> listener, ErrorListener errorListener) {
		super(url, errorListener);
		// TODO Auto-generated constructor stub
		this.fileName = fileLocalPath;
		this.mListener = listener;
	}

	public FileRequest(String url, String fileLocalPath, Handler handler, Response.Listener<File> listener, ErrorListener errorListener) {
		super(url, errorListener);
		// TODO Auto-generated constructor stub
		this.fileName = fileLocalPath;
		this.mListener = listener;
		this.mHandler = mHandler;
	}

	@Override
	protected Response<File> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		synchronized (sDecodeLock) {
			byte[] data = response.data;
			File file = new File(fileName);
			try {
				FileOutputStream fOutStream = new FileOutputStream(file);
				fOutStream.write(data);
				fOutStream.flush();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (file == null) {
				return Response.error(new ParseError(response));
			} else {
				return Response.success(file, HttpHeaderParser.parseCacheHeaders(response));
			}
		}
	}

	@Override
	protected void deliverResponse(File response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}
}
