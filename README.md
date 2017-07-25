# goLA
[ACM SIGSPATIAL CUP 2017](http://sigspatial2017.sigspatial.org/giscup2017/)

## Developers

Dongmin Kim, Pusan National University


Bolat Azamat, Pusan National University


## Library

ELKI


## How to run

1. Go to project folder and compile using maven:

```
    $ mvn package
```

2. In target directory of project folder you will find run.jar. Run using line command:

```
    java -jar run.jar {path to dataset.txt} {path to queries.txt} {path to result folder}
```

## Main Idea

In order to process given queries which try to find trajectories, we used not only frechet distance decision but also R*-tree about points, Douglas Peucker simplification, discrete frechet distance decision.
We applied some useful theory to our project : 

* Frechet distance between two trajectories is equal or longer than both distances which are between start points and between end points.
* After simplification, frechet distance is bounded by simplification parameter.
* Discrete frechet distance must be equal or longer than normal frechet distance.


## References
