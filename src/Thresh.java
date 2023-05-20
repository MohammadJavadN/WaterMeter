import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Thresh {
    public static Mat threshingImg(Mat img) {
        try {
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        } catch (Exception ignored) {
        }

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(2, 2));

        Mat topHat = new Mat();
        Imgproc.morphologyEx(img, topHat, Imgproc.MORPH_TOPHAT, kernel);

        Mat blackHat = new Mat();
        Imgproc.morphologyEx(img, blackHat, Imgproc.MORPH_BLACKHAT, kernel);

        Mat grayPlusTopHat = new Mat();
        Core.add(img, topHat, grayPlusTopHat);

        Mat grayPlusTopHatMinesBlackHat = new Mat();
        Core.subtract(grayPlusTopHat, blackHat, grayPlusTopHatMinesBlackHat);

        Mat blur = new Mat();
        Imgproc.medianBlur(grayPlusTopHatMinesBlackHat, blur, 5);

        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(blur, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 19, 5);

        return thresh;
    }
}