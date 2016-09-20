/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.nisrulz.optimushttp;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.Log;

/**
 * The type Optimus http.
 */
public class OptimusHTTP {

  //LOGTAG
  private final String LOGTAG = getClass().getSimpleName();

  /**
   * The constant METHOD_GET.
   */
  public final static int METHOD_GET = 0;
  /**
   * The constant METHOD_POST.
   */
  public final static int METHOD_POST = 1;
  /**
   * The constant METHOD_PUT.
   */
  public final static int METHOD_PUT = 100;
  /**
   * The constant METHOD_DELETE.
   */
  public final static int METHOD_DELETE = 101;

  /**
   * The constant CONTENT_TYPE_FORM_URL_ENCODED.
   */
  public final static String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
  /**
   * The constant CONTENT_TYPE_JSON.
   */
  public final static String CONTENT_TYPE_JSON = "application/json";
  /**
   * The constant CONTENT_TYPE_PDF.
   */
  public final static String CONTENT_TYPE_PDF = "application/pdf";
  /**
   * The constant CONTENT_TYPE_HTML.
   */
  public final static String CONTENT_TYPE_HTML = "text/html";
  /**
   * The constant CONTENT_TYPE_IMG_PNG.
   */
  public final static String CONTENT_TYPE_IMG_PNG = "image/png";
  /**
   * The constant CONTENT_TYPE_TEXT.
   */
  public final static String CONTENT_TYPE_TEXT = "text/plain";

  /**
   * The constant MODE_SEQ.
   */
  public final static int MODE_SEQ = 2;
  /**
   * The constant MODE_PARALLEL.
   */
  public final static int MODE_PARALLEL = 3;

  private int method;
  private int mode;
  private boolean DEBUG = false;
  private Context context;

  private int connectTimeout = 10 * 1000; //10s
  private int readTimeout = 10 * 1000; //10s
  private String contentType = CONTENT_TYPE_FORM_URL_ENCODED;

  private ArrayMap<String, String> headerMap = new ArrayMap<>();

  /**
   * Gets connect timeout.
   *
   * @return the connect timeout
   */
  public int getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Gets read timeout.
   *
   * @return the read timeout
   */
  public int getReadTimeout() {
    return readTimeout;
  }

  /**
   * Gets content type.
   *
   * @return the content type
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets connect timeout.
   *
   * @param timeInMs
   *     the time in ms
   */
  public void setConnectTimeout(int timeInMs) {
    this.connectTimeout = timeInMs;
  }

  public void setHeaderMap(ArrayMap<String, String> headerMap) {
    this.headerMap = headerMap;
  }

  /**
   * Sets read timeout.
   *
   * @param timeInMs
   *     the time in ms
   */
  public void setReadTimeout(int timeInMs) {
    this.readTimeout = timeInMs;
  }

  /**
   * Sets content type.
   *
   * @param contentType
   *     the content type
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Instantiates a new Optimus http.
   *
   * @param context
   *     the context
   */
  public OptimusHTTP(Context context) {
    this.context = context;
    setMode(MODE_SEQ);
    setMethod(METHOD_GET);
  }

  /**
   * Enable debugging.
   */
  public void enableDebugging() {
    DEBUG = true;
  }

  /**
   * Gets method.
   *
   * @return the method
   */
  public int getMethod() {
    return method;
  }

  /**
   * Sets method.
   *
   * @param method
   *     the method
   */
  public void setMethod(int method) {
    this.method = method;
  }

  /**
   * Gets mode.
   *
   * @return the mode
   */
  public int getMode() {
    return mode;
  }

  /**
   * Sets mode.
   *
   * @param mode
   *     the mode
   */
  public void setMode(int mode) {
    this.mode = mode;
  }

  /**
   * Make the Request
   *
   * @param url
   *     the url
   * @param params
   *     the params
   * @param listener
   *     the listener
   * @return HttpReq reference if a request is made null if no request is made
   */
  public HttpReq makeRequest(String url, ArrayMap<String, String> params,
      ResponseListener listener) {
    HttpReq req = new HttpReq(connectTimeout, readTimeout, contentType, headerMap);
    HttpReqPkg pkg = new HttpReqPkg();
    if (method == METHOD_GET) {
      pkg.setMethod("GET");
      if (DEBUG) {
        Log.d(LOGTAG, "*---------------------- GET Request ----------------------*");
      }
    }
    else if (method == METHOD_POST) {
      pkg.setMethod("POST");
      if (DEBUG) {
        Log.d(LOGTAG, "*---------------------- POST Request ----------------------*");
      }
    }
    else if (method == METHOD_PUT) {
      pkg.setMethod("PUT");
      if (DEBUG) {
        Log.d(LOGTAG, "*---------------------- PUT Request ----------------------*");
      }
    }
    else if (method == METHOD_DELETE) {
      pkg.setMethod("DELETE");
      if (DEBUG) {
        Log.d(LOGTAG, "*---------------------- DELETE Request ----------------------*");
      }
    }

    pkg.setUri(url);
    pkg.setParams(params);

    if (isOnline()) {
      if (mode == MODE_SEQ) {
        SeqAsyncTask(req, pkg, listener);
      }
      else if (mode == MODE_PARALLEL) {
        ParallelAsyncTask(req, pkg, listener);
      }
      return req;
    }
    else {
      if (DEBUG) {
        Log.d(LOGTAG, "Not connected to Internet ! OptimusHTTP didn't make a request!");
      }
    }
    return null;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void ParallelAsyncTask(HttpReq req, HttpReqPkg p, ResponseListener listener) {
    req.setOnResultsListener(listener);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
    }
    else {
      req.execute(p);
    }
  }

  private void SeqAsyncTask(HttpReq req, HttpReqPkg p, ResponseListener listener) {
    req.setOnResultsListener(listener);
    req.execute(p);
  }

  /**
   * Cancel req.
   *
   * @param req
   *     the req
   */
  public void cancelReq(HttpReq req) {
    if (req != null && (req.getStatus() == AsyncTask.Status.RUNNING
        || req.getStatus() == AsyncTask.Status.PENDING)) {
      req.cancel(true);
      if (DEBUG) {
        Log.d(LOGTAG, "*---------------------- Request Cancelled ----------------*");
      }
    }
  }

  private boolean isOnline() {
    ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnected();
  }

  /**
   * The interface Response listener.
   */
  public interface ResponseListener {
    /**
     * On success.
     *
     * @param msg
     *     the msg
     */
    void onSuccess(String msg);

    /**
     * On failure.
     *
     * @param msg
     *     the msg
     */
    void onFailure(String msg);
  }
}
