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
    private CBoard prev;

    public CBoard(Board board, int n) {
      nSteps = n;
      b = board;
      prev = null;
    }


    public int compareTo(CBoard that) {
      return (this.b.manhattan() + this.nSteps) 
             - (that.b.manhattan() + that.nSteps);   
    }
    public int getSteps() {
      return nSteps;
    }

    public void setPrev(CBoard p) {
      prev = p;
    }

    public CBoard getPrev() {
      return prev;
    }
  }
  
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    isSolvable = false;
    ranOnce = false;
    board = initial;
    solutionSeq = new LinkedList<Board>();
    nMoves = -1;
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

    if (isSolvable)
      return solutionSeq;

    return null;
  }     

  private void solve() {
    ranOnce = true;
    HashMap<String, Boolean> seen = new HashMap<String, Boolean>();
    // Using a string representation of the board + number of steps as a key,
    // We accept a duplicate position in case we came to it using via a shorter 
    // path.
    seen.put(board.toString() + 0, true);

    HashMap<String, Boolean> seenTwin = new HashMap<String, Boolean>();
    Board twin = board.twin();
    seenTwin.put(twin.toString() + 0, true);

    //int n = 0;
    MinPQ<CBoard> pq = new MinPQ<CBoard>();
    MinPQ<CBoard> pqTwin = new MinPQ<CBoard>();

    pq.insert(new CBoard(board, 0)); 
    pqTwin.insert(new CBoard(twin, 0)); 

    while (true) {
      CBoard cb = pq.delMin();             
      CBoard cbTwin = pqTwin.delMin();

      //StdOut.printf("Popped out: %s", cb.b.toString());

      if (cb.b.hamming() == 0) { // Found solution
        // Solvable 
        isSolvable = true;
          
        // Iterate over the chain of boards which led to the solution
        do {
          solutionSeq.addFirst(cb.b);
          cb = cb.getPrev();
          ++nMoves;
        } while (cb != null);
        break;
      } 

      // Twin is solvable 
      if (cbTwin.b.hamming() == 0) {
        isSolvable = false;
        break;
      }

      for (Board it : cb.b.neighbors()) {
        // If this is the first time
        if (!seen.containsKey(it.toString() + cb.nSteps)) {
            CBoard tmp = new CBoard(it, cb.nSteps + 1);
            tmp.setPrev(cb);
            pq.insert(tmp);
            //StdOut.printf("Pushed: %s", it.toString());
            seen.put(it.toString() + cb.nSteps, true);
          }
      }

      for (Board it: cbTwin.b.neighbors()) {
        // If this is the first time 
        if (!seenTwin.containsKey(it.toString() + cbTwin.nSteps)) {
            CBoard tmp = new CBoard(it, cbTwin.nSteps + 1);
            tmp.setPrev(cbTwin);
            pqTwin.insert(tmp);
            seenTwin.put(it.toString() + cbTwin.nSteps, true);
          }
      }
    }
  }

  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}

  
}

