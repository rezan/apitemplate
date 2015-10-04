package company.api.tags;

import company.api.utils.Utilities;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author Reza Naghibi
 */
public class JsonFormat extends SimpleTagSupport {

  @Override
  public void doTag() throws JspException, IOException {
    StringWriter sw = new StringWriter();
    getJspBody().invoke(sw);
    String json = Utilities.formatJSON(sw.toString());
    getJspContext().getOut().print(json);
  }
}
