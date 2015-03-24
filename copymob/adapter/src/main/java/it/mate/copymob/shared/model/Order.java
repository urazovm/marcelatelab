package it.mate.copymob.shared.model;

import java.io.Serializable;
import java.util.List;

public interface Order extends Serializable {
  
  public final static int STATE_OPEN = 0;

  public final static int STATE_SENT = 1;

  public final static int STATE_RECEIVED = 2;

  public final static int STATE_PREVIEW_IN_PROGRESS = 3;

  public final static int STATE_PREVIEW_AVAILABLE = 4;

  public final static int STATE_PREVIEW_PAYED = 5;

  public final static int STATE_WORK_IN_PROGRESS = 6;

  public final static int STATE_CLOSE = 7;

  public final static int STATE_SHIPED = 8;

  public Integer getId();

  public void setId(Integer id);

  public String getCodice();

  public void setCodice(String codice);

  public Integer getAccountId();

  public void setAccountId(Integer accountId);

  public void setItems(List<OrderItem> items);

  public List<OrderItem> getItems();

  public int getState();

  public void setState(int state);
  
}