package in.excogitation.optimushttp.library;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

class HttpReqPkg {

    private String uri;
    private String username=null;
    private String password=null;
    private String method = "GET";
    private Map<String, String> params = new HashMap<String, String>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return uri;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        String value = null;
        for (String key : params.keySet()) {
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
        return sb.toString();

    }
}

