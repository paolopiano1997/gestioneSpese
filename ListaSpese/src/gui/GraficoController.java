package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import model.Categoria;
import model.Circuito;
import model.Movimenti;
import model.Movimento;

public class GraficoController implements Initializable{

	@FXML
	private PieChart grafico;

	@FXML
	private RadioButton tutti;
	
	@FXML
	private RadioButton contanti;
	
	@FXML
	private RadioButton bancomat;
	
	@FXML
	private RadioButton carta;
	
	@FXML
	private DatePicker filtroGiorno;
	
	@FXML
	private DatePicker filtroSettimana;
	
	@FXML
	private DatePicker filtroMese;
	
	private String msg = "now";
	
	private LocalDate date = LocalDate.now();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Movimenti ms = getMovimentiByDay();
		if(ms==null) {
			SpeseController.error("Errore inizializzazione grafico");
			return;
		}
		HashMap<Categoria,Float> data = new HashMap<Categoria,Float>();
		carica(ms, data);
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
		for(Categoria c : data.keySet()) {
//			System.out.println("Categoria: " + c.name() + " importo: " + data.get(c));
			pieChartData.add(new PieChart.Data(c.name() + " " + data.get(c) +"€", data.get(c)));
		}
		grafico.setData(pieChartData);
	}

	private void carica(Movimenti ms, HashMap<Categoria, Float> data) {
		Movimenti mv = ms;
		if(contanti.isSelected())
			mv = carica(ms,Circuito.CONTANTI);
		else if(bancomat.isSelected())
			mv = carica(ms,Circuito.BANCOMAT);
		else if(carta.isSelected())
			mv = carica(ms,Circuito.CARTA);
		for(Movimento m : mv.getMovimenti()) {
			if(!m.getCategoria().equals(Categoria.ENTRATA)) {
				if(data.containsKey(m.getCategoria())) {
					data.put(m.getCategoria(), data.get(m.getCategoria()) + -m.getImporto());
				}
				else {
					data.put(m.getCategoria(), -m.getImporto());
				}
			}
		}
	}

	private Movimenti carica(Movimenti ms,Circuito c) {
		Movimenti res = new Movimenti();
		for(Movimento m : ms.getMovimenti()) {
			if(m.getCircuito().equals(c))
				res.addMovimento(m);
		}
		return res;
	}

	private Movimenti getMovimentiByDay() {
		switch(msg) {
			case "now" : return SpeseController.controller.getMovimenti();
			case "day" : return SpeseController.controller.getMovimentiByDay(date);
			case "week" : return SpeseController.controller.getMovimentiByWeek(date);
			case "month" : return SpeseController.controller.getMovimentiByMonth(date);
			default : return null;
		}
	}

	public void filtraPerGiorno(ActionEvent e) {
		if(filtroGiorno.getValue()!=null) {
			msg = "day";
			date = filtroGiorno.getValue();
			initialize(null,null);
		}
	}
	
	public void filtraPerSettimana(ActionEvent e) {
		if(filtroSettimana.getValue()!=null) {
			msg = "week";
			date = filtroSettimana.getValue();
			initialize(null,null);
		}
	}
	
	public void filtraPerMese(ActionEvent e) {
		if(filtroMese.getValue()!=null) {
			msg = "month";
			date = filtroMese.getValue();
			initialize(null,null);
		}
	}
	
	public void visualizza(ActionEvent e) {
		initialize(null,null);
	}
	
}
