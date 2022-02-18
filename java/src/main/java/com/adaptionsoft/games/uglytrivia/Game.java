package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
	private final Presenter systemPresenter;
	private final List<Player> players = new ArrayList<>();
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(Presenter systemPresenter){
		this.systemPresenter = systemPresenter;
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
    	}
	}

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	public boolean add(String playerName) {
		players.add(new Player(playerName));

		systemPresenter.present(playerName  + " was added");
		systemPresenter.present("They are player number " + players.size());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		systemPresenter.present(currentPlayer().getName() + " is the current player");
		systemPresenter.present("They have rolled a " + roll);

		if (currentPlayer().isInPenaltyBox()) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				systemPresenter.present(currentPlayer().getName() + " is getting out of the penalty box");
				movePlayer(roll);

				systemPresenter.present(currentPlayer().getName()
										+ "'s new location is "
										+ currentPlayer().position());
				systemPresenter.present("The category is " + currentCategory(currentPlayer().position()));
				askQuestion();
			} else {
				systemPresenter.present(currentPlayer().getName() + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

			movePlayer(roll);

			systemPresenter.present(currentPlayer().getName()
									+ "'s new location is "
									+ currentPlayer().position());
			systemPresenter.present("The category is " + currentCategory(currentPlayer().position()));
			askQuestion();
		}
		
	}

	private void movePlayer(int roll) {
		currentPlayer().move(roll);
	}

	private Player currentPlayer() {
		return players.get(currentPlayer);
	}

	private void askQuestion() {
		if (currentCategory(currentPlayer().position()) == "Pop")
			systemPresenter.present(popQuestions.removeFirst().toString());
		if (currentCategory(currentPlayer().position()) == "Science")
			systemPresenter.present(scienceQuestions.removeFirst().toString());
		if (currentCategory(currentPlayer().position()) == "Sports")
			systemPresenter.present(sportsQuestions.removeFirst().toString());
		if (currentCategory(currentPlayer().position()) == "Rock")
			systemPresenter.present(rockQuestions.removeFirst().toString());
	}
	
	
	private String currentCategory(int position) {
		if (position % 4 == 0) return "Pop";
		if (position % 4 == 1) return "Science";
		if (position % 4 == 2) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (currentPlayer().isInPenaltyBox()){
			if (isGettingOutOfPenaltyBox) {
				systemPresenter.present("Answer was correct!!!!");
				currentPlayer().addCoin();
				systemPresenter.present(currentPlayer().getName()
										+ " now has "
										+ currentPlayer().score()
										+ " Gold Coins.");

				boolean winner = !currentPlayer().hasWon();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {

			systemPresenter.present("Answer was corrent!!!!");
			currentPlayer().addCoin();
			systemPresenter.present(currentPlayer().getName()
									+ " now has "
									+ currentPlayer().score()
									+ " Gold Coins.");

			boolean winner = !currentPlayer().hasWon();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		systemPresenter.present("Question was incorrectly answered");
		systemPresenter.present(currentPlayer().getName() + " was sent to the penalty box");
		currentPlayer().toPenaltyBox();
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


}
