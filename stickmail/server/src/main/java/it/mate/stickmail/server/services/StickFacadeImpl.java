package it.mate.stickmail.server.services;

import it.mate.stickmail.shared.model.RemoteUser;
import it.mate.stickmail.shared.model.StickMail;
import it.mate.stickmail.shared.service.StickFacade;

import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class StickFacadeImpl extends RemoteServiceServlet implements StickFacade {

  private static Logger logger = Logger.getLogger(StickFacadeImpl.class);
  
  private StickAdapter adapter;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    logger.debug("initialized " + this);
//  AdapterUtil.initContext();
    adapter = AdapterUtil.getAdapter();
  }
  
  @Override
  public RemoteUser getRemoteUser() {
    UserService userService = UserServiceFactory.getUserService();
    User googleUser = userService.getCurrentUser();
    if (googleUser == null) {
      logger.error("", new javax.servlet.UnavailableException("GOOGLE USER NOT SET!"));
      return null;
    }
    RemoteUser remoteUser = new RemoteUser();
    remoteUser.setEmail(googleUser.getEmail());
    remoteUser.setUserId(googleUser.getUserId());
    remoteUser.setNickname(googleUser.getNickname());
    remoteUser.setAuthDomain(googleUser.getAuthDomain());
    remoteUser.setFederatedIdentity(googleUser.getFederatedIdentity());
    return remoteUser;
  }

  @Override
  public Date getServerTime() {
    return new Date();
  }

  @Override
  public void createStickMail(StickMail stickMail) {
    logger.debug("received " + stickMail);
  }
  
}
