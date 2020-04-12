package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Categoria;
import model.Circuito;
import model.Movimento;

public class ModificaController implements Initializable{

	@FXML
	private Button modifica;
	
	@FXML
	private Button elimina;

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
	
	private MovimentoProperty m;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		circuito.setItems(FXCollections.observableArrayList(Circuito.values()));
		categoria.setItems(FXCollections.observableArrayList(Categoria.values()));
		m = SpeseController.mp;
		circuito.getSelectionModel().select(Circuito.valueOf(m.getCircuito()));
		categoria.getSelectionModel().select(Categoria.valueOf(m.getCategoria()));
		data.setValue(LocalDate.parse(m.getData(),DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		importo.setText("" + (-m.getImporto()));
		descrizione.setText(m.getDescrizione());
	}
	
	public void modificaMovimento(ActionEvent e) {
		if(check()) {
			SpeseController.controller.eliminaMovimento(new Movimento(m.getId(),Categoria.valueOf(m.getCategoria()),m.getImporto(),LocalDate.parse(m.getData(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
	        		Circuito.valueOf(m.getCircuito()),m.getDescrizione()));
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
	
	public void eliminaMovimento(ActionEvent e) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Eliminazione");
		alert.setHeaderText("Stai per eliminare un movimento");
		alert.setContentText("Vuoi continuare con l'eliminazione o cancellare?");
		Optional<ButtonType> option = alert.showAndWait();
		if(option.get().equals(ButtonType.OK)){
	        SpeseController.controller.eliminaMovimento(new Movimento(m.getId(),Categoria.valueOf(m.getCategoria()),m.getImporto(),LocalDate.parse(m.getData(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
	        		Circuito.valueOf(m.getCircuito()),m.getDescrizione()));
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
