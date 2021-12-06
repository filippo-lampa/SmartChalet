import java.util.Objects;

public class Ombrellone {

    private int idTipo;
    private int numeroLettiniAssociati;
    private boolean prenotato;
    private int idOmbrellone;

    public Ombrellone(int idTipo){
        this.idTipo = idTipo;
        this.numeroLettiniAssociati = 0;
        this.prenotato = false;
    }

    public int getIdTipo(){
        return this.idTipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ombrellone that = (Ombrellone) o;
        return idOmbrellone == that.idOmbrellone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOmbrellone);
    }

    public int getIdOmbrellone(){
        return this.idOmbrellone;
    }

    public boolean isBooked(){
        return prenotato;
    }

    public int getNumeroLettiniAssociati(){
        return this.numeroLettiniAssociati;
    }

    public void setTipo(int idTipoOmbrellone){
        this.idTipo = idTipoOmbrellone;
    }

    public void setNumeroLettiniAssociati(int numeroLettini){
        this.numeroLettiniAssociati = numeroLettini;
    }
}
