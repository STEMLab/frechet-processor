package goLA.utils;

import goLA.model.Coordinate;

/**
 * Created by Azamat on 7/4/2017.
 */
public class EuclideanDistance {
    public static double pointAndLine(Coordinate point, Coordinate lineStart, Coordinate lineEnd) {
        double lineLen = distance(lineStart, lineEnd);
        if (lineLen == 0) return distance(point, lineStart);
        double prj = ((point.getPointX() - lineStart.getPointX()) * (lineEnd.getPointX() - lineStart.getPointX()) +
                (point.getPointY() - lineStart.getPointY()) * (lineEnd.getPointY() - lineStart.getPointY())) / lineLen;
        if (prj < 0) return distance(point, lineStart);
        else if (prj > lineLen) return distance(point, lineEnd);
        else
            return Math.abs((-1) * (point.getPointX() - lineStart.getPointX()) * (lineEnd.getPointY() - lineStart.getPointY()) +
                    (point.getPointY() - lineStart.getPointY()) * (lineEnd.getPointX() - lineStart.getPointX())) / lineLen;
    }

    public static double distance(Coordinate start, Coordinate end) {
        return Math.sqrt(Math.pow(end.getPointX() - start.getPointX(), 2) + Math.pow(end.getPointY() - start.getPointY(), 2));
    }
}
