package it.mate.protons.client.factories;

import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.onscommons.client.mvp.OnsGinModule;
import it.mate.protons.client.activities.mapper.MainActivityMapper;
import it.mate.protons.client.view.HomeView;
import it.mate.protons.client.view.SearchView;
import it.mate.protons.client.view.SettingsView;
import it.mate.protons.client.view.SubSettingsView;
import it.mate.protons.shared.service.RemoteFacadeAsync;

import com.google.gwt.inject.client.GinModules;

//@GinModules ({CommonGinModule.class, AppGinModule.class})
@GinModules ({OnsGinModule.class, AppGinModule.class})
public interface AppGinjector extends CommonGinjector {
  
  public MainActivityMapper getMainActivityMapper();
  
  public RemoteFacadeAsync getRemoteFacade();
  
  public HomeView getHomeView();
  
  public SettingsView getSettingsView();
  
  public SubSettingsView getSubSettingsView();
  
  public SearchView getSearchView();
  
}
