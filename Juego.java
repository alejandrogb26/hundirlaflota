package arrays.arraylist.ejercicios.hundirlaflota;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Juego implements Serializable {
	private static final long serialVersionUID = 6569033908962768655L;
	private Jugador jugador1;
	private Jugador jugador2;
	private int turno;

	public Juego(ArrayList<Nave> flotaJ1, ArrayList<Nave> flotaJ2) {
		jugador1 = new Jugador(flotaJ1);
		jugador2 = new Jugador(flotaJ2);
		turno = 0;
	}

	public int next() {
		return ++turno;
	}

	public Jugador getJugador1() {
		return jugador1;
	}

	public Jugador getJugador2() {
		return jugador2;
	}

	public Jugador jugadorActual() {
		if (turno % 2 == 0)
			return jugador2;
		else
			return jugador1;
	}

	public Jugador jugadorAdversario() {
		if (turno % 2 == 0)
			return jugador1;
		else
			return jugador2;
	}

	public void mostrarTablero() {
		if (turno % 2 == 0)
			jugador2.mostrarEstado();
		else
			jugador1.mostrarEstado();
	}

	public void guardarPartida() {
		LocalDateTime ldt = LocalDateTime.now();
		String nombreFichero = "hundirFlotaBK_" + ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm")) + ".bk";

		try (ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(nombreFichero)))) {
			out.writeObject(this);
			System.out.println("El fichero " + nombreFichero + "se ha guardado correctamente.");
		} catch (FileNotFoundException e) {
			System.err.println("Error al intentar crear el fichero.");
		} catch (IOException e) {
			System.err.println("Error al intentar escribir en el fichero.");
		}
	}
}
