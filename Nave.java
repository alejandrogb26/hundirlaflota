package arrays.arraylist.ejercicios.hundirlaflota;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class Nave implements Serializable {
	private static final long serialVersionUID = 154901682354453104L;
	public static final int PORTAAVIONES = 0;
	public static final int ACORAZADO = 1;
	public static final int SUBMARINO = 2;
	public static final int DESTRUCTOR = 3;
	public static final int FRAGATA = 4;

	private int tipo;
	private ArrayList<Casilla> casillas;
	private TreeMap<Integer, Casilla> casillasTocadas;

	public Nave(int tipo, ArrayList<Casilla> casillas) {
		if (tipo < 0 || tipo > 4)
			throw new IllegalArgumentException("El tipo de nave debe estar en 0 y 4.");
		this.tipo = tipo;

		//@formatter:off
		switch (tipo) {
			case PORTAAVIONES -> {if (casillas.size() != 5) throw new IllegalArgumentException("El número de casillas para un Portaaviones es de 5.");}
			case ACORAZADO -> {if (casillas.size() != 4) throw new IllegalArgumentException("El número de casillas para un Acorazado es de 4.");}
			case SUBMARINO, DESTRUCTOR -> {if (casillas.size() != 3)throw new IllegalArgumentException("El número de casillas para un Submarino/Destructor es de 3.");}
			case FRAGATA -> {if (casillas.size() != 2) throw new IllegalArgumentException("El número de casillas para un Fragata es de 2.");}
		}
		//@formatter:on

		this.casillas = casillas;
		casillasTocadas = new TreeMap<Integer, Casilla>();
	}

	public int getTipo() {
		return tipo;
	}

	public ArrayList<Casilla> getCasillas() {
		return casillas;
	}

	public boolean comprobarDisparo(Casilla casilla) {
		for (int i = 0; i < casillas.size(); i++)
			if (casillas.get(i).convertFilaInt() == casilla.convertFilaInt()
					&& casillas.get(i).getColumna() == casilla.getColumna()) {
				if (!casillasTocadas.containsKey(i))
					casillasTocadas.put(i, casilla);
				return true;

			}
		return false;
	}

	public boolean comprobarCasilla(int index) {
		if (casillasTocadas.containsKey(index))
			return true;
		return false;
	}

	public boolean estáHundido() {
		for (int i = 0; i < casillas.size(); i++)
			if (!casillasTocadas.containsKey(i))
				return false;
		return true;
	}
}
