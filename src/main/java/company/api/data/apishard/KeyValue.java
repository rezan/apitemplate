package company.api.data.apishard;

import company.api.storage.dbrouting.ThreadDBChooser;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *
 * @author Reza Naghibi
 */
@Entity
@Table(indexes = {@Index(columnList = "keyStr", name = "keyStrIndex")})
public class KeyValue implements Serializable {

  static final long serialVersionUID = 10000001;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Basic
  private String keyStr;

  @Basic
  private String valueStr;

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();

    s.append("id=").append(getId()).append(",");
    s.append("keyStr='").append(getKeyStr()).append("',");
    s.append("valueStr='").append(getValueStr()).append("'");

    return s.toString();
  }

  public static void setShard(String key) {
    char c = Character.toLowerCase(key.charAt(0));

    if(c >= 'a' && c <= 'k') {
      ThreadDBChooser.setDB("apishard0");
    } else {
      ThreadDBChooser.setDB("apishard1");
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKeyStr() {
    return keyStr;
  }

  public void setKeyStr(String keyStr) {
    this.keyStr = keyStr;
  }

  public String getValueStr() {
    return valueStr;
  }

  public void setValueStr(String valueStr) {
    this.valueStr = valueStr;
  }
}
