package com.miss.server.alert.domain.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @Auther: liq
 * @Date: 2019/6/3 13:54
 * @Description:
 */
public class HttpHelper {

    private static final String ENCODING = HTTP.UTF_8;
    private static final String POST = "POST";
    private static final String GZIP = "gzip";

    private static final int CONNECTION_TIMEOUT_MILLIS = 1000;
    private static final int READ_TIMEOUT_MILLIS = 1000;

    private static Proxy CONNECTION_PROXY = Proxy.NO_PROXY;

    public enum ContentTypeEnum {
        CONTENT_TYPE_JSON("application/json"),
        CONTENT_TYPE_URLENCODE("application/x-www-form-urlencoded"),
        CONTENT_TYPE_TEXT("text/plain");

        private String desc;

        private ContentTypeEnum(String desc) {
            this.desc = desc;
        }

        public String desc() {
            return this.desc;
        }
    }

    /**
     * 发送key-value参数形式post请求
     *
     * @param url       请求url
     * @param bodyParam 放置到的body请求参数
     * @return
     */
    public static String post(String url, JSONObject bodyParam) throws IOException {
        return post(url, bodyParam, null, ContentTypeEnum.CONTENT_TYPE_JSON);
    }

    /**
     * 发送key-value参数形式post请求
     *
     * @param url       请求url
     * @param bodyParam 放置到的body请求参数
     * @return
     */
    public static String post(String url, Map<String, String> bodyParam) throws IOException {
        return post(url, bodyParam, null, ContentTypeEnum.CONTENT_TYPE_JSON);
    }

    /**
     * 发送key-value参数形式post请求
     *
     * @param url       请求url
     * @param bodyParam 放置到的body请求参数
     * @param urlParam  放置到url中的请求参数
     * @return
     */
    public static String post(String url, JSONObject bodyParam, JSONObject urlParam) throws IOException {
        return post(url, bodyParam, urlParam, ContentTypeEnum.CONTENT_TYPE_JSON);
    }

    /**
     * 发送key-value参数形式post请求
     *
     * @param url       请求url
     * @param bodyParam 放置到的body请求参数
     * @param urlParam  放置到url中的请求参数
     * @return
     */
    public static String post(String url, Map<String, String> bodyParam, Map<String, String> urlParam) throws IOException {
        return post(url, bodyParam, urlParam, ContentTypeEnum.CONTENT_TYPE_JSON);
    }

    /**
     * 发送key-value参数形式post请求
     *
     * @param url         请求url
     * @param bodyParam   放置到的body请求参数
     * @param urlParam    放置到url中的请求参数
     * @param contentType 媒体类型
     * @return
     */
    public static <T> String post(String url, Map<String, T> bodyParam, Map<String, T> urlParam, ContentTypeEnum contentType) throws IOException {
        return execute(RequestTypeEnum.POST, url, bodyParam, urlParam, null, contentType);
    }

    /**
     * 发送raw参数形式post请求
     *
     * @param strUrl   请求url
     * @param rawParam 流形式的jaon参数
     * @param headers  头部信息
     * @return
     */
    public static <T> String post(String strUrl, String rawParam, Map<String, T> headers) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = prepareConnection(new URL(strUrl), headers);
            connection.connect();

            try (DataOutputStream send = new DataOutputStream(connection.getOutputStream())) {
                send.writeBytes(rawParam);
                send.flush();
                send.close();
            }
            final boolean useGzip = useGzip(connection);

            try (InputStream answer = getStream(connection.getInputStream(), useGzip)) {
                return readResponse(answer);
            }

//            // read and return value
//            try {
//                try (InputStream answer = getStream(connection.getInputStream(), useGzip)) {
//                    return readResponse(answer);
//                }
//            } catch (IOException e) {
//                try (InputStream answer = getStream(connection.getErrorStream(), useGzip)) {
//                    return readResponse(answer);
//                } catch (IOException ef) {
//                    log.error(readErrorString(connection), ef);
//                    e.printStackTrace();
//                    return null;
//                }
//            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 发送 get请求
     *
     * @param url 请求url
     * @return
     */
    public static String get(String url) throws IOException {
        return execute(RequestTypeEnum.GET, url, null, null, null, ContentTypeEnum.CONTENT_TYPE_JSON);
    }

    /**
     * 发送 get请求
     *
     * @param url      请求url
     * @param urlParam 放置到url中的请求参数
     * @return
     */
    public static <T> String get(String url, Map<String, T> urlParam) throws IOException {
        return execute(RequestTypeEnum.GET, url, null, urlParam, null, ContentTypeEnum.CONTENT_TYPE_JSON);
    }


    // 执行 HttpRequestBase 请求
    private static <T> String execute(RequestTypeEnum requestType, String url, Map<String, T> bodyParam, Map<String, T> urlParam, Map<String, T> headers, ContentTypeEnum contentType) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        System.out.println("===== url ======" + url);

