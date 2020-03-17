import java.io.*;
import java.net.*;

class StagServer {
    static Game game;

    public static void main(String args[]) {
        if (args.length != 2)
            System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else {
            new StagServer(args[0], args[1], 8888);
        }
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber) {
        Builder builder = new Builder(entityFilename, actionFilename);
        game = builder.buildWorld();

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

        Controller controller = new Controller(game, line);
        out.write(controller.processLine());
    }
}
