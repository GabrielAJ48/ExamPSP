package ejercicio;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class ClienteFTP {
		
	public static void main(String[] args) {
		
		try {
            System.out.println("Estableciendo conexión con el Servidor FTP");
            FTPClient clienteFTP = new FTPClient();
            String servidorFTP = "127.0.0.1";
            clienteFTP.connect(servidorFTP);
            System.out.println(clienteFTP.getReplyString());
            
            String usuario = "usuario1";
            String contraseña = "usu1";
            boolean login = clienteFTP.login(usuario, contraseña);
            if (login) {
            	System.out.println("Login correcto");
            } else {
            	System.out.println("Login incorrecto");
            	clienteFTP.disconnect();
                return;
            }
            
            clienteFTP.enterLocalPassiveMode();
            
            boolean existeEjercicios = clienteFTP.changeWorkingDirectory("ejercicios");
            
            if(!existeEjercicios) {
            	System.out.println("La carpeta de ejercicios no existe");
            	clienteFTP.makeDirectory("ejercicios");
            	System.out.println("Carpeta ejercicios creada");
            	clienteFTP.changeWorkingDirectory("ejercicios");
            	System.out.println("Entrando en /ejercicios");
            } else {
            	System.out.println("Entrando en /ejercicios");
            }
            
            String anio = String.valueOf(LocalDate.now().getYear());

            boolean existeAnio =
                clienteFTP.changeWorkingDirectory(anio);

            if (!existeAnio) {
                boolean creada = clienteFTP.makeDirectory(anio);

                if (creada) {
                    System.out.println("Carpeta " + anio + " creada");
                    clienteFTP.changeWorkingDirectory(anio);
                } else {
                    System.out.println("No se pudo crear " + anio);
                }
            } else {
                System.out.println("La carpeta " + anio + " ya existía");
            }
            
            clienteFTP.setFileType(FTP.ASCII_FILE_TYPE);
            FileInputStream fis = new FileInputStream("maniobra\\datos.txt");
            BufferedInputStream bis = new BufferedInputStream(fis);
            boolean subida = clienteFTP.storeFile("registro.txt", bis);
            
            if (subida) {
                System.out.println("Fichero subido correctamente");
            } else {
                System.out.println("No se pudo subir el fichero");
            }
            
            String nuevaLinea = System.lineSeparator()+"Segunda linea añadida desde Java";
            
            byte[] datos = nuevaLinea.getBytes(StandardCharsets.UTF_8);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(datos);
            
            boolean anadido = clienteFTP.appendFile("registro.txt", bais);
            
            if (anadido) {
                System.out.println("Texto añadido correctamente");
            } else {
                System.out.println("No se pudo añadir el texto");
            }
            
            FileOutputStream fos = new FileOutputStream("maniobra\\copia_registro.txt");
            boolean descargado = clienteFTP.retrieveFile("registro.txt", fos);
            
            if (descargado) {
            	System.out.println("Fichero descargado correctamente");
            } else {
            	System.out.println("No se ha podido descargar el archivo");
            }
            
            fis.close();
            bis.close();
            bais.close();
            fos.close();
            clienteFTP.logout();
            clienteFTP.disconnect();
            
            System.out.println("Desconecto del servidor FTP");
            
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
