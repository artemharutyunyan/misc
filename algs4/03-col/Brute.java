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

public class Brute {

  private static class HorizontalOrder implements Comparator<Point> {
    public int compare(Point first, Point second) {
      return first.compareTo(second);
    }
  }
  public static void main(String[] args) {

    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);

    /* Read the number of points; create input array */
    In in = new In(args[0]);    
    int nPoints = in.readInt();
    Point[] point = new Point[nPoints];

    /* Read the input array */
    int i;
    for (i = 0; i < nPoints; ++i) {
      int x = in.readInt();
      int y = in.readInt();
      point[i] = new Point(x, y);
      point[i].draw();
    }
    Arrays.sort(point, new HorizontalOrder());

    /* Brute force */ 
    for (i = 0; i < nPoints; ++i)
      for (int j = i + 1; j < nPoints; ++j)
        for (int k = j + 1; k < nPoints; ++k)
          for (int m = k + 1; m < nPoints; ++m) {
            double slopeJ = point[i].slopeTo(point[j]);
            double slopeK = point[i].slopeTo(point[k]);
            double slopeM = point[i].slopeTo(point[m]);
            if (slopeJ == slopeK && slopeJ == slopeM) {
              StdOut.printf("%s -> %s -> %s -> %s\n", 
                            point[i], point[j], point[k], point[m]);
              point[i].drawTo(point[m]);
            }
          }
  }
}

