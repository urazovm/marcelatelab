package it.mate.therapyreminder.client.logic;

import it.mate.gwtcommons.client.utils.Delegate;
import it.mate.gwtcommons.client.utils.GwtUtils;
import it.mate.gwtcommons.shared.services.ServiceException;
import it.mate.phgcommons.client.plugins.CalendarPlugin;
import it.mate.phgcommons.client.plugins.CalendarPlugin.Event;
import it.mate.phgcommons.client.utils.JSONUtils;
import it.mate.phgcommons.client.utils.OsDetectionUtils;
import it.mate.phgcommons.client.utils.PhgDialogUtils;
import it.mate.phgcommons.client.utils.PhonegapLog;
import it.mate.phgcommons.client.utils.PhonegapUtils;
import it.mate.phgcommons.client.utils.Time;
import it.mate.therapyreminder.client.factories.AppClientFactory;
import it.mate.therapyreminder.shared.model.Account;
import it.mate.therapyreminder.shared.model.Dosaggio;
import it.mate.therapyreminder.shared.model.Prescrizione;
import it.mate.therapyreminder.shared.model.Somministrazione;
import it.mate.therapyreminder.shared.model.impl.AccountTx;
import it.mate.therapyreminder.shared.model.impl.PrescrizioneTx;
import it.mate.therapyreminder.shared.model.impl.SomministrazioneTx;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.googlecode.mgwt.ui.client.MGWT;

public class MainController {

  private MainDao dao = AppClientFactory.IMPL.getGinjector().getPrescrizioniDao();
  
  private static MainController instance;
  
  public static MainController getInstance() {
    if (instance == null)
      instance = new MainController();
    return instance;
  }
  
  protected MainController() {

  }
  
  public void createPrescrizioneWithDefaults(Delegate<Prescrizione> delegate) {
    Prescrizione prescrizione = new PrescrizioneTx();
    prescrizione.setDataInizio(new Date());
    prescrizione.setQuantita(1d);
    prescrizione.setTipoRicorrenza(Prescrizione.TIPO_RICORRENZA_OGNI_GIORNO);
    prescrizione.setValoreRicorrenza(1);
    prescrizione.setCodUdM("01");
    prescrizione.setTipoRicorrenzaOraria(Prescrizione.TIPO_ORARI_FISSI);
    prescrizione.setIntervalloOrario(1);
    delegate.execute(prescrizione);
  }
  
  public void findPrimaSomministrazioneScaduta(final Delegate<Somministrazione> delegate) {
    Date dataRiferimento = new Date();
    dao.findSomministrazioniScadute(dataRiferimento, new Delegate<List<Somministrazione>>() {
      public void execute(List<Somministrazione> somministrazioni) {
        if (somministrazioni == null || somministrazioni.size() <= 0) {
          delegate.execute(null);
        } else {
          delegate.execute(somministrazioni.get(0));
        }
      }
    });
  }
  
  public void sviluppaSomministrazioniInBackground() {
    dao.findAllPrescrizioniAttive(new Date(), new Delegate<List<Prescrizione>>() {
      public void execute(List<Prescrizione> prescrizioni) {
        if (prescrizioni == null || prescrizioni.size() == 0) {
          return;
        }
        for (Prescrizione prescrizione : prescrizioni) {
          sviluppaSomministrazioni(prescrizione);
        }
      }
    });
  }
  
  public void updateSomministrazione(final Somministrazione newSomministrazione, final Delegate<Somministrazione> somministrazioneAggiornataDelegate, 
      final Delegate<Somministrazione> farmacoDaRiordinareDelegate) {
    dao.findSomministrazioneById(newSomministrazione.getId(), new Delegate<Somministrazione>() {
      public void execute(final Somministrazione oldSomministrazione) {
        dao.saveSomministrazione(newSomministrazione, new Delegate<Somministrazione>() {
          public void execute(Somministrazione somministrazioneAggiornata) {
            if (!oldSomministrazione.isEseguita() && newSomministrazione.isEseguita()) {
              double qtaSomministrata = newSomministrazione.getQuantita();
              Prescrizione prescrizione = somministrazioneAggiornata.getPrescrizione();
              double qtaRimanente = prescrizione.getQtaRimanente();
              double qtaPerRiordino = prescrizione.getQtaPerAvviso();
              if (qtaRimanente - qtaSomministrata > 0) {
                qtaRimanente -= qtaSomministrata;
                prescrizione.setQtaRimanente(qtaRimanente);
                dao.savePrescrizione(prescrizione, new Delegate<Prescrizione>() {
                  public void execute(Prescrizione result) {
                    
                  }
                });
              }
              if (qtaRimanente < qtaPerRiordino) {
                farmacoDaRiordinareDelegate.execute(somministrazioneAggiornata);
                return;
              }
            }
            somministrazioneAggiornataDelegate.execute(somministrazioneAggiornata);
          }
        });
      }
    });
    
    
  }
  
