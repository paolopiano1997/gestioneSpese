package model;

import java.time.LocalDate;

public class SaldoIniziale {
	private Circuito circuito;
	private LocalDate data;
	private float importo;
	public SaldoIniziale(Circuito circuito, LocalDate data, float importo) {
		super();
		this.circuito = circuito;
		this.data = data;
		this.importo = importo;
	}
	public Circuito getCircuito() {
		return circuito;
	}
	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public float getImporto() {
		return importo;
	}
	public void setImporto(float importo) {
		this.importo = importo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((circuito == null) ? 0 : circuito.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		SaldoIniziale other = (SaldoIniziale) obj;
		if (circuito != other.circuito)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (Float.floatToIntBits(importo) != Float.floatToIntBits(other.importo))
			return false;
		return true;
	}

}
