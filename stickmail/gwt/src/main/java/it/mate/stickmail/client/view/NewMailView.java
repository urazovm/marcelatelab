package it.mate.stickmail.client.view;

import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.phgcommons.client.ui.TouchButton;
import it.mate.phgcommons.client.ui.ph.PhCalendarBox;
import it.mate.phgcommons.client.ui.ph.PhTimeBox;
import it.mate.phgcommons.client.utils.PhgDialogUtils;
import it.mate.phgcommons.client.utils.PhonegapUtils;
import it.mate.phgcommons.client.utils.Time;
import it.mate.phgcommons.client.utils.TouchUtils;
import it.mate.phgcommons.client.view.BaseMgwtView;
import it.mate.stickmail.client.factories.AppClientFactory;
import it.mate.stickmail.client.ui.SignPanel;
import it.mate.stickmail.client.view.NewMailView.Presenter;
import it.mate.stickmail.shared.model.StickMail;
import it.mate.stickmail.shared.model.impl.StickMailTx;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.ui.client.widget.MTextArea;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class NewMailView extends BaseMgwtView <Presenter> {

  public interface Presenter extends BasePresenter, SignPanel.Presenter {
    public void goToHome();
  }

  public interface ViewUiBinder extends UiBinder<Widget, NewMailView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField MTextArea bodyArea;
  @UiField PhCalendarBox calBox;
  @UiField PhTimeBox timeBox;
  @UiField TouchButton sendBtn;
  @UiField MTextBox subjectBox;
  @UiField SignPanel signPanel;
  
  private Date scheduledDate;
  private Time scheduledTime;
  
  public NewMailView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initHeaderBackButton("Back", new Delegate<TapEvent>() {
      public void execute(TapEvent element) {
        getPresenter().goToHome();
      }
    });
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
//  onNowBtn(null);
    calBox.setValue(new Date());
    timeBox.setValue(Time.fromDate(new Date()).incHours(1).setMinutes(0));
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    super.setPresenter(presenter);
    signPanel.setRemoteUserDelegate(presenter, null);
  }
  
  @Override
  public void setModel(Object model, String tag) {
    
  }

  @UiHandler ("calBox")
  public void onCalChange (ValueChangeEvent<Date> event) {
    PhonegapUtils.log("new value is " + event.getValue());
    this.scheduledDate = event.getValue();
  }
  
  @UiHandler ("timeBox")
  public void onTimeChange (ValueChangeEvent<Time> event) {
    PhonegapUtils.log("new value is " + event.getValue());
    this.scheduledTime = event.getValue();
  }

  @UiHandler ("nowBtn")
  public void onNowBtn (TouchEndEvent event) {
    Date now = new Date();
    calBox.setValue(now);
    timeBox.setValue(Time.fromDate(now));
  }
  
  @UiHandler ("sendBtn")
  public void onTouchBtn (TapEvent event) {
    
    TouchUtils.applyQuickFixFocusPatch();
    
    if (signPanel.getRemoteUser() == null) {
      PhgDialogUtils.showMessageDialog("You must be signed");
      return;
    }
    
    if (subjectBox.getValue().trim().length() == 0) {
      PhgDialogUtils.showMessageDialog("Subject is empty");
      return;
    }
    
    AppClientFactory.IMPL.getStickFacade().getServerTime(new AsyncCallback<Date>() {
      public void onFailure(Throwable caught) {
        PhonegapUtils.log("FAILURE");
        caught.printStackTrace();
      }
      public void onSuccess(Date serverTime) {
        
        Date clientTime = new Date();
        
//      long deltaTime = clientTime.getTime() - serverTime.getTime();
        long deltaTime = 0;
        
        PhonegapUtils.log("serverTime = " + serverTime);
        
        PhonegapUtils.log("clientTime = " + clientTime);
        
        PhonegapUtils.log("delta time = " + deltaTime);
        
        Date serverScheduled = new Date(scheduledTime.setToDate(scheduledDate).getTime() - deltaTime);
        Date serverCreated = new Date(clientTime.getTime() - deltaTime);
        
        StickMail stickMail = new StickMailTx();
        stickMail.setSubject(subjectBox.getValue());
        stickMail.setBody(bodyArea.getValue());
        stickMail.setUser(signPanel.getRemoteUser());
        stickMail.setScheduled(serverScheduled);
        stickMail.setCreated(serverCreated);
        stickMail.setState(StickMail.STATE_NEW);
        
        AppClientFactory.IMPL.getStickFacade().create(stickMail, new AsyncCallback<StickMail>() {
          public void onSuccess(StickMail result) {
            PhonegapUtils.log("SUCCESS - created " + result);
            getPresenter().goToHome();
          }
          public void onFailure(Throwable caught) {
            PhonegapUtils.log("FAILURE");
          }
        });
        
      }
    });
    
  }
  
}