package it.unicam.ids.smartchalet.asf;

public class FasciaDiPrezzo {

    private String nome;
    private Coordinate coordinateInizio;
    private Coordinate coordinateFine;
    private int id; //TODO

    public FasciaDiPrezzo(String nome){
        this(nome,null,null);
    }

    public FasciaDiPrezzo(String nome, Coordinate coordinateInizio ,Coordinate coordinateFine){
        this.nome = nome;
        this.coordinateInizio = coordinateInizio;
        this.coordinateFine = coordinateFine;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Coordinate getCoordinateInizio() {
        return this.coordinateInizio;
    }

    public void setCoordinateInizio(Coordinate coordinateInizio) {
        this.coordinateInizio = coordinateInizio;
    }

    public Coordinate getCoordinateFine() {
        return this.coordinateFine;
    }

    public void setCoordinateFine(Coordinate coordinateFine) {
        this.coordinateFine = coordinateFine;
    }

    @Override
    public String toString() {
        return "{"+ nome + '\'' + ", Inizio: " + coordinateInizio + ", Fine: " + coordinateFine + '}';
    }
}