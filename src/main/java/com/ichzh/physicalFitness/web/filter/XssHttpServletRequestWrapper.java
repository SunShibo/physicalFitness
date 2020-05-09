package com.ichzh.physicalFitness.web.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

/**
 * spring boot 接收参数 有@RequestBody形式接收，有@RequestParam形式接收，这两种情况需要单独处理
 * @author yangjinfei
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    HttpServletRequest orgRequest = null;
    
    private String body;
 
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
        body = HttpRequestBodyReader.getBodyString(request);
    }
 
    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name, 0));
        if (null != value) {
            value = xssEncode(value, 0);
        }
        return value;
    }
 
    @Override
    public String[] getParameterValues(String name) {
    	
    	
        String[] values = super.getParameterValues(xssEncode(name, 0));
        if (values == null) {
            return null;
        }
        if(values!=null && name.equals("noticeJSON") ){
    		return values;
    	}	
        if(values!=null && name.equals("emall") ){
    		return values;
    	}	
        
        
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = xssEncode(values[i], 0);
        }
        return encodedValues;
    }
 
    @Override
    public Map getParameterMap() {
 
        HashMap paramMap = (HashMap) super.getParameterMap();
        paramMap = (HashMap) paramMap.clone();
 
        for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {
                    values[i] = xssEncode(values[i], 0);
                }
            }
            entry.setValue(values);
        }
        return paramMap;
    }
 
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream inputStream = null;
        if (StringUtils.isNotEmpty(body)) {
            body = xssEncode(body, 1);
            ByteArrayInputStream byteArrayInputStream = TranslateServletInputStream.stringToByteInputStream(body);
            inputStream = TranslateServletInputStream.InputStreamToServletInputStream(byteArrayInputStream);
        }
        return inputStream;
    }
 
    
   
    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name, 0));
        if (value != null) {
            value = xssEncode(value, 0);
        }
        return value;
    }
 
    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private static String xssEncode(String s, int type) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (type == 0) {
            	 switch (c) {
                 case '\'':
                     // 全角单引号
                     sb.append("");
                     break;
//                 case '"':
//                     // 全角双引号
//                     sb.append("");
//                     break;
                 case '>':
                     // 全角大于号
                     sb.append("");
                     break;
                 case '<':
                     // 全角小于号
                     sb.append("");
                     break;
                 case '&':
                     // 全角&符号
                     sb.append("");
                     break;  
                     
                 case '\n':
                     // 全角斜线
                     sb.append("");
                     break;
                 case '\t':
                     // 全角斜线
                     sb.append("");
                     break;
                 case '\0':
                     // 全角斜线
                     sb.append("");
                     break;
                 case '@':
                     // 全角斜线
                     sb.append("");
                     break;
                
                 case '\\':
                     // 全角斜线
                     sb.append("");
                     break;   
                 case '#':
                     // 全角井号
                     sb.append("");
                     break;
                  
                 // < 字符的 URL 编码形式表示的 ASCII 字符（十六进制格式） 是: %3c
                 case '%':
                     processUrlEncoder(sb, s, i);
                     break;
                 default:
                     sb.append(c);
                     break;
           	  }
            } else {
            	  switch (c) {
                  case '\'':
                      // 全角单引号
                      sb.append("");
                      break;
//                  case '"':
//                      // 全角双引号
//                      sb.append("");
//                      break;
                  case '>':
                      // 全角大于号
                      sb.append("");
                      break;
                  case '<':
                      // 全角小于号
                      sb.append("");
                      break;
                  case '&':
                      // 全角&符号
                      sb.append("");
                      break;  
                      
                  case '\n':
                      // 全角斜线
                      sb.append("");
                      break;
                  case '\t':
                      // 全角斜线
                      sb.append("");
                      break;
                  case '\0':
                      // 全角斜线
                      sb.append("");
                      break;
                  case '@':
                      // 全角斜线
                      sb.append("");
                      break;
                 
                  case '\\':
                      // 全角斜线
                      sb.append("");
                      break;   
                  case '#':
                      // 全角井号
                      sb.append("");
                      break;
                   
                  // < 字符的 URL 编码形式表示的 ASCII 字符（十六进制格式） 是: %3c
                  case '%':
                      processUrlEncoder(sb, s, i);
                      break;
                  default:
                      sb.append(c);
                      break;
            	  }
              }
 
        }
        return sb.toString();
    }
 
    public static void processUrlEncoder(StringBuilder sb, String s, int index) {
        if (s.length() >= index + 2) {
            // %3c, %3C
            if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'c' || s.charAt(index + 2) == 'C')) {
                sb.append('＜');
                return;
            }
            // %3c (0x3c=60)
            if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '0') {
                sb.append('＜');
                return;
            }
            // %3e, %3E
            if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'e' || s.charAt(index + 2) == 'E')) {
                sb.append('＞');
                return;
            }
            // %3e (0x3e=62)
            if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '2') {
                sb.append('＞');
                return;
            }
        }
        sb.append(s.charAt(index));
    }
 
    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }
 
    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }
}
