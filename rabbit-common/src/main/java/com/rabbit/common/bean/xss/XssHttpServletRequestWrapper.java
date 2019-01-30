package com.rabbit.common.bean.xss;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName XssHttpServletRequestWrapper
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/30 21:56
 **/
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    HttpServletRequest orgRequest;
    private static final HtmlFilter HTML_FILTER = new HtmlFilter();

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.orgRequest = request;
    }

    public ServletInputStream getInputStream()
            throws IOException
    {
        if (!"application/json".equalsIgnoreCase(super.getHeader("Content-Type"))) {
            return super.getInputStream();
        }

        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        }

        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
        return new ServletInputStream()
        {
            public boolean isFinished() {
                return true;
            }

            public boolean isReady()
            {
                return true;
            }

            public void setReadListener(ReadListener readListener)
            {
            }

            public int read()
            {
                return bis.read();
            }
        };
    }

    public String getParameter(String name)
    {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    public String[] getParameterValues(String name)
    {
        String[] parameters = super.getParameterValues(name);
        if ((parameters == null) || (parameters.length == 0)) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    public Map<String, String[]> getParameterMap()
    {
        Map map = new LinkedHashMap();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    public String getHeader(String name)
    {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        return HTML_FILTER.filter(input);
    }

    public HttpServletRequest getOrgRequest()
    {
        return this.orgRequest;
    }

    public static HttpServletRequest getOrgRequest(HttpServletRequest request)
    {
        if ((request instanceof XssHttpServletRequestWrapper)) {
            return ((XssHttpServletRequestWrapper)request).getOrgRequest();
        }

        return request;
    }
}
