package telran.net.games;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="game")
public class Game {
@Id
private Long id;
private LocalDate date;
private Boolean is_finished;
private String sequence;

public Game() {
}

public Game(Long id, LocalDate date, Boolean isFinished, String sequence) {
	this.id = id;
	this.date = date;
	this.is_finished = isFinished;
	this.sequence = sequence;
}

public Long getId() {
	return id;
}
public LocalDate getDate() {
	return date;
}
public Boolean getIsFinished() {
	return is_finished;
}
public String getSequence() {
	return sequence;
}

@Override
public String toString() {
	return "Game [id=" + id + ", date=" + date + ", isFinished=" + is_finished + ", sequence=" + sequence + "]";
}


}
