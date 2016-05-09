import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pyh on 2016. 3. 17..
 */
public class Gammago {

    public static void main(String args[]){

        Board b = new Board(15);
        AI ai = new AI(15);
        Scanner scan = new Scanner(System.in);
        String position = " ";
        Boolean checkOverlap = false;
        Pattern match_input_value = Pattern.compile("[A-Z][0-9][0-9]?"); //only 5 by 5
        int[] trans_position = new int[2];

        b.initBoard();
        System.out.println("Start the game..");

        while(true)
        {
            if (!b.checkTurn)
            {
                double startTime = System.currentTimeMillis();

                int[] aiPos = ai.selectProperState();
//                int[] aiPos = ai.selectNextMove();
                checkOverlap = b.inputStoneOnBoard(aiPos);
                if (!checkOverlap) continue;

                boolean ck = b.checkThreeThree();
                if (ck) {
                    b.changeTurn();
                    b.board[aiPos[0]][aiPos[1]] = 2;
                    continue;
                }

                b.printBoard();
                b.init_calculatedBoard();

                if (b.hasWon() == b.AI_WIN) {
                    System.out.println("AI wins");
                    break;
                } else if (b.isDraw()) {
                    System.out.println("Game Draw");
                    break;
                }

                double endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime)/1000 + " seconds");
            }
            else
            {
                // Player
                System.out.print("input your position (ex: A3): ");
                position = scan.nextLine();
                Matcher match = match_input_value.matcher(position);
                if(!match.matches())
                {
                    System.out.println("invalid format, try again");
                    continue;
                }
                trans_position = b.positionParse(position);
                checkOverlap = b.inputStoneOnBoard(trans_position);
                if (!checkOverlap) continue;

                boolean ck = b.checkThreeThree();

                if(ck){
                    b.changeTurn();
                    System.out.println("3-3 position, try again");
                    b.board[trans_position[0]][trans_position[1]] = 0;
                    continue;
                }

                b.printBoard();

                if (b.hasWon() == b.PLAYER_WIN)
                {
                    System.out.println("Player wins.");
                    break;
                }
                else if (b.isDraw())
                {
                    System.out.println("Game Draw");
                    break;
                }
            }
        }
    }
}
