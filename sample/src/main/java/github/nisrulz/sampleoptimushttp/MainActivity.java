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

package github.nisrulz.sampleoptimushttp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import github.nisrulz.optimushttp.HttpReq;
import github.nisrulz.optimushttp.OptimusHTTP;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Define a SERVER link
    //private final String SERVER = "http://uinames.com/api/";

    String API_KEY = "c6e381d8c7ff98f0fee43775817cf6ad";

    String SERVER = "http://api.openweathermap.org/data/2.5/weather";

    ProgressDialog progressDialog;

    TextView textView_res;

    private ArrayAdapter<String> adapter;

    // Create objects
    private OptimusHTTP client;

    private ArrayList<String> data;

    // ListView
    private ListView lv;

    private ArrayList<HttpReq> refHttpReqList;

    private HttpReq req;

    // Listener for the Response received from server
    private final OptimusHTTP.ResponseListener responseListener = new OptimusHTTP.ResponseListener() {
        @Override
        public void onFailure(String msg) {
            progressDialog.dismiss();
            System.out.println(msg);
        }

        @Override
        public void onSuccess(String msg) {
            progressDialog.dismiss();
            System.out.println(msg);
            getInfoFromJson(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Data
        data = new ArrayList<>();
        data.add("POST Request");
        data.add("GET Request");
        data.add("PUT Request");
        data.add("DELETE Request");
        data.add("Cancel All Request");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting");

        textView_res = findViewById(R.id.txt_res);

        //ListView
        lv = findViewById(R.id.listView);
        adapter =
                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        // Initialize the OptimusHTTP Client
        client = new OptimusHTTP(this);

        // Enable Logs
        client.enableDebugging();

        // Store the references of all the requests made in an arraylist for the purpose of cancelling them later on
        refHttpReqList = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        // POST
                        // Create a param key value map to post in body of req
                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("email", "abc@abc.com");
                        params.put("mobileno", "000000000");
                        params.put("Key1", "value1");
                        params.put("key2", "value2");

                        // Set method
                        client.setMethod(OptimusHTTP.METHOD_POST);
                        // Set Mode
                        client.setMode(OptimusHTTP.MODE_PARALLEL);
                        // Set Connect Timeout
                        client.setConnectTimeout(10 * 1000); //ms
                        // Set Read Timeout
                        client.setReadTimeout(10 * 1000); //ms
                        // Set Content Type
                        client.setContentType(OptimusHTTP.CONTENT_TYPE_JSON);
                        // Create a map of header key value pair and set headers to the client
                        ArrayMap<String, String> headerMap = new ArrayMap<>();
                        headerMap.put("Accept-Encoding", "gzip, deflate");
                        headerMap.put("Accept-Language", "en-US");
                        headerMap.put("Content-Language", "en-US");
                        client.setHeaderMap(headerMap);
                        try {
                            // makeRequest() returns the reference of the request made
                            // which can be used later to call the cancelReq() if required
                            // if no request is made the makeRequest() returns null

                            progressDialog.show();
                            req = client.makeRequest(SERVER, params, new OptimusHTTP.ResponseListener() {
                                @Override
                                public void onFailure(String msg) {
                                    progressDialog.dismiss();
                                    loadInfo(msg);
                                }

                                @Override
                                public void onSuccess(String msg) {
                                    progressDialog.dismiss();
                                    getInfoFromJson(msg);
                                }
                            });
                            if (req != null) {
                                refHttpReqList.add(req);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 1: {
                        // GET
                        //ArrayMap<String, String> params = new ArrayMap<>();
                        //params.put("email", "abc@abc.com");
                        //params.put("mobileno", "000000000");

                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("appid", API_KEY);
                        params.put("q", "London");
                        params.put("units", "metric");

                        client.setMethod(OptimusHTTP.METHOD_GET);
                        client.setMode(OptimusHTTP.MODE_SEQ);
                        // Set Connect Timeout
                        client.setConnectTimeout(10 * 1000); //ms
                        // Set Read Timeout
                        client.setReadTimeout(10 * 1000); //ms

                        try {
                            progressDialog.show();
                            req = client.makeRequest(SERVER, params, responseListener);
                            if (req != null) {
                                refHttpReqList.add(req);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 2: {
                        //PUT
                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("email", "abc@abc.com");
                        params.put("mobileno", "000000000");
                        params.put("Key1", "value3");
                        params.put("key2", "value2");
                        client.setMethod(OptimusHTTP.METHOD_PUT);
                        client.setMode(OptimusHTTP.MODE_SEQ);
                        try {
                            progressDialog.show();
                            req = client.makeRequest(SERVER, params, responseListener);
                            if (req != null) {
                                refHttpReqList.add(req);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 3: {
                        // DELETE
                        ArrayMap<String, String> params = new ArrayMap<>();
                        client.setMethod(OptimusHTTP.METHOD_DELETE);
                        client.setMode(OptimusHTTP.MODE_SEQ);
                        try {
                            progressDialog.show();
                            req = client.makeRequest(SERVER, params, responseListener);
                            if (req != null) {
                                refHttpReqList.add(req);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 4: {
                        // Cancel All

                        if (refHttpReqList.size() > 0) {
                            view.setEnabled(false);
                            for (int i = 0; i < refHttpReqList.size(); i++) {
                                client.cancelReq(refHttpReqList.get(i));
                            }
                            refHttpReqList.clear();
                            view.setEnabled(true);
                        }
                    }
                    break;
                }
            }
        });
    }

    private void getInfoFromJson(String msg) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(msg);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Name : ")
                    .append(jsonObject.getString("name"))
                    .append(" ")
                    .append(jsonObject.getString("surname"))
                    .append("\n")
                    .append("Region : ")
                    .append(jsonObject.getString("region"))
                    .append("\n")
                    .append("Gender : ")
                    .append(jsonObject.getString("gender"))
                    .append("\n");

            loadInfo(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadInfo(String data) {
        System.out.println(data);
        textView_res.setText(data);
    }
}

