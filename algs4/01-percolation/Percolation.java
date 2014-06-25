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

public class Percolation {
  private boolean[]  grid; // grid of sites 
  private WeightedQuickUnionUF ufPercolates;  // union-find object (percolates) 
  private WeightedQuickUnionUF ufFull;  // union-find object (isFull)
  private int N; // Size of a grid 
  private int sourceIndex; // Index of the source site 
  private int sinkIndex; // Index of the source site 
    
  /**
   * Constructor. Creates and N-by-N grid, with all sites blocked. 
   * @param N size of a grid
   */
  public Percolation(int n) {
    if (n < 1)
      throw new IllegalArgumentException("illegal input parameters");

    N = n;
    /* Initialize the grid */
    grid = new boolean[N*N];    
    for (int i = 0; i < N*N; ++i)
      grid[i] = false;
    
    /* Initialize the union-find object. Using two extra elements as  
     * virtual nodes to which all top row elements are connected. This
     * object will be used to check whether the system percolates. */  
    ufPercolates = new WeightedQuickUnionUF(N*N + 2);
    /* Another UF object (with only top virtual node) to deal with 
     * backwashing. This object will be used to check whether the site 
     * is full. */
    ufFull = new WeightedQuickUnionUF(N*N + 1); 
  
    sourceIndex = N*N;
    sinkIndex = sourceIndex + 1;
  }
  /**
   * Opens the site (row i, column j) if it is not already. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out 
   * of [1, N] bounds  
   */
  public void open(int i, int j) {
    /* Open the site itself */
    int siteIndex = coord2index(i, j);
    grid[siteIndex] = true;
    
    /* Connect this site to open adjacent sites */
    
    /* Up */
    if (i == 1) { 
      // Union with both sources 
      ufPercolates.union(siteIndex, sourceIndex); 
      ufFull.union(siteIndex, sourceIndex); 
    }
    else 
    if (i != 1 && isOpen(i - 1, j)) {
      ufPercolates.union(siteIndex, coord2index(i-1, j));
      ufFull.union(siteIndex, coord2index(i-1, j));
    }

    /* Left */
    if (j != 1 && isOpen(i, j - 1)) {
      ufPercolates.union(siteIndex, coord2index(i, j - 1));
      ufFull.union(siteIndex, coord2index(i, j - 1));
    }

    /* Right */
    if (j != N && isOpen(i, j + 1)) {
      ufPercolates.union(siteIndex, coord2index(i, j + 1));
      ufFull.union(siteIndex, coord2index(i, j + 1));
    }

    /* Down */
    if (i == N)  
      ufPercolates.union(siteIndex, sinkIndex); // Union with sink
    else 
    if (i != N && isOpen(i + 1, j)) {
      ufPercolates.union(siteIndex, coord2index(i+1, j));
      ufFull.union(siteIndex, coord2index(i+1, j));
    }
  }
  /**
   * Checks whether site (row i, column j) is open. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out 
   * of [1, N] bounds
   */
  public boolean isOpen(int i, int j) {
    return grid[coord2index(i, j)];
  }

   /**
   * Checks whether site (row i, column j) is full. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out 
   * of [1, N] bounds
   */
  public boolean isFull(int i, int j) {
    return ufFull.connected(sourceIndex, coord2index(i, j));
  }

  /**
   * Checks whether the system percolates. 
   */
  public boolean percolates() {
    return ufPercolates.connected(sourceIndex, sinkIndex);
  }
  
  /**
   * Converts grid coordinates to the vector index.
   * @throws java.lang.IndexOutOfBoundsException if either i or j are 
   * out of [1, N] bounds
   */
  private int coord2index(int i, int j) {
    /* Validate input params */
    if (!(i >= 1 && i <= N))
      throw new IndexOutOfBoundsException("row is out of bounds"); 
    
    if (!(j >= 1 && j <= N))
      throw new IndexOutOfBoundsException("column is out of bounds");    
  
    return (i - 1) * N + (j - 1);
  }
}
