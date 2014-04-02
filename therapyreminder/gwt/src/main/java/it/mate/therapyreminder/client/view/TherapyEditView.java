package it.mate.therapyreminder.client.view;

import it.mate.gwtcommons.client.mvp.BasePresenter;
import it.mate.gwtcommons.client.ui.StatePanel;
import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.StatePanelUtil;
import it.mate.phgcommons.client.ui.HasTag;
import it.mate.phgcommons.client.ui.TouchAnchor;
import it.mate.phgcommons.client.ui.TouchCombo;
import it.mate.phgcommons.client.ui.TouchHTML;
import it.mate.phgcommons.client.ui.ph.PhCalendarBox;
import it.mate.phgcommons.client.ui.ph.PhTextBox;
import it.mate.phgcommons.client.ui.ph.PhTimeBox;
import it.mate.phgcommons.client.view.BaseMgwtView;
import it.mate.therapyreminder.client.ui.SignPanel;
import it.mate.therapyreminder.client.view.TherapyEditView.Presenter;
import it.mate.therapyreminder.shared.model.Prescrizione;
import it.mate.therapyreminder.shared.model.UdM;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class TherapyEditView extends BaseMgwtView <Presenter> {
  
  public static final String TAG_PRESCRIZIONE = "prescrizione";

  public interface Presenter extends BasePresenter, SignPanel.Presenter {
    public void goToHome();
    public void savePrescrizione(Prescrizione prescrizione);
    public void findAllUdM(Delegate<List<UdM>> delegate);
  }

  public interface ViewUiBinder extends UiBinder<Widget, TherapyEditView> { }

  private static ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
  
  @UiField Panel wrapperPanel;
  
  @UiField MTextBox titleBox;
  @UiField PhCalendarBox inizioBox;
//@UiField TouchCombo tipoTerapiaCombo;
  @UiField TouchCombo tipoRicorrenzaCombo;
  @UiField TouchCombo tipoOrariCombo;
  @UiField Panel orariRegolariPanel;
  @UiField Panel orariFissiPanel;
  @UiField ComplexPanel orariListPanel;
  @UiField TouchCombo umCombo;
  @UiField Panel ricorrenzaPanel;
  @UiField Label ricorrenzaLabel;
  @UiField PhTextBox qtaBox;
  @UiField StatePanel estremiPrescrizionePanel;
  @UiField StatePanel ricorrenzaPrescrizionePanel;
  @UiField StatePanel orariPrescrizionePanel;
  @UiField PhTextBox rangeBox;
//@UiField Spacer filler;
  
  StatePanelUtil statePanelUtil = new StatePanelUtil();
  
  List<PhTimeBox> orariBox = new ArrayList<PhTimeBox>();
  
//int initialFillerHeight;
  
  private Widget bottomBar = null;
  
  private Prescrizione prescrizione;

  /*
  private String[] umDescriptions = new String[] {
      "Compress/a/e",
      "Fial/a/e",
      "Ovul/o/i",
      "Suppost/a/e",
      "Gocc/ia/e",
      "Bustin/a/e",
      "Garz/a/e",
      "Flacon/e/i",
      "Capsul/a/e",
      "Confett/o/i"
    };
  */
  
  public TherapyEditView() {
    initUI();
  }

  private void initProvidedElements() {

  }

  private void initUI() {
    initProvidedElements();
    initWidget(uiBinder.createAndBindUi(this));
    
//  initialFillerHeight = filler.getOffsetHeight();
    
    wrapperPanel.getElement().getStyle().clearHeight();
    
    /*
    tipoTerapiaCombo.addItem("O", "Orale", false);
    tipoTerapiaCombo.addItem("I", "Intramuscolare", false);
    tipoTerapiaCombo.addItem("E", "Endovenosa", false);
    tipoTerapiaCombo.addItem("S", "Sottocute", false);
    tipoTerapiaCombo.addItem("T", "Transcutanea", false);
    tipoTerapiaCombo.addItem("R", "Rettale", false);
    tipoTerapiaCombo.addItem("C", "Oculare", false);
    tipoTerapiaCombo.addItem("L", "Sublinguale", false);
    tipoTerapiaCombo.addItem("A", "Altro", false);
    tipoTerapiaCombo.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        PhonegapUtils.log("selected value is " + event.getValue());
      }
    });
    */
    
    tipoRicorrenzaCombo.addItem("G", "Giornaliera", false);
    tipoRicorrenzaCombo.addItem("S", "Settimanale", false);
    tipoRicorrenzaCombo.addItem("M", "Mensile", false);
    tipoRicorrenzaCombo.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        checkTipoRicorrenzaValue();
      }
    });
    
    tipoOrariCombo.addItem("I", "A intervalli regolari", false);
    tipoOrariCombo.addItem("O", "A orari fissi", false);
    tipoOrariCombo.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        checkTipoOrarioValue();
      }
    });

    qtaBox.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        adaptUmDescription(qtaBox.getValue());
      }
    });
    
    statePanelUtil.add(estremiPrescrizionePanel);
    statePanelUtil.add(ricorrenzaPrescrizionePanel);
    statePanelUtil.add(orariPrescrizionePanel);
    statePanelUtil.setCurrentState(estremiPrescrizionePanel.getStateId());
    
    initBottomBar();
    
  }
  
  private void checkTipoRicorrenzaValue() {
    String value = tipoRicorrenzaCombo.getValue();
    if ("G".equals(value)) {
      ricorrenzaLabel.setText("giorni");
      ricorrenzaPanel.setVisible(true);
    } else if ("S".equals(value)) {
      ricorrenzaLabel.setText("settimane");
      ricorrenzaPanel.setVisible(true);
    } else if ("M".equals(value)) {
      ricorrenzaLabel.setText("mesi");
      ricorrenzaPanel.setVisible(true);
    } else {
      ricorrenzaPanel.setVisible(false);
    }
    refreshScrollPanel();
  }
  
  private void checkTipoOrarioValue() {
    String value = tipoOrariCombo.getValue();
    if ("I".equals(value)) {
      orariRegolariPanel.setVisible(true);
      orariFissiPanel.setVisible(false);
    } else if ("O".equals(value)) {
      orariRegolariPanel.setVisible(false);
      orariFissiPanel.setVisible(true);
      showOrariListPanel();
      refreshScrollPanel();
    } else {
      orariRegolariPanel.setVisible(false);
      orariFissiPanel.setVisible(false);
    }
  }
  
  @Override
  public void onUnload() {
    if (bottomBar != null) {
      bottomBar.setVisible(false);
      bottomBar = null;
    }
    super.onUnload();
  }
  
  private void adaptUmDescription(String value) {
    final boolean singular = "1".equals(value.trim());
    getPresenter().findAllUdM(new Delegate<List<UdM>>() {
      public void execute(List<UdM> udms) {
        if (udms == null)
          return;
        for (int it = 0; it < udms.size(); it++) {
          UdM udm = udms.get(it);
          String[] tokens = udm.getDescrizione().split("/");
          String desc = tokens[0] + (singular ? tokens[1] : tokens[2]);
          umCombo.addItem(udm.getCodice(), desc, it == 0 ? true : false);
        }
      }
    });
  }
  
  private void showOrariListPanel() {
    if (orariBox.size() == 0) {
      orariBox.add(createOrarioBox());
    }
    orariListPanel.clear();
    HorizontalPanel row = null;
    for (final PhTimeBox orarioBox : orariBox) {
      row = new HorizontalPanel();
      row.add(orarioBox);
      TouchHTML removeBtn = new TouchHTML();
      removeBtn.addStyleName("ui-remove-btn");
      row.add(removeBtn);
      removeBtn.addTouchEndHandler(new TouchEndHandler() {
        public void onTouchEnd(TouchEndEvent event) {
          orariBox.remove(orarioBox);
          showOrariListPanel();
        }
      });
      orariListPanel.add(row);
    }
    TouchHTML addBtn = new TouchHTML();
    addBtn.addStyleName("ui-add-btn");
    row.add(addBtn);
    addBtn.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        orariBox.add(createOrarioBox());
        showOrariListPanel();
      }
    });
    orariListPanel.add(row);
    refreshScrollPanel();
