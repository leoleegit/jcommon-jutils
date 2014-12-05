// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

public class HttpRequest
  implements Runnable
{
  protected static Logger logger = Logger.getLogger(HttpRequest.class);
  public static SSLSocketFactory factory;
  public static Map<String, Integer> trust_host = new HashMap<String, Integer>();
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String DELETE = "DELETE";
  protected String url_;
  protected HttpListener listener_;
  private String content_;
  private String charset_;
  protected String method_;
  private String passphrase = "changeit";
  private String user;
  private String password;
  private Properties properties = new Properties();
  private Map<Object, Object> attributes = new HashMap<Object, Object>();

  private boolean trusted = false;

  private Object handler;
  protected StringBuilder sResult = new StringBuilder();

  private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

  public HttpRequest(String url, String content, String charset, String method, HttpListener listener)
  {
    this.url_ = url;
    this.charset_ = charset;
    this.content_ = content;
    this.method_ = method;
    this.listener_ = listener;
  }

  public HttpRequest(String url, String content, String method, HttpListener listener) {
    this(url, content, "utf-8", method, listener);
  }

  public HttpRequest(String url, String content, String method, HttpListener listener, boolean trusted) {
    this(url, content, "utf-8", method, listener);
    this.trusted = trusted;
  }

  public HttpRequest(String url, String charset, HttpListener listener) {
    this(url, null, charset, "GET", listener);
  }

  public HttpRequest(String url, HttpListener listener) {
    this(url, null, "utf-8", "GET", listener);
  }

  public HttpRequest(String url, HttpListener listener, boolean trusted) {
    this(url, null, "utf-8", "GET", listener);
    this.trusted = trusted;
  }

  public void run()
  {
    String charsetName = this.charset_;
    URL url = null;
    HttpURLConnection httpConnection = null;
    InputStream httpIS = null;
    BufferedReader http_reader = null;
    try {
      url = new URL(this.url_);

      if ((this.url_ != null) && (this.url_.startsWith("https")) && (!this.trusted)) {
        try {
          factory = verifyCert();
        }
        catch (Exception e) {
          logger.error("verifyCert:", e);
          if (this.listener_ != null) this.listener_.onException(this, e);
          return;
        }
        httpConnection = (HttpsURLConnection)url.openConnection();
        if (factory != null)
          ((HttpsURLConnection)httpConnection).setSSLSocketFactory(factory);
        ((HttpsURLConnection)httpConnection).setHostnameVerifier(new NullHostnameVerifier());
      }
      else {
        httpConnection = (HttpURLConnection)url.openConnection();
      }
      httpConnection.setConnectTimeout(10000);
      httpConnection.setReadTimeout(10000);
      httpConnection.setRequestMethod(this.method_);
      if ((this.user != null) && (this.password != null)) {
        String userpass = new StringBuilder().append(this.user).append(":").append(this.password).toString();
        String encoding = new BASE64Encoder().encode(userpass.getBytes());
        String basicAuth = new StringBuilder().append("Basic ").append(encoding).toString();
        httpConnection.setRequestProperty("Authorization", basicAuth);
      }
      if ("GET".equalsIgnoreCase(this.method_)) {
        httpConnection.setRequestProperty("Accept", "*/*");
      } else if ("POST".equalsIgnoreCase(this.method_)) {
        httpConnection.setDoOutput(true);
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpConnection.setRequestProperty("Content-Length", String.valueOf(this.content_.getBytes().length));

        PrintWriter out = null;
        out = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), charsetName));
        out.print(this.content_);
        out.flush();
        out.close();
      }
      int responseCode = httpConnection.getResponseCode();
      if (responseCode == 200) {
        httpIS = httpConnection.getInputStream();
        http_reader = new BufferedReader(new InputStreamReader(httpIS, charsetName));
        String line = null;
        while ((line = http_reader.readLine()) != null) {
          this.sResult.append(line);
        }
        if (this.listener_ != null) this.listener_.onSuccessful(this, this.sResult);
      }
      else if (responseCode >= 400) {
        httpIS = httpConnection.getErrorStream();
        http_reader = new BufferedReader(new InputStreamReader(httpIS, charsetName));
        String line = null;
        while ((line = http_reader.readLine()) != null) {
          this.sResult.append(line);
        }
        logger.info(new StringBuilder().append("[URL][response][failure]").append(this.sResult).append("\n").append(this.url_).toString());
        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult);
      }
      else
      {
        this.sResult.append(new StringBuilder().append("[URL][response][failure][code : ").append(responseCode).append(" ]").toString());
        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult);
        logger.info(new StringBuilder().append("[URL][response][failure][code : ").append(responseCode).append(" ]").append("\n").append(this.url_).toString());
      }
    } catch (SocketTimeoutException e) {
      if (this.listener_ != null) this.listener_.onTimeout(this);
      logger.warn(new StringBuilder().append("[HttpReqeust]TimeOut:").append(this.url_).toString());
    }
    catch (IOException e) {
      if (this.listener_ != null) this.listener_.onException(this, e);
      logger.warn(new StringBuilder().append("[HttpReqeust] error:").append(this.url_).append("\n").append(e).toString());
    }
    finally {
      try {
        if (http_reader != null)
          http_reader.close();
        if (httpIS != null)
          httpIS.close();
        if (httpConnection != null)
          httpConnection.disconnect();
      } catch (IOException e) {
        logger.error(new StringBuilder().append("[HttpReqeust]finally error:").append(this.url_).append("\n").append(e).toString());
        if (this.listener_ != null) this.listener_.onException(this, e); 
      }
    }
  }

  public SSLSocketFactory verifyCert() throws Exception {
    if ((this.url_ != null) && (this.url_.startsWith("https"))) {
      String host = this.url_.substring(this.url_.indexOf("/", 7) + 1, this.url_.indexOf("/", 9));
      String[] c = host.split(":");
      host = c[0];
      int port = c.length == 1 ? 443 : Integer.parseInt(c[1]);

      if ((trust_host.containsKey(host)) && (factory != null) && 
        (((Integer)trust_host.get(host)).intValue() == port)) {
        return factory;
      }
      char[] passphrase = this.passphrase.toCharArray();

      char SEP = File.separatorChar;
      File dir = new File(new StringBuilder().append(System.getProperty("java.home")).append(SEP).append("lib").append(SEP).append("security").toString());

      File file = new File(dir, "cacerts");

      logger.info(new StringBuilder().append("Loading KeyStore ").append(file).append("...").toString());
      InputStream in = new FileInputStream(file);
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(in, passphrase);
      in.close();

      SSLContext context = SSLContext.getInstance("TLS");
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

      tmf.init(ks);
      X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
      SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
      context.init(null, new TrustManager[] { tm }, null);
      factory = factory == null ? context.getSocketFactory() : factory;
      logger.info(new StringBuilder().append("factory:").append(factory).toString());
      logger.info(new StringBuilder().append("Opening connection to ").append(host).append(":").append(port).append("...").toString());
      SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
      socket.setSoTimeout(10000);
      try {
        logger.info("Starting SSL handshake...");
        socket.startHandshake();
        socket.close();
        trust_host.put(host, Integer.valueOf(port));
        logger.info("certificate is already trusted");
        return factory;
      } catch (SSLException e) {
        logger.warn("no certificate trusted");

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
          logger.warn("Could not obtain server certificate chain");
          return null;
        }

        logger.info(new StringBuilder().append("Server sent ").append(chain.length).append(" certificate(s):").toString());
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
          X509Certificate cert = chain[i];
          logger.info(new StringBuilder().append(" ").append(i + 1).append(" Subject ").append(cert.getSubjectDN()).toString());

          logger.info(new StringBuilder().append("   Issuer  ").append(cert.getIssuerDN()).toString());
          sha1.update(cert.getEncoded());
          logger.info(new StringBuilder().append("   sha1    ").append(toHexString(sha1.digest())).toString());
          md5.update(cert.getEncoded());
          logger.info(new StringBuilder().append("   md5     ").append(toHexString(md5.digest())).toString());
        }

        logger.info("certificate to add to trusted keystore");

        X509Certificate cert = chain[0];
        String alias = new StringBuilder().append(host).append("-").append(1).toString();
        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream(file);
        ks.store(out, passphrase);
        out.close();
        trust_host.put(host, Integer.valueOf(port));
        logger.info(cert);
        logger.info(new StringBuilder().append("Added certificate to keystore using alias '").append(alias).append("'").toString());
        return factory;
      }
    }
    return null;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  public String getPassphrase() {
    return this.passphrase;
  }

  private static String toHexString(byte[] bytes)
  {
    StringBuilder sb = new StringBuilder(bytes.length * 3);
    for (int b : bytes) {
      b &= 255;
      sb.append(HEXDIGITS[(b >> 4)]);
      sb.append(HEXDIGITS[(b & 0xF)]);
      sb.append(' ');
    }
    return sb.toString();
  }

  public void setResult(StringBuilder sResult)
  {
    this.sResult = sResult;
  }

  public String getResult() {
    return this.sResult.toString();
  }

  public String getContent() {
    return this.content_;
  }

  public void setContent(String content) {
    this.content_ = content;
  }

  public String getUrl() {
    return this.url_;
  }

  public void setUrl(String url) {
    this.url_ = url;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public Properties getProperties() {
    return this.properties;
  }
  
  public Object getHandler() {
	    return this.handler;
  }

  public void setHandler(Object handler) {
    this.handler = handler;
  }

  public void setProperty(String key, String value) {
    this.properties.put(key, value);
  }

  public void setAttribute(Object key, Object value){
	  this.attributes.put(key, value);
  }
  public Object getAttibute(Object key){
	  if(attributes.containsKey(key))
		  return attributes.get(key);
	  return null;
  }
  
  public String getProperty(String key) {
    if (this.properties.containsKey(key))
      return this.properties.getProperty(key);
    return null;
  }
  
  public HttpListener getListener(){
	  return listener_;
  }

  public void setAuthorization(String user, String password) {
    this.user = user;
    this.password = password;
  }

  private static class SavingTrustManager
    implements X509TrustManager
  {
    private final X509TrustManager tm;
    private X509Certificate[] chain;

    SavingTrustManager(X509TrustManager tm)
    {
      this.tm = tm;
    }

    public X509Certificate[] getAcceptedIssuers() {
      throw new UnsupportedOperationException();
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
      throw new UnsupportedOperationException();
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
      this.chain = chain;
      this.tm.checkServerTrusted(chain, authType);
    }
  }

  private static class NullHostnameVerifier
    implements HostnameVerifier
  {
    public boolean verify(String hostname, SSLSession session)
    {
      return true;
    }
  }
}