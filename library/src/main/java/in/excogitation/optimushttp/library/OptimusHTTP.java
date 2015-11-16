package in.excogitation.optimushttp.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.Log;

public class OptimusHTTP {

    //LOGTAG
    private final String LOGTAG = getClass().getSimpleName();

    public final static int METHOD_GET = 0;
    public final static int METHOD_POST = 1;
    public final static int MODE_SEQ = 2;
    public final static int MODE_PARALLEL = 3;

    private int method;
    private int mode;
    private boolean DEBUG = false;

    public OptimusHTTP() {
        setMode(MODE_SEQ);
        setMethod(METHOD_GET);
    }

    public void enableDebugging() {
        DEBUG = true;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Make the Request
     *
     * @param context
     * @param url
     * @param params
     * @param listener
     * @return HttpReq reference if a request is made
     * null if no request is made
     */
    public HttpReq makeRequest(Context context, String url, ArrayMap<String, String> params, ResponseListener listener) {
        HttpReq req = new HttpReq();
        HttpReqPkg pkg = new HttpReqPkg();
        if (method == METHOD_GET) {
            pkg.setMethod("GET");
            if (DEBUG)
                Log.d(LOGTAG, "*---------------------- GET Request ----------------------*");
        } else if (method == METHOD_POST) {
            pkg.setMethod("POST");
            if (DEBUG)
                Log.d(LOGTAG, "*---------------------- POST Request ----------------------*");
        }
        pkg.setUri(url);
        pkg.setParams(params);

        if (isOnline(context)) {
            if (mode == MODE_SEQ) {
                SeqAsyncTask(req, pkg, listener);
            } else if (mode == MODE_PARALLEL) {
                ParallelAsyncTask(req, pkg, listener);
            }
            return req;
        } else {
            if (DEBUG)
                Log.d(LOGTAG, "Not connected to Internet ! OptimusHTTP didn't make a request!");
        }
        return null;

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void ParallelAsyncTask(HttpReq req, HttpReqPkg p, ResponseListener listener) {
        req.setOnResultsListener(listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p);
        else
            req.execute(p);
    }

    private void SeqAsyncTask(HttpReq req, HttpReqPkg p, ResponseListener listener) {
        req.setOnResultsListener(listener);
        req.execute(p);
    }

    public void cancelReq(HttpReq req) {
        if (req != null && req.getStatus() == AsyncTask.Status.RUNNING || req.getStatus() == AsyncTask.Status.PENDING) {
            req.cancel(true);
            if (DEBUG)
                Log.d(LOGTAG, "*---------------------- Request Cancelled ----------------*");
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    public interface ResponseListener {
        void onSuccess(String msg);

        void onFailure(String msg);
    }


}
