import java.util.Random;
public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
       players[0].addTile(tiles[0]);
        
        for (int i = 0; i < players.length; i++) {
            for (int j = 1; i < 15; i++) {
                player[i].addTile(tiles[i * 14 + j]);
            }   
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() 
    {
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        players[currentPlayerIndex].numberOfTiles++;
        
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        players[currentPlayerIndex].addTile(tiles[0]);
        Tile [] newTiles = new Tile[tiles.length -1];
        for (int i = 1; i < tiles.length; i++) {
            newTiles[i-1] = tiles[i];
        }
        return tiles[0].toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles()
    {
        Random rand = new Random();
        for(int i = 0; i<tiles.length; i++)
        {
            tiles[i] = tiles[rand.nextInt(tiles.length)];
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */
    public boolean didGameFinish() {
        return players[getCurrentPlayerIndex()].checkWinning();
    }

    /* TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    public Player[] getPlayerWithHighestLongestChain() {
        Player playerWithHighestChain = new Player(getCurrentPlayerName());
        playerWithHighestChain = players[0];
        for (int i = 1; i < players.length; i++) {
            if(players[i].findLongestChain() > playerWithHighestChain.findLongestChain()){
                playerWithHighestChain = players[i];
            }
        }
        int winnersCount = 0;

        for (int i = 0; i < players.length; i++) {
            if(playerWithHighestChain.findLongestChain() == players[i].findLongestChain()){
                winnersCount++;
            }
        }
        Player[] winners = new Player[winnersCount];
        int index = 0;
        for (int i = 0; i < players.length; i++) {
            if(playerWithHighestChain.findLongestChain() == players[i].findLongestChain()){
                winners[index++] = players[i];
            }
        }

        return winners;
    }
    
    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    public void pickTileForComputer() {
        if (lastDiscardedTile != null) {
            if (players[currentPlayerIndex].findPositionOfTile(lastDiscardedTile) != -1) {
                getLastDiscardedTile();
            }
        }
        else {
            getTopTile();
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        int minChainLength = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < currentPlayer.numberOfTiles; i++) {
            Tile currentTile = currentPlayer.getTiles()[i];
            currentPlayer.getAndRemoveTile(i);

            int currentChainLength = currentPlayer.findLongestChain();
            if (currentChainLength < minChainLength) {
                minChainLength = currentChainLength;
                minIndex = i;
            }

            currentPlayer.addTile(currentTile);
        }

        Tile discardedTile = currentPlayer.getAndRemoveTile(minIndex);
        lastDiscardedTile = discardedTile;

        System.out.println(currentPlayer.getName() + " discarded tile: " + discardedTile.toString());
    }
    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) 
    {
        lastDiscardedTile = tiles[tileIndex];

        // new array will be formed after discarding a tile
        Tile [] discardedTile = new Tile[players[currentPlayerIndex].getTiles().length-1];

        for(int i = 0; i<tileIndex; i++)
        {
            discardedTile[i] = players[currentPlayerIndex].getTiles()[i];
        }

        for(int j = tileIndex; j<discardedTile.length; j++)
        {
            discardedTile[j] = players[currentPlayerIndex].getTiles()[j+1];
        }

        players[currentPlayerIndex].playerTiles = discardedTile; // the player tile is set to discarded tile
        players[currentPlayerIndex].numberOfTiles--; // number of tiles is decremented
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
