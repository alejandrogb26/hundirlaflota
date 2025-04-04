package arrays.arraylist.ejercicios.hundirlaflota;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements Serializable {
	private static final long serialVersionUID = 1059089259149949257L;
	private ArrayList<Nave> flota;
	private ArrayList<Casilla> disparosAcertados;
	private ArrayList<Casilla> disparosFallados;

	public Jugador(ArrayList<Nave> flota) {
		this.flota = flota;
		disparosAcertados = new ArrayList<Casilla>();
		disparosFallados = new ArrayList<Casilla>();
	}

	public void añadirNave(Nave nave) {
		if (flota.size() < 5)
			flota.add(nave);
	}

	public boolean comprobarDisparo(Casilla casilla) {
		for (Nave n : flota)
			if (n.comprobarDisparo(casilla))
				return true;
		return false;
	}

	public boolean disparoYaRealizado(Casilla casilla) {
		for (Casilla c : disparosAcertados)
			if (c.convertFilaInt() == casilla.convertFilaInt() && c.getColumna() == casilla.getColumna())
				return true;
		for (Casilla c : disparosFallados)
			if (c.convertFilaInt() == casilla.convertFilaInt() && c.getColumna() == casilla.getColumna())
				return true;
		return false;
	}

	public void anotarDisparo(Casilla casilla, boolean acertado) {
		if (acertado)
			disparosAcertados.add(casilla);
		else
			disparosFallados.add(casilla);
	}

	public void mostrarEstado() {
		String[][] tableroBase = { { "  ", " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 " },
				{ "A ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "B ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "C ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "D ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "E ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "F ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "G ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "H ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "I ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " },
				{ "J ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " }, };

		System.out.println();

		System.out.println("[] -> Casilla");
		System.out.println("X -> Casilla tocada");

		System.out.println();

		// Imprimir el borde superior
		System.out.print("+");
		for (int c = 0; c < tableroBase[0].length; c++) {
			System.out.print("---+");
		}
		System.out.println();

		// Imprimir el contenido del tablero
		for (int f = 0; f < tableroBase.length; f++) {
			System.out.print("|");
			for (int c = 0; c < tableroBase[0].length; c++) {
				// Mostrar el contenido de la celda
				String contenido = tableroBase[f][c].trim(); // Eliminar espacios innecesarios
				boolean hayNave = false;

				// Verificar si hay una nave en esta casilla
				for (Nave nave : flota) {
					for (int i = 0; i < nave.getCasillas().size(); i++) {
						if (nave.getCasillas().get(i).convertFilaInt() == f
								&& nave.getCasillas().get(i).getColumna() == c) {
							if (nave.comprobarCasilla(i))
								contenido = "X";
							else
								contenido = "[]";
							hayNave = true;
							break;
						}
					}
					if (hayNave)
						break;
				}

				// Alinear el contenido en el centro de la celda
				System.out.print(String.format(" %-2s|", contenido));
			}
			System.out.println();

			// Imprimir el borde inferior de la fila
			System.out.print("+");
			for (int c = 0; c < tableroBase[0].length; c++) {
				System.out.print("---+");
			}
			System.out.println();
		}

		System.out.println();
		System.out.println();
		System.out.println("-".repeat(45));
		System.out.println();

		System.out.println("X -> Disparo acertado");
		System.out.println("O -> Disparo fallido");

		System.out.println();

		// Imprimir el borde superior
		System.out.print("+");
		for (int c = 0; c < tableroBase[0].length; c++) {
			System.out.print("---+");
		}
		System.out.println();

		// Imprimir el contenido del tablero
		for (int f = 0; f < tableroBase.length; f++) {
			System.out.print("|");
			for (int c = 0; c < tableroBase[0].length; c++) {
				// Mostrar el contenido de la celda
				String contenido = tableroBase[f][c].trim(); // Eliminar espacios innecesarios

				// Verificar si hay un disparo acertado en esta casilla
				boolean disparoAcertado = false;
				for (int i = 0; i < disparosAcertados.size(); i++) {
					if (disparosAcertados.get(i).convertFilaInt() == f && disparosAcertados.get(i).getColumna() == c) {
						contenido = "X"; // Representación de disparo acertado
						disparoAcertado = true;
						break;
					}
				}

				// Verificar si hay un disparo fallado en esta casilla (solo si no hay disparo
				// acertado)
				if (!disparoAcertado) {
					for (int i = 0; i < disparosFallados.size(); i++) {
						if (disparosFallados.get(i).convertFilaInt() == f
								&& disparosFallados.get(i).getColumna() == c) {
							contenido = "O"; // Representación de disparo fallado
							break;
						}
					}
				}

				// Alinear el contenido en el centro de la celda
				System.out.print(String.format(" %-2s|", contenido));
			}
			System.out.println();

			// Imprimir el borde inferior de la fila
			System.out.print("+");
			for (int c = 0; c < tableroBase[0].length; c++) {
				System.out.print("---+");
			}
			System.out.println();
		}

		System.out.println();
	}

	public boolean flotaHundida() {
		for (Nave nave : flota)
			if (!nave.estáHundido())
				return false;
		return true;
	}
}