  public void sviluppaSomministrazioni(final Prescrizione prescrizione) {
    dao.findLastSomministrazioneByPrescrizione(prescrizione, new Delegate<Somministrazione>() {
      public void execute(Somministrazione ultimaSomministrazione) {
        Date dataUltimaSomministrazione = null;
        if (ultimaSomministrazione != null) {
          dataUltimaSomministrazione = ultimaSomministrazione.getData();
        }
        sviluppaSomministrazioniAPartireDa(prescrizione, dataUltimaSomministrazione);
      }
    });
  }
  
  private void sviluppaSomministrazioniAPartireDa(final Prescrizione prescrizione, Date dataUltimaSomministrazione) {
    final Delegate<List<Somministrazione>> completionDelegate = new Delegate<List<Somministrazione>>() {
      public void execute(List<Somministrazione> somministrazioni) {
        if (somministrazioni != null && somministrazioni.size() > 0) {
          //TODO
          PhonegapLog.log("EXECUTING REMOTE SAVE WITH " + somministrazioni.size() + " NEW SOMMINISTRAZIONI");
          saveRemoteSomministrazioni(somministrazioni, new Delegate<List<Somministrazione>>() {
            public void execute(List<Somministrazione> somministrazioni) {
              
            }
          });
        }
      }
    };
    if (dataUltimaSomministrazione == null) {
      iterateDosaggiForInsert(prescrizione, prescrizione.getDataInizio(), prescrizione.getDosaggi().iterator(), 
          new ArrayList<Somministrazione>(), new Delegate<List<Somministrazione>>() {
        public void execute(List<Somministrazione> somministrazioni) {
          sviluppaRicorrenzaSuccessiva(prescrizione, prescrizione.getDataInizio(), somministrazioni, completionDelegate);
        }
      });
    } else {
      sviluppaRicorrenzaSuccessiva(prescrizione, dataUltimaSomministrazione, new ArrayList<Somministrazione>(), completionDelegate);
    }
  }
  
