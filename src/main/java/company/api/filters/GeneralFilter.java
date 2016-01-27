package company.api.filters;

import company.api.utils.Utilities;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Reza Naghibi
 */
public class GeneralFilter implements Filter {

  private static final Logger log = Logger.getLogger(GeneralFilter.class);

  @Override
  public void init(FilterConfig config) throws ServletException {
    log.info("GeneralFilter initialized");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    long start = System.nanoTime();

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    chain.doFilter(request, response);

    long diff = System.nanoTime() - start;
    log.info("Call '" + httpRequest.getMethod() + " " + httpRequest.getRequestURI() + "' took " + Utilities.getTime(diff));
  }

  @Override
  public void destroy() {
  }

}
