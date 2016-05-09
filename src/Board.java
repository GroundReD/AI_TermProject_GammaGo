import java.util.ArrayList;

/**
 * Created by pyh on 2016. 5. 2..
 */

class Board {

    //    variable about turn
    public final int EMPTY = 0;
    public final static int WHITE_PLAYER = -1;
    public final static int BLACK_COMPUTER = 1;

    private static final boolean CONTINUE = false;
    private static final boolean DRAW = true;

    public final int PLAYER_WIN = -5;
    public final int AI_WIN = 5;
    public final int NONE_WIN = 0;

    public static int boardSize;
    public static int[][] board;
    public boolean checkTurn; // false : black, true : white

    public int[] lastMove = null;
    public int last_AI_ThreeThreeCount = 0;
    public int last_Player_ThreeThreeCount = 0;

    private ArrayList<int[]> winCheckPos;
    private final int inRow = 5;
    ArrayList<int[]> check_dup = new ArrayList<int[]>(); // check duplication ( --ooo-, -ooo--)

    //    constructor
    public Board(int boardSize) {
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];
        checkTurn = false;
    }

    //    initailize board
    void initBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++)
                board[i][j] = 0;
        }
    }

    void printBoard() {
        for (int i = 0; i < boardSize + 2; i++) {
            if (i == 0 || i == boardSize + 1)
                System.out.print("\t");

            else
                System.out.print((i) + "\t");
            for (int j = 0; j < boardSize; j++) {
                if (i == 0 || i == boardSize + 1)
                    System.out.format("%c ", j + 65);
                else if (board[i - 1][j] == EMPTY || board[i - 1][j] == 2)
                    System.out.print("- ");
                else if (board[i - 1][j] == WHITE_PLAYER)
                    System.out.print("○ ");
                else if (board[i - 1][j] == BLACK_COMPUTER)
                    System.out.print("● ");
            }

            if (i == 0 || i == boardSize + 1)
                System.out.println();
            else
                System.out.println("\t  " + (i));
        }
        System.out.println();
    }

    Boolean inputStoneOnBoard(int[] a) {
        int pos_row = a[0];
        int pos_col = a[1];

        int checkCurrentTurn = changeTurn();

        if (checkCurrentTurn == WHITE_PLAYER && board[pos_row][pos_col] == 0) {
            board[pos_row][pos_col] = WHITE_PLAYER;
            lastMove = a;
            return true;
        } else if (checkCurrentTurn == BLACK_COMPUTER && board[pos_row][pos_col] == 0) {
            board[pos_row][pos_col] = BLACK_COMPUTER;
            lastMove = a;
            return true;
        } else {
            System.out.println("Stone is already in the place. Choose another place.");
            changeTurn();
            return false;
        }
    }

    public int changeTurn() {
        //ai's turn
        if (!checkTurn) {
            checkTurn = true;
            return BLACK_COMPUTER;

        }
        //player's turn
        else {
            checkTurn = false;
            return WHITE_PLAYER;
        }
    }

    public int checkWhosTurn(boolean check) {
        return check?WHITE_PLAYER:BLACK_COMPUTER;
    }

    public int hasWon() {

        if (lastMove == null) return NONE_WIN;

        int[] pos = lastMove;

        winCheckPos = new ArrayList<int[]>();

        int count = leftRight(pos, -1, -1) + leftRight(pos, 1, boardSize) - 1;
        if (count == inRow) {
            winCheckPos.remove(pos);
            return !checkTurn?PLAYER_WIN:AI_WIN;
        }

        winCheckPos = new ArrayList<int[]>();
        count = upDown(pos, -1, -1) + upDown(pos, 1, boardSize) - 1;
        if (count == inRow) {
            winCheckPos.remove(pos);
            return !checkTurn?PLAYER_WIN:AI_WIN;
        }

        winCheckPos = new ArrayList<int[]>();
        count = diag(pos, -1, -1, -1, -1) + diag(pos, 1, 1, boardSize, boardSize) - 1;
        if (count == inRow) {
            winCheckPos.remove(pos);
            return !checkTurn?PLAYER_WIN:AI_WIN;
        }

        winCheckPos = new ArrayList<int[]>();
        count = diag(pos, 1, -1, boardSize, -1) + diag(pos, -1, 1, -1, boardSize) - 1;
        if (count == inRow) {
            winCheckPos.remove(pos);
            return !checkTurn?PLAYER_WIN:AI_WIN;
        }

        return NONE_WIN;
    }

    public int leftRight(int[] pos, int offsetX, int maxX) {
        if (pos[1] == maxX || board[ pos[0] ][ pos[1] ] != checkWhosTurn(!checkTurn)) return 0;
        else {
            winCheckPos.add(pos);
            int[] pos_n = {pos[0], pos[1] + offsetX};
            return 1 + leftRight(pos_n, offsetX, maxX);
        }
    }

    public int upDown(int[] pos, int offsetY, int maxY) {
        if (pos[0] == maxY || board[pos[0]][pos[1]] != checkWhosTurn(!checkTurn)) return 0;
        else {
            winCheckPos.add(pos);
            int[] pos_n = {pos[0] + offsetY, pos[1]};

            return 1 + upDown(pos_n, offsetY, maxY);
        }
    }

    public int diag(int[] pos, int offsetX, int offsetY, int maxX, int maxY) {
        if (pos[1] == maxX || pos[0] == maxY || board[pos[0]][pos[1]] != checkWhosTurn(!checkTurn)) return 0;
        else {
            winCheckPos.add(pos);
            int[] pos_n = {pos[0] + offsetY, pos[1] + offsetX};
            return 1 + diag(pos_n, offsetX, offsetY, maxX, maxY);
        }
    }
    /*public int isWin(int row, int col) {

        int sum_checkRow  = 0;
        int sum_checkCol  = 0;
        int sum_checkLDiag = 0;
        int sum_checkRDiag = 0;

//        check winnig state. If sum_checker is -5, player wins, else sum_checker is 5, AI wins.
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
    }*/

    boolean isDraw() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == EMPTY) return CONTINUE;
            }
        }
        return DRAW;
    }

    //  transform input String position value to array postion on board.
    int[] positionParse(String pos) {
        int col_Intpos = Character.getNumericValue(pos.charAt(0)) - 10; // A = 10
        int row_Intpos = Character.getNumericValue(pos.charAt(1)) - 1; // array start 0

        if (pos.length() == 3)
            row_Intpos = Character.getNumericValue(pos.charAt(1))*10 + Character.getNumericValue(pos.charAt(2)) - 1;

        int[] pos_XY = {row_Intpos, col_Intpos};

        return pos_XY;

    }
    public int isThreeThree_Value()
    {
        int checkRow = 0;
        int checkCol = 0;
        int checkLDiag = 0;
        int checkRDiag = 0;
        int[] skip_pos = new int[2];

        int sum = 0;
        int turn = 0;

        if ( !checkTurn ) // !checkTurn -> Player turn
            turn = -3;
        else
            turn = 3;
        //row
        for ( int row = 0 ; row<boardSize ; row++)
        {
            for( int col = 0 ; col < boardSize-5 ; col++)
            {
                if ( board[row][col] == 0 && board[row][col+5] == 0 )
                {
                    if ( board[row][col+1] + board[row][col+2] + board[row][col+3] + board[row][col+4] == turn )
                    {
                        if( board[row][col+1] == 0)
                            col++;

                        checkRow++;
                    }
                }
            }
        }
        //col
        for( int col = 0 ; col < boardSize ; col++)
        {
            for ( int row = 0 ; row<boardSize-5 ; row++)
            {
                if ( board[row][col] == 0 && board[row+5][col] == 0 )
                {
                    if ( board[row+1][col] + board[row+2][col] + board[row+3][col] + board[row+4][col] == turn )
                    {
                        if ( board[row+1][col] == 0 )
                            row++;
                        checkCol++;
                    }
                }
            }
        }
        //Diagonal
        //left diagonal
        for ( int row = 0 ; row<boardSize-5 ; row++)
        {
            for( int col = 0 ; col < boardSize-5 ; col++)
            {
                if ( board[row][col] == 0 && board[row+5][col+5] == 0 ) //-oooo-
                {
                    int [] _pos = {row,col};
                    if ( check_dup.contains(_pos) ) // -ooo--, --ooo- check
                    {
                        int index = check_dup.indexOf(_pos);
                        check_dup.remove(index);
                        continue;
                    }

                    else if ( board[row+1][col+1] + board[row+2][col+2] + board[row+3][col+3] + board[row+4][col+4] == turn )
                    {
                        if ( board[row+1][col+1] == 0 ) //chekc skip position
                        {
                            skip_pos[0] = row + 1;
                            skip_pos[1] = col + 1;
                            check_dup.add(skip_pos);
                        }
                        checkLDiag++;
                    }
                }
                else if ( ( row == 0 && col == 9 && board[row+1][col+2] + board[row+2][col+3] + board[row+3][col+4] == turn )
                        || (row == 9 && col == 0 && board[row+2][col+1] + board[row+3][col+2] + board[row+4][col+3] == turn) )
                {
                    checkLDiag++;
                }
            }
            check_dup.clear();
        }

        // right Diagonal
        for ( int row = 0 ; row < boardSize - 5 ; row++ )
        {
            for ( int col = 0; col < boardSize - 5; col++ )
            {
                if ( (row == 0 && col == 0 && board[row + 1][col + 3] + board[row + 2][col + 2] + board[row + 3][col + 1] == turn)
                        || (row == 9 && col == 9 && board[row + 2][col + 4] + board[row + 3][col + 3] + board[row + 4][col + 2] == turn))
                {
                    checkLDiag++;
                }
                else if (board[row + 5][col] == 0 && board[row][col + 5] == 0)
                {
                    int [] _pos = {row,col};
                    if ( check_dup.contains(_pos) ) // -ooo--, --ooo- check
                    {
                        int index = check_dup.indexOf(_pos);
                        check_dup.remove(index);
                        continue;
                    }
                    else if (board[row + 4][col + 1] + board[row + 3][col + 2] + board[row + 2][col + 3] + board[row + 1][col + 4] == turn)
                    {
                        if ( board[row+1][col-1] == 0 ) //chekc skip position
                        {
                            skip_pos[0] = row + 1;
                            skip_pos[1] = col - 1;
                            check_dup.add(skip_pos);
                        }
                        checkRDiag++;
                    }
                }
                check_dup.clear();
            }
        }
        sum = checkCol+checkLDiag+checkRDiag+checkRow;

        return checkTurn?sum:-sum;
    }

    boolean checkThreeThree ()
    {
        int currentCount = 0;
        int lastCount = 0;
        boolean chk = true;

        if ( !checkTurn ) lastCount = last_Player_ThreeThreeCount;
        else lastCount = last_AI_ThreeThreeCount;

        currentCount = isThreeThree_Value();

        if ( currentCount - lastCount >= 2 ) chk = true;
        else chk =  false;

        if ( !checkTurn ) last_Player_ThreeThreeCount = currentCount;
        else    last_AI_ThreeThreeCount = currentCount;

        return chk;
    }
