package company.api.controller;

import company.api.data.apidb.EmailSignup;
import company.api.data.apishard.KeyValue;
import company.api.exceptions.BadParameterException;
import company.api.exceptions.MsgCodeException;
import company.api.settings.Configuration;
import company.api.utils.Utilities;
import company.api.storage.DBStorageManager;

import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
  private DBStorageManager dbStorageManager;

  @Autowired
  private Configuration configuration;

  @RequestMapping(value = "/keyvalue", method = RequestMethod.GET)
  public String keyValue(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "key", required = false) String key) throws Exception {

    log.info("*API* keyValue() key: '" + key + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(key)) {
      throw new BadParameterException("'key' required");
    }

    KeyValue keyValue = dbStorageManager.getKey(key);

    if(keyValue == null) {
      throw new BadParameterException("Key not found");
    }

    map.put("key", keyValue.getKeyStr());
    map.put("value", keyValue.getValueStr());

    map.put("apiVersion", configuration.getProperty("project.version"));
    map.put("apiBuildDate", configuration.getProperty("project.build.date"));
    map.put("environment", configuration.getProperty("env"));

    model.addAttribute("params", map);

    return "200";
  }

  @RequestMapping(value = "/emailsignup", method = RequestMethod.POST)
  public String emailSignup(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "email", required = false) String email) throws Exception {

    log.info("*API* emailSignup() email: '" + email + "'");

    if(Utilities.empty(email)) {
      throw new BadParameterException("'email' required");
    }

    email = email.toLowerCase();

    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (Exception ex) {
      throw new MsgCodeException("Invalid email address");
    }

    EmailSignup signup = new EmailSignup(email);

    dbStorageManager.storeEmailSignup(signup);

    return "200";
  }
}
