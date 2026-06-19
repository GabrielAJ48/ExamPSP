package ejercicio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class ServidorTCP {
	
	public static void main(String[] args) {
		
		Producto teclado = new Producto(1, "Teclado", 25.50f);
		Producto raton = new Producto(2, "Ratón", 15.00f);
		Producto monitor = new Producto(3, "Monitor", 180.00f);
		
		TreeMap<Integer, Producto> bd= new TreeMap<>();
		
		bd.put(teclado.getId(), teclado);
		bd.put(raton.getId(), raton);
		bd.put(monitor.getId(), monitor);
		
		try {
			System.out.println("Inicio el servidor en puerto 30500");
			ServerSocket socketServidor = new ServerSocket(30500);
			Socket cliente;
			int numeroCliente = 0;
			SesionServidor sesion;
			ArrayList<SesionServidor> sesiones = new ArrayList<>();
			
			do {
				cliente = socketServidor.accept();
				numeroCliente++;
				sesion = new SesionServidor(numeroCliente, cliente, bd);
				sesiones.add(sesion);
				sesion.start();
			} while (true);
			
		} catch (IOException ex) {
	        System.out.println(ex.getMessage());
	    }
		
	}
	
}
