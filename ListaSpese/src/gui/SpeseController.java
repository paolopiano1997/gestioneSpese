package gui;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import controller.Controller;
import controller.MovimentiDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Circuito;
import model.Movimenti;
import model.Movimento;
import model.SaldoIniziale;


public class SpeseController implements Initializable{
	public static Controller controller;
	public static MovimentoProperty mp;
	private double xOffset = 0;
	private double yOffset = 0;
	
	@FXML
	private ImageView exitButton;
	
	@FXML
	private TableView<MovimentoProperty> speseTable;
		@FXML private TableColumn<MovimentoProperty,LocalDate> dataColumn;
		@FXML private TableColumn<MovimentoProperty,Float> importoColumn;
		@FXML private TableColumn<MovimentoProperty,String> categoriaColumn;
		@FXML private TableColumn<MovimentoProperty,String> circuitoColumn;
		@FXML private TableColumn<MovimentoProperty,String> descrizioneColumn;
		
	@FXML
	private DatePicker filtraPerMese;
	
	@FXML
	private Label saldoInizialeAttuale;
	
	@FXML
	private DatePicker saldoAtDay;
	
	@FXML
	private Label saldoAtDayLabel;
	
	@FXML
	private Label saldoAttuale;
	
	@FXML
	private DatePicker dataSaldoIniziale;
	
	@FXML
	private TextField importoSaldoIniziale;
	
	@FXML
	private Button salvaSaldoIniziale;
	
	@FXML
	private DatePicker filtraPerSettimana;
	
	@FXML
	private RadioButton contanti;
	
	@FXML
	private RadioButton bancomat;
	
	@FXML
	private RadioButton carta;
	
	@FXML
	private DatePicker filtraPerGiorno;
	
	@FXML
	private Button modifica;
	
	@FXML
	private Button inserisci;
	
	@FXML
	private Button graficoSpese;

	
	private DecimalFormat formatter = new DecimalFormat("##.##");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		File f = new File("resources/Movimenti.db");
//		System.out.println("Absolute path: "+f.getAbsolutePath());
//		String dbUri = "jdbc:sqlite:"+f.getAbsolutePath();
		String dbUri =  "jdbc:sqlite:"+"resources/Movimenti.db";
		String fileOp = "operations.log";
		controller = new MovimentiDBController(dbUri,fileOp);
		
