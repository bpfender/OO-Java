import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Expression.Context;
import Expression.Expression;
import Parser.Parser;

public class DBServer {
    Parser parser = new Parser();
    Context context = new Context();
    Expression expression;

    public static void main() {
        new DBServer(8888);
    }

    public DBServer(int portNumber) {
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Database server listening.");
            while (true) {
                acceptNextConnection(ss);
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void acceptNextConnection(ServerSocket ss) {
        try {
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        String line = in.readLine();
        expression = parser.parseQuery(line);

        try {
            out.write(expression.interpret(context));
        } catch (Exception e) {
            out.write(e.getMessage());
        }

    }

}
