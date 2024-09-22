package telran.net.games;

import java.time.LocalDate;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.spi.PersistenceUnitInfo;
import telran.net.games.exceptions.GameNotFoundException;
import telran.net.games.exceptions.GamerAlreadyExistsException;
import telran.net.games.exceptions.GamerNotFoundException;

public class BullsCowsRepositoryJpa implements BullsCowsRepository {
 private EntityManager em;
 
	public BullsCowsRepositoryJpa(PersistenceUnitInfo persistenceUnit,
			HashMap<String, Object> hibernateProperties) {
		EntityManagerFactory emf = new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(persistenceUnit, hibernateProperties);
		em = emf.createEntityManager();
	}

	@Override
	public Game getGame(long id) {
		Game game = em.find(Game.class,id);
		if(game == null) {
			throw new GameNotFoundException(id);
		}
		return game;
	}

	@Override
	public Gamer getGamer(String username) {
		Gamer gamer = em.find(Gamer.class,username);
		if(gamer == null) {
			throw new GamerNotFoundException(username);
		}
		return gamer;
	}

	@Override
	public long createNewGame(String sequence) {
		Game game = new Game(null,false,sequence);
		createObject(game);
		return game.getId();
	}
	private <T> void createObject(T obj) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(obj);
		transaction.commit();
	}

	@Override
	public void createNewGamer(String username, LocalDate birthdate) {
		try {
			Gamer gamer = new Gamer(username,birthdate);
			createObject(gamer);
		} catch (Exception e) {
			throw new GamerAlreadyExistsException(username);
		}
	}

	@Override
	public boolean isGameStarted(long id) {
		Game game = getGame(id);
		return game.getDate() != null;
	}

	@Override
	public void setStartDate(long gameId, LocalDateTime dateTime) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Game game =getGame(gameId);
		game.setDate(dateTime);
		transaction.commit();
	}

	@Override
	public boolean isGameFinished(long id) {
		
		return getGame(id).isfinished();
	}

	@Override
	public void setIsFinished(long gameId) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		getGame(gameId).setfinished(true);
		transaction.commit();

	}

	@Override
	public List<Long> getGameIdsNotStarted() {
		TypedQuery<Long> query =em.createQuery(
				"select id from Game where dateTime is null",
				Long.class);
		return query.getResultList();
	}

	@Override
	public List<String> getGameGamers(long id) {
		TypedQuery<String> query = em.createQuery(
				"select gamer.username from GameGamer where game.id =?1"
				,String.class);
		return query.setParameter(1, id).getResultList();
	}

	@Override
	public void createGameGamer(long gameId, String username) {
		Game game = getGame(gameId);
		Gamer gamer = getGamer(username);
		GameGamer gameGamer = new GameGamer(false,game,gamer);
		createObject(gameGamer);

	}

	@Override
	public void createGameGamerMove(MoveDto moveDto) {
		GameGamer gg = getGameGamer(moveDto.gameId(),moveDto.username());
		Move move = new Move(moveDto.sequence(),moveDto.bulls(),moveDto.cows(),gg);
        createObject(move);
	}

	@Override
	public List<MoveData> getAllGameGamerMoves(long gameId, String username) {
		GameGamer gg = getGameGamer(gameId, username);
		TypedQuery<MoveData> query = em.createQuery(
				"select sequence,bulls,cows from Move where gameGamer.id = ?1 ",
				MoveData.class);
		   query.setParameter(1, gg.getId());
		return query.getResultList();
	}

	@Override
	public void setWinner(long gameId, String username) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		GameGamer gameGamer = getGameGamer(gameId,username);
		gameGamer.setwinner(true);
		transaction.commit();

	}

	private GameGamer getGameGamer(long gameId, String username) {
		TypedQuery<GameGamer> query = em.createQuery(
				"select gg from GameGamer gg where gg.game.id = :id and gg.gamer.username =:username",
				GameGamer.class);
		query.setParameter("id", gameId).setParameter("username", username);
		
			return query.getSingleResult();
	}

	@Override
	public boolean isWinner(long gameId, String username) {
		return getGameGamer(gameId, username).iswinner();
	}

}
