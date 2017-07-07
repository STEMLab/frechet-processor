package visualization;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import goLA.compute.SimpleFrechet;
import goLA.data.SE_Two_Rtree;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.Trajectory;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Azamat on 7/3/2017.
 */
public class FrechetDistanceDemo2D {

    private static String TEST_DATA_SET_PATH = "dataset.txt";

    public static void main(String[] args) throws Exception {

        Manager manager = new ManagerImpl(new SimpleFrechet(), new SE_Two_Rtree(), new DataImporter());


        System.out.print("Data loading... ");

        Instant start = Instant.now();
        HashSet<Trajectory> trajectories = manager.makeStructureToVisualize(TEST_DATA_SET_PATH);
        Instant end = Instant.now();

        System.out.println(Duration.between(start, end));

        MapContent map = new MapContent();
        map.setTitle("Data visualize");

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureType TYPE = DataUtilities.createType("frechet", "line", "the_geom:LineString");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder((SimpleFeatureType) TYPE);

        DefaultFeatureCollection lineCollection = new DefaultFeatureCollection();

        System.out.print("Drawing... ");
        Instant drawStart = Instant.now();
        for (Trajectory trajectory : trajectories) {
            Coordinate[] coords = new Coordinate[trajectory.getCoordinates().size()];
            for (int i = 0; i < trajectory.getCoordinates().size(); i++) {
                coords[i] = new Coordinate(trajectory.getCoordinates().get(i).getPointX(), trajectory.getCoordinates().get(i).getPointY());
            }
            LineString line = geometryFactory.createLineString(coords);
            featureBuilder.add(line);
            SimpleFeature feature = featureBuilder.buildFeature(trajectory.getName());
            lineCollection.add(feature);
        }

        Instant drawFinish = Instant.now();
        System.out.println(Duration.between(drawStart, drawFinish));

        Style style = SLD.createLineStyle(Color.BLUE, 1);
        Layer layer = new FeatureLayer(lineCollection, style);
        map.addLayer(layer);

        trajectories.clear();
        JMapFrame.showMap(map);
    }

    public static void vis(List<Trajectory> trajectories, Color c1, Color c2){
        System.out.print("Data loading... \n");

        MapContent map = new MapContent();
        map.setTitle("Data visualize");

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureType TYPE = null;
        try {
            TYPE = DataUtilities.createType("frechet", "line", "the_geom:LineString");
        } catch (SchemaException e) {
            e.printStackTrace();
        }
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder((SimpleFeatureType) TYPE);

        DefaultFeatureCollection lineCollection = new DefaultFeatureCollection();

        System.out.print("Drawing... \n");
        int index = 0;
        for (Trajectory trajectory : trajectories) {
            Coordinate[] coords = new Coordinate[trajectory.getCoordinates().size()];
            for (int i = 0; i < trajectory.getCoordinates().size(); i++) {
                coords[i] = new Coordinate(trajectory.getCoordinates().get(i).getPointX(), trajectory.getCoordinates().get(i).getPointY());
            }
            LineString line = geometryFactory.createLineString(coords);
            featureBuilder.add(line);
            SimpleFeature feature = featureBuilder.buildFeature(trajectory.getName());
            lineCollection.add(feature);

            if (index % 2 == 0){
                Style style = SLD.createLineStyle(c1, 1);
                Layer layer = new FeatureLayer(lineCollection, style);
                map.addLayer(layer);
            }
            else{
                Style style = SLD.createLineStyle(c2, 10);
                Layer layer = new FeatureLayer(lineCollection, style);
                map.addLayer(layer);
            }

            index ++;
        }



        //trajectories.clear();
        JMapFrame.showMap(map);
    }

}