		initializeUsciteTable();
		modifica.setDisable(true);
		speseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null)
		        modifica.setDisable(false);
		    else
		    	modifica.setDisable(true);
		});
		filtraPerGiorno.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(newValue!=null)
            	filtraPerGiorno(newValue);
        });
		filtraPerSettimana.valueProperty().addListener((ov,oldValue,newValue) ->{
			if(newValue!=null)
				filtraPerSettimana(newValue);
		});
		filtraPerMese.valueProperty().addListener((ov,oldValue,newValue)->{
			if(newValue!=null)
				filtraPerMese(newValue);
		});
		saldoAtDay.valueProperty().addListener((ov,oldValue,newValue)->{
			if(newValue!=null) {
				saldoAtDayLabel.setText("Saldo " + circuito() + " al " + newValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + formatter.format(controller.getSaldoAttualeAt(newValue,Circuito.valueOf(circuito()))) +"€");
				verifica(saldoAtDayLabel,controller.getSaldoAttualeAt(newValue,Circuito.valueOf(circuito())));
				saldoAtDay.setValue(null);
			}
		});
		SaldoIniziale si;
		float importo = 0;
		String data = "";
		if((si = controller.getSaldoIniziale(Circuito.valueOf(circuito())))!=null) {
			saldoAttuale.setText(formatter.format(controller.getSaldoAttualeAt(LocalDate.now(),Circuito.valueOf(circuito()))) +"€");
			importo = si.getImporto();
			data = si.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
		saldoInizialeAttuale.setText(circuito() + " " + importo + "€ " + data);
	}
	
	
	private String circuito() {
		if(contanti.isSelected())
			return "CONTANTI";
		else if(bancomat.isSelected())
			return "BANCOMAT";
		else if(carta.isSelected())
			return "CARTA";
		return "";
	}


	private void initializeUsciteTable() {
        dataColumn.setCellValueFactory(new PropertyValueFactory<MovimentoProperty, LocalDate>("data"));
        importoColumn.setCellValueFactory(new PropertyValueFactory<MovimentoProperty,Float>("importo"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<MovimentoProperty,String>("categoria"));
        descrizioneColumn.setCellValueFactory(new PropertyValueFactory<MovimentoProperty,String>("descrizione"));
        circuitoColumn.setCellValueFactory(new PropertyValueFactory<MovimentoProperty,String>("circuito"));
        speseTable.setItems(getItems());
	}


	private ObservableList<MovimentoProperty> getItems() {
		Movimenti s = controller.getMovimenti();
		ObservableList<MovimentoProperty> res = FXCollections.observableArrayList();
		List<Movimento> movimenti = s.getMovimenti();
		Collections.sort(movimenti);
		for(Movimento m : movimenti) {
			res.add(new MovimentoProperty(m.getId(),m.getCategoria().name(),m.getImporto(),m.getData(),m.getCircuito().name(),m.getDescrizione()));
		}
		return res;
	}

	
	public void graficoSpese(ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Grafico.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
			error("Errore caricamento inserimento");
			return;
		}
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.getIcons().add(new Image("file:Spese.png"));
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	public void visualizzaTutti(ActionEvent e) {
		speseTable.setItems(getItems());
	}
	
	
	public void filtraPerSettimana(LocalDate day) {
		speseTable.setItems(getItemsPerWeek(day));
	}
	
	public void filtraPerGiorno(LocalDate day) {
		speseTable.setItems(getItemsPerDay(day));
	}

	public void filtraPerMese(LocalDate newValue) {
		speseTable.setItems(getItemsPerMonth(newValue));
	}
	
	private ObservableList<MovimentoProperty> getItemsPerDay(LocalDate day) {
		ObservableList<MovimentoProperty> res = FXCollections.observableArrayList();
		for(Movimento m : controller.getMovimentiByDay(day).getMovimenti()) {
			res.add(new MovimentoProperty(m.getId(),m.getCategoria().name(),m.getImporto(),m.getData(),m.getCircuito().name(),m.getDescrizione()));
		}
		return res;
	}
	
	private ObservableList<MovimentoProperty> getItemsPerWeek(LocalDate day) {
		ObservableList<MovimentoProperty> res = FXCollections.observableArrayList();
		List<Movimento> movimenti = controller.getMovimentiByWeek(day).getMovimenti();
		Collections.sort(movimenti);
		for(Movimento m : movimenti) {
			res.add(new MovimentoProperty(m.getId(),m.getCategoria().name(),m.getImporto(),m.getData(),m.getCircuito().name(),m.getDescrizione()));
		}
		return res;
	}
	
	private ObservableList<MovimentoProperty> getItemsPerMonth(LocalDate newValue) {
		ObservableList<MovimentoProperty> res = FXCollections.observableArrayList();
		List<Movimento> movimenti = controller.getMovimentiByMonth(newValue).getMovimenti();
		Collections.sort(movimenti);
		for(Movimento m : movimenti) {
			res.add(new MovimentoProperty(m.getId(),m.getCategoria().name(),m.getImporto(),m.getData(),m.getCircuito().name(),m.getDescrizione()));
		}
		return res;
	}
	
	public void modificaMovimento(ActionEvent e) {
		mp = speseTable.getSelectionModel().getSelectedItem();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Modifica.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
			error("Errore caricamento modifica");
			return;
		}
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.getIcons().add(new Image("file:Spese.png"));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(false);
		root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
		stage.setScene(scene);
		stage.showAndWait();
		aggiornaAttuale(e);
		visualizzaTutti(e);
	}
	
	public void inserisciMovimento(ActionEvent e) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Inserimento.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
			error("Errore caricamento inserimento");
			return;
		}
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.getIcons().add(new Image("file:Spese.png"));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(false);
		root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
		stage.setScene(scene);
		stage.showAndWait();
		aggiornaAttuale(e);
		visualizzaTutti(e);
	}
	
	public void salvaSaldoIniziale(ActionEvent e) {
		if(dataSaldoIniziale.getValue()==null) {
			error("Inserisci data saldo iniziale");
			return;
		}
		if(importoSaldoIniziale.getText() == null) {
			error("Inserisci importo saldo iniziale");
			return;
		}
		String importo = importoSaldoIniziale.getText().replace(",", ".");
		if(!checkImporto(importo)) {
			error("Inserisci un valore ammissibile per l'importo del saldo iniziale");
			return;
		}
		float imp = Float.parseFloat(importo);
		if(!controller.setSaldoIniziale(dataSaldoIniziale.getValue(), imp,Circuito.valueOf(circuito()))) {
			error("Errore inserimento saldo iniziale");
			return;
		}
		float saldo = controller.getSaldoAttualeAt(LocalDate.now(),Circuito.valueOf(circuito()));
		saldoAttuale.setText(formatter.format(saldo) +"€");
		verifica(saldoAttuale,saldo);
		saldoInizialeAttuale.setText(circuito() + " " + imp + "€ " + dataSaldoIniziale.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		dataSaldoIniziale.setValue(null);
		importoSaldoIniziale.setText("");
	}

	public void aggiornaAttuale(ActionEvent e) {
		saldoAtDayLabel.setText("");
		float saldo = controller.getSaldoAttualeAt(LocalDate.now(),Circuito.valueOf(circuito()));
		saldoAttuale.setText(formatter.format(saldo) +"€");
		verifica(saldoAttuale,saldo);
		SaldoIniziale si = controller.getSaldoIniziale(Circuito.valueOf(circuito()));
		if(si!=null)
			saldoInizialeAttuale.setText(circuito() + " " + si.getImporto() + "€ " + si.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		else
			saldoInizialeAttuale.setText(circuito() + " 0 €");
	}

	private void verifica(Label saldoLabel, float saldo) {
		if(saldo<0)
			saldoLabel.setStyle("-fx-text-fill: red;");
		else
			saldoLabel.setStyle("-fx-text-fill: #33d72b;");
	}


	private boolean checkImporto(String importo) {
		try {
			Float.parseFloat(importo);
		}catch(Exception e) {
			return false;
		}
		return true;
	}


	public static void error(String msg) {
		Alert alert = new Alert(AlertType.ERROR,msg,ButtonType.OK);
		alert.showAndWait();
	}

	
}
