package srconsola201b;

import java.io.BufferedReader;
import java.io.IOException;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

public class SRConsola201BClientePOP3 {

    public static void main(String[] args) {
        String server = "localhost", username = "usuario1", password = "usu1";
        int puerto = 110;
        POP3SClient pop3 = new POP3SClient();
        try {
            pop3.connect(server, puerto);
            System.out.println("Conexion realizada al servidor POP3 " + server);
            // nos logueamos
            if (!pop3.login(username, password)) {
                System.err.println("Error al hacer login");
            } else {
                POP3MessageInfo[] men = pop3.listMessages();
                if (men == null) {
                    System.out.println("Imposible recuperar mensajes.");
                } else {
                    System.out.println("Nº de mensajes: " + men.length);
                    if (men.length > 0) {
                        Recuperarmesajes(men, pop3);
                    }
                }
                // finalizar sesion
                pop3.logout();
            }
            // nos desconectamos
            pop3.disconnect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

    private static void Recuperarmesajes(POP3MessageInfo[] men, POP3SClient pop3)
            throws IOException {
        for (int i = 0; i < men.length; i++) {//
            // POP3MessageInfo msginfo : messages) {
            System.out.println("Mensaje: " + (i + 1));
            POP3MessageInfo msginfo = men[i]; // lista de mensajes
            System.out.println("IDentificador: " + msginfo.identifier
                    + ", Number: " + msginfo.number + ", Tamaño: "
                    + msginfo.size);
            System.out.println("Prueba de listUniqueIdentifier: ");
            POP3MessageInfo pmi = pop3.listUniqueIdentifier(i + 1);// un mensaje
            System.out.println("\tIdentificador: " + pmi.identifier
                    + ", Number: " + pmi.number + ", Tamaño: " + pmi.size);
            // solo recupera cabecera
            System.out.println("Cabecera del mensaje:");
            BufferedReader reader = (BufferedReader) pop3.retrieveMessageTop(msginfo.number, 0);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line.toString());
            }
            reader.close();
            //recupera todo
            BufferedReader reader2 = (BufferedReader) pop3.retrieveMessage(msginfo.number);
            String linea;
            while ((linea = reader2.readLine()) != null) {
                System.out.println(linea.toString());
            }
            reader2.close();
        }
    }
}
