package company.api.service;

import company.data.apishard.KeyValue;
import company.data.apidb.EmailSignup;
import company.api.exceptions.BadParameterException;
import company.api.exceptions.MsgCodeException;
import company.api.utils.Utilities;
import company.api.settings.Configuration;
import company.api.storage.DBStorageManager;

import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

/**
 *
 * @author Reza Naghibi
 */
public class GeneralApiService {

  private static final Logger log = Logger.getLogger(GeneralApiService.class);

  @Autowired
  private DBStorageManager dbStorageManager;

  @Autowired
  private Configuration configuration;

  public String keyValue(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      String key) throws Exception {

    log.info("*API* keyValue() key: '" + key + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(key)) {
      throw new BadParameterException();
    }

    KeyValue keyValue = dbStorageManager.getKey(key);

    if(keyValue == null) {
      throw new BadParameterException();
    }

    map.put("key", keyValue.getKeyStr());
    map.put("value", keyValue.getValueStr());

    map.put("apiVersion", configuration.getProperty("project.version"));
    map.put("apiBuildDate", configuration.getProperty("project.build.date"));

    model.addAttribute("params", map);

    return "200";
  }

  public String emailSignup(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      String email) throws Exception {

    log.info("*API* emailSignup() email: '" + email + "'");

    if(Utilities.empty(email)) {
      throw new BadParameterException();
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
