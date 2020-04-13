package controller;

import java.time.LocalDate;

import model.Categoria;
import model.Circuito;
import model.Movimenti;
import model.Movimento;
import model.SaldoIniziale;

public interface Controller {
	
	public boolean setSaldoIniziale(LocalDate day, float importo,Circuito circuito);
	
	public SaldoIniziale getSaldoIniziale(Circuito circuito);
	
	public float getSaldoAttualeAt(LocalDate day, Circuito circuito);
	
	public boolean putMovimento(Movimento m);
	
	public boolean eliminaMovimento(Movimento m);
	
	public Movimenti getMovimenti();
	
	public Movimenti getMovimentiByDay(LocalDate day);
	
	public Movimenti getMovimentiByWeek(LocalDate dayWeek);
	
	public Movimenti getMovimentiByMonth(LocalDate day);
	
	public Movimenti getMovimentiBetweenTwoDays(LocalDate first, LocalDate second);
	
	public Movimenti getMovimentiByCategoria(Categoria c);
	
	public Movimenti getMovimentiByCircuito(Circuito c);
	
}
