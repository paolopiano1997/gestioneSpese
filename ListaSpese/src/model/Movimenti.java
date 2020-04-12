package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class Movimenti {
	private List<Movimento> movimenti = new ArrayList<Movimento>();
	
	public Movimenti(List<Movimento> movimenti) {
		this.movimenti=movimenti;
	}
	
	public Movimenti(Movimento ...m) {
		for(int i=0;i<m.length;i++) {
			movimenti.add(m[i]);
		}
	}
	
	
	public List<Movimento> getMovimenti() {
		return movimenti;
	}
	
	public void addMovimento(Movimento u) {
		movimenti.add(u);
	}
	
	public void removeMovimento(Movimento u) {
		movimenti.remove(u);
	}
	
	public void removeMovimentoDay(LocalDate day) {
		int [] indici = new int[movimenti.size()];
		int count=0;
		for(int i=0;i<movimenti.size();i++) {
			if(movimenti.get(i).getData().getDayOfYear()==day.getDayOfYear()) {
				indici[count]=i;
				count++;
			}
		}
		for(int i=0;i<count;i++) {
			movimenti.remove(indici[i]);
		}
	}
	
	public List<Movimento> getMovimentiByDay(LocalDate day) {
		List<Movimento> res = new ArrayList<Movimento>();
		for(Movimento u : movimenti) {
			if(u.getData().getDayOfYear() == day.getDayOfYear())
				res.add(u);
		}
		return res;
	}
	
	public List<Movimento> getMovimentiByWeek(LocalDate day) {
		List<Movimento> res = new ArrayList<Movimento>();
		for(Movimento u : movimenti) {
			if( (u.getData().isAfter(day.minusDays(day.getDayOfWeek().getValue())) || (u.getData().isEqual(day.minusDays(day.getDayOfWeek().getValue())))) && 
					(u.getData().isBefore(day.minusDays(day.getDayOfWeek().getValue()).plusDays(7)) || u.getData().isEqual(day.minusDays(day.getDayOfWeek().getValue()).plusDays(7))))
				res.add(u);
		}
		return res;
	}
	
	public List<Movimento> getMovimentiByMonth(LocalDate day) {
		List<Movimento> res = new ArrayList<Movimento>();
		for(Movimento u : movimenti) {
			if( (u.getData().isAfter(day.minusDays(day.getDayOfMonth())) || (u.getData().isEqual(day.minusDays(day.getDayOfMonth())))) && 
					(u.getData().isBefore(day.minusDays(day.getDayOfMonth()).plusMonths(1)) || u.getData().isEqual(day.minusDays(day.getDayOfMonth()).plusMonths(1))))
				res.add(u);
		}
		return res;
	}
	
	public List<Movimento> getMovimentiBetweenTwoDays(LocalDate first, LocalDate second){
		List<Movimento> res = new ArrayList<Movimento>();
		for(Movimento u : movimenti) {
			if( (u.getData().isAfter(first) || (u.getData().isEqual(first))) && 
					(u.getData().isBefore(second) || u.getData().isEqual(second)))
				res.add(u);
		}
		return res;
	}
}