//  WebkitCssUtil.moveScrollPanelY(getScrollPanelImpl(), 50);
  }
  
  private PhTimeBox createOrarioBox() {
    PhTimeBox orarioBox = new PhTimeBox();
    orarioBox.addStyleName("ui-app-timebox");
    return orarioBox;
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    super.setPresenter(presenter);
  }
  
  @Override
  public void setModel(Object model, String tag) {
    if (TAG_PRESCRIZIONE.equals(tag)) {
      this.prescrizione = (Prescrizione)model;
      titleBox.setValue(prescrizione.getNome());
      inizioBox.setValue(prescrizione.getDataInizio());
      qtaBox.setValue(prescrizione.getQuantita());
      tipoRicorrenzaCombo.setValue(prescrizione.getTipoRicorrenza());
      umCombo.setValue(prescrizione.getCodUdM());
      rangeBox.setValue(prescrizione.getValoreRicorrenza());
    }
  }
  
  private void refreshScrollPanel() {
    getScrollPanel().refresh();
  }

  @UiHandler ("saveBtn")
  public void onSaveBtn (TouchEndEvent event) {
    prescrizione.setNome(titleBox.getValue());
    prescrizione.setDataInizio(inizioBox.getValue());
    prescrizione.setQuantita(qtaBox.getValueAsDouble());
    prescrizione.setTipoRicorrenza(tipoRicorrenzaCombo.getValue());
    prescrizione.setCodUdM(umCombo.getValue());
    prescrizione.setValoreRicorrenza(rangeBox.getValueAsInteger());
    getPresenter().savePrescrizione(prescrizione);
  }

  private void initBottomBar() {
    HorizontalPanel bottomBar = new HorizontalPanel();
    bottomBar.addStyleName("ui-bottom-button-bar");
    bottomBar.setSpacing(0);
    initBottomBarItem(bottomBar, "What", "what", "estremiPrescrizionePanel");
    initBottomBarItem(bottomBar, "When", "when", "ricorrenzaPrescrizionePanel");
    initBottomBarItem(bottomBar, "Hours", "hours", "orariPrescrizionePanel");
    RootPanel.get().add(bottomBar);
    this.bottomBar = bottomBar;
  }
  
  private void initBottomBarItem(Panel bottomBar, String text, String css, String tag) {
    TouchAnchor button = new TouchAnchor();
    button.setText(text);
    button.addStyleName("ui-bottom-action-btn");
    button.addStyleName("ui-bottom-action-"+css+"-btn");
    button.setTag(tag);
    button.addTouchEndHandler(new TouchEndHandler() {
      public void onTouchEnd(TouchEndEvent event) {
        HasTag taggable = (HasTag)event.getSource();
        statePanelUtil.setCurrentState(taggable.getTag());
        checkTipoOrarioValue();
        checkTipoRicorrenzaValue();
      }
    });
    bottomBar.add(button);
  }
  
  public static int getBottomBarHeightPx() {
    return (Window.getClientHeight() / 8);
  }
  public static String getBottomBarHeight() {
    return getBottomBarHeightPx()+"px";
  }
  public static int getBottomBarTopPx() {
    return (Window.getClientHeight() - getBottomBarHeightPx());
  }
  public static String getBottomBarTop() {
    return getBottomBarTopPx()+"px";
  }

}