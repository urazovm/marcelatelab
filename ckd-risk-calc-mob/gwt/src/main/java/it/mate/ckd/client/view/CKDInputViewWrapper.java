package it.mate.ckd.client.view;

import it.mate.ckd.client.model.CKD;
import it.mate.ckd.client.utils.OsDetectionPatch;
import it.mate.ckd.client.view.CKDInputViewWrapper.Presenter;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.mvp.BaseView;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;

public class CKDInputViewWrapper extends BaseMgwtView <Presenter> {
  
  private boolean isTablet = OsDetectionPatch.isTablet();
  
  private CKDInputView inputView;
  private CKDOutputView outputView;

  public interface Presenter extends BasePresenter {
    void goToCkdOutput(CKD ckd);
    void goToHome();
  }

  public CKDInputViewWrapper() {
    initUI();
  }

  private void initUI() {
    
    HorizontalPanel layoutPanel = new HorizontalPanel();
    
    inputView = new CKDInputView();
    layoutPanel.add(inputView.asWidget());

    if (isTablet) {
      outputView = new CKDOutputView();
      outputView.setVisible(false);
      layoutPanel.add(outputView.asWidget());
    }
    
    initWidget(layoutPanel);
    
    getHeaderBackButton().setText("Home");
    getHeaderBackButton().addTapHandler(new TapHandler() {
      public void onTap(TapEvent event) {
        getPresenter().goToHome();
      }
    });
    
  }
  
  @Override
  public void setModel(Object model, String tag) {
    inputView.setModel(model, tag);
  }
  
  @Override
  public void setPresenter(final Presenter presenter) {
    super.setPresenter(presenter);
    
    inputView.setPresenter(new CKDInputView.Presenter() {
      public void goToPrevious() {

      }
      public BaseView getView() {
        return null;
      }
      public void goToHome() {
        presenter.goToHome();
      }
      public void goToCkdOutput(CKD ckd) {
        if (isTablet) {
          outputView.setVisible(true);
          outputView.setModel(ckd, null);
        } else {
          presenter.goToCkdOutput(ckd);
        }
      }
    });
    
  }
  
}
