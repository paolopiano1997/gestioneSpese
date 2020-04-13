package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.Circuito;
import model.Movimento;
import model.SaldoIniziale;
import model.Movimenti;
import persistence.GestioneMovimento;
import persistence.PersistenceController;


public class MovimentiDBController extends PersistenceController implements Controller, GestioneMovimento{
	private Movimenti movimenti;
	
	public MovimentiDBController(String connString, String pathFileOp) {
		super(connString, pathFileOp);
		try {
//			System.out.println("Carico movimenti");
			visualizzaMovimenti();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Movimenti visualizzaMovimenti() throws SQLException {
		List<Movimento> result = new ArrayList<Movimento>();
		Connection db = getConnection();
		Statement stmt  = db.createStatement();
		String sql = "SELECT * FROM Movimento";
		ResultSet rs = stmt.executeQuery(sql);
		/*
		 * creazione della lista delle Movimenti
		 */
		while (rs.next()) {
            	result.add(new Movimento(rs.getLong("id"),
            			Categoria.valueOf(rs.getString("categoria")),
            			rs.getFloat("Importo"),
            			LocalDate.parse(rs.getString("Data"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            			Circuito.valueOf(rs.getString("Circuito")),
            			rs.getString("Descrizione")
            			)); 
        }
		db.close();
//		System.out.println("Finita la query: " + result);
		this.movimenti = new Movimenti(result);
		return movimenti;
	}

	@Override
	public boolean inserisciMovimento(Movimento e) throws SQLException {
		Connection db = getConnection();
		Statement stmt  = db.createStatement();
		String sql = "INSERT INTO Movimento (Data,Categoria,Importo,Circuito,Descrizione) "+
				"VALUES ('"+e.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"',"+"'"+e.getCategoria().name()+"',"+"'"+e.getImporto()+"','"+ e.getCircuito().name() + "',"+
				"'"+e.getDescrizione()  + "')";
		int res = stmt.executeUpdate(sql);
//		System.out.println("Inserimento movimento...\nResult: "+res);
		db.close();
		return res>0 ? true : false;
	}


	@Override
	public Movimenti getMovimentiByDay(LocalDate day) {
		return new Movimenti(this.movimenti.getMovimentiByDay(day));
	}

	@Override
	public Movimenti getMovimentiByWeek(LocalDate dayWeek) {
		return new Movimenti(this.movimenti.getMovimentiByWeek(dayWeek));
	}

	@Override
	public Movimenti getMovimentiBetweenTwoDays(LocalDate first, LocalDate second) {
		return new Movimenti(this.movimenti.getMovimentiBetweenTwoDays(first, second));
	}

	@Override
	public Movimenti getMovimentiByMonth(LocalDate day) {
		return new Movimenti(this.movimenti.getMovimentiByMonth(day));
	}

	@Override
	public boolean rimuoviMovimento(Movimento m) throws SQLException {
		movimenti.removeMovimento(m);
		Connection db = getConnection();
		Statement stmt  = db.createStatement();
		String sql = "DELETE FROM Movimento WHERE id='" + m.getId() +"'";
		int res = stmt.executeUpdate(sql);
		db.close();
//		System.out.println("rimozione movimento...\nResult: "+res);
		return res>0 ? true : false;
	}

	@Override
	public Movimenti getMovimenti() {
		try {
			return visualizzaMovimenti();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean modificaMovimento(Movimento m1, Movimento m2) throws SQLException {
		return (rimuoviMovimento(m1) && inserisciMovimento(m2));
		
	}

	@Override
	public boolean setSaldoIniziale(LocalDate day, float importo, Circuito circuito){
		try {
			Connection db = getConnection();
			Statement stmt  = db.createStatement();
			stmt.execute("DELETE FROM SaldoIniziale where circuito='" + circuito.name() + "'");
			db.close();
			db = getConnection();
			stmt = db.createStatement();
			String sql = "INSERT INTO SaldoIniziale (Data,Importo,Circuito) "+
					"VALUES ('"+day.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"',"+"'"+importo+ "'," + "'" + circuito.name() + "')";
			int res = stmt.executeUpdate(sql);
			db.close();
//			System.out.println("Inserimento Saldo Iniziale...\nResult: "+res);
			return res>0 ? true : false;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public SaldoIniziale getSaldoIniziale(Circuito circuito){
		try {
		Connection db = getConnection();
		Statement stmt = db.createStatement();
		String sql = "SELECT * FROM SaldoIniziale where circuito='" + circuito +"'";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			float imp = rs.getFloat("Importo");
			LocalDate data = LocalDate.parse(rs.getString("data"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			db.close();
			return new SaldoIniziale(circuito,data,imp);
		}
		else {
			db.close();
			return null;
		}
		}catch(SQLException e) {
			return null;
		}
	}

	@Override
	public float getSaldoAttualeAt(LocalDate day,Circuito circuito) {
		SaldoIniziale si = getSaldoIniziale(circuito);
		if(si==null)
			return 0;
		if(si.getData().isAfter(day))
			return 0;
		float importo = si.getImporto();
		for(Movimento m : movimenti.getMovimenti()) {
			if(m.getCircuito().equals(circuito))
				if(m.getData().isBefore(day) || m.getData().isEqual(day))
					if(m.getData().isAfter(si.getData()) || m.getData().isEqual(si.getData()))
						importo+=m.getImporto();
		}
		return importo;
	}

	@Override
	public boolean eliminaMovimento(Movimento m) {
		try {
			return rimuoviMovimento(m);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Movimenti getMovimentiByCategoria(Categoria c) {
		try {
		List<Movimento> result = new ArrayList<Movimento>();
		Connection db = getConnection();
		Statement stmt  = db.createStatement();
		String sql = "SELECT * FROM Movimento where categoria='" + c.name() + "'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
            	result.add(new Movimento(rs.getLong("id"),
            			Categoria.valueOf(rs.getString("categoria")),
            			rs.getFloat("Importo"),
            			LocalDate.parse(rs.getString("Data"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            			Circuito.valueOf(rs.getString("Circuito")),
            			rs.getString("Descrizione")
            			)); 
        }
		db.close();
		this.movimenti = new Movimenti(result);
		return movimenti;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Movimenti getMovimentiByCircuito(Circuito c) {
		try {
		List<Movimento> result = new ArrayList<Movimento>();
		Connection db = getConnection();
		Statement stmt  = db.createStatement();
		String sql = "SELECT * FROM Movimento where circuito='" + c.name() + "'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
            	result.add(new Movimento(rs.getLong("id"),
            			Categoria.valueOf(rs.getString("categoria")),
            			rs.getFloat("Importo"),
            			LocalDate.parse(rs.getString("Data"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            			Circuito.valueOf(rs.getString("Circuito")),
            			rs.getString("Descrizione")
            			)); 
        }
		db.close();
		this.movimenti = new Movimenti(result);
		return movimenti;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean putMovimento(Movimento m) {
		try {
			return inserisciMovimento(m);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


}
