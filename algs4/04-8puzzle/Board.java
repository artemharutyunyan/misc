/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
*/

import java.util.Iterator;
import java.util.Arrays;

public class Board {
  private int d; 
  private int[][] b;

  // Iterable board 
  private class BoardIterable implements Iterable<Board> {
    Board b;

    public BoardIterable(Board board) {
      b = board;
    }

    public Iterator<Board> iterator() {
      return new BoardIterator(b);
    }
  }

  // Board iterator
  private class BoardIterator implements Iterator<Board> {
    private int n;             // Current step   
    private int nPossible;     // Number of possible steps (eiter 4 or 2)
    private boolean[] moves = new boolean[4];  // Allowed moves (up, down, left, right)
    private int index;         // Current step index
    private int i_empty;
    private int j_empty;
    Board start; 

    public BoardIterator(Board board) {
      n = 0;
      start = board;

      for (int i = 0; i < d; ++i)
        for (int j = 0; j < d; ++j) {
          if (board.b[i][j] == 0) {

            // Upper left corner
            if (i == 0 && j == 0) { 
              moves[1] = true; // down is possible
              moves[3] = true; // right is possible
              nPossible = 2;
              index = 1;
              //StdOut.println("upper left\n");
            }
            // Upper right corner 
            else if (i == 0 && j == (d - 1)) {
              moves[1] = true; // down is possible
              moves[2] = true; // left is possible
              nPossible = 2;
              index = 1;
              //StdOut.println("upper right\n");
            }
            // Lower left corner 
            else if (i == (d - 1) && j == 0) {
              moves[0] = true; // up is possible
              moves[3] = true; // right is possible
              nPossible = 2;
              index = 0;
              //StdOut.println("lower left\n");
            }
            // Lower right corner 
            else if (i == (d - 1) && j == (d - 1)) {
              moves[0] = true; // up is possible
              moves[2] = true; // left is possible
              nPossible = 2;
              index = 0;
              //StdOut.println("lower right\n");
            }
            // Left column 
            else if (j == 0) {
              moves[0] = true; // up is possible
              moves[1] = true; // down is possible
              moves[3] = true; // right is possible
              nPossible = 3;
              index = 0;
            }
            // Right column 
            else if (j == (d - 1)) {
              moves[0] = true; // up is possible
              moves[1] = true; // down is possible
              moves[2] = true; // left 
              nPossible = 3;
              index = 0;
            }
            // Top row 
            else if (i == 0) {
              moves[1] = true; // down is possible
              moves[2] = true; // left is possible 
              moves[3] = true; // right is possible
              nPossible = 3;
              index = 1;
            }
            // Bottom row 
            else if (i == (d - 1)) {
              moves[0] = true; // up is possible
              moves[2] = true; // left is possible 
              moves[3] = true; // right is possible
              nPossible = 3;
              index = 0;
            }
            // All moves possible
            else {
              for (int k = 0; k < 4; ++k)
                moves[k] = true;
              nPossible = 4;
              index = 0;
            }

            i_empty = i;
            j_empty = j;
            //for (i = 0; i < 4; ++i) StdOut.printf("%b ", moves[i]);
            //StdOut.println();
            return;
          }
        }
    }

    public boolean hasNext() {
      return n < nPossible; 
    }

    public Board next() {

      //StdOut.printf("index is %d\n", index);
      Board tmp = new Board(start.b);
      //StdOut.printf("array is\n%s", tmp.toString());

      if (index == 0) { // Up
        tmp.b[i_empty][j_empty] = tmp.b[i_empty - 1][j_empty];
        tmp.b[i_empty - 1][j_empty] = 0;
      }
      else if (index == 1) { // Down 
        tmp.b[i_empty][j_empty] = tmp.b[i_empty + 1][j_empty];
        tmp.b[i_empty + 1][j_empty] = 0;
      }
      else if (index == 2) { // Left 
        tmp.b[i_empty][j_empty] = tmp.b[i_empty][j_empty - 1];
        tmp.b[i_empty][j_empty - 1] = 0;
      }
     else if (index == 3) { // Right 
        tmp.b[i_empty][j_empty] = tmp.b[i_empty][j_empty + 1];
        tmp.b[i_empty][j_empty + 1] = 0;
      }

      // Advance index to the next move position 
      ++index;
      while (index < 4) { 
        if (moves[index] == true) break;
        else ++index;
      }
      //StdOut.printf("array is\n%s", tmp.toString());
      //StdOut.printf("next index is %d\n", index);
     
      ++n;
      return tmp;
    }
    
