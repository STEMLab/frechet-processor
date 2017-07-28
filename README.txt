## How to compile the program

Go to project folder and compile using maven:

$ mvn package

## How to run

In target directory of project folder you will find run.jar. Run using line command:

$ java -jar run.jar {path to dataset.txt} {path to queries.txt} {path to result folder}

## Main Idea

In order to process queries to find trajectories in given Fréchet distance bounds, our solution use Fréchet distance decision algorithm
, R*-index indexing for trajectory start points, Douglas Peucker algorithm for line simplification, and discrete Fréchet distance decision algorithm too.
Some useful theory was applied to project :

* Fréchet distance between two trajectories is equal or longer than both distances between start points and end points.
* After simplification, Fréchet distance is bounded by simplification parameter.
* Discrete Fréchet distance must be equal or longer than normal Fréchet distance.
