package it.mate.therapyreminder.client.factories;

import it.mate.gwtcommons.client.factories.CommonGinModule;
import it.mate.gwtcommons.client.factories.CommonGinjector;
import it.mate.therapyreminder.client.activities.mapper.MainActivityMapper;
import it.mate.therapyreminder.client.view.CalendarEventTestView;
import it.mate.therapyreminder.client.view.EditTherapyView;
import it.mate.therapyreminder.client.view.HomeView;
import it.mate.therapyreminder.shared.service.StickFacadeAsync;

import com.google.gwt.inject.client.GinModules;

@GinModules ({CommonGinModule.class, AppGinModule.class})
public interface AppGinjector extends CommonGinjector {
  
  public MainActivityMapper getMainActivityMapper();
  
  public StickFacadeAsync getStickFacade();
  
  public HomeView getHomeView();
  
  public EditTherapyView getEditTherapyView();
  
  public CalendarEventTestView getCalendarEventTestView();
  
}
