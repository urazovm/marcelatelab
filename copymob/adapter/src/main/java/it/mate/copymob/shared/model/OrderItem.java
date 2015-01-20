package it.mate.copymob.shared.model;

import java.io.Serializable;
import java.util.List;

public interface OrderItem extends Serializable {

  public Integer getId();

  public void setId(Integer id);

  public void setQuantity(Double quantity);

  public Double getQuantity();

  public void setTimbroId(Integer timbroId);

  public Integer getTimbroId();

  public void setOrderId(Integer orderId);

  public Integer getOrderId();
  
  public List<OrderItemRow> getRows();

  public void setRows(List<OrderItemRow> rows);

  public void setTimbro(Timbro timbro);

  public Timbro getTimbro();

}
