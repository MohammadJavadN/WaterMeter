import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

import java.util.Arrays;

public class SVMTrain {

    public static boolean debug = false;

    public static void train() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Starting training process");

/*
        int[][] dataFromFile = new int[1821][400];// TODO: ۱۸/۰۹/۲۰۲۱
        File dataFile = new File("data.txt");
        if (dataFile.exists()) {
            try {
                int lineNum = 0;
                Scanner scanner = new Scanner(dataFile);
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    String[] lineSplit = line.split(" ");
                    for (int i = 0; i < lineSplit.length; i++) {
                        dataFromFile[(lineNum)][i] = parseInt(lineSplit[i]);
                    }
                    lineNum++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Mat mat = new Mat(1821,400, CvType.CV_32F);
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                mat.put(i,j, dataFromFile[i][j]);
            }
        }
        if (debug) {
            System.out.println(mat.depth());
            System.out.println(mat.channels());
            System.out.println(mat.type());
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < mat.cols(); j++) {
                    System.out.print(Arrays.toString(mat.get(i, j)) + ", ");
                }
                System.out.println();
            }
        }

        Imgcodecs.imwrite("sample.png",mat);
*/


        SVM svm = setParameter();

        Mat trainData = Imgcodecs.imread("trainData.png");
        if (debug) {
            showMat(trainData, 0, 1);
        }
        Imgproc.cvtColor(trainData, trainData, Imgproc.COLOR_RGB2GRAY);
        trainData.convertTo(trainData, CvType.CV_32F);

//        Mat sample = Imgcodecs.imread("6.jpg");
//        Imgproc.cvtColor(sample, sample, Imgproc.COLOR_RGB2GRAY);
//        sample = sample.reshape(0, 1);
//        sample.convertTo(sample, CvType.CV_32F);

        Mat labels = new Mat(1821, 1, CvType.CV_32S);
        for (int i = 0; i < 10; i++) {
            labels.rowRange(i * 100, (i + 1) * 100).setTo(new Scalar(i));
        }
        labels.rowRange(1000, 1821).setTo(new Scalar(10));
        if (debug) {
            showMat(labels, 0, labels.rows());
        }


        svm.train(trainData, Ml.ROW_SAMPLE, labels);

//        System.out.println(svm.predict(sample));
    }

    static SVM setParameter() {
        org.opencv.ml.SVM svm = org.opencv.ml.SVM.create();
        svm.setType(org.opencv.ml.SVM.C_SVC);
        svm.setC(1);
        svm.setKernel(SVM.POLY);
        svm.setDegree(3);
        svm.setGamma(0.001);
        return svm;
    }

    public static void showMat(Mat mat, int startRow, int endRow) {
        System.out.println(mat.depth());
        System.out.println(mat.channels());
        System.out.println(mat.type());
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < mat.cols(); j++) {
                System.out.print(Arrays.toString(mat.get(i, j)) + ", ");
            }
            System.out.println();
        }

    }

   /* static int parseInt(String s) {
        int e = Integer.parseInt(s.split("\\+")[1]);
        String number = s.split("\\.")[0] + s.split("\\.")[1].split("e")[0].substring(0, e);
        return Integer.parseInt(number);
    }*/
}