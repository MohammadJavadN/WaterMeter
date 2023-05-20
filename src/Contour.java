import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Contour {
    public static List<MatOfPoint> deleteZeros(List<MatOfPoint> contours){
        List<Double> areaList = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            areaList.add(Imgproc.contourArea(contour));
        }
        List<MatOfPoint> newContours = new ArrayList<>();
        int counter = 0;
        for (MatOfPoint contour : contours) {
            if (areaList.get(counter) != 0)
                newContours.add(contour);
            counter++;
        }
        return newContours;
    }
}
