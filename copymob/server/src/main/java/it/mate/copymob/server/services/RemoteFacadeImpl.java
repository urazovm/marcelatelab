package it.mate.copymob.server.services;

import it.mate.commons.server.utils.CloneUtils;
import it.mate.copymob.shared.model.Timbro;
import it.mate.copymob.shared.model.impl.AccountTx;
import it.mate.copymob.shared.model.impl.DevInfoTx;
import it.mate.copymob.shared.model.impl.OrderTx;
import it.mate.copymob.shared.model.impl.TimbroTx;
import it.mate.copymob.shared.service.RemoteFacade;
import it.mate.copymob.shared.service.RemoteFacadeException;
import it.mate.gwtcommons.shared.rpc.RpcMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RemoteFacadeImpl extends RemoteServiceServlet implements RemoteFacade {

  private static Logger logger = Logger.getLogger(RemoteFacadeImpl.class);
  
  private RemoteAdapter adapter = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    AdapterUtil.initContext(config.getServletContext());
    adapter = AdapterUtil.getRemoteAdapter();
    logger.debug("initialized " + this);
  }
  
  @Override
  public Date getServerTime() {
    return new Date();
  }
  
  @Override
  public RpcMap sendDevInfo(RpcMap map) {
    DevInfoTx devInfo = new DevInfoTx().fromRpcMap(map);
    devInfo.setDevIp(getThreadLocalRequest().getRemoteAddr());
    devInfo = (DevInfoTx)adapter.sendDevInfo(devInfo);
    return devInfo.toRpcMap();
  }

  @Override
  public RpcMap saveAccount(RpcMap entity) {
    AccountTx tx = new AccountTx().fromRpcMap(entity);
    tx = (AccountTx)adapter.saveAccount(tx);
    return tx.toRpcMap();
  }
  
  @Override
  public Boolean checkConnection() {
    return true;
  }
  
  @Override
  public List<RpcMap> getTimbri() throws RemoteFacadeException {
    try {
      List<RpcMap> results = new ArrayList<RpcMap>();
      List<Timbro> timbri = adapter.getTimbri();
      for (Timbro timbro : timbri) {
        results.add(CloneUtils.clone(timbro, TimbroTx.class).toRpcMap());
      }
      return results;
    } catch (Exception ex) {
      throw new RemoteFacadeException(ex.getMessage());
    }
  }

  @Override
  public RpcMap saveOrder(RpcMap entity) {
    OrderTx tx = new OrderTx().fromRpcMap(entity);
    tx = (OrderTx)adapter.saveOrder(tx);
    return tx.toRpcMap();
  }

}