    public void remove() {}
  }

  // construct a board from an N-by-N array of blocks
  // (where blocks[i][j] = block in row i, column j)
  public Board(int[][] blocks) {
      d = blocks.length;
      b = new int[d][d]; 
      for (int i = 0; i < d; ++i)
        for (int j = 0; j < d; ++j) 
         b[i][j] = blocks[i][j];    
  }

  // board dimension N
  public int dimension() {
      return d;
  }                 

  // number of blocks out of place
  public int hamming() { 
    int distance = 0;

      for (int i = 0; i < d; ++i)
        for (int j = 0; j < d; ++j)
          if ((b[i][j]) != 0 && (b[i][j] != (1 + i * d + j)))
            ++distance; 
    return distance; 
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() { 
    int distance = 0;
    
    for (int i = 0; i < d; ++i)
      for (int j = 0; j < d; ++j) {
        if (b[i][j] != 0) {
          int i_goal = (b[i][j] - 1) / d;
          int j_goal = (b[i][j] - 1) % d;
          //StdOut.printf("pos for %d is %d %d\n", b[i][j], i_goal, j_goal);
          distance += (abs(i - i_goal) +  abs(j - j_goal));
        }
      }
    return distance;
  }                

  // is this board the goal board?
  public boolean isGoal() { return (hamming() == 0); }

  // a board obtained by exchanging two adjacent blocks in the same row
  public Board twin() { 
    int row = 0;
    int[][] twin = new int[d][d];
    if (twin[row][0] == 0 || twin[row][1] == 0)
      ++row;

    for (int i = 0; i < d; ++i)
      for (int j = 0; j < d; ++j)
        twin[i][j] = b[i][j];

    int tmp = twin[row][0];
    twin[row][0] = twin[row][1];
    twin[row][1] = tmp; 
    return new Board(twin);
  }                    

  // does this board equal y?
  public boolean equals(Object y) { 
    if (y == this) return true;
    if (y == null) return false;

    if (y.getClass() != this.getClass())
      return false;
    
    Board that = (Board) y;
    if (d != that.d) return false;
    for (int i = 0; i < d; ++i)
      for (int j = 0; j < d; ++j)
        if (b[i][j] != that.b[i][j]) return false;

    return true;
  }        

  // all neighboring boards
  public Iterable<Board> neighbors() { 
    return new BoardIterable(this);
  }     


  // string representation of the board (in the output format specified below)
  public String toString() { 
    String s = new String();
    
    /* Lame but straigthforward */
    s += d + "\n";
    for (int i = 0; i < d; ++i) {
      s += ' ';
      for (int j = 0; j < d; ++j) {
        if (j != 0) s += "  "; 
        s += (new Integer(b[i][j])).toString();
      }
      s += "\n";
    }   
    s += "\n";
    return s;
  }
  
  // main 
  /* public static void main(String[] args) {
    // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);


        StdOut.printf("%s", initial.toString());
        StdOut.printf("hamming %d\n", initial.hamming());
        StdOut.printf("manhattan %s\n", initial.manhattan());
        StdOut.printf("isGoal %b\n", initial.isGoal());
        
        for (Board ib : initial.neighbors()) {
          StdOut.printf("%s", ib.toString());
          StdOut.println("");
        }

        Board t = initial.twin();
        StdOut.printf("%s", t.toString());
  } */
  
  //
  // Private functions 
  private int abs(int i) {
    return i < 0 ? -i:i;
  }
}