//    public int isThreeThree_Value(int row, int col) {
//
//        int sum_checkRow = 0;
//        int sum_checkCol = 0;
//        int sum_checkLDiag = 0;
//        int sum_checkRDiag = 0;
//
//        for (int i = 0; i < boardSize; i++) {
//            if (board[row][i] == BLACK_COMPUTER)
//                sum_checkRow += board[row][i] * 100;
//            else
//                sum_checkRow += board[row][i];
//
//
//            if (board[i][col] == BLACK_COMPUTER)
//                sum_checkCol += board[i][col] * 100;
//            else
//                sum_checkCol += board[i][col] * 100;
//
//
//            if (board[i][i] == BLACK_COMPUTER)
//                sum_checkLDiag += board[i][i] * 100;
//            else
//                sum_checkLDiag += board[i][i];
//
//            if (board[i][boardSize - i - 1] == BLACK_COMPUTER)
//                sum_checkRDiag += board[i][boardSize - i - 1] * 100;
//            else
//                sum_checkRDiag += board[i][boardSize - i - 1];
//        }
//
//        if ((sum_checkCol == 300 && sum_checkRow == 300) ||
//                (sum_checkCol == 300 && sum_checkLDiag == 300) ||
//                (sum_checkCol == 300 && sum_checkRDiag == 300) ||
//                (sum_checkRow == 300 && sum_checkRDiag == 300) ||
//                (sum_checkRow == 300 && sum_checkLDiag == 300) ||
//                (sum_checkRDiag == 300 && sum_checkLDiag == 300))
//            return 1;
//
//        else if ((sum_checkCol == -3 && sum_checkRow == -3) ||
//                (sum_checkCol == -3 && sum_checkLDiag == -3) ||
//                (sum_checkCol == -3 && sum_checkRDiag == -3) ||
//                (sum_checkRow == -3 && sum_checkRDiag == -3) ||
//                (sum_checkRow == -3 && sum_checkLDiag == -3) ||
//                (sum_checkRDiag == -3 && sum_checkLDiag == -3)) {
//            return -1;
//        } else
//            return 0;
//
//    }

    void init_calculatedBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 2) {
                    board[i][j] = 0;
                }
            }
        }
    }

    public int [][] copy_Board(){

        int[][] copy_Board = new int[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                copy_Board[row][col] = board[row][col];
            }
        }
        return copy_Board;

    }
}

