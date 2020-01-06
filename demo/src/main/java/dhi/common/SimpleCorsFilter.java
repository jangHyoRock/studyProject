package dhi.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)

@WebFilter(
        urlPatterns = "/uploadFilter",
        initParams = @WebInitParam(name = "fileTypes", value = "doc;xls;zip;txt;jpg;png;gif;js")       
)
public class SimpleCorsFilter implements Filter {
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    //login id 값 null 예외처리
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    	HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    	
    	//httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        //httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    	
    	httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");        
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");        

        // logger.info("########## " + httpServletRequest.getRequestURL()+" #############");
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
