import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by pyh on 2016. 5. 2..
 */
public class AI extends Board {
    public AI(int boardSize) {
        super(boardSize);
    }

    ArrayList<int[]> nextMovePostion = new ArrayList<int[]>();
    int [][] evaluate_board = copy_Board();

    //use Minimax algorithm with alpha-beta pruning

    int[] selectProperState() {
        int[] nextPosition = new int[]{};
        if ( isEmpty() )
            return new int[] {boardSize/2, boardSize/2};

        else
            alphaBeta(2, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

        nextPosition = selectNextMove();

        return nextPosition;

    }
    boolean isEmpty () {
        int check_empty = 0;
        for ( int i = 0 ; i < boardSize ; i++ )
        {
            for ( int j = 0 ; j < boardSize ; j++)
            {
                if (board[i][j] != 0 )
                    check_empty++;
            }
        }

        return check_empty==0?true:false;
    }
    int alphaBeta(int depth, int checkTurn, int alpha, int beta) {
        ArrayList<int[]> emptyPostion = availalbePostion();
        int[] properPos = new int[3];

        int currentAlpha = 0;//Integer.MIN_VALUE;
        int currentBeta = 0;//Integer.MAX_VALUE;

        if (depth <= 0 || emptyPostion.isEmpty() || hasWon()!=0) {
            return board_Evaluate(checkTurn);
        }

        if (depth == 2 ) nextMovePostion.clear();

        for (int i = 0; i < emptyPostion.size(); i++) {
            properPos = emptyPostion.get(i);
            int score;
            // AI turn
            if (checkTurn == 1) {
                board[properPos[0]][properPos[1]] = 1;
//                System.out.println("depth : " + depth);
//                printBoard();

                score = alphaBeta(depth - 1, -1, alpha, beta);
//                System.out.println("alpha " + score + " " + currentAlpha + " " + alpha + "\t beta " + beta);
//                currentAlpha = Math.max(currentAlpha,score);
//                currentAlpha = Math.max(currentAlpha,selectProperState(depth-1, -1, alpha, beta));
//                System.out.println("alpha "+score+" " + currentAlpha + " " + alpha+"\t beta "+ beta);

                alpha = Math.max(score, alpha);
//                System.out.println("alpha " + score + " " + currentAlpha + " " + alpha + "\t beta " + beta + "\n");


                if (depth == 2) {
                    properPos[2] = score;
                    nextMovePostion.add(properPos);
                }
                if (alpha >= beta) {
//                    System.out.println("alpa-cut\n");
//                    printBoard();
                    board[properPos[0]][properPos[1]] = 0;
                    return alpha;
                }
            } else if (checkTurn == -1) {
                board[properPos[0]][properPos[1]] = -1;
//                System.out.println("depth : " + depth);
//                printBoard();
                score = alphaBeta(depth - 1, 1, alpha, beta);
//                System.out.println("beta " + score + " " + currentBeta + " " + beta + "\t alpha " + alpha);
//                currentBeta = Math.min(currentBeta,selectProperState(depth-1, 1,alpha,beta));
//                currentBeta = Math.min(currentBeta,score);
//                System.out.println("beta "+score+" " + currentBeta + " " + beta+"\t alpha "+ alpha);
                beta = Math.min(score, beta);
//                System.out.println("beta " + score + " " + currentBeta + " " + beta + "\t alpha " + alpha + "\n");


                if (alpha >= beta) {
//                    System.out.println("beta-cut");
//                    printBoard();
                    board[properPos[0]][properPos[1]] = 0;
                    return beta;
                }
            }
            board[properPos[0]][properPos[1]] = 0;
        }
        if (checkTurn == 1) {
            return alpha;
        } else return beta;
    }

    int[] selectNextMove() {

        int[] tempArr = nextMovePostion.get(0);
        ArrayList<int[]> maxMove = new ArrayList<int[]>();
        //        Collections.sort(nextMvePostion, new Comparator<int[]>() {
        //            @Override
        //            public int compare(int[] o1, int[] o2) {
        //                return (o1[2] > o2[2]) ? -1: (o1[2] < o2[2]) ? 1:0 ;
        //            }
        //        });
        for (int i = 1; i < nextMovePostion.size(); i++)
        {
//                    System.out.println(nextMovePostion.get(i)[0] + " " +
//                            nextMovePostion.get(i)[1] + " " +
//                            nextMovePostion.get(i)[2]);
//            if ( tempArr[2] > nextMovePostion.get(i)[2])

            if (tempArr[2] < nextMovePostion.get(i)[2])
            {
                tempArr = nextMovePostion.get(i);
            }
        }
        for (int[] temp: nextMovePostion)
        {
            if (temp[2] == tempArr[2])
                maxMove.add(temp);
        }
        Random r = new Random();
        int[] maxPos = maxMove.get(r.nextInt(maxMove.size()));

        return maxPos;
    }

    ArrayList<int[]> availalbePostion() {
        ArrayList<int[]> emptyPosition = new ArrayList<int[]>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                int x0 = Math.max(0, i - 1);
                int x1 = Math.min(boardSize - 1, i + 1);
                int y0 = Math.max(0, j - 1);
                int y1 = Math.min(boardSize - 1, j + 1);

                for (int row = x0; row <= x1; row++) {
                    for (int col = y0; col <= y1; col++) {

                        int[] pos = new int[]{row,col,0};
                        if (board[row][col] == 0)
                        {
                            if (emptyPosition.contains(pos))
                                continue;
                            else
                                emptyPosition.add(pos);
                        }
                    }
                }
            }
        }
        return emptyPosition;
    }

    int[][] direction = {
            {1,0}, // row
            {1,-1}, // left diagonal
            {0,1}, // column
            {1,1} // right diagonal
    };

    public boolean isOutOfBoard(int x, int y) {
        if (x < 0 || y < 0 || x > 14 || y > 14) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRow_Possible_Win(int x, int y, int[][] board, int[] offset, int player) {
        int count = 0;
        int againstPlayer;
        if (player == BLACK_COMPUTER) {
            againstPlayer = WHITE_PLAYER;
        } else {
            againstPlayer = BLACK_COMPUTER;
        }

        // counts free and forPlayer stones on one side
        for (int i=-4; i<=1; i++) {
            if (isOutOfBoard(x+i*offset[0], y+i*offset[1]))
                count = 0;
            else if (board[x+i*offset[0]][ y+i*offset[1]] == againstPlayer)
                count = 0;
            else
                count++;

        }
        // counts on the other side
        for (int i=1; i<=4; i++) {
            if (isOutOfBoard(x+i*offset[0], y+i*offset[1]))
                break;
            else if (board[x+i*offset[0]][y+i*offset[1]] == againstPlayer)
                break;
            else
                count++;
        }

        if (count>=4) {
            return true;
        } else {
            return false;
        }
    }

    public int[] position_Evaluate(int[][] board, int[] pos, int[] offset)
    {
        int check_type = board[pos[0]][pos[1]];

        if ( check_type == 0 )
        {
            return new int[]{0, 0};
        }
        int size = boardSize;

        int same_serial_r = 0;
        int empty_r = 0;
        int same_r = 0;
        for (int i = pos[0], j = pos[1]; i >= 0 && i < size && j >= 0 && j < size; i += offset[0], j += offset[1])
        {
            int current_type = board[i][j];
            if ( current_type == check_type )
            {
                if ( empty_r == 0 )
                    same_serial_r++;
                else
                    same_r++;
            } else if ( current_type == 0 )
            {
                empty_r++;
                if ( empty_r > 1 )
                    break;

            } else
                break;
        }

        int same_serial_l = 0;
        int empty_l = 0;
        int same_l = 0;

        for (int i = pos[0] - offset[0], j = pos[1] - offset[1]; i >= 0 && i < size && j >= 0 && j < size; i -= offset[0], j -= offset[1]) {
            int current_type = board[i][j];
            if ( current_type == check_type )
            {
                if ( empty_l == 0 )
                    same_serial_l++;
                else
                    same_l++;
            } else if ( current_type == 0 )
            {
                empty_l++;
                if ( empty_l > 1 )
                    break;
            } else
                break;
        }


        int pattern = same_serial_r + same_serial_l + (Math.max(same_r, same_l));
        int empty = Math.min(1, empty_r) + Math.min(1, empty_l);
        if (!isRow_Possible_Win(pos[0],pos[1],board,offset,check_type)) {
            pattern--;
            empty = 0;
        }
        return new int[]{pattern, empty};
    }

    public int board_Evaluate(int turn)
    {
        int ai_Value = 0;
        int player_Value = 0;

        for ( int i = 0 ; i < boardSize ; i++)
        {
            for ( int j = 0 ; j < boardSize ; j++)
            {
                if ( board[i][j]  != 0 )
                {
                    int[] _pos = {i, j};

                    for (int[] d : direction)
                    {
                        double value = convert_SingleWeight(board, _pos, d, turn);

                        if ( board[i][j] == BLACK_COMPUTER)
                            ai_Value += value;
                        else
                            player_Value += value;
                    }
                }
            }
        }
        return turn == 1? ai_Value - player_Value : player_Value -  ai_Value;
    }

    public int[] getMaxPoint(int[][] value_board)
    {
        Random r = new Random();
        ArrayList<int[]> arr_Max = new ArrayList<int[]>();
        int maxPoint = 0;
        int[] pos = new int[2];
        int[] maxPos = new int[2];


        maxPoint = 0;

        for(int i=0 ; i<boardSize ; i++)
            for(int j=0 ; j<boardSize ; j++)
            {
                if(maxPoint<value_board[i][j])
                    maxPoint=value_board[i][j];
            }

        for(int i=0 ; i<boardSize ; i++)
            for(int j=0 ; j<boardSize ; j++)
            {
                if(maxPoint == value_board[i][j])
                {
                    pos[0] = i;
                    pos[1] = j;
                    arr_Max.add(pos);
                }
            }

        maxPos = arr_Max.get(r.nextInt(arr_Max.size()));

        return maxPoint==0?new int[] {7,7}:maxPos;

    }

    int convert_SingleWeight(int [][] board, int[] pos, int[] offset, int turn)
    {
        int[] pattern = position_Evaluate(board, pos, offset);
        int value = Math.min(pattern[0] * 10 + pattern[1], 50 );

        if (turn == -1)
        {
            if (value == 50 ) return 99999999;
            else if (value == 42) return 99999999;
            else if (value == 41) return 99999999;
            else if (value == 32) return 170000;
            else if (value == 31) return 12000;
            else if (value == 22) return 13000;
            else if (value == 21) return 3000;
            else if (value == 12) return 5000;
            else if (value == 11) return 1200;
            else if (value == 0) return 1;
            else return 0;
        }
        else
        {
            if (value == 50 ) return 200000;
            else if (value == 42) return 200000;
            else if (value == 41) return 200000;
            else if (value == 32) return 60000;
            else if (value == 31) return 11000;
            else if (value == 22) return 4000;
            else if (value == 21) return 1000;
            else if (value == 12) return 2000;
            else if (value == 11) return 500;
            else if (value == 0) return 1;
            else return 0;
        }

    }

}
