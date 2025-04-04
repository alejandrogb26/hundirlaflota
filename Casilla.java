package arrays.arraylist.ejercicios.hundirlaflota;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Casilla implements Serializable {
	private static final long serialVersionUID = -4158918403497996765L;
	private String fila;
	private int columna;

	public Casilla(String fila, int columna) {
		Pattern p = Pattern.compile("^[A-Ja-j]$");
		if (!p.matcher(fila).matches())
			throw new IllegalArgumentException("La fila debe estar en A|a y J|j.");
		this.fila = fila.toUpperCase();

		if (columna < 1 || columna > 10)
			throw new IllegalArgumentException("La columna debe estar entre 1 y 10.");
		this.columna = columna;
	}

	public String getFila() {
		return fila;
	}

	public int convertFilaInt() {
		int convFila = 0;
		switch (fila) {
		case "A" -> convFila = 1;
		case "B" -> convFila = 2;
		case "C" -> convFila = 3;
		case "D" -> convFila = 4;
		case "E" -> convFila = 5;
		case "F" -> convFila = 6;
		case "G" -> convFila = 7;
		case "H" -> convFila = 8;
		case "I" -> convFila = 9;
		case "J" -> convFila = 10;
		}

		return convFila;
	}

	public void setFila(String fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	@Override
	public String toString() {
		return fila + " - " + columna;
	}
}
