package telran.net.games.exceptions;
@SuppressWarnings("serial")
public class WrongMoveException extends IllegalArgumentException {
	public WrongMoveException(String sequence, int nDigits) {
		super(String.format("Wrong sequence - \"%s\", must be %d unrepeated digits"
				,sequence, nDigits ));
	}
}