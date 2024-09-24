package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GameFinishedException extends IllegalStateException{
   public GameFinishedException(long gameId) {
	super("game "+gameId +" is finished");
}
}
