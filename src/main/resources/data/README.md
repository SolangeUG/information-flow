### FILES

- `facebook_ucsd.txt`

   This graph contains Facebook friendships between students at UCSD in 2005.
This data was originally stored in a Matlab sparse matrix; however, this was processed using Python to create a more suitable format for reading with Java.
The edges in this file are directed; however, each edge also appears in reverse order, making the final result undirected.
  - Source: [2005 Oxford Facebook Matrix](https://archive.org/details/oxford-2005-facebook-matrix)
  - Citation: Facebook data scrape related to paper [The Social Structure of Facebook Networks](https://arxiv.org/abs/1102.2166), by Amanda L. Traud, Peter J. Mucha, Mason A. Porter.

- `facebook_1000.txt` and `facebook_2000.txt`

   These are smaller versions of `facebook_ucsd.txt` that only contain the first 1000 or 2000 vertices. Note that `facebook_1000.txt` is fully connected while `facebook_2000.txt` is not. If you have an algorithm with a large runtime, it may be helpful to use these files instead of a larger one.


- `example.txt`

    This is a simple, small graph for testing purposes only.

In all of the files, each line represents an edge from the vertex with the first number to the vertex to the second.
