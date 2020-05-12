import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Database.DatabaseHandler;
import Expression.Context;
import Expression.Expression;
import Parser.Parser;

public class DBServer {
    Parser parser = new Parser();
    Context context = new Context();
    Expression expression;

    public static void main(String[] args) {
        new DBServer(8888);
    }

    public DBServer(int portNumber) {
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Database server listening.");

            Socket socket = ss.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                processNextCommand(in, out);
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        String line = in.readLine();
        String output;
        try {
            expression = parser.parseQuery(line);
            output = expression.interpret(context);
        } catch (Exception e) {
            output = e.getMessage();
        }

        out.write(output + "\n" + DBClient.EOT + "\n");
        out.flush();

        // TODO would like some more intelligence with this.

        DatabaseHandler.getInstance().writeChangesToFile();
    }

}
