import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

class StagServer {
    private Controller controller;

    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Usage: java StagServer <entity-file> <action-file>");
            System.exit(-1);
        } else {
            new StagServer(args[0], args[1], 8888);
        }
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber) {
        Builder builder = new Builder(entityFilename, actionFilename);
        Game game = builder.buildGame();

        controller = new Controller(game);

        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while (true)
                acceptNextConnection(ss);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss) {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        String line = in.readLine();
        out.write(controller.processLine(line));
    }
}
