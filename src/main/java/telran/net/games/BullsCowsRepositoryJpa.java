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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setIsFinished(long gameId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Long> getGameIdsNotStarted() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<MoveData> getAllGameGamerMoves(long gameId, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWinner(long gameId, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWinner(long gameId, String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
