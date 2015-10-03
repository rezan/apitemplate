package company.api.exceptions;

/**
 *
 * @author Reza Naghibi
 */
public class MsgCodeException extends Exception {

  private String msgCode;

  private int respCode;

  private MsgCodeException() {
  }

  public MsgCodeException(String message) {
    this(message, 0, null);
  }

  public MsgCodeException(String message, int respCode, String msgCode) {
    super(message);
    this.respCode = respCode;
    this.msgCode = msgCode;
  }

  public MsgCodeException(Exception ex, int respCode, String msgCode) {
    super(ex);
    this.respCode = respCode;
    this.msgCode = msgCode;
  }

  public String getMsgCode() {
    return msgCode;
  }

  public void setMsgCode(String msgCode) {
    this.msgCode = msgCode;
  }

  public int getRespCode() {
    return respCode;
  }

  public void setRespCode(int respCode) {
    this.respCode = respCode;
  }
}
