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

package in.excogitation.library_optimushttp;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import in.excogitation.optimushttp.library.HttpReq;
import in.excogitation.optimushttp.library.OptimusHTTP;


public class MainActivity extends AppCompatActivity {

    // Define a SERVER link
    // Link obtained from : http://requestb.in/
    private final String SERVER = "http://requestb.in/168uy1z1";

    // Create objects
    private OptimusHTTP client;
    private HttpReq req;
    private ArrayList<HttpReq> refHttpReqList;

    // ListView
    private ListView lv;
    private ArrayList<String> data;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Data
        data = new ArrayList<>();
        data.add("POST Request");
        data.add("GET Request");
        data.add("Cancel All Request");

        //ListView
        lv = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);

        // Initialize the OptimusHTTP Client
        client = new OptimusHTTP();

        // Enable Logs
        client.enableDebugging();

        // Store the references of all the requests made in an arraylist for the purpose of cancelling them later on
        refHttpReqList = new ArrayList<>();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("email", "abc@abc.com");
                        params.put("pass", "abc");
                        client.setMethod(OptimusHTTP.METHOD_POST);
                        client.setMode(OptimusHTTP.MODE_PARALLEL);
                        try {
                            // makeRequest() returns the reference of the request made
                            // which can be used later to call the cancelReq() if required
                            // if no request is made the makeRequest() returns null
                            req = client.makeRequest(MainActivity.this, SERVER, params, new OptimusHTTP.ResponseListener() {
                                @Override
                                public void onSuccess(String msg) {
                                    System.out.println(msg);
                                }

                                @Override
                                public void onFailure(String msg) {
                                    System.out.println(msg);
                                }
                            });
                            if (req != null)
                                refHttpReqList.add(req);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 1: {
                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("email", "abc@abc.com");
                        params.put("mobileno", "000000000");
                        params.put("Key1", "value1");
                        params.put("key2", "value2");
                        client.setMethod(OptimusHTTP.METHOD_GET);
                        client.setMode(OptimusHTTP.MODE_SEQ);
                        try {
                            req = client.makeRequest(MainActivity.this, SERVER, params, responseListener);
                            if (req != null)
                                refHttpReqList.add(req);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case 2: {
                        if (refHttpReqList.size() > 0) {
                            view.setEnabled(false);
                            for (int i = 0; i < refHttpReqList.size(); i++)
                                client.cancelReq(refHttpReqList.get(i));
                            refHttpReqList.clear();
                            view.setEnabled(true);
                        }
                    }
                    break;

                }
            }
        });
    }

    // Listener for the Response received from server
    private final OptimusHTTP.ResponseListener responseListener = new OptimusHTTP.ResponseListener() {
        @Override
        public void onSuccess(String msg) {
            System.out.println(msg);
        }

        @Override
        public void onFailure(String msg) {
            System.out.println(msg);
        }
    };
}

