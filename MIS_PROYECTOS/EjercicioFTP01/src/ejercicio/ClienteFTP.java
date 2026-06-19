package ejercicio;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


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
            
            System.out.println("Directorio actual: "+clienteFTP.printWorkingDirectory());
            
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
                } else {
                    System.out.println("No se pudo crear " + anio);
                }
            } else {
                System.out.println("La carpeta " + anio + " ya existía");
            }
            
            clienteFTP.logout();
            clienteFTP.disconnect();
            System.out.println("Desconectado correctamente");
		} catch(IOException ex) {
            System.out.println("Excepción: " + ex);
        }
	}
	
	public static void mostrarRespuesta(FTPClient clienteFTP) {
        String mensajeRespuesta = convertirUTF8(clienteFTP.getReplyString());
        System.out.println("Respuesta:\n" + mensajeRespuesta);
        int codigoRespuesta = clienteFTP.getReplyCode();
        if (!FTPReply.isPositiveCompletion(codigoRespuesta)) {
            System.out.println("ERROR de Conexión - Código de Error: " + codigoRespuesta);
        }
    }
	
	public static String convertirUTF8(String mensaje) {
        if (mensaje == null) {
            return null;
        } else {
            byte[] apoyo = mensaje.getBytes(ISO_8859_1);
            return (new String(apoyo, UTF_8));
        }
    }
	
}
