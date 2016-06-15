#OptimusHTTP

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.nisrulz/optimushttp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.nisrulz/optimushttp) [![API](https://img.shields.io/badge/API-9%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=9) [![Javadocs](https://www.javadoc.io/badge/com.github.nisrulz/optimushttp.svg?color=blue&label=Javadoc)](https://www.javadoc.io/doc/com.github.nisrulz/optimushttp)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OptimusHTTP-green.svg?style=true)](https://android-arsenal.com/details/1/3592)

[![Twitter Follow](https://img.shields.io/twitter/follow/nisrulz.svg?style=social)](https://twitter.com/nisrulz)

Simplified android async HTTP client

#Integration
- OptimusHTTP is available in the MavenCentral, so getting it is as simple as adding it as a dependency
```gradle
compile 'com.github.nisrulz:optimushttp:1.0.0'
```

#Usage
+ Setup your SERVER url
```java
// Link obtained from : http://requestb.in/
String SERVER = "http://requestb.in/168uy1z1";
```

+ Create an instance of the ***OptimusHTTP*** class
```java
 OptimusHTTP client = new OptimusHTTP();
```

+ Next if in debug stage, enable logs
```java
client.enableDebugging();
```

+ Define parameters to be sent with the request
```java
ArrayMap<String, String> params = new ArrayMap<>();
params.put("email", "abc@abc.com");
params.put("pass", "abc");
```

+ Define configurations for the request
 + Type of Method : POST/GET
	 + POST Request
  ```java
  client.setMethod(OptimusHTTP.METHOD_POST);
  ```
	 + GET Request
  ```java
  client.setMethod(OptimusHTTP.METHOD_GET);
  ```
   + Type of Mode : PARALLEL/SEQ
	 + Parallel Request
  ```java
  client.setMode(OptimusHTTP.MODE_PARALLEL);
  ```
	 + Sequential Request
  ```java
  client.setMode(OptimusHTTP.MODE_SEQ);
  ```

+ To make a request create an object of **HttpReq** class.
> The ***client.makeRequest()*** function returns reference to each ***HttpReq*** object created which you can save in an *ArrayList* and then later on call cancel function on them to *cancel* the requests

	```java
	
	ArrayList<HttpReq> refHttpReqList = new ArrayList<>();
	try {
	     // makeRequest() returns the reference of the request made
	     // which can be used later to call the cancelReq() if required
	     // if no request is made the makeRequest() returns null
	    HttpReq req = client.makeRequest(MainActivity.this, SERVER, params, responseListener);
	        if (req != null)
	            refHttpReqList.add(req);
	} catch (Exception e) {
	        e.printStackTrace();
	}
	```

+ To cancel one requests
```java
client.cancelReq(req);
```

+ To cancel all requests
```java
if (refHttpReqList.size() > 0) {
    for (int i = 0; i < refHttpReqList.size(); i++)
        client.cancelReq(refHttpReqList.get(i));
    refHttpReqList.clear();
}
```

+ Implement the Callback
```java
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
```

License
=======

    Copyright 2016 Nishant Srivastava

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
