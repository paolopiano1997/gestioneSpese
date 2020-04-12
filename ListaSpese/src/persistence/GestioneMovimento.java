package persistence;

import java.sql.SQLException;

import model.Movimenti;
import model.Movimento;

public interface GestioneMovimento {

	public boolean inserisciMovimento(Movimento m) throws SQLException;
	
	public boolean rimuoviMovimento(Movimento m) throws SQLException;
	
	public Movimenti visualizzaMovimenti() throws SQLException;
	
	public boolean modificaMovimento(Movimento m1,Movimento m2) throws SQLException;
	
}
