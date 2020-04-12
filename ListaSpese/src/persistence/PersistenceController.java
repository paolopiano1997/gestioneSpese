package persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PersistenceController {
	private String connStr;
	private Connection dbConnection;
	private Writer printerOperazioni;
	
	public PersistenceController(String connString, String pathFileOp) {
		this.connStr = connString;
		try {
			printerOperazioni = openLogOperation(pathFileOp);
			logOp("Getting controller instance at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " with connString=" + connString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Connection openConnection() throws PersistenceException {
		try {
			logOp("Try to open connection at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " with connString=" + connStr);
		    dbConnection = DriverManager.getConnection(connStr);
//		    System.out.println("Connessione avvenuta!!!!!");
		}
		catch(SQLException | IOException e) {
			System.out.println(e.getMessage());
			throw new PersistenceException(e.getMessage());
		}
		return dbConnection;
	}
	
	private Writer openLogOperation(String pathFile) throws IOException {
		return new PrintWriter(new FileWriter(pathFile,true));
	}
	
	protected void logOp(String m) throws IOException {
		printerOperazioni.append(m + "\n");
		printerOperazioni.flush();
	}
	
	protected Connection getConnection() {
		try {
			logOp("GettingConnection at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " with connString=" + connStr);
			dbConnection =  openConnection();
		} catch (PersistenceException | IOException e) {
			e.printStackTrace();
		}
		return dbConnection;
	}
	
}
