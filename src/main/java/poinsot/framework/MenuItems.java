package poinsot.framework;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuItems {
	private List<Accion> acciones = new ArrayList<>();

	public MenuItems(String path) {
		cargarAcciones(path);
	}

	private void cargarAcciones(String configFilePath) {
		Configuracion configuracion = new Configuracion(configFilePath);
		List<String> clasesAcciones = configuracion.obtenerClases();
		for (String clase : clasesAcciones) {
			try {
				Class<?> clazz = Class.forName(clase);
				Accion accion = (Accion) clazz.getDeclaredConstructor().newInstance();
				acciones.add(accion);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
					 InvocationTargetException e) {
				throw new RuntimeException("Error al cargar la clase: " + clase, e);
			}
		}
	}

	public void iniciar() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			mostrarMenu();
			int opcion = leerOpcionUsuario(scanner);
			if (opcion == 0) {
				System.out.println("Saliendo...");
				break;
			} else if (opcion > 0 && opcion <= acciones.size()) {
				Accion accion = acciones.get(opcion - 1);
				ejecutarAccion(accion);
			} else {
				System.out.println("Opción no válida, intente nuevamente.");
			}
		}
	}

	private int leerOpcionUsuario(Scanner scanner) {
		System.out.print("Seleccione una opción: ");
		try {
			return Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void mostrarMenu() {
		System.out.println("\nMenú de opciones:");

		for (int i = 0; i < acciones.size(); i++) {
			Accion accion = acciones.get(i);
			System.out.println((i + 1) + ". " + accion.nombreItemMenu() + " - " + accion.descripcionItemMenu());
		}
		System.out.println("0. Salir");
	}

	private void ejecutarAccion(Accion accion) {
		try {
			accion.ejecutar();
			System.out.println("Acción ejecutada exitosamente.");
		} catch (Exception e) {
			System.out.println("Error al ejecutar la acción: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
