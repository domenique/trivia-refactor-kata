package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
	private final Presenter systemPresenter;
	int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
	private final List<Player> playerList = new ArrayList<>();
    
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
		playerList.add(new Player(playerName));
	    places[howManyPlayers()] = 0;
	    purses[howManyPlayers()] = 0;
	    inPenaltyBox[howManyPlayers()] = false;

		systemPresenter.present(playerName  + " was added");
		systemPresenter.present("They are player number " + playerList.size());
		return true;
	}

	public int howManyPlayers() {
		return playerList.size();
	}

	public void roll(int roll) {
		systemPresenter.present(currentPlayer().getName() + " is the current player");
		systemPresenter.present("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				systemPresenter.present(currentPlayer().getName() + " is getting out of the penalty box");
				movePlayer(roll);

				systemPresenter.present(currentPlayer().getName()
										+ "'s new location is "
										+ currentPlayer().position());
				systemPresenter.present("The category is " + currentCategory());
				askQuestion();
			} else {
				systemPresenter.present(currentPlayer().getName() + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

			movePlayer(roll);

			systemPresenter.present(currentPlayer().getName()
									+ "'s new location is "
									+ places[currentPlayer]);
			systemPresenter.present("The category is " + currentCategory());
			askQuestion();
		}
		
	}

	private void movePlayer(int roll) {
		currentPlayer().move(roll);
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
	}

	private Player currentPlayer() {
		return playerList.get(currentPlayer);
	}

	private void askQuestion() {
		if (currentCategory() == "Pop")
			systemPresenter.present(popQuestions.removeFirst().toString());
		if (currentCategory() == "Science")
			systemPresenter.present(scienceQuestions.removeFirst().toString());
		if (currentCategory() == "Sports")
			systemPresenter.present(sportsQuestions.removeFirst().toString());
		if (currentCategory() == "Rock")
			systemPresenter.present(rockQuestions.removeFirst().toString());
	}
	
	
	private String currentCategory() {
		if (places[currentPlayer] % 4 == 0) return "Pop";
		if (places[currentPlayer] % 4 == 1) return "Science";
		if (places[currentPlayer] % 4 == 2) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				systemPresenter.present("Answer was correct!!!!");
				purses[currentPlayer]++;
				systemPresenter.present(currentPlayer().getName()
										+ " now has "
										+ purses[currentPlayer]
										+ " Gold Coins.");

				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == playerList.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == playerList.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {

			systemPresenter.present("Answer was corrent!!!!");
			purses[currentPlayer]++;
			systemPresenter.present(currentPlayer().getName()
									+ " now has "
									+ purses[currentPlayer]
									+ " Gold Coins.");

			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == playerList.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		systemPresenter.present("Question was incorrectly answered");
		systemPresenter.present(currentPlayer().getName() + " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == playerList.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
