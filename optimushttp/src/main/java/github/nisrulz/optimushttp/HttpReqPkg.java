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

import android.support.v4.util.ArrayMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * The type Http req pkg.
 */
class HttpReqPkg {

  private String uri;
  private String username = null;
  private String password = null;
  private String method = "GET";
  private Map<String, String> params = new ArrayMap<>();

  /**
   * Gets uri.
   *
   * @return the uri
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets uri.
   *
   * @param uri the uri
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  String getPassword() {
    return uri;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets method.
   *
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets method.
   *
   * @param method the method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Gets params.
   *
   * @return the params
   */
  public Map<String, String> getParams() {
    return params;
  }

  /**
   * Sets params.
   *
   * @param params the params
   */
  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  /**
   * Sets param.
   *
   * @param key the key
   * @param value the value
   */
  public void setParam(String key, String value) {
    params.put(key, value);
  }

  /**
   * Gets encoded params.
   *
   * @return the encoded params
   */
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

