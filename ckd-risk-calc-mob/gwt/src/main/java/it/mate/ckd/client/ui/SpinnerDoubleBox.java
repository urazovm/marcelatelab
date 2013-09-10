package it.mate.ckd.client.ui;

import it.mate.ckd.client.constants.AppProperties;
import it.mate.ckd.client.ui.theme.custom.CustomTheme;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.phgcommons.client.utils.PhonegapUtils;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.ui.client.widget.MIntegerBox;

public class SpinnerDoubleBox extends Composite implements HasValueChangeHandlers<Double> {
  
//private MDoubleBox valueBox;
  
  private MIntegerBox intValueBox;
  private MIntegerBox decValueBox;
  
//private DoubleBox valueBox;
  
  private SpinControl leftSpin;
  private SpinControl rightSpin;
  
  private double increment;
  
  private boolean needFireEvents = false;
  
  private boolean disableSpinButtons = AppProperties.IMPL.SpinnerDoubleBox_disableSpinButtons(); 
  
  private Double minValue;
  private Double maxValue;

  private CustomTheme.CustomBundle bundle = CustomTheme.Instance.get();
  
  public SpinnerDoubleBox() {
    initUI();
  }
  
  private void initUI() {
    
    HorizontalPanel hp = new HorizontalPanel();
    hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    
    if (!disableSpinButtons) {
      leftSpin = new SpinControl(bundle.minusImage());
      GwtUtils.setStyleAttribute(leftSpin, "paddingLeft", "4px");
      GwtUtils.setStyleAttribute(leftSpin, "paddingRight", "6px");
      hp.add(leftSpin);
    }
    
    HorizontalPanel innerHp = new HorizontalPanel();
    innerHp.setBorderWidth(0);
    innerHp.setSpacing(0);
    
    intValueBox = new MIntegerBox();
    intValueBox.addStyleName("mgwt-SpinnerDouble-int-part");
    decValueBox = new MIntegerBox();
    decValueBox.addStyleName("mgwt-SpinnerDouble-dec-part");
    
    intValueBox.getElement().setPropertyString("type", "number");
    decValueBox.getElement().setPropertyString("type", "number");
    
    innerHp.add(intValueBox);
    innerHp.add(decValueBox);

    hp.add(innerHp);
    
    if (!disableSpinButtons) {
      rightSpin = new SpinControl(bundle.plusImage());
      GwtUtils.setStyleAttribute(rightSpin, "paddingLeft", "6px");
      hp.add(rightSpin);
    }
    
    initWidget(hp);

    if (!disableSpinButtons) {
      
      leftSpin.addHandler(new SpinControl.SpinHandler() {
        public void onSpin() {
          inc(increment * -1);
        }
      });
      
      rightSpin.addHandler(new SpinControl.SpinHandler() {
        public void onSpin() {
          inc(increment * +1);
        }
      });
    }
    
    final String language = PhonegapUtils.getNavigator().getLanguage();

    /*
    if (AppProperties.IMPL.SpinnerDoubleBox_keyPress_fix_enabled()) {
      valueBox.addKeyPressHandler(new KeyPressHandler() {
        public void onKeyPress(KeyPressEvent event) {
          char separatorToAvoid = ',';
          if (event.getCharCode() == separatorToAvoid) {
            event.preventDefault();
          }
        }
      });
    }
    */
    
  }
  
  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Double> handler) {
    needFireEvents = true;
//  return valueBox.addValueChangeHandler(handler);
    return null;
  }
  
  private void inc(double increment) {
    if (intValueBox.getValue() == null)
      return;
    
    double value = GwtUtils.composeDouble(intValueBox.getValue(), decValueBox.getValue());
    value += increment;
    boolean accept = true;
    if (minValue != null) {
      accept = accept && value >= minValue;
    }
    if (maxValue != null) {
      accept = accept && value <= maxValue;
    }
    if (accept) {
      setValueImpl(value, needFireEvents);
    }
  }
  
  private void setValueImpl(double value, boolean fireEvents) {
    intValueBox.setValue((int)value, fireEvents);
    decValueBox.setValue(GwtUtils.getDecimals(value), fireEvents);
  }
  
  public void setIncrement(double increment) {
    this.increment = increment;
  }
  
  public void setValue(double value) {
    setValueImpl(value, false);
  }
  
  public Double getValue() {
    Double res = null;
    res = GwtUtils.composeDouble(intValueBox.getValue(), decValueBox.getValue());
    return res;
  }
  
  public class SpinAnchor extends Anchor {
    public SpinAnchor(ImageResource resource) {
      super();
      Image image = new Image(resource);
      getElement().appendChild(image.getElement());
      addStyleName("ckd-spinAnchor");
    }
  }
  
  public void setMinValue(Double minValue) {
    this.minValue = minValue;
  }
  
  public void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
  }
  
  private static native String getLocalLanguageCookie() /*-{
    return $wnd.getLocalLanguageCookie();
  }-*/;

}
