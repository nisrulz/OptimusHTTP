#OptimusHTTP


### Specs
[ ![Download](https://api.bintray.com/packages/nisrulz/maven/com.github.nisrulz%3Aoptimushttp/images/download.svg) ](https://bintray.com/nisrulz/maven/com.github.nisrulz%3Aoptimushttp/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=9)

### Featured in
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OptimusHTTP-green.svg?style=true)](https://android-arsenal.com/details/1/3592) [![AndroidDev Digest](https://img.shields.io/badge/AndroidDev%20Digest-%23100-blue.svg)](https://www.androiddevdigest.com/digest-100/)

### Show some :heart:
[![GitHub stars](https://img.shields.io/github/stars/nisrulz/sensey.svg?style=social&label=Star)](https://github.com/nisrulz/sensey) [![GitHub forks](https://img.shields.io/github/forks/nisrulz/sensey.svg?style=social&label=Fork)](https://github.com/nisrulz/sensey/fork) [![GitHub watchers](https://img.shields.io/github/watchers/nisrulz/sensey.svg?style=social&label=Watch)](https://github.com/nisrulz/sensey) [![GitHub followers](https://img.shields.io/github/followers/nisrulz.svg?style=social&label=Follow)](https://github.com/nisrulz/sensey)
[![Twitter Follow](https://img.shields.io/twitter/follow/nisrulz.svg?style=social)](https://twitter.com/nisrulz) 


Simplified android async HTTP client

# Including in your project
OptimusHTTP is available in the Jcenter, so getting it as simple as adding it as a dependency
```gradle
compile 'com.github.nisrulz:optimushttp:{latest version}'
```
where `{latest version}` corresponds to published version in [ ![Download](https://api.bintray.com/packages/nisrulz/maven/com.github.nisrulz%3Aoptimushttp/images/download.svg) ](https://bintray.com/nisrulz/maven/com.github.nisrulz%3Aoptimushttp/_latestVersion)


#Usage
+ Setup your SERVER url
```java
String SERVER = "http://uinames.com/api/";
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
     + PUT Request
    ```java
    client.setMethod(OptimusHTTP.METHOD_PUT);
    ```
     + DELETE Request
    ```java
    client.setMethod(OptimusHTTP.METHOD_DELETE);
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

  + Setup timeout values (optional, default is 10s)
      + Connect Timeout
     ```java
     client.setConnectTimeout(10 * 1000);
     ```
      + Read Timeout
     ```java
     client.setReadTimeout(10 * 1000);
     ```

  + Setup content type (optional, deafult is `CONTENT_TYPE_FORM_URL_ENCODED`)
       ```java
       client.setContentType(OptimusHTTP.CONTENT_TYPE_JSON);
       ```

      Available Types

        + `OptimusHTTP.CONTENT_TYPE_FORM_URL_ENCODED`
        + `OptimusHTTP.CONTENT_TYPE_JSON`
        + `OptimusHTTP.CONTENT_TYPE_PDF`
        + `OptimusHTTP.CONTENT_TYPE_HTML`
        + `OptimusHTTP.CONTENT_TYPE_IMG_PNG`
        + `OptimusHTTP.CONTENT_TYPE_TEXT`

  + Setup Headers (optional)
      ```java
      ArrayMap<String, String> headerMap = new ArrayMap<>();
      headerMap.put("Accept-Encoding", "gzip, deflate");
      headerMap.put("Accept-Language", "en-US");
      headerMap.put("Content-Language", "en-US");
      client.setHeaderMap(headerMap);
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

# Pull Requests
I welcome and encourage all pull requests. It usually will take me within 24-48 hours to respond to any issue or request. Here are some basic rules to follow to ensure timely addition of your request:
  1. Match coding style (braces, spacing, etc.) This is best achieved using `CMD`+`Option`+`L` (Reformat code) on Mac (not sure for Windows) with Android Studio defaults.
  2. If its a feature, bugfix, or anything please only change code to what you specify.
  3. Please keep PR titles easy to read and descriptive of changes, this will make them easier to merge :)
  4. Pull requests _must_ be made against `develop` branch. Any other branch (unless specified by the maintainers) will get rejected.
  5. Check for existing [issues](https://github.com/nisrulz/optimushttp/issues) first, before filing an issue.  
  6. Have fun!

### Created & Maintained By
[Nishant Srivastava](https://github.com/nisrulz) ([@nisrulz](https://www.twitter.com/nisrulz))


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
