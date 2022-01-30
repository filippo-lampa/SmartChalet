import it.unicam.ids.smartchalet.asf.Coordinate;
import it.unicam.ids.smartchalet.asf.Ombrellone;
import it.unicam.ids.smartchalet.asf.Prenotazione;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HandlerPrenotazioneTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Coordinate coordinate1 = new Coordinate(0,0);
        Ombrellone ombrellone1 = new Ombrellone("Uno", coordinate1, 1);

        Coordinate coordinate2 = new Coordinate(0,1);
        Ombrellone ombrellone2 = new Ombrellone("Uno", coordinate2, 2);

        Coordinate coordinate3 = new Coordinate(1,0);
        Ombrellone ombrellone3 = new Ombrellone("Uno", coordinate3, 3);

        Coordinate coordinate4 = new Coordinate(1,1);
        Ombrellone ombrellone4 = new Ombrellone("Uno", coordinate4, 4);

        ArrayList<Prenotazione> listaPrenTest= new ArrayList<Prenotazione>();
        Prenotazione prenotazioneTest1 = new Prenotazione();
        prenotazioneTest1.setId(1);
        prenotazioneTest1.setIdCliente(1);

        Date dataInizio1 = null;
        Date dataFine1 = null;
        Date dataTest1 = null;
        try {
            dataInizio1 = new SimpleDateFormat("dd/MM/yyyy").parse("22/02/2022");
            dataFine1 = new SimpleDateFormat("dd/MM/yyyy").parse("24/02/2022");
            dataTest1 = new SimpleDateFormat("dd/MM/yyyy").parse("23/02/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest1.setDataInizio(dataInizio1);
        prenotazioneTest1.setDataFine(dataFine1);
        prenotazioneTest1.setNumeroLettini(2);
        prenotazioneTest1.setPrezzoTotale(10.00);
        HashMap<Date, Integer> mappaDateFasceTemp1 = new HashMap<>();
        mappaDateFasceTemp1.put(dataInizio1,1);
        mappaDateFasceTemp1.put(dataTest1,2);
        mappaDateFasceTemp1.put(dataFine1,3);
        prenotazioneTest1.setMappaDateFasce(mappaDateFasceTemp1);
        ArrayList<Ombrellone> listaOmbrelloniTest1 = new ArrayList<>();
        listaOmbrelloniTest1.add(ombrellone1);
        prenotazioneTest1.setOmbrelloni(listaOmbrelloniTest1);



        Prenotazione prenotazioneTest2 = new Prenotazione();
        prenotazioneTest2.setId(2);
        prenotazioneTest2.setIdCliente(1);
        Date dataInizio2 = null;
        Date dataFine2 = null;
        Date dataTest2 = null;

        try {
            dataInizio2 = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2022");
            dataFine2 = new SimpleDateFormat("dd/MM/yyyy").parse("03/03/2022");
            dataTest2 = new SimpleDateFormat("dd/MM/yyyy").parse("02/03/2022");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest2.setDataInizio(dataInizio2);
        prenotazioneTest2.setDataFine(dataFine2);
        prenotazioneTest2.setNumeroLettini(1);
        prenotazioneTest2.setPrezzoTotale(20.00);
        HashMap<Date, Integer> mappaDateFasceTemp2 = new HashMap<>();
        mappaDateFasceTemp2.put(dataInizio2,1);
        mappaDateFasceTemp2.put(dataTest2,2);
        mappaDateFasceTemp2.put(dataFine2,3);
        prenotazioneTest2.setMappaDateFasce(mappaDateFasceTemp2);
        ArrayList<Ombrellone> listaOmbrelloniTest2 = new ArrayList<>();
        listaOmbrelloniTest2.add(ombrellone1);
        listaOmbrelloniTest2.add(ombrellone2);
        listaOmbrelloniTest2.add(ombrellone3);
        listaOmbrelloniTest2.add(ombrellone4);
        prenotazioneTest2.setOmbrelloni(listaOmbrelloniTest2);


        Prenotazione prenotazioneTest3 = new Prenotazione();
        prenotazioneTest3.setId(3);
        prenotazioneTest3.setIdCliente(1);
        Date dataInizio3 = null;
        Date dataFine3 = null;
        try {
            dataInizio3 = new SimpleDateFormat("dd/MM/yyyy").parse("05/02/2022");
            dataFine3 = new SimpleDateFormat("dd/MM/yyyy").parse("05/02/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest3.setDataInizio(dataInizio3);
        prenotazioneTest3.setDataFine(dataFine3);
        prenotazioneTest3.setNumeroLettini(1);
        prenotazioneTest3.setPrezzoTotale(30.00);
        HashMap<Date, Integer> mappaDateFasceTemp3 = new HashMap<>();
        mappaDateFasceTemp3.put(dataInizio3, 3);
        prenotazioneTest3.setMappaDateFasce(mappaDateFasceTemp3);
        ArrayList<Ombrellone> listaOmbrelloniTest3 = new ArrayList<>();
        listaOmbrelloniTest3.add(ombrellone3);
        listaOmbrelloniTest3.add(ombrellone4);
        prenotazioneTest3.setOmbrelloni(listaOmbrelloniTest3);



        listaPrenTest.add(prenotazioneTest1);
        listaPrenTest.add(prenotazioneTest2);
        listaPrenTest.add(prenotazioneTest3);
    }
}