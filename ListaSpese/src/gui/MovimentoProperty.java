package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class MovimentoProperty {
	private SimpleLongProperty id;
	private SimpleStringProperty categoria;
	private SimpleFloatProperty importo;
	private SimpleStringProperty data;
	private SimpleStringProperty circuito;
	private SimpleStringProperty descrizione;
	
	
	public MovimentoProperty(long id,String categoria, float importo, LocalDate data,
			String circuito, String descrizione) {
		super();
		this.id = new SimpleLongProperty(id);
		this.categoria = new SimpleStringProperty(categoria);
		this.importo = new SimpleFloatProperty(importo);
		this.data = new SimpleStringProperty(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		this.circuito =new SimpleStringProperty(circuito);
		this.descrizione = new SimpleStringProperty(descrizione);
	}
	

	public String getData() {
		return data.getValue();
	}
	
	public void setData(LocalDate data) {
		this.data = new SimpleStringProperty(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}


	public String getCategoria() {
		return categoria.getValue();
	}


	public void setCategoria(String categoria) {
		this.categoria = new SimpleStringProperty(categoria);
	}


	public float getImporto() {
		return importo.floatValue();
	}


	public void setImporto(float importo) {
		this.importo = new SimpleFloatProperty(importo);
	}


	public String getCircuito() {
		return circuito.getValue();
	}


	public void setCircuito(String circuito) {
		this.circuito = new SimpleStringProperty(circuito);
	}


	public String getDescrizione() {
		return descrizione.getValue();
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = new SimpleStringProperty(descrizione);
	}

	public long getId() {
		return id.get();
	}


	public void setId(long id) {
		this.id = new SimpleLongProperty(id);
	}
	
}

