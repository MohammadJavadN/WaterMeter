import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessImage {

    public static ArrayList<ArrayList<ImageDetails>> firstAttempt(Mat gray) {
        System.out.println("gray = " + gray.size());


        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 5);
        System.out.println("blur = " + blur.size());

        Mat thresh = Thresh.threshingImg(blur);

        Mat th2 = thresh.clone();
        Mat th3 = thresh.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(th2, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        List<MatOfPoint> contours1 = Contour.deleteZeros(contours);
        ArrayList<ImageDetails> listOfImg = shredImg(contours1, th3);


        ArrayList<ImageDetails> newListOfImg = possibleChar(listOfImg);

        ArrayList<ArrayList<ImageDetails>> charInPlate = findListOfListsOfMatchingChar1(newListOfImg);

        if (!charInPlate.isEmpty()) {
            for (ArrayList<ImageDetails> imageDetails : charInPlate) {
                imageDetails.sort((o1, o2) -> (int) (o1.x - o2.x));// TODO: ۱۹/۰۹/۲۰۲۱
            }
            for (ArrayList<ImageDetails> i : charInPlate) {
                ArrayList<Boolean> isLinear = isLinear(i);

                for (Boolean aBoolean : isLinear) {
                    if (!aBoolean) {
                        charInPlate.remove(i);
                        break;
                    }
                }
            }
            return charInPlate;
        } else return null;
    }

    public static ArrayList<ArrayList<ImageDetails>> secondAttempt(Mat gray) {
        System.out.println("gray = " + gray.size());


        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 5);
        System.out.println("blur = " + blur.size());

        Mat thresh = Thresh.threshingImg(blur);

        Mat th2 = thresh.clone();
        Mat th3 = thresh.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(th2, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        List<MatOfPoint> contours1 = Contour.deleteZeros(contours);
        ArrayList<ImageDetails> listOfImg = shredImg(contours1, th3);


        ArrayList<ImageDetails> newListOfImg = possibleChar(listOfImg);

        ArrayList<ArrayList<ImageDetails>> charInPlate = findListOfListsOfMatchingChar2(newListOfImg);

        if (!charInPlate.isEmpty()) {
            for (ArrayList<ImageDetails> imageDetails : charInPlate) {
                imageDetails.sort((o1, o2) -> (int) (o1.x - o2.x));
            }
            for (ArrayList<ImageDetails> i : charInPlate) {
                ArrayList<Boolean> isLinear = isLinear(i);

                for (Boolean aBoolean : isLinear) {
                    if (!aBoolean) {
                        charInPlate.remove(i);
                        break;
                    }
                }
            }
            return charInPlate;
        } else return null;
    }

    public static ArrayList<ArrayList<ImageDetails>> thirdAttempt(Mat gray) {
        Mat blur = new Mat();
        Imgproc.medianBlur(gray, blur, 5);
        System.out.println("blur = " + blur.size());

        Mat thresh = Thresh.threshingImg(blur);

        Mat th2 = thresh.clone();
        Mat th3 = thresh.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(th2, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        List<MatOfPoint> contours1 = Contour.deleteZeros(contours);
        ArrayList<ImageDetails> listOfImg = shredImg(contours1, th3);


        ArrayList<ImageDetails> newListOfImg = possibleChar(listOfImg);

        ArrayList<ArrayList<ImageDetails>> charInPlate = findListOfListsOfMatchingChar3(newListOfImg);

        if (!charInPlate.isEmpty()) {
            for (ArrayList<ImageDetails> imageDetails : charInPlate) {
                imageDetails.sort((o1, o2) -> (int) (o1.x - o2.x));
            }
            for (ArrayList<ImageDetails> i : charInPlate) {
                ArrayList<Boolean> isLinear = isLinear(i);

                for (Boolean aBoolean : isLinear) {
                    if (!aBoolean) {
                        charInPlate.remove(i);
                        break;
                    }
                }
            }
            return charInPlate;
        } else return null;
    }

    public static ArrayList<ArrayList<ImageDetails>> fourthAttempt(Mat gray){
        // TODO: ۰۲/۱۱/۲۰۲۱
        return null;
    }


    public static ArrayList<ImageDetails> possibleChar(ArrayList<ImageDetails> listOfImg) {
        ArrayList<ImageDetails> newListOfImg = new ArrayList<>();
        for (ImageDetails img : listOfImg) {
            if (Param.MIN_WIDTH_HEIGHT_RATIO_FOR_NUMBER < (img.h / img.w) && (img.h / img.w) < Param.MAX_WIDTH_HEIGHT_RATIO_FOR_NUMBER)
                newListOfImg.add(img);
        }
        return newListOfImg;
    }

    public static ArrayList<ImageDetails> shredImg(List<MatOfPoint> contours, Mat img) {
        ArrayList<ImageDetails> imgList = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            imgList.add(new ImageDetails(contour, img.clone()));
        }
        return imgList;
    }

    static ArrayList<Boolean> isLinear(ArrayList<ImageDetails> listOfNumbers) {
        HashMap<Integer, Double> keyValue = new HashMap<>();
        for (int i = 0; i < listOfNumbers.size(); i++) {
            keyValue.put(i, listOfNumbers.get(i).cx);
        }
        ArrayList<Boolean> booleanList = new ArrayList<>();
        double a = Math.abs(keyValue.get(0) - keyValue.get(1));
        double x0 = keyValue.get(0);

        for (int i = 0; i < listOfNumbers.size(); i++) {
            if (Param.MIN_ERROR_VERIFICATION * keyValue.get(i) <= a * i + x0 && a * i + x0 <= Param.MAX_ERROR_VERIFICATION * keyValue.get(i)) {
                booleanList.add(true);
            } else booleanList.add(false);
        }
        return booleanList;
    }

    static ArrayList<ArrayList<ImageDetails>> findListOfListsOfMatchingChar1(ArrayList<ImageDetails> listOfPossibleChar) {
        ArrayList<ArrayList<ImageDetails>> listOfListsOfMatchingChar = new ArrayList<>();

        for (ImageDetails possibleChar : listOfPossibleChar) {
            ArrayList<ImageDetails> listOfMatchingChar = findListOfMatchingChar1(possibleChar, listOfPossibleChar);
            listOfMatchingChar.add(possibleChar);
            if (listOfMatchingChar.size() < Param.MIN_NUMBER_OF_MATCHING_CHARS_1_2)
                continue;
            listOfListsOfMatchingChar.add(listOfMatchingChar);
        }
        return listOfListsOfMatchingChar;
    }

    static ArrayList<ImageDetails> findListOfMatchingChar1(ImageDetails possibleChar, ArrayList<ImageDetails> listOfPossibleChar) {
        ArrayList<ImageDetails> listOfMatchingChar = new ArrayList<>();
        for (ImageDetails possibleMatchingChar : listOfPossibleChar) {
            if (possibleMatchingChar.equals(possibleChar))
                continue;
            double distanceBetweenChars = distanceBetweenChars(possibleChar, possibleMatchingChar);
            double angleBetweenChars = angleBetweenChars(possibleChar, possibleMatchingChar);

            float changeInArea = (float) ((float) Math.abs(possibleMatchingChar.area - possibleChar.area) / possibleChar.area);
            float changeInWidth = (float) ((float) Math.abs(possibleMatchingChar.w - possibleChar.w) / possibleChar.w);
            float changeInHeight = (float) ((float) Math.abs(possibleMatchingChar.h - possibleChar.h) / possibleChar.h);

            if (distanceBetweenChars < (possibleChar.diagonal * Param.MAX_DIAG_SIZE_MULTIPLE_AWAY_1) &&
                    angleBetweenChars < Param.MAX_ANGLE_BETWEEN_CHARS_1 &&
                    changeInArea < Param.MAX_CHANGE_IN_AREA_1 &&
                    changeInWidth < Param.MAX_CHANGE_IN_WIDTH_1 &&
                    changeInHeight < Param.MAX_CHANGE_IN_HEIGHT_1)
                listOfMatchingChar.add(possibleMatchingChar);
        }
        return listOfMatchingChar;
    }

    static ArrayList<ArrayList<ImageDetails>> findListOfListsOfMatchingChar2(ArrayList<ImageDetails> listOfPossibleChar) {
        ArrayList<ArrayList<ImageDetails>> listOfListsOfMatchingChar = new ArrayList<>();

        for (ImageDetails possibleChar : listOfPossibleChar) {
            ArrayList<ImageDetails> listOfMatchingChar = findListOfMatchingChar2(possibleChar, listOfPossibleChar);
            listOfMatchingChar.add(possibleChar);
            if (listOfMatchingChar.size() < Param.MIN_NUMBER_OF_MATCHING_CHARS_1_2)
                continue;
            listOfListsOfMatchingChar.add(listOfMatchingChar);
        }
        return listOfListsOfMatchingChar;
    }

    static ArrayList<ImageDetails> findListOfMatchingChar2(ImageDetails possibleChar, ArrayList<ImageDetails> listOfPossibleChar) {
        ArrayList<ImageDetails> listOfMatchingChar = new ArrayList<>();
        for (ImageDetails possibleMatchingChar : listOfPossibleChar) {
            if (possibleMatchingChar.equals(possibleChar))
                continue;
            double distanceBetweenChars = distanceBetweenChars(possibleChar, possibleMatchingChar);
            double angleBetweenChars = angleBetweenChars(possibleChar, possibleMatchingChar);

            float changeInArea = (float) ((float) Math.abs(possibleMatchingChar.area - possibleChar.area) / possibleChar.area);
            float changeInWidth = (float) ((float) Math.abs(possibleMatchingChar.w - possibleChar.w) / possibleChar.w);
            float changeInHeight = (float) ((float) Math.abs(possibleMatchingChar.h - possibleChar.h) / possibleChar.h);

            if (distanceBetweenChars < (possibleChar.diagonal * Param.MAX_DIAG_SIZE_MULTIPLE_AWAY_2) &&
                    angleBetweenChars < Param.MAX_ANGLE_BETWEEN_CHARS_2 &&
                    changeInArea < Param.MAX_CHANGE_IN_AREA_2 &&
                    changeInWidth < Param.MAX_CHANGE_IN_WIDTH_2 &&
                    changeInHeight < Param.MAX_CHANGE_IN_HEIGHT_2)
                listOfMatchingChar.add(possibleMatchingChar);
        }
        return listOfMatchingChar;
    }

    static ArrayList<ArrayList<ImageDetails>> findListOfListsOfMatchingChar3(ArrayList<ImageDetails> listOfPossibleChar) {
        ArrayList<ArrayList<ImageDetails>> listOfListsOfMatchingChar = new ArrayList<>();

        for (ImageDetails possibleChar : listOfPossibleChar) {
            ArrayList<ImageDetails> listOfMatchingChar = findListOfMatchingChar2(possibleChar, listOfPossibleChar);
            listOfMatchingChar.add(possibleChar);
            if (listOfMatchingChar.size() < Param.MIN_NUMBER_OF_MATCHING_CHARS_3)
                continue;
            listOfListsOfMatchingChar.add(listOfMatchingChar);
        }
        return listOfListsOfMatchingChar;
    }

    static double distanceBetweenChars(ImageDetails firstChar, ImageDetails secondChar) {
        double x = Math.abs(firstChar.cx - secondChar.cx);
        double y = Math.abs(firstChar.cy - secondChar.cy);
        return Math.sqrt(x * x + y * y);
    }

    static double angleBetweenChars(ImageDetails firstChar, ImageDetails secondChar) {
        double angleInRad;
        float adj = (float) Math.abs(firstChar.cx - secondChar.cx);
        float opp = (float) Math.abs(firstChar.cy - secondChar.cy);

        if (adj != 0.0)
            angleInRad = Math.atan(opp / adj);
        else angleInRad = 1.5708;

        return angleInRad * (180 / Math.PI);
    }
}
