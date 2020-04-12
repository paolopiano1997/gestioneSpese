package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Categoria;
import model.Circuito;
import model.Movimento;

public class InserimentoController implements Initializable{

	@FXML
	private Button inserisci;

	@FXML
	private TextArea descrizione;
	
	@FXML
	private RadioButton entrata;
	
	@FXML
	private RadioButton uscita;
	
	@FXML
	private DatePicker data;
	
	@FXML
	private ComboBox<Categoria> categoria;
	
	@FXML
	private Button annulla;
	
	@FXML
	private ComboBox<Circuito> circuito;
	
	@FXML
	private TextField importo;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		circuito.setItems(FXCollections.observableArrayList(Circuito.values()));
		categoria.setItems(FXCollections.observableArrayList(Categoria.values()));
	}
	
	public void inserisciMovimento(ActionEvent e) {
		if(check()) {
			float importo = Float.parseFloat(this.importo.getText());
			if(entrata.isSelected())
				SpeseController.controller.putMovimento(new Movimento(
						categoria.getValue(),
						importo,
						data.getValue(),
						circuito.getValue(),
						descrizione.getText()
						));
			else if(uscita.isSelected())
				SpeseController.controller.putMovimento(new Movimento(
						categoria.getValue(),
						-importo,
						data.getValue(),
						circuito.getValue(),
						descrizione.getText()
						));
			Node n = (Node)e.getSource();
			Stage s = (Stage)n.getScene().getWindow();
			s.close();
		}
		
	}
	
	public void annulla(ActionEvent e) {
		Node n = (Node)e.getSource();
		Stage s = (Stage)n.getScene().getWindow();
		s.close();
	}

	private boolean check() {
		try {
			Float.parseFloat(importo.getText());
		}catch(NumberFormatException e) {
			SpeseController.error("Inserisci un importo");
			return false;
		}
		if(circuito.getValue()==null) {
			SpeseController.error("Inserisci un circuito");
			return false;
		}
		if(categoria.getValue()==null && uscita.isSelected()) {
			SpeseController.error("Inserisci una categoria");
			return false;
		}
		if(data.getValue()==null) {
			SpeseController.error("Inserisci una data");
			return false;
		}	
		return true;
	}

}
