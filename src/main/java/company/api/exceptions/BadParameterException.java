package company.api.exceptions;

/**
 *
 * @author Reza Naghibi
 */
public class BadParameterException extends Exception {

  public BadParameterException() {
    super("Invalid parameter");
  }

  public BadParameterException(String msg) {
    super(msg);
  }
}
