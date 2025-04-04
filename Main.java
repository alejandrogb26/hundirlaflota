package arrays.arraylist.ejercicios.hundirlaflota;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";
	public static final String BLUE = "\u001B[34m";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// Crear el juego "Hundir la flota"
		System.out.printf("%s%n%s%n%s%n%s%n%n", "Hundir la flota", "-".repeat(15), "1. Nueva partida",
				"2. Continuar");

		int opción = 0;
		while (opción < 1 || opción > 2) {
			System.out.print("Seleccione una opción (1 o 2): ");
			if (scanner.hasNextInt()) {
				opción = scanner.nextInt();
				scanner.nextLine(); // Limpiar el retorno de carro.
			} else
				scanner.nextLine(); // Limpiar retorno de carro de entrada inválida.
		}

		Juego juego = null;
		if (opción == 1) {
			ArrayList<Nave> flotaJ1 = new ArrayList<Nave>();
			ArrayList<Nave> flotaJ2 = new ArrayList<Nave>();

			// Crear las flotas de los jugadores.
			for (int i = 0; i < 2; i++) {
				ArrayList<Nave> flota;

				// Comprobar para quien es la fragata.
				String jugador;
				if (i == 0) {
					jugador = RED + "Jugador 1" + RESET;
					flota = flotaJ1;
				} else {
					jugador = BLUE + "Jugador 2" + RESET;
					flota = flotaJ2;
				}

				// Preguntar la posición de cada nave al jugador.
				int nmCasillas = 0;
				int tipo = 0;

				for (int n = 0; n < 5; n++) {
					clearConsole();
					System.out.println("Flota del " + jugador);
					System.out.println("-------------------");
					System.out.println("Debes colocar las naves en el tablero:");
					System.out.println();

					mostrarTablero(flota); // Mostrar el tablero del jugador.

					//@formatter:off
					switch (n) {
					case 0 -> {System.out.println("Portaaviones (5)"); nmCasillas = 5; tipo = Nave.PORTAAVIONES;}
					case 1 -> {System.out.println("Acorazado (4)"); nmCasillas = 4; tipo = Nave.ACORAZADO;}
					case 2 -> {System.out.println("Submarino (3)"); nmCasillas = 3; tipo = Nave.SUBMARINO;}
					case 3 -> {System.out.println("Destructor (3)"); nmCasillas = 3; tipo = Nave.DESTRUCTOR;}
					case 4 -> {System.out.println("Fragata (2)"); nmCasillas = 2; tipo = Nave.FRAGATA;}
					}
					//@formatter:on

					Pattern pattern = Pattern.compile("^([A-Ja-j])(\\d{1,2}):([A-Ja-j])(\\d{1,2})$");
					boolean esVálida = false;
					while (!esVálida) {
						// Comprobar que la posición tenga un formato válido.
						System.out.print("Posición (ej: C1:G1): ");
						String posición = scanner.nextLine();
						Matcher matcher = pattern.matcher(posición.trim());
						if (!matcher.matches())
							continue;

						// Comprobar si la posición es posible.
						int f1 = matcher.group(1).toUpperCase().charAt(0);
						int c1 = Integer.parseInt(matcher.group(2));

						int f2 = matcher.group(3).toUpperCase().charAt(0);
						int c2 = Integer.parseInt(matcher.group(4));

						// Crear las casillas de la nave, y añadir la nave a la fragata.
						ArrayList<Casilla> casillas = new ArrayList<Casilla>();

						if (f1 == f2 && c1 < c2 && nmCasillas == (c2 - c1) + 1) {
							for (int cActual = c1; cActual <= c2; cActual++)
								casillas.add(new Casilla(String.valueOf((char) f1), cActual));
							flota.add(new Nave(tipo, casillas));
							esVálida = true;

						} else if (c1 == c2 && f1 < f2 && nmCasillas == (f2 - f1) + 1) {
							for (int fActual = f1; fActual <= f2; fActual++)
								casillas.add(new Casilla(String.valueOf((char) fActual), c1));
							flota.add(new Nave(tipo, casillas));
							esVálida = true;
						}
					}
				}
			}
			juego = new Juego(flotaJ1, flotaJ2);
		} else {
			System.out.print("Nombre del fichero: ");
			String nombreFichero = scanner.nextLine().trim();

			try (ObjectInputStream in = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(nombreFichero)))) {
				juego = (Juego) in.readObject();
			} catch (FileNotFoundException e) {
				generarInformeError("El fichero no fue encontrado. Por favor, verifica el nombre del archivo.", e);
				System.exit(1); // Código 1: Error de archivo no encontrado.
			} catch (IOException e) {
				generarInformeError("Error al leer el fichero. Es posible que esté corrupto o inaccesible.", e);
				System.exit(2); // Código 2: Error de R/W.
			} catch (ClassNotFoundException e) {
				generarInformeError("El contenido del fichero no es válido. Se esperaba un objeto del tipo 'Juego'.",
						e);
				System.exit(3); // Código 3: Error de clase no encontrada.
			}
		}

		while (true) {
			clearConsole(); // Limpiar la pantalla.

			// 1. Obtener el turno actual.
			String nombreJugador;
			int turnoActual = juego.next();

			if (turnoActual % 2 == 0)
				nombreJugador = BLUE + "Jugador 2" + RESET;
			else
				nombreJugador = RED + "Jugador 1" + RESET;

			System.out.println("Turno " + turnoActual + " (" + nombreJugador + ")");

			// 2. Obtener el jugador actual y el adversario.
			Jugador jgActual = juego.jugadorActual();
			Jugador jgAdversario = juego.jugadorAdversario();

			// 3. Mostrar el tablero del jugador actual.
			jgActual.mostrarEstado();

			// 4. Pedir una casilla al usuario.
			Casilla casilla = null;
			Pattern patternCasilla = Pattern.compile("^([A-Ja-j])(\\d{1,2})$");
			Pattern patternSave = Pattern.compile("^[Gg]$");
			boolean esVálida = false;
			while (!esVálida) {
				// Comprobar que la casilla tenga un formato válido.
				System.out.print("Casilla / Guardar (ej: C2 ; G|g): ");
				String casillaLeída = scanner.nextLine();
				Matcher matcherCasilla = patternCasilla.matcher(casillaLeída);
				Matcher matcherSave = patternSave.matcher(casillaLeída);

				if (matcherSave.matches()) {
					juego.guardarPartida();
					System.exit(0);
				} else if (!matcherCasilla.matches())
					continue;

				// Comprobar si el disparo ya fue realizado.
				casilla = new Casilla(matcherCasilla.group(1).toUpperCase(), Integer.parseInt(matcherCasilla.group(2)));

				if (jgActual.disparoYaRealizado(casilla))
					System.out.println("El disparo ya fue realizado.");
				else
					esVálida = true;
			}

			// 5. Anota si el disparo fue acertado o no.
			jgActual.anotarDisparo(casilla, jgAdversario.comprobarDisparo(casilla));

			// 6. Comprobar si el jugador actual ha ganado.
			if (jgAdversario.flotaHundida()) {
				System.out.println("El " + nombreJugador + " ha ganado!!");
				break;
			}
		}
		scanner.close();
	}

	public static void mostrarTablero(ArrayList<Nave> flota) {
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

		// Imprimir el borde superior
		System.out.print("+");
		for (int c = 0; c < tableroBase[0].length; c++)
			System.out.print("---+");

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
							contenido = "[]"; // Representación de la nave
							hayNave = true;
							break;
						}
					}
					if (hayNave)
						break;
				}

				// Alinear el contenido en el centro de la celda
				System.out.format(" %-2s|", contenido);
			}
			System.out.println();

			// Imprimir el borde inferior de la fila
			System.out.print("+");
			for (int c = 0; c < tableroBase[0].length; c++)
				System.out.print("---+");

			System.out.println();
		}
	}

	public static void clearConsole() {
		try {
			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				// Para Windows
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				// Para Linux/Unix/Mac
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			}
		} catch (IOException | InterruptedException ex) {
		}
		System.out.println();
	}

	private static void generarInformeError(String mensaje, Exception e) {
		// Generar un informe detallado en la consola
		System.err.println("==== Informe de Error ====");
		System.err.println("Mensaje: " + mensaje);
		System.err.println("Causa: " + e.getMessage());
		System.err.println("Traza:");
		e.printStackTrace(System.err);
	}
}
