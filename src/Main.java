import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        SVMTrain.train();
        Mat cropped = ReadImage.remBlack(ReadImage.remBlue(ReadImage.remRed(ReadImage.readImg())));
        HighGui.imshow("meter", cropped);

        Mat gray =new Mat();
        Imgproc.cvtColor(cropped,gray,Imgproc.COLOR_BGR2GRAY);

//        Mat blur = new Mat();
//        Imgproc.medianBlur(gray, blur,5);


        Mat image = new Mat();
        ArrayList<ArrayList<ImageDetails>> charInPlate = ProcessImage.firstAttempt(gray);
        if (charInPlate == null) {
            charInPlate = ProcessImage.secondAttempt(gray);
        }if (charInPlate == null) {
            charInPlate = ProcessImage.thirdAttempt(gray);
        }// TODO: ۲۰/۰۹/۲۰۲۱ forthAttempt 
        HighGui.waitKey(0);
    }
}
