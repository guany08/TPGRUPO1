package Carga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cargador {
    private String factFile;
    private String dim1File;
    private String dim2File;
    private String dim3File;

    public Cargador(Configurador config) {
        this.factFile = config.getFact();
        this.dim1File = config.getDim1();
        this.dim2File = config.getDim2();
        this.dim3File = config.getDim3();
    }

    public List<List<Celda>> cargarArchivoCSV(String filePath) throws IOException {
        List<List<Celda>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Celda> valor = new ArrayList<>();
                String[] values = line.split(";");
                for (String v : values){
                    valor.add(new Celda(v, true));
                }   
                data.add(valor);
            }
        }
        return data;
    }

    public List<List<Celda>> cargarFact() throws IOException {
        return cargarArchivoCSV(factFile);
    }

    public List<List<Celda>> cargarDim1() throws IOException {
        return cargarArchivoCSV(dim1File);
    }

    public List<List<Celda>> cargarDim2() throws IOException {
        return cargarArchivoCSV(dim2File);
    }

    public List<List<Celda>> cargarDim3() throws IOException {
        return cargarArchivoCSV(dim3File);
    }

    public String getFactFile() {
        return this.factFile;
    }

    public String getDim1File() {
        return dim1File;
    }

    public String getDim2File() {
        return dim2File;
    }

    public String getDim3File() {
        return dim3File;
    }
}