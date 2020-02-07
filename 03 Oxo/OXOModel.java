import java.util.*;

class OXOModel
{
    // Could have used a 2D array, but in some situations it might be useful to be dynamic !
    private ArrayList<ArrayList<OXOPlayer>> rows;
    private ArrayList<OXOPlayer> players;
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private OXOView gameView;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh, OXOView view)
    {
        winThreshold = winThresh;
        gameView = view;
        rows = new ArrayList<ArrayList<OXOPlayer>>();
        for(int i=0; i<numberOfRows ;i++) {
            ArrayList<OXOPlayer> newRow = new ArrayList<OXOPlayer>(numberOfColumns);
            for(int j=0; j<numberOfColumns ;j++) newRow.add(null);
            rows.add(newRow);
        }
        players = new ArrayList<OXOPlayer>();
    }

    public int getNumberOfPlayers()
    {
        return players.size();
    }

    public void addPlayer(OXOPlayer player)
    {
        players.add(player);
    }

    public OXOPlayer getPlayerByNumber(int number)
    {
        return players.get(number);
    }

    public OXOPlayer getWinner()
    {
        return winner;
    }

    public void setWinner(OXOPlayer player)
    {
        winner = player;
        if(gameView != null) gameView.drawBoard(this);
    }

    public OXOPlayer getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(OXOPlayer player)
    {
        currentPlayer = player;
        if(gameView != null) gameView.drawBoard(this);
    }

    public int getNumberOfRows()
    {
        return rows.size();
    }

    public ArrayList<OXOPlayer> getRow(int rowNumber)
    {
        return rows.get(rowNumber);
    }

    public int getNumberOfColumns()
    {
        int maxRowLength = 0;
        for(int i=0; i<rows.size() ;i++) {
            if(rows.get(i).size() > maxRowLength) maxRowLength = rows.get(i).size();
        }
        return maxRowLength;
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber)
    {
        if((rowNumber > this.getNumberOfRows()-1) || (colNumber > rows.get(rowNumber).size()-1)) return null;
        else return rows.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player)
    {
        rows.get(rowNumber).set(colNumber, player);
        if(gameView != null) gameView.drawBoard(this);
    }

    public int getWinThreshold()
    {
        return winThreshold;
    }

    public void setGameDrawn()
    {
        gameDrawn = true;
        if(gameView != null) gameView.drawBoard(this);
    }

    public boolean isGameDrawn()
    {
        return gameDrawn;
    }
}
