package it.mate.gend.client.factories;

import it.mate.gend.client.api.CommandsProxy;
import it.mate.gend.client.api.GreetingsProxy;
import it.mate.gwtcommons.client.factories.BaseClientFactory;
import it.mate.gwtcommons.client.history.BaseActivityMapper;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.view.BaseMgwtView;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.gwtphonegap.client.PhoneGap;

public interface AppClientFactory extends BaseClientFactory<AppGinjector> {
  
  public static final AppClientFactory IMPL = Initializer.create();

  class Initializer {
    private static AppClientFactory create() {
      AppClientFactory clientFactory = new AppClientFactoryImpl();
      return clientFactory;
    }
  }
  
  public void initMvp(SimplePanel panel, BaseActivityMapper activityMapper);
  
  public com.google.web.bindery.event.shared.EventBus getBinderyEventBus();
  
  public PhoneGap getPhoneGap();
  
  public int getWrapperPct();
  
  public void adaptWrapperPanel(Panel wrapperPanel, String id, boolean adaptVerMargin, int headerPanelHeight, Delegate<Element> delegate);

  public int getTabletWrapperHeight();
  
  public int getTabletWrapperWidth();
  
  public void initTitle(BaseMgwtView view);
  
  public GreetingsProxy getGreetingsProxy();
  
  public CommandsProxy getCommandsProxy();
  
//public void adaptWrapperPanel(BaseMgwtView mgwtView, final Panel wrapperPanel);
  
}