        String params = string2params(urlParam);
        if (StringUtils.isNoneBlank(params)) {
            System.out.println("===== urlParam ======" + urlParam.toString());
            params = "?" + params;
        }

        HttpRequestBase httpRequest;
        switch (requestType) {
            case GET:
                httpRequest = new HttpGet(url + params);
                break;
            case POST:
                httpRequest = new HttpPost(url + params);
                break;
            default:
                return null;
        }

        if (null != headers) {
            for (Map.Entry<String, T> entry : headers.entrySet()) {
                httpRequest.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT_MILLIS).setConnectionRequestTimeout(CONNECTION_TIMEOUT_MILLIS)
                .setSocketTimeout(CONNECTION_TIMEOUT_MILLIS).build();
        httpRequest.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            if (httpRequest instanceof HttpPost && null != bodyParam) {
                System.out.println("===== bodyParam ======" + bodyParam.toString());
                switch (contentType) {
                    case CONTENT_TYPE_JSON:
                    case CONTENT_TYPE_TEXT:
                        ((HttpPost) httpRequest).setEntity(getStringEntity(bodyParam, contentType));
                        break;
                    case CONTENT_TYPE_URLENCODE:
                        ((HttpPost) httpRequest).setEntity(getUrlEncodedFormEntity(bodyParam, contentType));
                        break;
                }
            }

            response = httpClient.execute(httpRequest);

            System.out.println("========HttpResponseProxy：========" + response.getStatusLine());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpRequest.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }

            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, ENCODING);
            }
            EntityUtils.consume(entity);

            System.out.println("========接口返回=======" + result);
            return result;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    /*******************************************************************************/

    private static <T> HttpURLConnection prepareConnection(URL url, Map<String, T> headers) throws IOException {

        // create URLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(CONNECTION_PROXY);
        // 超时设置，防止 网络异常的情况下，可能会导致程序僵死而不继续往下执行
        connection.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setAllowUserInteraction(false);
        connection.setDefaultUseCaches(false);
        // 设置是否从httpUrlConnection读入，默认情况下是true;
        connection.setDoInput(true);
        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
        connection.setDoOutput(true);
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod(POST);

        setupSsl(connection, null, null);
        addHeaders(connection, headers);

        return connection;
    }

    private static InputStream getStream(final InputStream inputStream, final boolean useGzip) throws IOException {
        return useGzip ? new GZIPInputStream(inputStream) : inputStream;
    }

    private static boolean useGzip(final HttpURLConnection connection) {
        String contentEncoding = connection.getHeaderField("Content-Encoding");
        return contentEncoding != null && contentEncoding.equalsIgnoreCase(GZIP);
    }

    private static String readResponse(InputStream answer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(answer, ENCODING));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        reader.close();
        return buffer.toString();
    }

    private static String readErrorString(final HttpURLConnection connection) throws IOException {
        try (InputStream stream = connection.getErrorStream()) {
            StringBuilder buffer = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, ENCODING))) {
                for (int ch = reader.read(); ch >= 0; ch = reader.read()) {
                    buffer.append((char) ch);
                }
            }
            return buffer.toString();
        }
    }

    private static void setupSsl(HttpURLConnection connection, HostnameVerifier hostNameVerifier, SSLContext sslContext) {
        if (HttpsURLConnection.class.isInstance(connection)) {
            HttpsURLConnection https = HttpsURLConnection.class.cast(connection);
            if (hostNameVerifier != null) {
                https.setHostnameVerifier(hostNameVerifier);
            }
            if (sslContext != null) {
                https.setSSLSocketFactory(sslContext.getSocketFactory());
            }
        }
    }

    private static <T> void addHeaders(HttpURLConnection connection, Map<String, T> headers) {
        // 设定传送的内容类型
        connection.setRequestProperty("Content-Type", ContentTypeEnum.CONTENT_TYPE_JSON.desc());
        for (Map.Entry entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    private static <T> HttpEntity getUrlEncodedFormEntity(Map<String, T> params, ContentTypeEnum contentType) throws UnsupportedEncodingException {
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, ENCODING);
        entity.setContentType(contentType.desc());
        return new UrlEncodedFormEntity(pairs, ENCODING);
    }

    private static <T> HttpEntity getStringEntity(Map<String, T> jsonParam, ContentTypeEnum contentType) {
        StringEntity stringEntity = new StringEntity(jsonParam.toString(), ENCODING);
        stringEntity.setContentType(contentType.desc());
        return stringEntity;
    }

    private static <T> String string2params(Map<String, T> param) {
        if (null == param || param.size() < 1) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        for (Map.Entry entry : param.entrySet()) {
            result.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }

    enum RequestTypeEnum {
        POST("POST"),
        GET("GET");

        private String desc;

        private RequestTypeEnum(String desc) {
            this.desc = desc;
        }

        public String desc() {
            return desc;
        }
    }
}