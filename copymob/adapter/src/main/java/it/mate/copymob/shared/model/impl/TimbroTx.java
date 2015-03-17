package it.mate.copymob.shared.model.impl;

import it.mate.copymob.shared.model.Timbro;
import it.mate.gwtcommons.shared.rpc.IsMappable;
import it.mate.gwtcommons.shared.rpc.RpcMap;

@SuppressWarnings("serial")
public class TimbroTx implements Timbro, IsMappable {
  
  private Integer id;
  
  private String nome;
  
  private String codice;
  
  private Double width;
  
  private Double height;
  
  private Boolean oval;

  private String image;
  
  private String remoteId;

  private Double prezzo;

  @Override
  public String toString() {
    return "TimbroTx [id=" + id + ", nome=" + nome + ", codice=" + codice + ", width=" + width + ", height=" + height + ", oval=" + oval + ", remoteId="
        + remoteId + ", prezzo=" + prezzo + "]";
  }

  public TimbroTx() {

  }

  protected TimbroTx(Integer id) {
    this.id = id;
  }

  @Override
  public RpcMap toRpcMap() {
    RpcMap map = new RpcMap();
    map.put("id", id);
    map.put("nome", nome);
    map.put("codice", codice);
    map.put("width", width);
    map.put("height", height);
    map.put("oval", oval);
    map.put("image", image);
    map.put("remoteId", remoteId);
    map.put("prezzo", prezzo);
    return map;
  }

  @Override
  public TimbroTx fromRpcMap(RpcMap map) {
    this.id = (Integer)map.get("id");
    this.nome = (String)map.get("nome");
    this.codice = (String)map.get("codice");
    this.width = (Double)map.get("width");
    this.height = (Double)map.get("height");
    this.oval = (Boolean)map.get("oval");
    this.image = (String)map.get("image");
    this.remoteId = (String)map.get("remoteId");
    this.prezzo = (Double)map.get("prezzo");
    return this;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getImage() {
    return image;
  }
  
  public String getImageData() {
    return !image.startsWith("data:") ? ("data:image/jpeg;base64," + image) : image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public String getRemoteId() {
    return remoteId;
  }

  public void setRemoteId(String remoteId) {
    this.remoteId = remoteId;
  }

  public Boolean getOval() {
    return oval;
  }

  public void setOval(Boolean oval) {
    this.oval = oval;
  }

  public Double getPrezzo() {
    return prezzo;
  }

  public void setPrezzo(Double prezzo) {
    this.prezzo = prezzo;
  }
  
}
