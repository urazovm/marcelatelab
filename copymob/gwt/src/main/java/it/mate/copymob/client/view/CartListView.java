package it.mate.copymob.client.view;

import it.mate.copymob.client.view.CartListView.Presenter;
import it.mate.copymob.shared.model.Order;
import it.mate.copymob.shared.model.OrderItem;
import it.mate.copymob.shared.model.Timbro;
import it.mate.gwtcommons.client.mvp.AbstractBaseView;
import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.Spacer;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.onscommons.client.event.TapEvent;
import it.mate.onscommons.client.event.TapHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.ui.OnsButton;
import it.mate.onscommons.client.ui.OnsHorizontalPanel;
import it.mate.onscommons.client.ui.OnsLabel;
import it.mate.onscommons.client.ui.OnsList;
import it.mate.onscommons.client.ui.OnsListItem;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class CartListView extends AbstractBaseView<Presenter> {

  public interface Presenter extends BasePresenter {
    public void saveOrderOnServer(Order order, Delegate<Order> delegate);
    public void goToOrderItemEditView(OrderItem orderItem);
    public void goToHomeView();
  }

  public interface ViewUiBinder extends UiBinder<Widget, CartListView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  @UiField OnsList cartList;
  
  private Order order;
  
  public CartListView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setModel(Object model, String tag) {
    if (model == null) {
      OnsLabel emptyLbl = new OnsLabel("Il carrello è vuoto");
      emptyLbl.addStyleName("app-cart-empty-lbl");
      cartList.add(emptyLbl);
      OnsenUi.refreshCurrentPage();
    } else if (model instanceof Order) {
      this.order = (Order)model;
      populateList();
    }
  }
  
  private void populateList() {
    
    OnsenUi.suspendCompilations();
    
    Iterator<OrderItem> it = order.getItems().iterator();
    
    while (it.hasNext()) {
      
      final OrderItem orderItem = it.next();
      
      TapHandler orderItemTapHandler = new TapHandler() {
        public void onTap(TapEvent event) {
          getPresenter().goToOrderItemEditView(orderItem);
        }
      };
      
      Timbro timbro = orderItem.getTimbro();
      
      OnsListItem listItem = new OnsListItem();
      
      OnsHorizontalPanel rowPanel = new OnsHorizontalPanel();
      rowPanel.setAddDirect(true);
      
      String html = "<img src='"+ timbro.getImageData() +"' class='app-cart-item-img'/>";
      HTML imgHtml = new HTML(html);
      OnsenUi.addTapHandler(imgHtml, orderItemTapHandler);
      rowPanel.add(imgHtml);
      
      OnsLabel nameLbl = new OnsLabel(timbro.getNome());
      OnsenUi.addTapHandler(nameLbl, orderItemTapHandler);
      nameLbl.addStyleName("app-cart-item-name");
      rowPanel.add(nameLbl);
      
      OnsHorizontalPanel qtaPanel = new OnsHorizontalPanel();
      qtaPanel.setAddDirect(true);
      qtaPanel.add(new Spacer("2em"));

      final OnsLabel qtaFld = new OnsLabel();
      setQtaLbl(qtaFld, orderItem.getQuantity());
      
      OnsLabel fillerBtn = new OnsLabel("Qtà:");
      fillerBtn.addStyleName("app-cart-item-label");
//    fillerBtn.setIcon("fa-hand-o-right");
//    fillerBtn.setModifier("quiet");
      
      OnsButton plusBtn = new OnsButton("");
      plusBtn.addStyleName("app-cart-btn-plus");
      plusBtn.setIcon("fa-plus-circle");
      plusBtn.setModifier("quiet");
      plusBtn.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          PhgUtils.log("plus");
          double qta = orderItem.getQuantity();
          qta ++;
          orderItem.setQuantity(qta);
          setQtaLbl(qtaFld, orderItem.getQuantity());
        }
      });
      OnsButton minusBtn = new OnsButton();
      minusBtn.getElement().removeClassName("ons-button");
      minusBtn.addStyleName("app-cart-btn-minus");
      minusBtn.setIcon("fa-minus-circle");
      minusBtn.setModifier("quiet");
      minusBtn.addTapHandler(new TapHandler() {
        public void onTap(TapEvent event) {
          double qta = orderItem.getQuantity();
          if (qta > 0) {
            qta --;
            orderItem.setQuantity(qta);
            setQtaLbl(qtaFld, orderItem.getQuantity());
          }
        }
      });
      
      qtaPanel.add(fillerBtn);
      qtaPanel.add(plusBtn);
      qtaPanel.add(qtaFld);
      qtaPanel.add(minusBtn);
      
      rowPanel.add(qtaPanel);
      
      OnsHorizontalPanel actionsPanel = new OnsHorizontalPanel();
      actionsPanel.setAddDirect(true);
      OnsButton editBtn = new OnsButton("");
      editBtn.addStyleName("app-cart-btn-edit");
      editBtn.setIcon("fa-edit");
      editBtn.setModifier("quiet");
      editBtn.addTapHandler(orderItemTapHandler);
      actionsPanel.add(editBtn);
      
      rowPanel.add(actionsPanel);
      
      listItem.add(rowPanel);
      
      cartList.add(listItem);

    }
    
    OnsenUi.refreshCurrentPage();
    
  }
  
  private void setQtaLbl(OnsLabel lbl, double qta) {
    lbl.setText(GwtUtils.formatDecimal(qta, 0));
  }
  
  @UiHandler("btnGo")
  public void onBtnGo(TapEvent event) {
    getPresenter().saveOrderOnServer(order, new Delegate<Order>() {
      public void execute(Order element) {
        getPresenter().goToHomeView();
      }
    });
  }
  
}
