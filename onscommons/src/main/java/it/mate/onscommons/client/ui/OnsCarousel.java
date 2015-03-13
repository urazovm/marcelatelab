package it.mate.onscommons.client.ui;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.client.utils.MoveHandler;
import it.mate.onscommons.client.onsen.OnsenUi;
import it.mate.onscommons.client.onsen.dom.Carousel;
import it.mate.phgcommons.client.utils.PhgUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class OnsCarousel extends HTMLPanel {

  private final static String TAG_NAME = "ons-carousel";
  
  private List<OnsCarouselItem> items = new ArrayList<OnsCarouselItem>();
  
  public OnsCarousel() {
    this(TAG_NAME, "");
  }
  
  public OnsCarousel(String html) {
    this(TAG_NAME, html);
  }
  
  protected OnsCarousel(String tag, String html) {
    super(tag, html);
    getElement().addClassName(TAG_NAME);
    PhgUtils.ensureId(getElement());
    getElement().setAttribute("var", "app.onsCarousel");
  }
  
  private void addItem(Widget widget, Element element) {
    super.add(widget, element);
  }

  @Override
  public void add(final Widget widget) {
    PhgUtils.log("before adding item " + widget.getElement());
    PhgUtils.log("  to carousel " + getElement());
    GwtUtils.onAvailable(getElement().getId(), new Delegate<Element>() {
      public void execute(Element carouselElem) {
        PhgUtils.log("adding item to carousel " + carouselElem);
        addItem(widget, carouselElem);
        OnsenUi.compileElement(carouselElem);
        
        if (getController() != null) {
          getController().refresh();
        }
        
        if (widget instanceof OnsCarouselItem) {
          
          OnsCarouselItem item = (OnsCarouselItem)widget;
          
//        PhgUtils.log("ADDING ITEM -- " + item.getElement().getId() + " -- " + item);
          
          items.add(item);

          // 03/02/2015
          if (items.size() == 1) {
            GwtUtils.onAvailable(item.getElement().getId(), new Delegate<Element>() {
              public void execute(Element itemElem) {
                
//              OnsenUi.compileElement(itemElem);

                GwtUtils.addMoveHandler(itemElem, new MoveHandler() {
                  public void onMove(Element element, int top, int left) {
                    PhgUtils.log("moving element to " + top + " " + left);
                    for (OnsCarouselItem item : items) {
                      item.setLastMovementTime(System.currentTimeMillis());
                    }
                  }
                });
                
              }
            });
          }
        }
        
      }
    });
  }

  public void getActiveItem(final Delegate<OnsCarouselItem> delegate) {
    onControllerAvailable(new Delegate<Carousel>() {
      public void execute(Carousel carousel) {
        final int index = carousel.getActiveCarouselItemIndex();
        delegate.execute(items.get(index));
      }
    });
  }
  
  public void setAutoscroll(String value) {
    getElement().setAttribute("auto-scroll", "");
  }

  public void setDraggable(String value) {
    getElement().setAttribute("draggable", "");
  }
  
  public void setOverscrollable(String value) {
    getElement().setAttribute("overscrollable", "");
  }
  
  public void setSwipeable(String value) {
    getElement().setAttribute("swipeable", "");
  }
  
  public final native Carousel getController() /*-{
    return $wnd.app.onsCarousel;    
  }-*/;
  
  protected void onControllerAvailable (final Delegate<Carousel> delegate) {
    final long t0 = System.currentTimeMillis();
    new Timer() {
      public void run() {
        long t1 = System.currentTimeMillis();
        Carousel carousel = OnsCarousel.this.getController();
        if (carousel != null) {
          this.cancel();
          delegate.execute(carousel);
        } else if (t1 - t0 > 10000) {
          this.cancel();
        }
      }
    }.scheduleRepeating(100);
  }

}
