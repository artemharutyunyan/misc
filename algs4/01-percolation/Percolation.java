
public class Percolation {
  private boolean[]  grid; // grid of sites 
  private QuickFindUF uf;  // union-find object 
  private int N; // Size of a grid 
  private int sourceIndex; // Index of the source site 
  private int sinkIndex; // Index of the sink site
  
  /**
   * Constructor. Creates and N-by-N grid, with all sites blocked. 
   * @param N size of a grid
   */
  public void Percolation(int N) {
    /* Initialize the grid */
    grid = new boolean[N*N];    
    for (int i = 0; i < N*N; ++i)
      grid[i] = false;
    
    /* Initialize the union-find object. We're going to use the 2 extra 
     * elements as a source and a sink. */
    uf = new QuickFindUF(N*N + 2); 
    sourceIndex = N*N;
    sinkIndex = N*N + 1;
  }
  
  /**
   * Opens the site (row i, column j) if it is not already. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out of [1, N] bounds  
   */
  public void open(int i, int j) {
    /* Open the site itself */
    int siteIndex = coord2index(i, j);
    grid[siteIndex] = true;
    
    /* Connect this site to open adjacent sites */
    
    /* Up */
    if ( i == 1) 
      uf.union(siteIndex, sourceIndex); // Union with source 
    else if (isOpen(i - 1, j))
      uf.union(siteIndex, coord2index(i-1, j));
    
    /* Left */
    if (j != 1 && isOpen(i, j - 1))
      uf.union(siteIndex, coord2index(i, j - 1));
    
    /* Right */
    if (j != N && isOpen(i, j + 1))
      uf.union(siteIndex, coord2index(i, j + 1));
    
    /* Down */
    if (j == N)  
      uf.union(siteIndex, sinkIndex); // Union with sink
    else if (isOpen(i + 1, j))
      uf.union(siteIndex, sinkIndex);    
  }
  /**
   * Checks whether site (row i, column j) is open. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out of [1, N] bounds
   */
  public boolean isOpen(int i, int j) {
    return grid[coord2index(i, j)];
  }

   /**
   * Checks whether site (row i, column j) is full. 
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out of [1, N] bounds
   */
  public boolean isFull(int i, int j) {
    return !grid[coord2index(i, j)];
  }

  /**
   * Checks whether the system percolates. 
   */
  public boolean percolates() {
    return uf.connected(sourceIndex, sinkIndex);
  }
  
  /**
   * Converts grid coordinates to the vector index.
   * @throws java.lang.IndexOutOfBoundsException if either i or j are out of [1, N] bounds
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