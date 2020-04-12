package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movimento implements Comparable<Movimento>{
	private long id;
	private float importo;
	private LocalDate data;
	private Circuito circuito;
	private String descrizione;
	private Categoria categoria;
	
	public Movimento(long id,Categoria categoria,float importo, LocalDate data, Circuito circuito, String descrizione) {
		this.setId(id);
		this.importo = importo;
		this.data = data;
		this.circuito = circuito;
		this.descrizione = descrizione;
		this.categoria=categoria;
	}
	
	public Movimento(Categoria categoria, float importo, LocalDate data, Circuito circuito, String descrizione) {
		this.categoria = categoria;
		this.importo = importo;
		this.data = data;
		this.circuito = circuito;
		this.descrizione = descrizione;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getImporto() {
		return importo;
	}

	public void setImporto(float importo) {
		this.importo = importo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Circuito getCircuito() {
		return circuito;
	}

	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public Categoria getCategoria() {
		return this.categoria;
	}


	
	@Override
	public int compareTo(Movimento o) {
		if(o.getData().isEqual(this.data))
			return 0;
		else if(o.getData().isAfter(this.data))
			return -1;
		else
			return 1;
	}

	@Override
	public String toString() {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +";" + importo + ";" + circuito.name() + ";" + descrizione +";" + categoria;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((circuito == null) ? 0 : circuito.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + Float.floatToIntBits(importo);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimento other = (Movimento) obj;
		if (categoria != other.categoria)
			return false;
		if (circuito != other.circuito)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (id != other.id)
			return false;
		if (Float.floatToIntBits(importo) != Float.floatToIntBits(other.importo))
			return false;
		return true;
	}

}
