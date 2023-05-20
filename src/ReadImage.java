import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReadImage {
    static Mat readImg() {
        Mat img = Imgcodecs.imread("1.jpg");
        Mat resizedImg = new Mat();
        Imgproc.resize(img,resizedImg, new Size(290, 95));
        return resizedImg;
    }

    static Mat remRed(Mat img) {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2HSV);

        Mat mask1 = new Mat();
        Core.inRange(imgHSV, new Scalar(0, 50, 100), new Scalar(5, 255, 255), mask1);

        Mat mask2 = new Mat();
        Core.inRange(imgHSV, new Scalar(175, 50, 20), new Scalar(180, 255, 255), mask2);

        Mat mask = new Mat();
        Core.bitwise_or(mask1, mask2, mask);

        Mat cropped = new Mat();
        Core.bitwise_and(img, img, cropped, mask);

        Mat gray = new Mat();
        Imgproc.cvtColor(cropped, gray, Imgproc.COLOR_HSV2BGR);
        Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 7);

        Mat thresh = new Mat();
        Imgproc.threshold(blur, thresh, 0, 255, Imgproc.THRESH_OTSU);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Optional<MatOfPoint> largest = contours.stream().max((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));
        contours.sort((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));
        try {
            Rect rect = Imgproc.boundingRect(Objects.requireNonNull(largest.get()));
            Imgproc.getRectSubPix(img, new Size(rect.x, thresh.height()), new Point(rect.x / 2, thresh.height() / 2), cropped);
            return cropped;
        } catch (Exception e) {
            return img;
        }
    }

    static Mat remBlue(Mat img) {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2HSV);

        Mat mask = new Mat();
        Core.inRange(imgHSV, new Scalar(80, 50, 20), new Scalar(140, 255, 255), mask);

        Mat cropped = new Mat();
        Core.bitwise_and(img, img, cropped, mask);

        Mat gray = new Mat();
        Imgproc.cvtColor(cropped, gray, Imgproc.COLOR_HSV2BGR);
        Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 5);

        Mat thresh = new Mat();
        Imgproc.threshold(blur, thresh, 0, 255, Imgproc.THRESH_OTSU);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(thresh, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Optional<MatOfPoint> largest = contours.stream().max((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));
        contours.sort((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));


        try {
            Rect rect = Imgproc.boundingRect(Objects.requireNonNull(largest.get()));
            Imgproc.getRectSubPix(img, new Size(rect.x, thresh.height()), new Point(rect.x / 2, thresh.height() / 2), cropped);
            return cropped;
        } catch (Exception e) {
            return img;
        }
    }

    static Mat remBlack(Mat img) {

        Mat imgHSV = new Mat();
        Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2HSV);

        Mat mask = new Mat();
        Core.inRange(imgHSV, new Scalar(0, 0, 0), new Scalar(255, 255, 30), mask);

        Mat blur = new Mat();
        Imgproc.medianBlur(mask, blur, 13);
//        HighGui.imshow("blur1",blur);

        int k = 5, p = 4;
        Mat dilate = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(k, k), new Point(p,p));
        Imgproc.dilate(blur, dilate,kernel,new Point(p,p),40);

//        HighGui.imshow("dilate",dilate);


        Mat blur2 = new Mat();

        Imgproc.medianBlur(dilate, blur2, 15);
//        HighGui.imshow("blur2",blur2);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(blur2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Optional<MatOfPoint> largest = contours.stream().max((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));
        contours.sort((c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));

        Mat cropped = new Mat();

        try {
            Rect rect = Imgproc.boundingRect(Objects.requireNonNull(largest.get()));
            if (rect.x > img.width() / 2 && rect.height / rect.width < 2.5 && img.height() * (1 / 3) < rect.height) {
                Imgproc.getRectSubPix(img, new Size(rect.x, img.height()), new Point(rect.x / 2, img.height() / 2), cropped);
                return cropped;
            }else {
                return img;
            }
        } catch (Exception e) {
            return img;
        }


    }

}
