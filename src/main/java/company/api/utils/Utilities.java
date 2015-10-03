package company.api.utils;

import org.apache.log4j.Logger;

public class Utilities {

  private static final Logger log = Logger.getLogger(Utilities.class);

  public Utilities() {
    log.info("Utilities initialized");
  }

  public static String getTime(long diffn) {
    return diffn / (1000 * 1000 * 1000) + "s " + diffn / (1000 * 1000) % 1000
        + "ms " + diffn / 1000 % 1000 + "us " + diffn % 1000 + "ns";
  }

  public static boolean empty(String... args) {
    for(String arg : args) {
      if(arg == null || arg.isEmpty() || arg.trim().isEmpty()) {
        return true;
      }
    }

    return false;
  }

  public static String formatJSON(String s) {
    if(empty(s)) {
      return s;
    }

    return s.replace("\"", "\\\"").replace("\n", " ");
  }
}
