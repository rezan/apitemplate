package company.api.servlet;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Reza Naghibi
 */
public class Echo extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String param=request.getParameter("param");

    StringBuilder sb=new StringBuilder();
    sb.append("{\"servlet\":true,\"param\":\"").append(param).append("\"}");

    response.setHeader("Content-Type", "application/json");

    Writer writer=response.getWriter();
    writer.write(sb.toString());
    writer.flush();
  }

}
