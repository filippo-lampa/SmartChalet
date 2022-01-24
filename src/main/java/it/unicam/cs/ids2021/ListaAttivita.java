package it.unicam.cs.ids2021;

import java.util.ArrayList;
import java.util.Date;

public class ListaAttivita {

    private ArrayList<Attivita> lista;

    public ListaAttivita(){
        this.lista = new ArrayList<>();
    }

    public void aggiornaListaAttivita(ArrayList<Attivita> listaAggiornata){
        this.lista = listaAggiornata;
    }

    public ArrayList<Attivita> ottieniListaAttivitaAggiornata() {
        return  this.lista;
    }

    public int getNewIdAttivita(){
        int highestId = -1;
        for(Attivita attivita : this.lista){
            if(attivita.getId() > highestId){
                highestId = attivita.getId();
            }
        }
        return highestId + 1;
    }

    public boolean isNuovaAttivita(Attivita attivita){  //controlloInserimento
        for(Attivita attivitaEsistenti : this.lista){
            if(attivitaEsistenti.equals(attivita)) return false;
        }
        return true;
    }

    public void aggiungiAttivita(Attivita attivita){
        this.lista.add(attivita);
    }

    public boolean controlloDisponibilitaNome(String nome, Date data, int fasciaOraria) {
        for(Attivita attivita : this.lista)
            if(attivita.getNome().equals(nome) && attivita.getData().equals(data) && attivita.getFasciaOraria() == fasciaOraria)
                return false;
        return true;
    }

    public void aggiornaNomeAttivita(Attivita attivitaDaModificare, String nome) {
        this.lista.get(this.lista.indexOf(attivitaDaModificare)).setNome(nome);
    }

    public void aggiornaDescrizioneAttivita(Attivita attivitaDaModificare, String descrizione) {
        this.lista.get(this.lista.indexOf(attivitaDaModificare)).setDescrizione(descrizione);

    }

    public void aggiornaDataEOraAttivita(Attivita attivitaDaModificare, Date nuovaData, int fasciaOraria) {
        this.lista.get(this.lista.indexOf(attivitaDaModificare)).setData(nuovaData);
        this.lista.get(this.lista.indexOf(attivitaDaModificare)).setFasciaOraria(fasciaOraria);
    }

    public void aggiornaNumeroMassimoPostiAttivita(Attivita attivitaDaModificare, int nuovoMaxPosti) {
        this.lista.get(this.lista.indexOf(attivitaDaModificare)).setMaxPartecipanti(nuovoMaxPosti);

    }

    public int ottieniNumeroIscrittiAttivita(Attivita attivitaDaModificare) {
        return attivitaDaModificare.getNumeroIscritti();
    }

    public void rimuoviAttivita(Attivita attivitaDaModificare) {
        this.lista.remove(attivitaDaModificare);
    }
}
