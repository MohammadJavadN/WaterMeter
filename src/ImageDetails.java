import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class ImageDetails {
    Mat img;
    double x, y, w, h;
    double area;
    double cx, cy;
    double diagonal;
    double ratio;

    public ImageDetails(MatOfPoint contour, Mat img) {
        this.img = img;
        setCoordinate(Imgproc.boundingRect(contour));
        area = w * h;
        cx = (2 * x + w) / 2;
        cy = (2 * y + h) / 2;
        diagonal = Math.sqrt(w * w + h * h);
        ratio = (double) w / (double) h;
        Imgproc.getRectSubPix(img,new Size(w,h),new Point(cx,cy),img);
    }

    public void setCoordinate(Rect rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.h = rect.height;
        this.w = rect.width;
    }
}
