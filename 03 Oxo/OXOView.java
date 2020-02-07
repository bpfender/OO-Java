import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.scene.text.*;
import javafx.geometry.*;

public class OXOView extends Application implements EventHandler<ActionEvent>
{
    private GraphicsContext g;
    private int width = 250;
    private int height = 250;
    private float margin = 40;
    private float fontSize = 16;
    private TextField commandInputBox;
    private Label messageLabel;
    private OXOController controller;

    public static void main(String args[])
    {
        Application.launch(args);
    }

    public void start(Stage stage)
    {
        stage.setTitle("OXO Board");
        Canvas canvas = new Canvas(width, height);
        g = canvas.getGraphicsContext2D();
        g.setFont(new Font("Arial", fontSize));
        commandInputBox = new TextField("");
        messageLabel = new Label("");
        messageLabel.setFont(new Font("Arial", 14));
        messageLabel.setPadding(new Insets(5));
        commandInputBox.setOnAction(this);
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(messageLabel);
        contentPane.setBottom(commandInputBox);
        contentPane.setCenter(canvas);
        Scene scene = new Scene(contentPane);
        stage.setScene(scene);
        stage.show();
        OXOModel gameModel = new OXOModel(3,3,3,this);
        gameModel.addPlayer(new OXOPlayer('X'));
        gameModel.addPlayer(new OXOPlayer('O'));
        controller = new OXOController(gameModel);
        drawBoard(gameModel);
    }

    public void drawBoard(OXOModel boardState)
    {
        // Clear the whole window
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, width, height);

        float horiSpacing = (width-margin*2)/boardState.getNumberOfColumns();
        float vertSpacing = (height-margin*2)/boardState.getNumberOfRows();

        // Draw horizontal lines
        g.setFill(Color.BLACK);
        for(int i=0; i<boardState.getNumberOfRows()-1 ;i++) {
            g.strokeLine(margin, margin+vertSpacing*(i+1), width - margin, margin+vertSpacing*(i+1));
        }
        // Draw vertical lines
        for(int i=0; i<boardState.getNumberOfColumns()-1 ;i++) {
            g.strokeLine(margin+horiSpacing*(i+1), margin, margin+horiSpacing*(i+1), height-margin);
        }

        // Draw the row lables
        g.setFill(Color.LIGHTGREY);
        for(int i=0; i<boardState.getNumberOfRows() ;i++) {
            g.fillText(""+(char)('a'+i),(int)margin/2, (int)(margin-2+(fontSize/2.0f)+vertSpacing*(i+0.5)));
        }

        // Draw the column lables
        g.setFill(Color.LIGHTGREY);
        for(int i=0; i<boardState.getNumberOfColumns() ;i++) {
            g.fillText(""+(char)('1'+i),(int)(margin+2-(fontSize/2.0f)+horiSpacing*(i+0.5)), (int)margin/2);
        }

        // Draw the board state
        g.setFill(Color.BLACK);
        for(int colNumber=0; colNumber<boardState.getNumberOfColumns() ;colNumber++) {
            for(int rowNumber=0; rowNumber<boardState.getNumberOfRows() ;rowNumber++) {
                int xpos = (int)(((float)margin) + 2 - (fontSize/2) + (horiSpacing * (colNumber+0.5f)));
                int ypos = (int)(((float)margin) + (fontSize/2) + (vertSpacing * (rowNumber+0.5f)));
                OXOPlayer cellOwner = boardState.getCellOwner(rowNumber,colNumber);
                if(cellOwner != null) g.fillText(""+cellOwner.getPlayingLetter(), xpos, ypos);
            }
        }
        if(boardState.getWinner() != null) messageLabel.setText("Player " + boardState.getWinner().getPlayingLetter() + " is the winner !");
        else if(boardState.isGameDrawn()) messageLabel.setText("Stalemate - game is a draw !");
        else if (boardState.getCurrentPlayer() != null) messageLabel.setText("Player " + boardState.getCurrentPlayer().getPlayingLetter() + "'s turn");
    }

    public void handle(ActionEvent event)
    {
        try {
            String command = commandInputBox.getText();
            commandInputBox.setText("");
            controller.handleIncomingCommand(command);
        } catch(InvalidCellIdentifierException icie) {
            System.out.println(icie);
        } catch(CellAlreadyTakenException cnae) {
            System.out.println(cnae);
        } catch(CellDoesNotExistException cdnee) {
            System.out.println(cdnee);
        }
    }

}
