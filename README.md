# STEM Fréchet Processor
[ACM SIGSPATIAL CUP 2017](http://sigspatial2017.sigspatial.org/giscup2017/)

## Developers

Dongmin Kim, Pusan National University


Bolat Azamat, Pusan National University


## Library

ELKI Data Mining Toolkit https://elki-project.github.io/


## How to run

1. Go to project folder and compile using maven:

```
    $ mvn package
```

2. In target directory of project folder you will find run.jar. Run using line command:

```
    $ java -jar run.jar {path to dataset.txt} {path to queries.txt} {path to result folder}
```

## Main Idea

In order to process queries to find trajectories in given Fréchet distance bounds, our solution use Fréchet distance decision algorithm
, R*-tree indexing for trajectory start points, Douglas Peucker algorithm for line simplification, and discrete Fréchet distance decision algorithm too.
Some useful theory was applied to project : 

* Fréchet distance between two trajectories is equal or longer than both distances between start points and end points.
* After simplification, Fréchet distance is bounded by simplification parameter.
* Discrete Fréchet distance must be equal or longer than normal Fréchet distance.

## References 
   - Alt, Helmut; Godau, Michael (1995), "Computing the Fréchet distance between two polygonal curves"
   - Beckmann, N.; Kriegel, H. P.; Schneider, R.; Seeger, B. (1990). "The R*-tree: an efficient and robust access method for points and rectangles"