  private void sviluppaRicorrenzaSuccessiva(final Prescrizione prescrizione, Date dataUltimaSomministrazione,
      List<Somministrazione> somministrazioni, final Delegate<List<Somministrazione>> completionDelegate) {
    final Date nextDataSomministrazione = CalendarUtil.copyDate(dataUltimaSomministrazione);
    if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_GIORNALIERA)) {
      int incremento = prescrizione.getValoreRicorrenza();
      CalendarUtil.addDaysToDate(nextDataSomministrazione, incremento);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_SETTIMANALE)) {
      int incremento = 7 * prescrizione.getValoreRicorrenza();
      CalendarUtil.addDaysToDate(nextDataSomministrazione, incremento);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_MENSILE)) {
      int incremento = prescrizione.getValoreRicorrenza();
      CalendarUtil.addMonthsToDate(nextDataSomministrazione, incremento);
    }
    Date dataLimiteSviluppoSomministrazioni = getDataLimiteSviluppoSomministrazioni(prescrizione);
    if (nextDataSomministrazione.after(dataLimiteSviluppoSomministrazioni)) {
      completionDelegate.execute(somministrazioni);
      return;
    }
    if (prescrizione.getDataFine() != null && nextDataSomministrazione.after(prescrizione.getDataFine())) {
      completionDelegate.execute(somministrazioni);
      return;
    }
    iterateDosaggiForInsert(prescrizione, nextDataSomministrazione, prescrizione.getDosaggi().iterator(), 
        somministrazioni, new Delegate<List<Somministrazione>>() {
      public void execute(List<Somministrazione> somministrazioni) {
        sviluppaRicorrenzaSuccessiva(prescrizione, nextDataSomministrazione, somministrazioni, completionDelegate);
      }
    });
  }
  
  private void iterateDosaggiForInsert(final Prescrizione prescrizione, final Date dataSomministrazione, final Iterator<Dosaggio> it, 
      final List<Somministrazione> results, final Delegate<List<Somministrazione>> completionDelegate) {
    if (it.hasNext()) {
      Dosaggio dosaggio = it.next();
      Somministrazione somministrazione = new SomministrazioneTx(prescrizione);
      Time.fromString(dosaggio.getOrario()).setInDate(dataSomministrazione);
      if (dataSomministrazione.before(new Date())) {
        iterateDosaggiForInsert(prescrizione, dataSomministrazione, it, results, completionDelegate);
        return;
      }
      somministrazione.setData(dataSomministrazione);
      somministrazione.setOrario(dosaggio.getOrario());
      Double qta = dosaggio.getQuantita();
      if (qta == null)
        qta = prescrizione.getQuantita();
      somministrazione.setQuantita(qta);
      somministrazione.setSchedulata();
      dao.saveSomministrazione(somministrazione, new Delegate<Somministrazione>() {
        public void execute(Somministrazione somministrazione) {
          saveCalEvent(somministrazione);
          results.add(somministrazione);
          iterateDosaggiForInsert(prescrizione, dataSomministrazione, it, results, completionDelegate);
        }
      });
    } else {
      completionDelegate.execute(results);
    }
  }
  
  private Date getDataLimiteSviluppoSomministrazioni(Prescrizione prescrizione) {
    Date dataLimiteSviluppoSomministrazioni = new Date();
    if (prescrizione.getValoreRicorrenza() == null || prescrizione.getValoreRicorrenza() <= 0) {
      throw new ServiceException("Date range not set");
    }
    if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_GIORNALIERA)) {
      int limite = 5 * prescrizione.getValoreRicorrenza();
      CalendarUtil.addDaysToDate(dataLimiteSviluppoSomministrazioni, limite);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_SETTIMANALE)) {
      int limite = 7 * 5 * prescrizione.getValoreRicorrenza();
      CalendarUtil.addDaysToDate(dataLimiteSviluppoSomministrazioni, limite);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_MENSILE)) {
      int limite = 5 * prescrizione.getValoreRicorrenza();
      CalendarUtil.addMonthsToDate(dataLimiteSviluppoSomministrazioni, limite);
    }
    
    /*
    final int DEFAULT_SVILUPPO_GIORNALIERO = 5;
    final int DEFAULT_SVILUPPO_SETTIMANALE = 5;
    final int DEFAULT_SVILUPPO_MENSILE = 5;
    if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_GIORNALIERA)) {
      int limite = Math.max(DEFAULT_SVILUPPO_GIORNALIERO, prescrizione.getValoreRicorrenza());
      CalendarUtil.addDaysToDate(dataLimiteSviluppoSomministrazioni, limite);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_SETTIMANALE)) {
      int limite = Math.max(DEFAULT_SVILUPPO_SETTIMANALE, prescrizione.getValoreRicorrenza());
      CalendarUtil.addDaysToDate(dataLimiteSviluppoSomministrazioni, limite * 7);
    } else if (prescrizione.getTipoRicorrenza().equals(Prescrizione.TIPO_RICORRENZA_MENSILE)) {
      int limite = Math.max(DEFAULT_SVILUPPO_MENSILE, prescrizione.getValoreRicorrenza());
      CalendarUtil.addMonthsToDate(dataLimiteSviluppoSomministrazioni, limite);
    }
    */
    
    return dataLimiteSviluppoSomministrazioni;
  }

  public void cancellaSomministrazioni(final Prescrizione prescrizione, final Delegate<Void> endDelegate) {
    dao.findSomministrazioniSchedulateByPrescrizione(prescrizione, new Delegate<List<Somministrazione>>() {
      public void execute(final List<Somministrazione> somministrazioni) {
        if (somministrazioni == null || somministrazioni.size() == 0) {
          endDelegate.execute(null);
          return;
        }
        iterateSomministrazioniForDelete(somministrazioni.iterator(), new Delegate<Void>() {
          public void execute(Void element) {
            
            //TODO
            PhonegapLog.log("EXECUTING REMOTE DELETE WITH " + somministrazioni.size() + " SOMMINISTRAZIONI");
            deleteRemoteSomministrazioni(somministrazioni, new Delegate<List<Somministrazione>>() {
              public void execute(List<Somministrazione> somministrazioni) {
                dao.deleteSomministrazioni(somministrazioni, endDelegate);
              }
            });
            
          }
        });
      }
    });
  }
  
  private void iterateSomministrazioniForDelete(Iterator<Somministrazione> it, Delegate<Void> endDelegate) {
    if (it.hasNext()) {
      Somministrazione somministrazione = it.next();
      deleteCalEvent(somministrazione);
      iterateSomministrazioniForDelete(it, endDelegate);
    } else {
      endDelegate.execute(null);
    }
  }
  
  private CalendarPlugin.Event instantiateCalEvent(Prescrizione prescrizione, Somministrazione somministrazione) {
    CalendarPlugin.Event calEvent = new CalendarPlugin.Event();
    
    if (somministrazione != null) {
      calEvent.setStartDate(somministrazione.getData());
    } else {
      calEvent.setStartDate(prescrizione.getDataInizio());
    }
    
    if (somministrazione != null) {
      calEvent.setEndDate(CalendarUtil.copyDate(somministrazione.getData()));
      Time endTime = Time.fromDate(calEvent.getEndDate());
      endTime.incMinutes(5).setInDate(calEvent.getEndDate());
    } else {
      if (prescrizione.getDataFine() != null) {
        calEvent.setEndDate(prescrizione.getDataFine());
      } else {
        calEvent.setEndDate(GwtUtils.getDate(31, 12, 2099));
      }
    }
    
    if (somministrazione != null) {
      calEvent.setTitle(prescrizione.getNome() + " at " + somministrazione.getOrario());
      if (MGWT.getOsDetection().isIOs()) {
        calEvent.setNotes("Tap here: therapy://open");
      } else {
        calEvent.setNotes("Keep the pill!");
      }
    }
    
    calEvent.setLocation("#"+prescrizione.getId());
    
    return calEvent;
  }
  
  private void saveCalEvent (Somministrazione somministrazione) {
    CalendarPlugin.Event calEvent = instantiateCalEvent(somministrazione.getPrescrizione(), somministrazione);
    PhonegapLog.log("creating " + calEvent);
    if (OsDetectionUtils.isDesktop())
      return;
    CalendarPlugin.createEvent(calEvent);
  }
  
  private void deleteCalEvent (Somministrazione somministrazione) {
    CalendarPlugin.Event calEvent = instantiateCalEvent(somministrazione.getPrescrizione(), somministrazione);
    PhonegapLog.log("deleting " + calEvent);
    if (OsDetectionUtils.isDesktop())
      return;
    CalendarPlugin.deleteEvent(calEvent);
  }

  public void findCalEvents (Prescrizione prescrizione) {
    CalendarPlugin.Event calEvent = instantiateCalEvent(prescrizione, null);
    PhonegapLog.log("finding " + calEvent);
    if (OsDetectionUtils.isDesktop())
      return;
    CalendarPlugin.findEvent(calEvent, new Delegate<List<CalendarPlugin.Event>>() {
      public void execute(List<Event> events) {
        for (Event event : events) {
          PhonegapLog.log("found " + event);
        }
      }
    });
  }
  
  public static boolean isScaduta(Somministrazione somministrazione) {
    Date NOW = new Date();
    Date datetimeSomministrazione = somministrazione.getData();
    Time.fromString(somministrazione.getOrario()).setInDate(datetimeSomministrazione);
    return somministrazione.isSchedulata() && datetimeSomministrazione.before(NOW);
  }
  
  public static String validatePrescrizione(Prescrizione prescrizione) {
    if (prescrizione.getNome() == null || prescrizione.getNome().trim().length() == 0) {
      return "Manca il nome del farmaco";
    }
    if (prescrizione.getDosaggi() == null) {
      return "Manca l'orario di assunzione";
    }
    for (Dosaggio dosaggio : prescrizione.getDosaggi()) {
      if (dosaggio.getOrario() == null) {
        return "Orario di assunzione non impostato";
      }
    }
    if (prescrizione.isGstAvvisoRiordino()) {
      if (prescrizione.getQtaPerConfez() <= 0) {
        return "Quantità per confezione errata";
      }
    }
    return null;
  }
  
  public void checkConnectionIfOnlineMode(final Delegate<Boolean> delegate) {
    if (isOnlineMode()) {
      AppClientFactory.IMPL.getRemoteFacade().checkConnection(new AsyncCallback<Boolean>() {
        public void onSuccess(Boolean result) {
          delegate.execute(true);
        }
        public void onFailure(Throwable caught) {
          PhgDialogUtils.showMessageDialog("In online mode you need to turn on the mobile data connection");
          delegate.execute(false);
        }
      });
    } else {
      delegate.execute(true);
    }
  }
  
  public void setOnlineMode(boolean onlineMode) {
    PhonegapUtils.setLocalStorageProperty("onlineMode", ""+onlineMode);
  }
  
  public boolean isOnlineMode() {
    String value = PhonegapUtils.getLocalStorageProperty("onlineMode");
    PhonegapUtils.log("onlineMode = " + value);
    return ("true".equalsIgnoreCase(value));
  }
  
  public void setDevInfoIdInLocalStorage(String devInfoId) {
    PhonegapUtils.setLocalStorageProperty("devInfoId", devInfoId);
  }

  public String getDevInfoIdFromLocalStorage() {
    return PhonegapUtils.getLocalStorageProperty("devInfoId");
  }

  public void setAccountInLocalStorage(Account account) {
    AccountTx tx = (AccountTx)account;
    PhonegapUtils.setLocalStorageProperty("account", JSONUtils.stringify(tx.asJS()));
  }
  
  public Account getAccountFromLocalStorage() {
    String accountJson = PhonegapUtils.getLocalStorageProperty("account");
    if (accountJson != null && accountJson.contains("{")) {
      Account account = AccountTx.fromJS(JSONUtils.parse(accountJson));
      return account;
    }
    return null;
  }
  
  
  // TODO
  private void saveRemoteSomministrazioni (List<Somministrazione> somministrazioni, final Delegate<List<Somministrazione>> delegate) {
    if (isOnlineMode()) {
      List<Somministrazione> somministrazioniDaSalvare = new ArrayList<Somministrazione>();
      for (Somministrazione somministrazione : somministrazioni) {
        if (somministrazione.getPrescrizione().getTutor() != null) {
          somministrazioniDaSalvare.add(somministrazione);
        }
      }
      if (somministrazioniDaSalvare.size() > 0) {
        Account account = getAccountFromLocalStorage();
        String devInfoId = getDevInfoIdFromLocalStorage();
        PhonegapUtils.log("calling save remote somministrazioni");
        AppClientFactory.IMPL.getRemoteFacade().saveSomministrazioni(somministrazioniDaSalvare, account, devInfoId, new AsyncCallback<List<Somministrazione>>() {
          public void onSuccess(List<Somministrazione> somministrazioni) {
            PhonegapUtils.log("success");
            delegate.execute(somministrazioni);
          }
          public void onFailure(Throwable caught) {
            processFailure(null, caught);
            delegate.execute(null);
          }
        });
      } else {
        delegate.execute(somministrazioni);
      }
    } else {
      delegate.execute(somministrazioni);
    }
  }
   
  // TODO
  private void deleteRemoteSomministrazioni (List<Somministrazione> somministrazioni, final Delegate<List<Somministrazione>> delegate) {
//  PhonegapLog.log("deleting remote " + somministrazione);
    delegate.execute(somministrazioni);
  }
  
  private void processFailure(String message, Throwable caught) {
    String popupTitle = "Alert";
    String popupMsg = "Failure";
    String logMsg = null;
    if (message != null) {
      popupMsg = message;
    } else if (caught != null) {
      logMsg = caught.getClass().getName()+" - "+caught.getMessage();
      if (caught instanceof InvocationException) {
        popupMsg = "Maybe data connection is not active";
      } else {
        if (caught.getMessage() != null) {
          popupMsg = caught.getMessage();
        } else {
          String cls = caught.getClass().getName();
          cls = cls.substring(cls.lastIndexOf(".") + 1); 
          popupMsg = cls;
        }
      }
      popupTitle = "Error";
      caught.printStackTrace();
    }
    if (logMsg != null)
      PhonegapUtils.log(logMsg);
    PhgDialogUtils.showMessageDialog(popupMsg, popupTitle, PhgDialogUtils.BUTTONS_OK);
  }
  
  
}