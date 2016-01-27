package company.api.controller;

import company.api.data.apidb.EmailSignup;
import company.api.data.apishard.KeyValue;
import company.api.exceptions.BadParameterException;
import company.api.exceptions.MsgCodeException;
import company.api.settings.FileSettings;
import company.api.utils.Utilities;
import company.api.storage.StorageManager;

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
  private StorageManager storageManager;

  @Autowired
  private FileSettings fileSettings;

  @RequestMapping(value = "/keyvalue", method = RequestMethod.GET)
  public String getKeyValue(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "key", required = false) String key) throws Exception {

    log.info("*API* getKeyValue() key: '" + key + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(key)) {
      throw new BadParameterException("'key' required");
    }

    KeyValue keyValue = storageManager.getKeyValue(key);

    if(keyValue == null) {
      throw new BadParameterException("Key not found");
    }

    map.put("key", keyValue.getKeyStr());
    map.put("value", keyValue.getValueStr());

    map.put("apiVersion", fileSettings.getProperty("project.version"));
    map.put("apiBuildDate", fileSettings.getProperty("project.build.date"));
    map.put("environment", fileSettings.getProperty("env"));

    model.addAttribute("params", map);

    return "200";
  }

  @RequestMapping(value = "/keyvalue", method = RequestMethod.POST)
  public String storeKeyValue(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "key", required = false) String key,
      @RequestParam(value = "value", required = false) String value) throws Exception {

    log.info("*API* storeKeyValue() key: '" + key + "' value: '" + value + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(key)) {
      throw new BadParameterException("'key' required");
    }

    if(value == null) {
      value = "";
    }

    KeyValue keyValue = new KeyValue(key, value);

    storageManager.storeKeyValue(keyValue);

    map.put("key", keyValue.getKeyStr());
    map.put("value", keyValue.getValueStr());

    map.put("apiVersion", fileSettings.getProperty("project.version"));
    map.put("apiBuildDate", fileSettings.getProperty("project.build.date"));
    map.put("environment", fileSettings.getProperty("env"));

    model.addAttribute("params", map);

    return "200";
  }

  @RequestMapping(value = "/s3", method = RequestMethod.GET)
  public String getS3(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "file", required = false) String file) throws Exception {

    log.info("*API* storeS3() file: '" + file + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(file)) {
      throw new BadParameterException("'file' required");
    }

    String content = storageManager.getS3(file);

    map.put("file", file);
    map.put("content", content);

    map.put("apiVersion", fileSettings.getProperty("project.version"));
    map.put("apiBuildDate", fileSettings.getProperty("project.build.date"));
    map.put("environment", fileSettings.getProperty("env"));

    model.addAttribute("params", map);

    return "200";
  }

  @RequestMapping(value = "/s3", method = RequestMethod.POST)
  public String storeS3(HttpServletRequest request, HttpServletResponse response, ModelMap model,
      @RequestParam(value = "file", required = false) String file,
      @RequestParam(value = "content", required = false) String content) throws Exception {

    log.info("*API* storeS3() file: '" + file + "' content: '" + content + "'");

    Map<String, String> map = new HashMap<>();

    if(Utilities.empty(file)) {
      throw new BadParameterException("'file' required");
    }

    if(content == null) {
      content = "";
    }

    storageManager.storeS3(file, content);

    map.put("file", file);
    map.put("content", content);

    map.put("apiVersion", fileSettings.getProperty("project.version"));
    map.put("apiBuildDate", fileSettings.getProperty("project.build.date"));
    map.put("environment", fileSettings.getProperty("env"));

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

    storageManager.storeEmailSignup(signup);

    return "200";
  }
}
