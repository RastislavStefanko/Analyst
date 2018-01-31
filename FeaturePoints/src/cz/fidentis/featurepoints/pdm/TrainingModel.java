/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.featurepoints.pdm;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import cz.fidentis.featurepoints.FacialPoint;
import cz.fidentis.featurepoints.FpModel;
import cz.fidentis.model.Model;
import java.util.List;
import javax.vecmath.Vector3f;

/**
 *
 * @author Rasto1
 */
public class TrainingModel {
    
    private double array[];
    private double array2[][];
    private int length;
   
    /**
     * compute mean shape from .csv shapes
     * @param trainingShapes list of training shapes
     * @param m model
     * @return mean shape as FpModel
     */
    public FpModel trainigModel(List trainingShapes, Model m) {

        FpModel meanShape = (FpModel) trainingShapes.get(0);

        meanShape.centralizeToModel(m);
        for (int i = 1; i < trainingShapes.size(); i++) {

            FpModel values = (FpModel) trainingShapes.get(i);

            values.centralizeToModel(m);

            for (int j = 0; j < values.getPointsNumber(); j++) {
                FacialPoint point = meanShape.getFacialPoints().get(j);

                meanShape.getFacialPoints().get(j).setCoords(new Vector3f(point.getPosition().x + values.getFacialPoints().get(j).getPosition().x,
                        point.getPosition().y + values.getFacialPoints().get(j).getPosition().y,
                        point.getPosition().z + values.getFacialPoints().get(j).getPosition().z));
            }

        }

        for (int j = 0; j < meanShape.getPointsNumber(); j++) {

            FacialPoint point = meanShape.getFacialPoints().get(j);

            meanShape.getFacialPoints().get(j).setCoords(new Vector3f(point.getPosition().x / trainingShapes.size(),
                    point.getPosition().y / trainingShapes.size(),
                    point.getPosition().z / trainingShapes.size()));
        }

        return meanShape;
    }

    /**
     * construct covariance matrix from mean shape and training shapes
     * @param meanShape 
     * @param trainingShapes
     * @param m model
     * @return covariance matrix
     */
    public Matrix covarianceMatrixCalculation(Matrix meanShape, List trainingShapes, Model m) {

        Matrix covariance = new Matrix(meanShape.getRowDimension(), meanShape.getRowDimension());

        for (int i = 0; i < trainingShapes.size(); i++) {
            FpModel values = (FpModel) trainingShapes.get(i);

            Matrix valuesMatrix = new Matrix(values.getPointsNumber(), 3);

            for (int j = 0; j < values.getPointsNumber(); j++) {
                valuesMatrix.set(j, 0, values.getFacialPoints().get(j).getPosition().x);
                valuesMatrix.set(j, 1, values.getFacialPoints().get(j).getPosition().y);
                valuesMatrix.set(j, 2, values.getFacialPoints().get(j).getPosition().z);
            }

            Matrix shapeSubtract = valuesMatrix.minus(meanShape);

            covariance = covariance.plus(shapeSubtract.times(shapeSubtract.transpose()));
        }

        double[][] tmp = covariance.getArray();

        if(trainingShapes.size() != 1){
        
        for (int j = 0; j < tmp.length; j++) {
            for (int k = 0; k < tmp[j].length; k++) {
                tmp[j][k] /= (trainingShapes.size() - 1);
            }
        }

        }
        return new Matrix(tmp);
    }

    /**
     * return first i-th eigenVectors from covariance matrix based on eigenvalue, where value(i) >= value(i+1)
     * @param i number of eigenVectors
     * @param covariance
     * @return first i-th matrix of eigenVectors
     */
    public Matrix getEigenVectors(int i, Matrix covariance) {
        EigenvalueDecomposition eigen = covariance.eig();
        double[][] vectors = eigen.getV().getArray();

        return new Matrix(vectors);
    }

    /**
     * return first i-th eigenValues from covariance matrix, where value(i) >= value(i+1)
     * @param i number of eigen values
     * @param covariance covariance matrix
     * @return first i-th matrix of eigen values
     */
    public Matrix[] getEigenValues(int i, Matrix covariance) {
        EigenvalueDecomposition eigen = covariance.eig();
        Matrix[] matr = new Matrix[2];
        double[] values = eigen.getRealEigenvalues();
        double[][] values2 = eigen.getV().transpose().getArray();
        sort(values, values2);

        double[][] eigenValues = new double[1][i];
        double[][] eigenVectors = new double[i][values2[0].length];
        int tmp = 0;

        for (int k = values.length - 1; k > values.length - 1 - i; k--) {

            eigenValues[0][tmp] = values[k];
            tmp++;
        }
        tmp = 0;
        for (int k = values2.length - 1; k > values2.length - 1 - i; k--) {
            for (int l = 0; l < values2[k].length; l++) {
                eigenVectors[tmp][l] = values2[k][l];
            }
            tmp++;
        }

        matr[0] = new Matrix(eigenValues);
        matr[1] = new Matrix(eigenVectors);

        return matr;
    }

    /////// help sorting methods
    
    public double distancePoints(Vector3f point1, Vector3f point2) {
        float x = point1.x - point2.x;
        float y = point1.y - point2.y;
        float z = point1.z - point2.z;

        return Math.sqrt(x * x + y * y + z * z);
    }

    public void sort(double[] inputArr, double[][] inputArr2) {

        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        array = inputArr;
        array2 = inputArr2;
        length = inputArr.length;
        quickSort(0, length - 1);
    }

    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        double pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
        // Divide into two arrays
        while (i <= j) {

            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j) {
            quickSort(lowerIndex, j);
        }
        if (i < higherIndex) {
            quickSort(i, higherIndex);
        }
    }

    private void exchangeNumbers(int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        double[] temp2 = array2[i];
        array2[i] = array2[j];
        array2[j] = temp2;
    }

}
