package it.mate.copymob.server.model;

import it.mate.commons.server.model.CollectionPropertyServerUtil;
import it.mate.commons.server.model.HasKey;
import it.mate.commons.server.model.UnownedRelationship;
import it.mate.copymob.shared.model.OrderItem;
import it.mate.copymob.shared.model.OrderItemRow;
import it.mate.copymob.shared.model.Timbro;
import it.mate.gwtcommons.shared.model.CloneableProperty;
import it.mate.gwtcommons.shared.model.CloneablePropertyMissingException;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable (detachable="true")
public class OrderItemDs implements OrderItem, HasKey {

  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  Key remoteId;
  
  @Persistent
  private Integer id;
  
  private Integer orderId;
  
  @Persistent
  private Double quantity;
  
  @Persistent (dependentKey="false")
  Key timbroKey;
  @UnownedRelationship (key="timbroKey")
  transient TimbroDs timbro;
  
  @Persistent
  private Boolean inCart;
  
  @Persistent (dependentKey="false", defaultFetchGroup="false")
  List<Key> rowKeys;
  @UnownedRelationship (key="rowKeys", itemClass=OrderItemRowDs.class)
  transient List<OrderItemRowDs> rows;
  
  public Key getKey() {
    return remoteId;
  }

  public String getRemoteId() {
    return remoteId != null ? KeyFactory.keyToString(remoteId) : null;
  }
  
  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId != null ? KeyFactory.stringToKey(remoteId) : null;
  }

  public void setTimbroId(Integer timbroId) {
    
  }

  public Integer getTimbroId() {
    return null;
  }

  public boolean isInCart() {
    return false;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public Boolean getInCart() {
    return inCart;
  }

  public void setInCart(Boolean inCart) {
    this.inCart = inCart;
  }

  public Timbro getTimbro() {
    return timbro;
  }

  @CloneableProperty (targetClass=TimbroDs.class)
  public void setTimbro(Timbro timbro) {
    if (timbro == null) {
      this.timbroKey = null;
      this.timbro = null;
    } else if (timbro instanceof TimbroDs) {
      this.timbro = (TimbroDs)timbro;
      this.timbroKey = this.timbro.getKey();
    } else {
      throw new CloneablePropertyMissingException(timbro);
    }
  }

  public List<OrderItemRow> getRows() {
    return rows != null ? new ArrayList<OrderItemRow>(rows) : null;
  }

  @CloneableProperty (targetClass=OrderItemRowDs.class)
  public void setRows(List<OrderItemRow> rows) {
    CollectionPropertyServerUtil<OrderItemRow, OrderItemRowDs> wrapper = CollectionPropertyServerUtil.clone(rows, OrderItemRowDs.class);
    this.rows = wrapper.getItems();
    this.rowKeys = wrapper.getKeys();
  }
  
}