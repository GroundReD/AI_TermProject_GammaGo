import java.awt.datatransfer.StringSelection;
import java.util.*;

/**
 * Created by pyh on 2016. 3. 17..
 */
public class Gammago {

    public static void main(String args[]){
        Board b = new Board(5);
        AI ai = new AI(5);
        Scanner scan = new Scanner(System.in);

        String position = " ";
        Boolean checkOverlap = false;

        b.initBoard();
        System.out.println("Start the game..");

        while(true)
        {
            if (!b.checkTurn)
            {
                ai.selectProperState(3, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                int[] aiPos = ai.selectNextMove();
                checkOverlap = b.inputStoneOnBoard(aiPos);

                if (!checkOverlap) continue;
                b.printBoard();

                if (b.isWin(aiPos[0], aiPos[1]) == 5)
                {
                    System.out.println("AI wins");
                    break;
                }
                else if (b.isDraw())
                {
                    System.out.println("Game Draw");
                    break;
                }
            }
            else
            {
                // Player
                System.out.print("input your position (ex: A3): ");
                position = scan.nextLine();
                checkOverlap = b.inputStoneOnBoard(position);
                if (!checkOverlap) continue;
                b.printBoard();

                if (b.isWin(position) == -5)
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

class Board {

    //    variable about turn
    private final int EMPTY          = 0;
    private final int WHITE_PLAYER   = -1;
    private final int BLACK_COMPUTER = 1;

    private final boolean CONTINUE  = false;
    private final boolean DRAW      = true;

    private final int PLAYER_WIN    = -5;
    private final int AI_WIN        = 5;

    public static int boardSize;
    public static int[][] board;
    public boolean checkTurn; // false : black, true : white

    //    constructor
    public Board(int boardSize)
    {
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];
        checkTurn = false;
    }
    //    initailize board
    void initBoard()
    {
        for ( int i = 0 ; i < boardSize ; i++ )
        {
            for ( int j = 0 ; j < boardSize ; j++ )
                board[i][j] = 0;
        }
    }
    void printBoard()
    {
        for ( int i = 0 ; i < boardSize+2 ; i++ )
        {
            if ( i == 0 || i == boardSize+1 )
                System.out.print("\t");

            else
                System.out.print((i)+"\t");
            for ( int j = 0 ; j < boardSize ; j++ )
            {
                if (i == 0 || i == boardSize+1)
                    System.out.format("%c",j+65);
                else if ( board[i-1][j] == EMPTY )
                    System.out.print('-');
                else if ( board[i-1][j] == WHITE_PLAYER )
                    System.out.print('○');
                else if ( board[i-1][j] == BLACK_COMPUTER )
                    System.out.print('●');
            }

            if( i == 0 || i == boardSize+1 )
                System.out.println();
            else
                System.out.println("\t  "+(i));
        }
        System.out.println();
    }
    boolean inputStoneOnBoard(String pos)
    {
        int[] pos_stone = positionParse(pos);
        int pos_row = pos_stone[0];
        int pos_col = pos_stone[1];

//        int x_Intpos = Character.getNumericValue(pos.charAt(0)) - 10; // A = 10
//        int y_Intpos = Character.getNumericValue(pos.charAt(1)) - 1; // array start 0
        int checkCurrentTurn = checkWhosTurn();

        if ( checkCurrentTurn == WHITE_PLAYER && board[pos_row][pos_col] == 0)
        {
            board[pos_row][pos_col] = WHITE_PLAYER;
            return true;
        }
        else if ( checkCurrentTurn == BLACK_COMPUTER && board[pos_row][pos_col] == 0)
        {
            board[pos_row][pos_col] = BLACK_COMPUTER;
            return true;
        }
        else {
            System.out.println("Stone is already in the place. Choose another place.");
            checkWhosTurn();
            return false;
        }
    }
    //Overloading
    Boolean inputStoneOnBoard(int[] a)
    {
        int pos_row = a[0];
        int pos_col = a[1];

        int checkCurrentTurn = checkWhosTurn();

        if ( checkCurrentTurn == WHITE_PLAYER && board[pos_row][pos_col] == 0)
        {
            board[pos_row][pos_col] = WHITE_PLAYER;
            return true;
        }
        else if ( checkCurrentTurn == BLACK_COMPUTER && board[pos_row][pos_col] == 0)
        {
            board[pos_row][pos_col] = BLACK_COMPUTER;
            return true;
        }
        else {
            System.out.println("Stone is already in the place. Choose another place.");
            checkWhosTurn();
            return false;
        }
    }

    public int checkWhosTurn ()
    {
        //ai's turn
        if (!checkTurn)
        {
            checkTurn = true;
            return BLACK_COMPUTER;

        }
        //player's turn
        else
        {
            checkTurn = false;
            return WHITE_PLAYER;
        }
    }

    public int isWin(String pos)
    {
        int[] currentPos = positionParse(pos);
        int currentRow = currentPos[0];
        int currentCol = currentPos[1];

        int sum_checkRow  = 0;
        int sum_checkCol  = 0;
        int sum_checkLDiag = 0;
        int sum_checkRDiag = 0;

//        check winnig state. If sum_checker is -3, player wins, else sum_checker is 3, AI wins.
        for (int i = 0; i < boardSize; i++)
        {
            sum_checkCol += board[i][currentCol];
            sum_checkRow += board[currentRow][i];
//            left diagonal
            if ( currentCol == currentRow)
            {
                sum_checkLDiag += board[i][i];
            }
//            right diagonal
            if (currentCol + currentRow == 4)
            {
                sum_checkRDiag += board[i][boardSize-i-1];
            }
            ;        }

        if ( sum_checkCol == PLAYER_WIN || sum_checkLDiag == PLAYER_WIN || sum_checkRDiag == PLAYER_WIN || sum_checkRow == PLAYER_WIN )
            return PLAYER_WIN;
        else if ( sum_checkCol == AI_WIN || sum_checkLDiag == AI_WIN ||  sum_checkRDiag == AI_WIN || sum_checkRow == AI_WIN)
            return AI_WIN;
        else
            return 0; //continue
    }
    // overloading isWin Method
    public int isWin(int row, int col) {

        int sum_checkRow  = 0;
        int sum_checkCol  = 0;
        int sum_checkLDiag = 0;
        int sum_checkRDiag = 0;

//        check winnig state. If sum_checker is -3, player wins, else sum_checker is 3, AI wins.
        for (int i = 0; i < boardSize; i++)
        {
            sum_checkCol += board[i][col];
            sum_checkRow += board[row][i];
//            left diagonal
            if ( col == row)
            {
                sum_checkLDiag += board[i][i];
            }
//            right diagonal
            if (col + row == 4)
            {
                sum_checkRDiag += board[i][boardSize-i-1];
            }
        }

        if ( sum_checkCol == PLAYER_WIN || sum_checkLDiag == PLAYER_WIN || sum_checkRow == PLAYER_WIN
                || sum_checkRDiag == PLAYER_WIN )
            return PLAYER_WIN;
        else if ( sum_checkCol == AI_WIN || sum_checkLDiag == AI_WIN || sum_checkRow == AI_WIN
                || sum_checkRDiag == AI_WIN)
            return AI_WIN;
        else
            return 0; //continue
    }
    boolean isDraw()
    {
        for (int i = 0 ; i < boardSize ; i++ )
        {
            for ( int j = 0 ; j < boardSize ; j++ )
            {
                if ( board[i][j] == EMPTY ) return CONTINUE;
            }
        }
        return DRAW;
    }
    //  transform input String position value to array postion on board.
    int[] positionParse (String pos)
    {
        int col_Intpos = Character.getNumericValue(pos.charAt(0)) - 10; // A = 10
        int row_Intpos = Character.getNumericValue(pos.charAt(1)) - 1; // array start 0

        int[] pos_XY = {row_Intpos,col_Intpos};

        return pos_XY;

    }
}

class AI extends Board
{
    int row;
    int col;

    public AI (int boardSize) {
        super(boardSize);
    }

    ArrayList<int[]> nextMovePostion = new ArrayList<int[]>();

    int calculateBoardWeight ()
    {
        final int WHITE = 0;
        final int BLACK = 1;
        int sum_boardWeight = 0;
/*
    [][0] is number of white stone, [][1] is number of blackstone
    [0,1,2] is low, [3,4,5] is col, [6,7] is diagnol
*/

        int[][] checker =   new int[(boardSize*2)+2][2];

        for ( int i = 0 ; i < boardSize ; i++ )
        {
            for ( int j = 0 ; j < boardSize ; j++ )
            {
                if      (board[i][j] == -1) checker[i][WHITE]++;
                else if (board[i][j] == 1)  checker[i][BLACK]++;

                if      (board[j][i] == -1) checker[i+5][WHITE]++;
                else if (board[j][i] == 1)  checker[i+5][BLACK]++;
            }
//            right diagnol
            if      (board[i][i] == -1)     checker[boardSize*2][WHITE]++;
            else if (board[i][i] == 1)      checker[boardSize*2][BLACK]++;
//            left diagnol
            if (board[i][boardSize - 1 - i] == -1)      checker[boardSize*2+1][WHITE]++;
            else if (board[i][boardSize - 1 - i] == 1) checker[boardSize*2+1][BLACK]++;
        }

        for ( int i = 0 ; i < (boardSize*2) + 2 ; i++ )
        {
            sum_boardWeight += convertWeight(checker[i][0],checker[i][1]);
        }
        return sum_boardWeight;
    }
    /*
         the case 3 stones in a low is 100 or -100,
         2 in a low is 10 or -10,
         1 in a low is 1 or-1
         black stone has plus value
    */
    int convertWeight(int white, int black)
    {
        int convertValue;

        if      ( white == 5 )                  convertValue = -10000;
        else if ( white == 4 && black == 0 )    convertValue = -1000;
        else if ( white == 3 && black == 0 )    convertValue = -100;
        else if ( white == 2 && black == 0 )    convertValue = -10;
        else if ( white == 1 && black == 0 )    convertValue = -1;

        else if ( black == 5 )                  convertValue = 10000;
        else if ( black == 4 && white == 0 )    convertValue = 1000;
        else if ( black == 3 && white == 0 )    convertValue = 100;
        else if ( black == 2 && white == 0 )    convertValue = 10;
        else if ( black == 1 && white == 0 )    convertValue = 1;
        else convertValue = 0;

        return convertValue;
    }
    //use Minimax algorithm with alpha-beta pruning

    int selectProperState(int depth, int checkTurn, int alpha, int beta)
    {
        ArrayList<int[]> emptyPostion = availalbePostion();
        int[] properPos = new int[3];

        int currentAlpha = 0;//Integer.MIN_VALUE;
        int currentBeta = 0;//Integer.MAX_VALUE;

        if ( depth <= 0 || emptyPostion.isEmpty() || checkWin() != 0 )
        {
            return calculateBoardWeight();
        }
        if ( depth == 3 ) nextMovePostion.clear();

        for ( int i = 0 ; i < emptyPostion.size() ; i++ )
        {
            properPos = emptyPostion.get(i);
            int score;
            // AI turn
            if ( checkTurn == 1 )
            {
                board[properPos[0]][properPos[1]] = 1;
                System.out.println("depth : " + depth);
                printBoard();

                score = selectProperState(depth-1, -1, alpha, beta);
                System.out.println("alpha "+score+" " + currentAlpha + " " + alpha+"\t beta "+ beta);
//                currentAlpha = Math.max(currentAlpha,score);
//                currentAlpha = Math.max(currentAlpha,selectProperState(depth-1, -1, alpha, beta));
//                System.out.println("alpha "+score+" " + currentAlpha + " " + alpha+"\t beta "+ beta);

                alpha = Math.max(score,alpha);
                System.out.println("alpha "+score+" " + currentAlpha + " " + alpha+"\t beta "+ beta+"\n");


                if ( depth == 3 )
                {
                    properPos[2] = score;
                    nextMovePostion.add(properPos);
                }
                if ( alpha >= beta )
                {
                    System.out.println("alpa-cut\n");
                    printBoard();
                    board[properPos[0]][properPos[1]] = 0;
                    return alpha;
                }
            }
            else if ( checkTurn == -1 )
            {
                board[properPos[0]][properPos[1]] = -1;
                System.out.println("depth : " + depth);
                printBoard();
                score = selectProperState(depth-1, 1,alpha,beta);
                System.out.println("beta "+score+" " + currentBeta + " " + beta+"\t alpha "+ alpha);
//                currentBeta = Math.min(currentBeta,selectProperState(depth-1, 1,alpha,beta));
//                currentBeta = Math.min(currentBeta,score);
//                System.out.println("beta "+score+" " + currentBeta + " " + beta+"\t alpha "+ alpha);
                beta = Math.min(score,beta);
                System.out.println("beta "+score+" " + currentBeta + " " + beta+"\t alpha "+ alpha+"\n");


                if ( alpha >= beta )
                {
                    System.out.println("beta-cut");
                    printBoard();
                    board[properPos[0]][properPos[1]] = 0;
                    return beta;
                }
            }
            board[properPos[0]][properPos[1]] = 0;
        }
        if ( checkTurn == 1 ) {
            return alpha;
        }
        else return beta;
    }

//    void printBoard()
//    {
//        for ( int i = 0 ; i < boardSize+2 ; i++ )
//        {
//            if ( i == 0 || i == boardSize+1 )
//                System.out.print("\t");
//
//            else
//                System.out.print((i)+"\t");
//            for ( int j = 0 ; j < boardSize ; j++ )
//            {
//                if (i == 0 || i == boardSize+1)
//                    System.out.format("%c",j+65);
//                else if ( board[i-1][j] == 0 )
//                    System.out.print('-');
//                else if ( board[i-1][j] == -1 )
//                    System.out.print('○');
//                else if ( board[i-1][j] == 1 )
//                    System.out.print('●');
//            }
//
//            if( i == 0 || i == boardSize+1 )
//                System.out.println();
//            else
//                System.out.println("\t  "+(i));
//        }
//        System.out.println();
//    }


    int[] selectNextMove()
    {
        int[] tempArr = nextMovePostion.get(0);
//        Collections.sort(nextMovePostion, new Comparator<int[]>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                return (o1[2] > o2[2]) ? -1: (o1[2] < o2[2]) ? 1:0 ;
//            }
//        });
        for (int i = 0 ; i<nextMovePostion.size() ; i++ )
        {
            System.out.println(nextMovePostion.get(i)[0]+" "+
                    nextMovePostion.get(i)[1]+ " " +
                    nextMovePostion.get(i)[2]);

            if ( tempArr[2] < nextMovePostion.get(i)[2] )
            {
                tempArr = nextMovePostion.get(i);
            }
        }

        return tempArr;
    }

    ArrayList<int[]> availalbePostion ()
    {
        ArrayList<int[]> emptyPosition = new ArrayList<int[]>();

        for ( int i = 0 ; i < boardSize ; i++ )
        {
            for ( int j = 0 ; j < boardSize ; j++ )
            {
                if( board[i][j] == 0 )
                {
                    emptyPosition.add(new int[] {i,j,0});
                }
            }
        }
        return emptyPosition;
    }

    public int checkWin() {

        int[] sum_checkRow  = {0,0,0,0,0};
        int[] sum_checkCol  = {0,0,0,0,0};
        int[] sum_checkDiag = {0,0};

//        check winnig state. If sum_checker is -3, player wins, else sum_checker is 3, AI wins.
        for ( int i = 0; i <boardSize; i++){
            for (int j = 0; j < boardSize; j++)
            {
                sum_checkCol[i] += board[j][i];
                sum_checkRow[i] += board[i][j];
            }
            //            left diagonal
            if ( col == row)
            {
                sum_checkDiag[0] += board[i][i];
            }
            //            right diagonal
            if (col + row == 2)
            {
                sum_checkDiag[1] += board[i][boardSize-i-1];
            }
        }

        if ( sum_checkCol[0] == -5 ||
                sum_checkCol[1] == -5 ||
                sum_checkCol[2] == -5 ||
                sum_checkRow[0] == -5 ||
                sum_checkRow[1] == -5 ||
                sum_checkRow[2] == -5 ||
                sum_checkDiag[0] == -5 ||
                sum_checkDiag[1] == -5 )
            return -5;
        else if ( sum_checkCol[0] == 5 ||
                sum_checkCol[1] == 5 ||
                sum_checkCol[2] == 5 ||
                sum_checkRow[0] == 5 ||
                sum_checkRow[1] == 5 ||
                sum_checkRow[2] == 5 ||
                sum_checkDiag[0] == 5 ||
                sum_checkDiag[1] == 5 )
            return 5;
        else
            return 0; //continue
    }
}