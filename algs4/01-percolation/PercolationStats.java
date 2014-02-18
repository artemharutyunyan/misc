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

public class PercolationStats {
  private double nTrials;
  private double[] threshold;

  /**
  * Constructor. Performs T independent computational experiments on an N-by-N grid. 
  * @param N size of the grid 
  * @param T number of experiments 
  */
  public PercolationStats(int N, int T) {
     if (N < 1 || T < 1)
        throw new IllegalArgumentException("illegal input parameters");


    threshold = new double[T];
    nTrials = T;

    for (int i = 0; i < T; ++i) {
      int counter = 0;
      Percolation p = new Percolation(N);
    
      /* Start simulation by openning sites before the instance percolates */
      while (!p.percolates()) {
        int row = StdRandom.uniform(1, N + 1);
        int column = StdRandom.uniform(1, N + 1);
      
        if (!p.isOpen(row, column)) {
          //StdOut.printf("Openning row: %d column: %d\n", row, column);
          p.open(row, column);
          ++counter;
        }
      }

      //StdOut.printf("The system percolated at: %d\n", counter);
      threshold[i] = (double) counter / (N*N);
    }
  }

  /**
   * Sample mean percolation threshold 
   */
  public double mean() {
    return StdStats.mean(threshold);
  }

  /**
   * Sample standard deviation of percolation threshold 
   */
  public double stddev() {
    return StdStats.stddev(threshold);
  }

  /**
   * Lower bound of the 95% confidence interval
   */
  public double confidenceLo() {
    return mean() - (1.96*stddev()/Math.sqrt(nTrials));
  }

  /**
   * Upper bound of the 95% confidence interval
   */
  public double confidenceHi() {
    return mean() + (1.96*stddev()/Math.sqrt(nTrials));
  }

  public static void main(String[] args) {
    Percolation p;
    int N = Integer.parseInt(args[0]); // Size of the matrix 
    int T = Integer.parseInt(args[1]); // Number of trials 
    
    // Stopwatch t = new Stopwatch();
    PercolationStats ps = new PercolationStats(N, T);  
    // double elapsedTime = t.elapsedTime();

    StdOut.printf("mean                    = %f\n", ps.mean());
    StdOut.printf("stddev                  = %f\n", ps.stddev());
    StdOut.printf("95%% confidence interval = %f, %f\n", 
                   ps.confidenceLo(), 
                   ps.confidenceHi()); 
    // StdOut.printf("Elapsed time is: %f\n", elapsedTime);
  }
}
