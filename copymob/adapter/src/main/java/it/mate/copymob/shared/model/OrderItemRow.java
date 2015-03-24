package it.mate.copymob.shared.model;

import java.io.Serializable;

public interface OrderItemRow extends Serializable {

  public Integer getId();

  public void setId(Integer id);

  public void setText(String text);

  public String getText();

  public void setOrderItemId(Integer orderItemId);

  public Integer getOrderItemId();

  public void setBold(boolean bold);

  public boolean isBold();

  public void setSize(int size);

  public int getSize();

  public void setFontFamily(String fontFamily);

  public String getFontFamily();

}