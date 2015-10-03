package company.api.controller;

import company.api.exceptions.MsgCodeException;
import company.api.service.GeneralApiService;
import company.api.utils.Utilities;
import company.api.settings.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Reza Naghibi
 */
@Controller
@RequestMapping("/general")
public class GeneralAPI extends ExceptionAPI {

  private static final Logger log = Logger.getLogger(GeneralAPI.class);

  @Autowired
  private GeneralApiService generalApiService;

  @RequestMapping(value = "/{action}", method = {RequestMethod.GET, RequestMethod.POST})
  public String generalController(HttpServletRequest request, HttpServletResponse response, ModelMap model,
          @PathVariable("action") String action,
          @RequestParam(value = "key", required = false) String key,
          @RequestParam(value = "email", required = false) String email) throws Exception {

    if(Utilities.empty(action)) {
      throw new MsgCodeException("Invalid path: '" + action + "'", 404, Constants.ERROR_PARAM);
    }

    action = action.toLowerCase();

    log.info("generalController action: '" + action + "'");

    if(action.equals("keyvalue")) {
      return generalApiService.keyValue(request, response, model, key);
    } else if(action.equals("emailsignup")) {
      return generalApiService.emailSignup(request, response, model, email);
    }

    throw new MsgCodeException("Invalid path: '" + action + "'", 404, Constants.ERROR_PARAM);
  }
}
