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

import java.util.HashMap;
import java.util.LinkedList;

public class Solver {
  private boolean isSolvable;
  private boolean ranOnce;
  private Board board;
  private int nMoves;
  private LinkedList<Board> solutionSeq;

  private static class CBoard implements Comparable<CBoard> {
    private Board b;
    private int nSteps;

    public CBoard(Board board, int n) {
      nSteps = n;
      b = board;
    }

    public int compareTo(CBoard that) {
      return (this.b.manhattan() + this.nSteps) 
             - (that.b.manhattan() + that.nSteps);   
    }
    public int getSteps() {
      return nSteps;
    }
  }
  
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    isSolvable = false;
    ranOnce = false;
    board = initial;
    solutionSeq = new LinkedList<Board>();
    nMoves = 0;
  }

  // is the initial board solvable
  public boolean isSolvable() {
    if (!ranOnce) solve();
    return isSolvable;
  }

  // min number of moves to solve initial board; -1 if no solution
  public int moves() {
    if (!ranOnce) solve();
    return nMoves;
  }

  // sequence of boards in a shortest solution; null if no solution     
  public Iterable<Board> solution() {
    if (!ranOnce) solve(); 
    return solutionSeq;
  }     

  private void solve() {
    ranOnce = true;
    HashMap<String, Boolean> seen = new HashMap<String, Boolean>();
    HashMap<String, Boolean> seenTwin = new HashMap<String, Boolean>();
    Board twin = board.twin();

    int n = 0;
    MinPQ<CBoard> pq = new MinPQ<CBoard>();
    MinPQ<CBoard> pqTwin = new MinPQ<CBoard>();

    pq.insert(new CBoard(board, n)); 
    pqTwin.insert(new CBoard(twin, n)); 

    while (true) {
      CBoard cb = pq.delMin();             
      CBoard cbTwin = pqTwin.delMin();

      solutionSeq.add(cb.b);

      if (cb.b.hamming() == 0) { // Found solution
        // Solvable 
        isSolvable = true;
        nMoves = n;
        break;
      } 

      // Twin is solvable 
      if (cbTwin.b.hamming() == 0) {
        isSolvable = false;
        break;
      }

      for (Board it : cb.b.neighbors()) {
        // If this is the first time
        if (!seen.containsKey(it.toString())) {
            pq.insert(new CBoard(it, n));
            seen.put(it.toString(), true);
          }
      }

      for (Board it: cbTwin.b.neighbors()) {
        // If this is the first time 
        if (!seenTwin.containsKey(it.toString())) {
            pqTwin.insert(new CBoard(it, n));
            seenTwin.put(it.toString(), true);
          }
      }
      ++n; // Increase move counter
    }
  }

  // solve a slider puzzle 
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        blocks[i][j] = in.readInt();

    Board initial = new Board(blocks);
    Solver s = new Solver(initial);
    if (s.isSolvable()) {
      StdOut.printf("Minimum number of moves = %d\n", s.moves());
      for (Board b : s.solution()) {
        StdOut.printf("%s", b.toString());
      }
    }
    else
      StdOut.print("No solution possible\n");

  } 
}

