package it.unicam.cs.ids2021;

import java.util.ArrayList;
import java.util.Date;

public class ListaAttivita {

    private final ArrayList<Attivita> listaAttivita ;

    public ListaAttivita() {
        this.listaAttivita = new ArrayList<>();
    }

    public void aggiornaListaAttivita(){

    }

    public boolean isNuovaAttivita(Attivita attivita){  //controlloInserimento
        for(Attivita attivitaEsistenti : this.listaAttivita){
            if(attivitaEsistenti.equals(attivita)) return false;
        }
        return true;
    }

    public ArrayList<Attivita> getListaAttivita(){
        return this.listaAttivita;
    }

    public boolean controlloDisponibilitaNome(){
        return true;
    }

    public void aggiornaNomeAttivita(String nuovoNome, int idAttivita){

    }

    public void aggiornaDescrizioneAttivita(String nuovaDescrizione, int idAttivita){

    }

    public void aggiornaDataEOraAttivita(Date nuovaData, int idAttivita){

    }

    public int ottieniNumeroIscrittiAttivita(int idAttivita){
        return 0;
    }

    public void aggiornaNumeroMassimoPostiAttivita(int numeroMassimo, int idAttivita){

    }

    public void aggiornaNumeroAttrezzaturaAttivita(int idAttivita,int numeroAttrezzatura, int idAttrezzatura){

    }

    public boolean rimuoviAttivita(int idAttivita){
        return true;
    }

    public void aggiungiAttivita(Attivita attivita){
        this.listaAttivita.add(attivita);
    }

    public int getNewIdAttivita(){
        int highestId = -1;
        for(Attivita attivita : this.listaAttivita){
            if(attivita.getId() > highestId){
                highestId = attivita.getId();
            }
        }
        return highestId + 1;
    }

}
