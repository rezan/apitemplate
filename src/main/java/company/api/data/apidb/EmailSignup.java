package company.api.data.apidb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Reza Naghibi
 */
@Entity
public class EmailSignup implements Serializable {

  static final long serialVersionUID = 10000001;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Basic
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date signupDate;

  @Basic
  private String email;

  public EmailSignup() {
  }

  public EmailSignup(String email) {
    this.email = email;
    this.signupDate = new Date();
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();

    s.append("id=").append(getId()).append(",");
    s.append("signupDate='").append(getSignupDate()).append("',");
    s.append("email='").append(getEmail()).append("'");

    return s.toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getSignupDate() {
    return signupDate;
  }

  public void setSignupDate(Date signupDate) {
    this.signupDate = signupDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
