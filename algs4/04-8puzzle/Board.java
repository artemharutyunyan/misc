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

public class Board {
  private int d; 
  private int[][] b;

  // construct a board from an N-by-N array of blocks
  // (where blocks[i][j] = block in row i, column j)
  public Board(int[][] blocks) {
      d = blocks.length;
      b = blocks;
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
          if ((b[i][j]) != 0 && (b[i][j] != i * d + j))
            ++distance; 
    return distance; 
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() { 
    int distance = 0;
    
    for (int i = 0; i < d; ++i)
      for (int j = 0; j < d; ++j) {
        int i_goal = b[i][j] / d;
        int j_goal = b[i][j] % d;
        distance += (abs(i - i_goal) +  abs(j - j_goal));
      }
    return distance;
  }                

  // is this board the goal board?
  public boolean isGoal() { return (hamming() == 0); }

  // a board obtained by exchanging two adjacent blocks in the same row
  public Board twin() { 
    int row = 0;
    int[][] twin = b;
    if (twin[row][0] == 0 || twin[row][1] == 0)
      ++row;

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
  public Iterable<Board> neighbors() { return null;}     

  // string representation of the board (in the output format specified below)
  public String toString() { 
    String s = new String();
    
    /* Lame but straigthforward */
    for (int i = 0; i < d; ++i) {
      for (int j = 0; j < d; ++j) {
        if (j != 0) s += ' '; 
        s += (new Integer(b[i][j])).toString();
      }
      s += "\n";
    }   
    return s;
  }
  
  // main 
  public static void main(String[] args) {}
  
  //
  // Private functions 
  private int abs(int i) {
    return i < 0 ? -i:i;
  }
}

