/**
 * AnySerialize
 * 
 * @author jeeun.moon
 * @since 2015.04.28
 * @version 1.0
 */

package dhi.optimizer.algorithm.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class AnySerialize {
	public static void serialize(Object obj, String fileName) {
		FileOutputStream fileStream; 
		try {
			fileStream = new FileOutputStream(fileName);
			ObjectOutputStream os;
			os = new ObjectOutputStream(fileStream);
			os.writeObject(obj);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object deSerialize(String fileName) {
		FileInputStream fileInStream;
		Object obj = null;
		try {
			fileInStream = new FileInputStream(fileName);
			ObjectInputStream ois;
			ois = new ObjectInputStream(fileInStream);
			obj = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}	
	
	/*
	 * 0.0,0.0;0.0,0.0
	 */
	public static String convertToString(double[][] dVals) {
		String temp = "";
		int rows = dVals.length;
		int cols = dVals[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				temp += dVals[i][j] + ((j < (cols - 1)) ? "," : "");
			}

			temp += ((i < (rows - 1)) ? ";" : "");
		}

		return temp;
	}

	/*
	 * 0.0,0.0;0.0,0.0
	 */
	public static double[][] convertToDouble(String sVals) {
		double[][] temp = null;
		String[] splitRows = null;
		String[] splitCols = null;
		int rows = 0;
		int cols = 0;

		splitRows = sVals.split(";");
		rows = splitRows.length;
		temp = new double[rows][];
		for (int i = 0; i < rows; i++) {
			splitCols = splitRows[i].split(",");
			cols = splitCols.length;
			temp[i] = new double[cols];
			for (int j = 0; j < cols; j++) {
				temp[i][j] = Double.parseDouble(splitCols[j]);
			}
		}

		return temp;
	}	
	
	
//	public static String serialize(Object o) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ObjectOutputStream oos;
//		try {
//			oos = new ObjectOutputStream(baos);
//			oos.writeObject(o);
//			oos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return DatatypeConverter.printBase64Binary(baos.toByteArray());
//	}
//
//	public static Object deSerialize(String s) {
//		ByteArrayInputStream bais = new ByteArrayInputStream(
//				DatatypeConverter.parseBase64Binary(s));
//		ObjectInputStream ois = null;
//		Object o = null;
//		try {
//			ois = new ObjectInputStream(bais);
//			o = ois.readObject();
//			ois.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return o;
//	}
}
