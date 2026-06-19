package ejercicio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

public class SesionServidor extends Thread{

	private int numeroCliente;
	private Socket cliente;
	private TreeMap<Integer, Producto> bd;
	
	public SesionServidor(int numeroCliente, Socket cliente, TreeMap<Integer, Producto> bd) {
		this.numeroCliente = numeroCliente;
		this.cliente = cliente;
		this.bd = bd;
	}

	@Override
	public void run() {
		
		try {
			System.out.println("Se ha conectado el cliente "+numeroCliente+" con ip: "+cliente.getInetAddress().getHostAddress());
			boolean fin = false;
			
			DataInputStream dis = new DataInputStream(cliente.getInputStream());
			DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
			ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
			
			dos.writeInt(numeroCliente);
			
			do {			
				int idSolicitado = dis.readInt();
				
				if (idSolicitado == 0) {
					fin = true;
				} else {
					Producto solicitado = bd.get(idSolicitado);
					
					if (solicitado == null) {
						System.out.println("No existe el producto con id "+idSolicitado);
						solicitado = new Producto(-1, "No existe el producto", -1f);
					} else {
						System.out.println("Se envía al cliente el producto con id "+idSolicitado);
					}
					
					oos.writeObject((Producto) solicitado);
					oos.flush();
				}
				
			} while (!fin);
			
			System.out.println("Sesión del cliente "+numeroCliente+" se cierra");
			dis.close();
			oos.close();
			cliente.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
