package company.api.exceptions;

/**
 *
 * @author Reza Naghibi
 */
public class UnauthorizedException extends Exception {

  public UnauthorizedException() {
    super("Unauthorized");
  }
}
