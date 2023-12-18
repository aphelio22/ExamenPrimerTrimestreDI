package com.example.examenprimertrimestredi.controllers;

import com.example.examenprimertrimestredi.Sesion;
import com.example.examenprimertrimestredi.models.Cliente;
import com.example.examenprimertrimestredi.models.Coche;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.SplittableRandom;

public class EjercicioExamenController implements Initializable {

    @FXML
    private TextField tvMatricula;
    @FXML
    private ComboBox<String> cbModelo;
    @FXML
    private ChoiceBox<Cliente> chbCliente;
    @FXML
    private RadioButton rbStandard;
    @FXML
    private RadioButton rbOferta;
    @FXML
    private RadioButton rbLargaD;
    @FXML
    private DatePicker dpEntrada;
    @FXML
    private DatePicker dpSalida;
    @FXML
    private Label lbCoste;
    @FXML
    private Button bAnhadirTabla;
    @FXML
    private Button bSalirApp;
    @FXML
    private TableView<Coche> tvCoches;
    @FXML
    private TableColumn<Coche, String> cMatricula;
    @FXML
    private TableColumn<Coche, String> cModelo;
    @FXML
    private TableColumn<Coche, String> cFechaEntrada;
    @FXML
    private TableColumn<Coche, String> cFechaSalida;
    @FXML
    private TableColumn<Coche, String> cCliente;
    @FXML
    private TableColumn<Coche, String> cTarifa;
    @FXML
    private TableColumn<Coche, String> cCoste;

    private ObservableList<Coche> cocheObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cMatricula.setCellValueFactory( (fila) -> {
            String matricula = fila.getValue().getMatricula();
            return new SimpleStringProperty( matricula );
        });

        cModelo.setCellValueFactory( (fila) -> {
            String modelo = fila.getValue().getModelo();
            return new SimpleStringProperty( modelo );
        });

        cFechaEntrada.setCellValueFactory( (fila) -> {
            String fechaEntrada = String.valueOf(fila.getValue().getFechaEntrada());
            return new SimpleStringProperty( fechaEntrada );
        });

        cFechaSalida.setCellValueFactory( (fila) -> {
            String fechaSalida = String.valueOf(fila.getValue().getFechaSalida());
            return new SimpleStringProperty( fechaSalida );
        });

        cCliente.setCellValueFactory( (fila) -> {
            String cliente = String.valueOf(fila.getValue().getCliente());
            return new SimpleStringProperty( cliente );
        });

        cTarifa.setCellValueFactory( (fila) -> {
            String tarifa = fila.getValue().getTarifa();
            return new SimpleStringProperty( tarifa );
        });

        cCoste.setCellValueFactory( (fila) -> {
            String coste = String.valueOf(fila.getValue().getCoste());
            return new SimpleStringProperty( coste );
        });

        if (Sesion.getCoches().isEmpty()){
            ArrayList<Coche> coches = new ArrayList<>();

            LocalDate fechaEntrada1 = LocalDate.of(2023, 12, 19);
            LocalDate fechaSalida1 = LocalDate.of(2023, 12, 21);

            Cliente cliente = new Cliente();
            cliente.setId(1);
            cliente.setNombre("Jorge");
            cliente.setCorreo("jorge@gmail.com");


            coches.add(new Coche("LJV-102", "Clase A",fechaEntrada1 , fechaSalida1, cliente, "Estándar", 16));
            Sesion.setCoches(coches);
        }

        cocheObservableList.addAll( Sesion.getCoches() );

        tvCoches.setItems(cocheObservableList);

        ObservableList<String> modelos = FXCollections.observableArrayList();
        modelos.addAll("Clase C", "Clase E", "Clase A", "Kamaro", "Hyundai-200");

        cbModelo.setItems(modelos);
        cbModelo.getSelectionModel().selectFirst();

        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        clientes.addAll(new Cliente(1, "Jorge", "jorge@gmail.com"),
                new Cliente(2, "Paula", "paula@gmail.com"),
                new Cliente(3, "Maria", "maria@gmail.com"));


        chbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                if(cliente!=null) return cliente.getNombre();
                else return null;
            }

            @Override
            public Cliente fromString(String s) {
                return null;
            }
        });
        chbCliente.setItems(clientes);
        chbCliente.getSelectionModel().selectFirst();


    }

    @FXML
    public void AnahdirTabla(ActionEvent actionEvent) {
        if (!tvMatricula.getText().isEmpty() && cbModelo.getValue() != null && chbCliente.getValue() != null
                && dpEntrada.getValue() != null && dpSalida.getValue() != null && (rbStandard.isSelected() || rbOferta.isSelected() || rbLargaD.isSelected())) {

            LocalDate fechaEntrada = dpEntrada.getValue();
            LocalDate fechaSalida = dpSalida.getValue();

            Period periodo = Period.between(fechaEntrada, fechaSalida);

            Integer dias = periodo.getDays();


            double precioDiario = 0;
            String tarifaSeleccionada = "";

            if (rbStandard.isSelected()) {
                precioDiario = 8;
                tarifaSeleccionada = "Standard";
            } else if (rbOferta.isSelected()) {
                precioDiario = 6;
                tarifaSeleccionada = "Oferta";
            } else if (rbLargaD.isSelected()) {
                precioDiario = 2;
                tarifaSeleccionada = "Larga Duración";
            }

            double coste = dias * precioDiario;

            Coche coche = new Coche();
            coche.setMatricula(tvMatricula.getText());
            coche.setModelo(cbModelo.getValue());
            coche.setFechaEntrada(fechaEntrada);
            coche.setFechaSalida(fechaSalida);
            coche.setCliente(chbCliente.getValue());
            coche.setTarifa(tarifaSeleccionada);
            coche.setCoste((int) coste);

            Sesion.getCoches().add(coche);
            cocheObservableList.add(coche);

            lbCoste.setText(coste +"€");

            limpiarCampos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error.");
            alert.setContentText("No se han rellenado todos los campos o seleccionado una tarifa");
            alert.showAndWait();
        }
    }

    @FXML
    public void SalirAplicacion(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Deprecated
    public void mostrarAcercaDe(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerda de");
        alert.setHeaderText("Jorge Alarcón Navarro.");
        alert.setContentText("Creado por Jorge Alarcón Navarro 2ºDAM");
        alert.showAndWait();
    }

    private void limpiarCampos() {
        tvMatricula.clear();
        cbModelo.setValue(null);
        chbCliente.setValue(null);
        dpEntrada.setValue(null);
        dpSalida.setValue(null);
        rbStandard.setSelected(false);
        rbOferta.setSelected(false);
        rbLargaD.setSelected(false);
    }
}