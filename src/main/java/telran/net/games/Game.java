package telran.net.games;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="game")
public class Game {
@Id
private Long id;
private LocalDate date;
private Boolean isFinished;
private String sequence;

public Game() {
}

public Game(Long id, LocalDate date, Boolean isFinished, String sequence) {
	this.id = id;
	this.date = date;
	this.isFinished = isFinished;
	this.sequence = sequence;
}

public Long getId() {
	return id;
}
public LocalDate getDate() {
	return date;
}
public Boolean getIsFinished() {
	return isFinished;
}
public String getSequence() {
	return sequence;
}

@Override
public String toString() {
	return "Game [id=" + id + ", date=" + date + ", isFinished=" + isFinished + ", sequence=" + sequence + "]";
}


}
