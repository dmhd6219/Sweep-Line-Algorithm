# Sweep-Line-algorithm
Implement Sweep line algorithm using the sorting algorithm and self-balanced binary search tree you implemented in other parts of this homework. Write a program that given a collection of line segments detects if any two intersect.

**Input format.** First line of the input contains a number *N* (*0* < *N* < *10^6*). Each of the next *N* lines contains a description of a segment *PQ*, consisting of 4 coordinates separated by spaces: *xP yP xQ yQ*. Coordinates are all integers (*−2^21* ≤ *x*, *y* ≤ *2^21*).
Example:
```
5
43 91 42 96
38 93 37 96
25 91 34 90
46 101 42 92
27 99 34 93
```
**Output format.** If there are no intersections, output *NO INTERSECTIONS*. Otherwise, output *INTERSECTION* on the first line and any two segments that intersect on the second and third lines, using the same format as input.
Example (for the input example above):
```
INTERSECTION
43 91 42 96
46 101 42 92
```
