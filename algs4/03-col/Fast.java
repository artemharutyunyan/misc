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

import java.util.Comparator;
import java.util.Arrays;
import java.util.TreeSet;

public class Fast {

  /* Going to hold arrays which have already been printed and drawn*/
  private static TreeSet<String> cache = new TreeSet<String>(); 

  /* Comparator for sorting Point arrays in lexicographic order */
  private static class HorizontalOrder implements Comparator<Point> {
    public int compare(Point first, Point second) {
      return first.compareTo(second);
    }
  }

  /* Printing and drawing array */
  private static void outputArray(Point[] point) {
    for (int i = 0; i < point.length; ++i) {
      if (i != 0) { 
        StdOut.printf(" -> ");
      }
      StdOut.printf("%s", point[i]);
    }
    StdOut.printf("\n");
    point[0].drawTo(point[point.length - 1]);
  }

  /* Entry point */
  public static void main(String[] args) {

    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);

    /* Read the number of points; create input array */
    In in = new In(args[0]);    
    int nPoints = in.readInt();
    Point[] point = new Point[nPoints];

    /* Need to restore the reference array after every sort */
    Point[] reference = new Point[nPoints]; 

    /* Read the input array */
    int i;
    for (i = 0; i < nPoints; ++i) {
      int x = in.readInt();
      int y = in.readInt();
      reference[i] = new Point(x, y);
      reference[i].draw();
    }

    /* Reference array needed for iteration */
    Arrays.sort(reference, new HorizontalOrder());

    /* Fast algorithm */ 
    double[] slope = new double[nPoints];
    for (i = 0; i < nPoints; ++i) {
      /* Copy even for the first iteration, otherwise have to sort the input twice */
      for (int j = 0; j < nPoints; ++j) 
        point[j] = reference[j];

      /* Sort array wrt i-th element */
      Arrays.sort(point, reference[i].SLOPE_ORDER);

      /* Calculate all the slope values */ 
      for (int j = 0; j < nPoints; ++j) {
        slope[j] = reference[i].slopeTo(point[j]);
      }

      /* Find (long enough) groups of collinear points */
      double currentSlope = slope[1];
      int count = 1;

      /* Extra cycle for when the last point needs to be included */
      for (int j = 2; j <= nPoints; ++j) {

        /* Make sure we always enter the else branch at the end */
        if (j < nPoints && currentSlope == slope[j])
          ++count;
        else { 
          if (count >= 3) {
            /* Create a temporary array and sort it */
            Point[] tmp = new Point[count + 1];
            
            for (int k = 0; k < count; ++k) { 
              tmp[k] = point[j - k - 1];
            }
            tmp[count] = point[0];

            Arrays.sort(tmp, new HorizontalOrder());

            /* Check whether we've seen it before */
            
            if (!cache.contains(tmp[0].toString() + tmp[1].toString())) {
              /* Output and record for future reference */
              cache.add(tmp[0].toString() + tmp[1].toString());
              outputArray(tmp);
            }
          }

          count = 1;
          if (j < nPoints) 
            currentSlope = slope[j];
        }
      }
    }
  }
}

