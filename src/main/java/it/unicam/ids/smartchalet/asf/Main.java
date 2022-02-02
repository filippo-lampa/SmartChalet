package it.unicam.ids.smartchalet.asf;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inizializzazione();
        boolean flag = false;
        do {
            System.out.println("Selezionare l'operazione");
            System.out.println("1- Login");
            System.out.println("2- Registrazione");
            System.out.println("Digitare 3 per uscire dall'applicazione");
            switch (provaScannerInt()) {
                case 1 -> login();
                case 2 -> registrazioneCliente();
                case 3 -> flag = true;
            }
        }while(!flag);

      HandlerSpiaggia.getInstance().aggiungiGrigliaSpiaggia();
      HandlerSpiaggia.getInstance().aggiungiTipologiaOmbrellone();
      HandlerListino.getInstance().impostaPrezziOmbrellone();
      HandlerSpiaggia.getInstance().aggiungiOmbrellone();
    }

    private static void setConnessioneDatabase(){
        System.out.println("Avvio prova connessione database ...");

        DBMSController dbmsController = DBMSController.getInstance();
        dbmsController.setDBMSController("jdbc:postgresql://localhost/SmartChalet","Filippo","smartchaletasf");
        dbmsController.DBMSControllerTest();
        System.out.println();
    }
    private static void inizializzazione(){
        setConnessioneDatabase();
        HandlerPrenotazione.getInstance();
        HandlerListino.getInstance();
        HandlerSpiaggia.getInstance();
        HandlerAttivita.getInstance();
        HandlerAttrezzatura.getInstance();
        HandlerSpiaggia.getInstance().setHandlerPrenotazioneAssociato();
        HandlerListino.getInstance().setHandlerSpiaggiaAssociato();
    }

    private static void login(){
        boolean flag = false;
        do {
            System.out.println("Selezionare il ruolo");
            System.out.println("1- Cliente");
            System.out.println("2- Gestore spiaggia");
            System.out.println("3- Gestore attrezzatura");
            System.out.println("4- Gestore prezzi bar");
            System.out.println("5- Gestore attivita'");
            System.out.println("6- Servizio bar");
            System.out.println("7- Gestore prezzi spiaggia");
            System.out.println("8- Gestore Promozioni (non implementato)");
            System.out.println("Digitare 9 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> loginCliente();
                case 2 -> operazioniGestoreSpiaggia();
                case 3 -> operazioniGestoreAttrezzatura();
                case 4 -> operazioniGestorePrezziBar();
                case 5 -> operazioniGestoreAttivita();
                case 6 -> operazioniServizioBar();
                case 7 -> operazioniGestorePrezziSpiaggia();
                case 8 -> System.out.println("Non implementato");
                case 9 -> flag = true;
            }
        }while(!flag);
    }

    private static void loginCliente() {
        System.out.println("Inserire il proprio id");
        int idCliente = provaScannerInt();
        Cliente cliente = DBMSController.getInstance().controlloIdCliente(idCliente);
        if(cliente != null){
            boolean flag = false;
            do {
                HandlerSpiaggia.getInstance().ottieniVistaSpiaggia();
                boolean isSpiaggiaEmpty = HandlerSpiaggia.getInstance().getSpiaggiaGestita().getTotaleOmbrelloni() == 0;
                HandlerPrenotazione.getInstance().setListaPrenotazioni(DBMSController.getInstance().ottieniListaPrenotazioni());
                boolean isClientePrenotato = !(HandlerPrenotazione.getInstance().filtroListaPrenotazioni(HandlerPrenotazione.getInstance().getListaPrenotazioni(), idCliente).isEmpty());
                boolean isAlmenoUnProdottoBarPresente = !(HandlerListino.getInstance().getPrezziBar().isEmpty());
                boolean isAlmenoUnAttivitaPresente = !(HandlerAttivita.getInstance().ottieniListaAttivitaDisponibili().isEmpty());
                System.out.println("Selezionare l'operazione");
                if(!isSpiaggiaEmpty) System.out.println("1- Prenota ombrellone");
                if(isClientePrenotato && isAlmenoUnProdottoBarPresente) System.out.println("2- Ordina dal bar");
                if(isClientePrenotato) System.out.println("3- Cancella prenotazione");
                if(isClientePrenotato && isAlmenoUnAttivitaPresente) System.out.println("4- Partecipa evento");
                System.out.println("Digitare 5 per chiudere il menu' di selezione");
                switch (provaScannerInt()) {
                    case 1 -> { if(!isSpiaggiaEmpty) HandlerPrenotazione.getInstance().prenotaOmbrellone(idCliente); else System.out.println("Non esistono ombrelloni da prenotare");}
                    case 2 -> { if(isClientePrenotato && isAlmenoUnProdottoBarPresente) HandlerPrenotazione.getInstance().ordinaBar(idCliente); else System.out.println("Il cliente non è prenotato o non è presente alcun prodotto bar");}
                    case 3 -> { if(isClientePrenotato) HandlerPrenotazione.getInstance().cancellaPrenotazione(idCliente); else System.out.println("Non esistono prenotazioni associate al cliente");}
                    case 4 -> { if(isClientePrenotato && isAlmenoUnAttivitaPresente) HandlerPrenotazione.getInstance().partecipaEvento(idCliente); else System.out.println("Il cliente non è prenotato o nessuna attività è disponibile");}
                    case 5 -> flag = true;
                }
            }while(!flag);
        } else System.out.println("Id errato, impossibile eseguire il login");
    }

    private static void operazioniGestoreSpiaggia() {
        boolean flag = false;
        do {
            HandlerSpiaggia.getInstance().ottieniVistaSpiaggia();
            boolean isGrigliaSpiaggiaNotCreated = HandlerSpiaggia.getInstance().getSpiaggiaGestita().getListaOmbrelloni().isEmpty();
            boolean isSpiaggiaEmpty = HandlerSpiaggia.getInstance().getSpiaggiaGestita().getTotaleOmbrelloni() == 0;
            HandlerListino.getInstance().ottieniPrezziTipologie();
            boolean isAlmenoUnaTipologiaUtilizzabilePresente = !(HandlerSpiaggia.getInstance().controlloTipologiaUtilizzabili(HandlerListino.getInstance().ottieniPrezziTipologie()).isEmpty());
            System.out.println("Selezionare l'operazione");
            if(isGrigliaSpiaggiaNotCreated) System.out.println("1- Crea griglia spiaggia");
            if(!isGrigliaSpiaggiaNotCreated) System.out.println("2- Modifica griglia spiaggia");
            if(!isSpiaggiaEmpty) System.out.println("3- Modifica ombrellone");
            System.out.println("4- Aggiungi tipologia ombrellone");
            if(isAlmenoUnaTipologiaUtilizzabilePresente && !isGrigliaSpiaggiaNotCreated) System.out.println("5- Aggiungi ombrellone");
            System.out.println("Digitare 6 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> { if(isGrigliaSpiaggiaNotCreated) HandlerSpiaggia.getInstance().aggiungiGrigliaSpiaggia(); else System.out.println("La griglia spiaggia è già stata creata");}
                case 2 -> { if(!isGrigliaSpiaggiaNotCreated) HandlerSpiaggia.getInstance().modificaGrigliaSpiaggia(); else System.out.println("La griglia spiaggia non esiste"); }
                case 3 -> { if(!isSpiaggiaEmpty) HandlerSpiaggia.getInstance().modificaOmbrellone(); else System.out.println("La spiaggia è vuota"); }
                case 4 -> HandlerSpiaggia.getInstance().aggiungiTipologiaOmbrellone();
                case 5 -> { if(isAlmenoUnaTipologiaUtilizzabilePresente&& !isGrigliaSpiaggiaNotCreated) HandlerSpiaggia.getInstance().aggiungiOmbrellone(); else System.out.println("La griglia spiaggia non esiste o non è presente alcuna tipologia ombrellone");}
                case 6 -> flag = true;
            }
        }while(!flag);
    }

    private static void operazioniGestoreAttrezzatura() {
        boolean flag = false;
        do {
            boolean isAlmenoUnAttrezzaturaPresente = !(HandlerAttrezzatura.getInstance().ottieniMappaAttrezzature().isEmpty());
            System.out.println("Selezionare l'operazione");
            System.out.println("1- Aggiungi attrezzatura");
            if(isAlmenoUnAttrezzaturaPresente) System.out.println("2- Rimuovi attrezzatura");
            System.out.println("Digitare 3 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> HandlerAttrezzatura.getInstance().aggiungiAttrezzatura();
                case 2 -> { if (isAlmenoUnAttrezzaturaPresente) HandlerAttrezzatura.getInstance().rimuoviAttrezzatura(); else System.out.println("Nessuna attrezzatura presente");}
                case 3 -> flag = true;
            }
        }while(!flag);
    }

    private static void operazioniGestorePrezziSpiaggia() {
        boolean flag = false;
        do {
            boolean isAlmenoUnaFasciaDiPrezzoDisponibile = !(DBMSController.getInstance().ottieniMappaFasce().isEmpty());
            HandlerSpiaggia.getInstance().ottieniVistaSpiaggia();
            boolean isGrigliaSpiaggiaNotCreated = HandlerSpiaggia.getInstance().getSpiaggiaGestita().getListaOmbrelloni().isEmpty();
            HandlerListino.getInstance().ottieniPrezziTipologie();
            boolean isAlmenoUnaTipologiaPresente = !(HandlerListino.getInstance().ottieniPrezziTipologie().isEmpty());
            System.out.println("Selezionare l'operazione");
            if(isAlmenoUnaFasciaDiPrezzoDisponibile) System.out.println("1- Modifica fasce di prezzo");
            if(!isGrigliaSpiaggiaNotCreated) System.out.println("2- Aggiungi fasce di prezzo");
            if(isAlmenoUnaTipologiaPresente) System.out.println("3- Imposta prezzi ombrellone");
            System.out.println("Digitare 4 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> { if(isAlmenoUnaFasciaDiPrezzoDisponibile) HandlerListino.getInstance().modificaFasciaDiPrezzo(); else System.out.println("Nessuna fascia di prezzo disponibile");}
                case 2 -> { if(!isGrigliaSpiaggiaNotCreated) HandlerListino.getInstance().aggiungiFasciaDiPrezzo(); else System.out.println("Non esiste una griglia spiaggia, impossibile creare fasce di prezzo");}
                case 3 -> { if(isAlmenoUnaTipologiaPresente) HandlerListino.getInstance().impostaPrezziOmbrellone(); else System.out.println("Non sono presenti tipologie");}
                case 4 -> flag = true;
            }
        }while(!flag);
    }

    private static void operazioniGestorePrezziBar() {
        boolean flag = false;
        do {
            boolean isAlmenoUnProdottoBarPresente = !(HandlerListino.getInstance().getPrezziBar().isEmpty());
            System.out.println("Selezionare l'operazione");
            System.out.println("1- Aggiungi prodotto bar");
            if(isAlmenoUnProdottoBarPresente) System.out.println("2- Modifica prodotto bar");
            System.out.println("Digitare 3 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> HandlerListino.getInstance().aggiungiProdottoBar();
                case 2 -> { if(isAlmenoUnProdottoBarPresente) HandlerListino.getInstance().modificaProdottoBar(); else System.out.println("Nessun prodotto bar esistente");}
                case 3 -> flag = true;
            }
        }while(!flag);
    }

    private static void operazioniGestoreAttivita() {
        boolean flag = false;
        do {
            boolean isAlmenoUnAttivitaPresente = !(HandlerAttivita.getInstance().ottieniListaAttivitaDisponibili().isEmpty());
            System.out.println("Selezionare l'operazione");
            System.out.println("1- Aggiungi attività");
            if(isAlmenoUnAttivitaPresente) System.out.println("2- Modifica attività");
            System.out.println("Digitare 3 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> HandlerAttivita.getInstance().aggiungiAttivita();
                case 2 -> { if(isAlmenoUnAttivitaPresente) HandlerAttivita.getInstance().modificaAttivita(); else System.out.println("Nessun'attività presente");}
                case 3 -> flag = true;
            }
        }while(!flag);
    }


    private static void operazioniServizioBar() {
        boolean flag = false;
        do {
            boolean isAlmenoUnOrdinazionePresente = !(DBMSController.getInstance().ottieniListaOrdiniBar().isEmpty());
            System.out.println("Selezionare l'operazione");
            if(isAlmenoUnOrdinazionePresente) System.out.println("1- Completa ordinazione");
            System.out.println("Digitare 2 per chiudere il menu' di selezione");
            switch (provaScannerInt()) {
                case 1 -> { if(isAlmenoUnOrdinazionePresente) ServizioBar.getInstance().completaOrdinazione(); else System.out.println("Nessun ordine in sospeso presente");}
                case 2 -> flag = true;
            }
        }while(!flag);
    }

    private static void registrazioneCliente(){
        System.out.println("Inserire il nome");
        String nome = sc.nextLine();
        System.out.println("Inserire il cognome");
        String cognome = sc.nextLine();
        System.out.println("Inserire il numero di telefono (facoltativo)");
        String telefono = sc.nextLine();
        System.out.println("Inserire la mail");
        String mail = sc.nextLine();
        DBMSController.getInstance().registraCliente(nome,cognome,telefono,mail);
        System.out.println("Il tuo id e': " + DBMSController.getInstance().getCliente(nome,cognome,telefono,mail));
    }

    private static int provaScannerInt(){
        while(true){
            try{
                int intero = sc.nextInt();
                sc.nextLine();
                return intero;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }
}
