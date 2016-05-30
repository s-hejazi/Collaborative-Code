package control;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import model.ComputerPlayer;
import model.HumanPlayer;
import model.Player;
import ui.Board;
import ui.Cards;
import ui.GameBoard;
public class Controller {

	static Player player1;
	static Player player2;
	private static int tie;
	private static int totalRound;
	private static Player currentPlayer;
	private static int turn;
	private static int currentRound;
	private static MoveStrategyContext ctx;
	private static Cards c;
	
	public Controller(String name1, String name2, String mark1, String mark2, int totalRound){
		c.dispose();
		currentRound = 1;
		tie = 0;
		Controller.totalRound = totalRound;
		turn = 1;
		player1 = new HumanPlayer(name1, mark1);
		player2 = new HumanPlayer(name2, mark2);
		currentPlayer = player1;
		GameBoard.mode = 2;
		GameBoard.main();
	}
	//TODO
	public Controller(String name, String mark1, String mark2, int totalRound, int HumanTurn, String level){
		c.dispose();
		currentRound = 1;
		tie = 0;
		Controller.totalRound = totalRound;
		if(HumanTurn ==1){
			player1 = new HumanPlayer(name, mark1);
			player2 = new ComputerPlayer( mark2);
		}
		else{
			player1 = new ComputerPlayer(mark2);
			player2 = new HumanPlayer(name, mark1);
		}
		turn = 1;
		currentPlayer = player1;
		 ctx = new MoveStrategyContext();
		if(level == "easy")
		ctx.setMoveStrategy(new Easy());
		else if(level == "medium")
			ctx.setMoveStrategy(new Medium());
		else 
			ctx.setMoveStrategy(new Hard());
		GameBoard.mode = 1;
		GameBoard.main();
		
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//new PlayerOptionMenu().setVisible(true);
				c = new Cards();
				c.createAndShowGUI();
			}
		});
	}
	
	public static String getPlayer1Name(){
		return player1.getName();
	}
	public static String getPlayer2Name(){
		return player2.getName();
	}

	public static int getTotalRound(){
		return totalRound;
	}
	public static String getCurrentPlayerName(){
		return currentPlayer.getName();
	}
	public static String getCurrentPlayerMark(){
		return currentPlayer.getToken();
	}
	public static void changeTurn(){
		if (turn == 1)
		{
			turn =2;
			currentPlayer = player2;
		}
		else
		{
			turn =1;
			currentPlayer = player1;	
		}
	}
	public static void resetTurn(){
		turn = 1;
		currentPlayer = player1;
	}
	public static void notifyComputer(String btnValue[]){
		ComputerPlayer.notifyTurn(btnValue, ctx);
	}
	public static void makeMove(int p){
		JButton button = Board.getButton(p);
		GameBoard.addMove(button);
	}
	public static void checkStatus(String btnValue[], String token, int checkPlayer){
		/**
		 * Win scenario
		 */
		boolean won = GameLogic.isWon(btnValue, token);
		if(won)
		{
			if (player1.getToken() == token)
				player1.incrementScore();
			else 
				player2.incrementScore();
			int[] line = new int [3];
			line = GameLogic.getLine();
			GameBoard.roundWon(line);
			currentRound++;
		}
		/**
		 * Tie scenario
		 */
		else if (GameLogic.isTie(checkPlayer)){
			tie++;
			GameBoard.roundTie();
			currentRound++;
			}
		
		if(currentRound > totalRound){
		String result = checkResult();
		GameBoard.gameWon(result);
		}
		
		GameBoard.updateScoreboard(player1.getScore(), player2.getScore(), tie);
	}
	public static String checkResult()
	{
		//TODO: format of displaying results
		
			int max = player1.getScore();
			String result = player1.getName() +  " is the winner with score of "+ player1.getScore()+" out of "+ totalRound;
			
			if(player2.getScore()> max){
				result = player2.getName() + " is the winner with score of "+ player2.getScore()+" out of "+ totalRound;
			}
			else if(player2.getScore()== max){
				result = player1.getName() + " and "+ player2.getName()+ " are tied each with a score of "+ player2.getScore()+" out of "+ totalRound;
			}
			return result;
		
	}
}
