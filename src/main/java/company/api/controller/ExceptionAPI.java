package company.api.controller;

import company.api.exceptions.BadParameterException;
import company.api.exceptions.MsgCodeException;
import company.api.exceptions.UnauthorizedException;
import company.api.exceptions.WrappedException;
import company.api.utils.Utilities;
import company.api.settings.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Reza Naghibi
 */
public class ExceptionAPI {

  private static final Logger log = Logger.getLogger(ExceptionAPI.class);

  @ExceptionHandler({BadParameterException.class, MissingServletRequestParameterException.class})
  public ModelAndView handleBadParameterException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
    return processException(ex, request, response, 400, Constants.ERROR_PARAM);
  }

  @ExceptionHandler({UnauthorizedException.class})
  public ModelAndView handleUnauthorizedException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
    return processException(ex, request, response, 401, Constants.ERROR_AUTH);
  }

  @ExceptionHandler(WrappedException.class)
  public ModelAndView handleWrappedException(WrappedException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
    return processException(ex.getException(), request, response, ex.getRespCode(), ex.getMsgCode());
  }

  @ExceptionHandler(MsgCodeException.class)
  public ModelAndView handleMsgCodeException(MsgCodeException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
    return processException(ex, request, response, ex.getRespCode(), ex.getMsgCode());
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
    return processException(ex, request, response, 500, Constants.ERROR_GENERAL);
  }

  public ModelAndView processException(Exception ex, HttpServletRequest request, HttpServletResponse response, int respCode, String msgCode) throws Exception {
    if(respCode <= 0) {
      respCode = 500;
    }

    if(Utilities.empty(msgCode)) {
      msgCode = Constants.ERROR_GENERAL;
    }

    String stack = getStackTrace(ex);
    response.sendError(respCode, ex.toString());

    log.error("Exception caught (" + msgCode + "): " + ex.toString() + ": " + stack);

    ModelMap model = new ModelMap();
    model.addAttribute("message", Utilities.formatJSON(ex.getMessage()));
    model.addAttribute("msgCode", msgCode);
    model.addAttribute("exclass", Utilities.formatJSON(ex.getClass().toString()));
    model.addAttribute("stack", Utilities.formatJSON(stack));

    if(request.getAttribute("nparams") != null) {
      model.addAttribute("nparams", request.getAttribute("nparams"));
    }

    return new ModelAndView("400", model);
  }

  public String getStackTrace(Exception ex) {
    StringBuilder sb = new StringBuilder();
    int count = 0;

    for(int i = 0; count < 3 && i < ex.getStackTrace().length; i++) {
      String stack = ex.getStackTrace()[i].toString();
      if(!stack.contains(Constants.PACKAGE_PREFIX) && count > 0) {
        continue;
      }
      if(count > 0) {
        sb.append("; ");
      }
      sb.append(stack);
      count++;
    }

    return sb.toString();
  }
}
