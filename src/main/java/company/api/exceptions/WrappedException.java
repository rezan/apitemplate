package company.api.exceptions;

/**
 *
 * @author Reza Naghibi
 */
public class WrappedException extends MsgCodeException {

  private Exception exception;

  public WrappedException(Exception exception, int respCode, String msgCode) {
    super(exception, respCode, msgCode);
    this.exception = exception;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

}
