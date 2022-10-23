package es.technest.security.api.config.http;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class MakeMultiReadableRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        MultiReadableRequestWrapper wrappedRequest = new MultiReadableRequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig)  {}

    @Override
    public void destroy() { }

    private class MultiReadableRequestWrapper extends HttpServletRequestWrapper {
        private byte[] body;

        public MultiReadableRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                body = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException ex) {
                body = new byte[0];
            }
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream() ,getCharacterEncoding()));
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ServletInputStream() {
                ByteArrayInputStream bais = new ByteArrayInputStream(body);

                @Override
                public int read() {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }
            };
        }

    }
}
