package telran.net.games.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;

import telran.net.games.BullsCowsTestPersistenceUnitInfo;

import telran.net.games.exceptions.*;
import telran.net.games.model.MoveData;
import telran.net.games.repo.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BullsCowsServiceTest {
	private static final int N_DIGITS = 4;
	private static final String GAMER_ID = "player";
	private static final LocalDate GAMER_BIRTH_DATE = LocalDate.of(2020, 1, 1);
	private static final String MOVING_SEQUENCE = "1234";
	private static final String INCORRECT_SEQUENCE = "123";
	private static final String PLAYER1_ID = "player1";
	static BullsCowsRepository repository;
	static BullsCowsService bcService;
	static long gameId;

	static {
		HashMap<String, Object> hibernateProperties = new HashMap<>();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		repository = new BullsCowsRepositoryJpa(new BullsCowsTestPersistenceUnitInfo(), hibernateProperties);
		BullsCowsGameRunner bcRunner = new BullsCowsGameRunner(N_DIGITS);
		bcService = new BullsCowsServiceImpl(repository, bcRunner);
	}

	@Order(1)
	@Test
	void createGameTest() {
		gameId = bcService.createGame();
		assertEquals(repository.getGameIdsNotStarted().get(0), gameId);
		assertThrowsExactly(NoGamerInGameException.class, () -> {
			bcService.startGame(gameId);
		});
		assertThrowsExactly(GameNotStartedException.class, () -> {
			bcService.moveProcessing(MOVING_SEQUENCE, gameId, GAMER_ID);
		});
	}

	@Order(2)
	@Test
	void registerGamerTest() {

		bcService.registerGamer(GAMER_ID, GAMER_BIRTH_DATE);
		assertNotNull(repository.getGamer(GAMER_ID));

		assertThrowsExactly(GamerAlreadyExistsException.class,
				() -> bcService.registerGamer(GAMER_ID, GAMER_BIRTH_DATE));
	}

	@Order(3)
	@Test
	void joinGamerInGame() {
		gameId = repository.getGameIdsNotStarted().get(0);
		bcService.gamerJoinGame(gameId, GAMER_ID);
		List<String> gamers = bcService.getGameGamers(gameId);
		assertTrue(gamers.contains(GAMER_ID));
	}

	@Order(4)
	@Test
	void startGameTest() {
		List<String> gamers = bcService.startGame(gameId);
		assertEquals(1, gamers.size());
		assertTrue(repository.isGameStarted(gameId));
		assertThrowsExactly(GameAlreadyStartedException.class, () -> {
			bcService.gamerJoinGame(gameId, PLAYER1_ID);
		});
	}

	@Order(5)
	@Test
	void moveProcessingTest() {
		String sequence = ((BullsCowsServiceImpl) bcService).getSequence(gameId);
		assertThrowsExactly(IncorrectMoveSequenceException.class, () -> {
			bcService.moveProcessing(INCORRECT_SEQUENCE, gameId, GAMER_ID);
		});
		List<MoveData> moves = bcService.moveProcessing(sequence, gameId, GAMER_ID);
		assertEquals(1, moves.size());
		assertEquals(sequence, moves.get(moves.size() - 1).sequence());

		assertThrowsExactly(GameFinishedException.class, () -> {
			bcService.moveProcessing(MOVING_SEQUENCE, gameId, GAMER_ID);
		});

	}

}
