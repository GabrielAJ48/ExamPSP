package ejercicio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {

	public static void main(String[] args) {
		
		try {
			System.out.println("Cliente - Me conecto al servidor");
			InetAddress ipServidor = InetAddress.getLocalHost();
			Socket socketCliente = new Socket(ipServidor, 30500);
			
			DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
			DataInputStream dis = new DataInputStream(socketCliente.getInputStream());
			ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
			
			boolean fin = false;
			Scanner scanner = new Scanner(System.in);
			int idSolicitado;
			Producto recibido = null;
			
			int miNumeroCliente = dis.readInt();
			System.out.println("Soy el cliente "+miNumeroCliente);
			
			do {			
				System.out.println("Introduzca el número id del producto que quiere revisar: ");
				idSolicitado = scanner.nextInt();
				
				dos.writeInt(idSolicitado);
				dos.flush();
				
				if (idSolicitado == 0) {
					fin = true;
				} else {
					recibido = (Producto) ois.readObject();
					
					if (recibido.getId() == -1) {
						System.out.println("No existía el producto que he solicitado");
					} else {
						System.out.println("El producto que he pedido es: ");
						System.out.println(recibido.toString());
					}
				}
							
			} while(!fin);
			
			scanner.close();
			ois.close();
			dos.close();
			socketCliente.close();
			
			System.out.println("Cliente se cierra");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
