package dhi.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

/*
 * Assorted classes of utility functions required by the system.
 */
public class Utilities {
	
	public static Number[] getRandomNumbers(int size) {
		ArrayList<Number> dataList = new ArrayList<Number>();
		Random generator = new Random();
		while (dataList.size() < size) {
			int num = generator.nextInt(size);
			boolean isSame = false;
			for (int i = 0; i < dataList.size(); i++) {
				if ((int) dataList.get(i) == num) {
					isSame = true;
					break;
				}
			}

			if (!isSame) {
				dataList.add(num);
			}
		}

		Number[] result = new Number[dataList.size()];
		for (int i = 0; i < dataList.size(); i++) {
			result[i] = dataList.get(i);

		}

		return result;
	}
	
	public static double getDoubleRandom(double min, double max) {
		double random = new Random().nextDouble();
		double result = min + (random * (max - min));
		return result;
	}

	public static double[][] convertRowsAndCommaColumnsToDouble(List<String> itemList) {
		int rowCount = itemList.size();
		int columnCount = 0;
		if (rowCount > 0) {
			columnCount = itemList.get(0).split(",").length;
		}

		double[][] dataValues = new double[rowCount][columnCount];

		for (int i = 0; i < itemList.size(); i++) {
			String[] columnValues = itemList.get(i).split(",");
			for (int j = 0; j < columnValues.length; j++) {

				dataValues[i][j] = Double.parseDouble(columnValues[j]);
			}
		}

		return dataValues;
	}
	
	public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {		
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		byte buffer[] = new byte[8192];
		
		int read;	
		while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
			byteOutputStream.write(buffer, 0, read);
		}
	
		return byteOutputStream.toByteArray();
	}
	
	public static String serialize(Object model) {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(model);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return DatatypeConverter.printBase64Binary(byteOutputStream.toByteArray());
	}
	
	public static Object deSerialize(String model) {
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(model));
		ObjectInputStream objectInputStream = null;
		Object object = null;
		try {
			objectInputStream = new ObjectInputStream(byteInputStream);
			object = objectInputStream.readObject();
			objectInputStream.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return object;
	}

	public static String convertIntToTimeString(int totalSec)
	{
		int hour = (int)(totalSec / 60 / 60);
		int min = (int)(totalSec * 60) % 60;
		int sec = (int)totalSec % 60;
		
		return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
	}
	
	public static int convertTimeStringToInt(String timeForamt)
	{
		String[] times = timeForamt.split(":");		
		int totalSec = 0;
		
		if(times.length != 3)
			return totalSec;
		
		for(int i=0; i < times.length; i++) {
			
			if(i == 0)
				totalSec += Integer.parseInt(times[i]) * 3600;
			else if(i == 1)
				totalSec += Integer.parseInt(times[i]) * 60;
			else if(i == 2)
				totalSec += Integer.parseInt(times[i]);
		}
		
		return totalSec;
	}

	public static String ObjectToTimeString(Object obj, String format) {
		String resultString = "";
		if (obj != null)
			resultString = new SimpleDateFormat(format).format(obj);
		
		return resultString;
	}
	
	public static String ObjectToIntString(Object obj) {
		String resultString = "";
		if (obj != null) {
			if (obj.getClass().equals(Integer.class))
				resultString = String.valueOf(obj);
			else if (obj.getClass().equals(Double.class))
				resultString = String.valueOf((int)(double) obj);
		}
		return resultString;
	}
	
	public static String ObjectToString(Object obj) {
		String resultString = "";
		if (obj != null)
			resultString = String.valueOf(obj);
		
		return resultString;
	}
	
	public static Double ObjectToDouble(Object obj) {
		double resultValue = 0.0;
		if (obj != null) {
			if (obj.getClass().equals(Integer.class))
				resultValue = Double.parseDouble(String.valueOf(obj));
			else if (obj.getClass().equals(Double.class))
				resultValue = (double) obj;
		}

		return resultValue;
	}
	
	/**
	 * double 값에 대하여 소수점 digit 자리에서 반올림 하는 함수
	 * @param val
	 * @param digit
	 * @return double
	 */
	public static double round(double val, int digit) {
		double num = Math.pow(10d, digit);
		return (double)Math.round(val * num) / num;
	}
	
	/**
	 * double값을 Int로 반올림
	 * @param val
	 * @return String
	 */
	public static String roundIntToString(double val) {
		return String.format("%.0f", val);
	}
	
	/**
	 * double값을 소수점 1째자리까지 반올림 
	 * @param val
	 * @return
	 */
	public static String roundFirstToString(double val) {
		return String.format("%.1f", val);
	}
	
	/**
	 * double값을 소수점 2째자리까지 반올림
	 * @param val
	 * @return
	 */
	public static String roundSecondToString(double val) {
		return String.format("%.2f", val);
	}
	
	/**
	 * double값을 소수점 3째자리까지 반올림
	 * @param val
	 * @return
	 */
	public static String roundThreeToString(double val) {
		return String.format("%.3f", val);
	}	
	
	/**
	 * double값을 소수점 4째자리까지 반올림
	 * @param val
	 * @return
	 */
	public static String roundFourToString(double val) {
		return String.format("%.4f", val);
	}	
	
	/**
	 * 숫자의 자리수(digit)와 소수점 자리수(decimal)를 입력받아서 표시 
	 * @param val
	 * @return
	 */
	public static String roundToStringAuto(double val, int digit, int decimal) {
		String result = String.valueOf((int)val);
		if (result.length() >= digit) { 
			return result;
		} else {
			return String.format("%." + decimal + "f", val);
		}
	}	
	
	/**
	 * double 값을  min/max 범위에서 표시함 (min보다 작으면 min으로, max보다 크면 max로)
	 * @param val
	 * @param min
	 * @param max
	 * @return
	 */
	public static double valueOfDoubleRange(double val, double min, double max) {
		if(val <=  min)
			return min;
		else if (val >= max)
			return max;
		return val;
	}
	
	/**
	 * Exception 발생 시 StackTrace를 불러오는 API
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Exception e) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		e.printStackTrace(printStream);
		String message = "";
		try {
			message = byteArrayOutputStream.toString("UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		return message;
	}

	/**
	 * 입력받은 경로(path + \\ + fileName)에 data를 저장하는 API
	 * @param path
	 * @param fileName
	 * @param data
	 * @param isFileAppend
	 */
	public static void saveFile(String path, String fileName, String data, boolean isFileAppend) {
		String fullPath = path + "\\" + fileName;
		try {
			File file = new File(fullPath);
			FileWriter fw = new FileWriter(file, isFileAppend);
			fw.write(data);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void get()
	{
		
	}
	
	
	
	public static void main(String [] args) {
		double a = 123110.67354;
		System.out.println("round = " + Utilities.round(a, 3));
		System.out.println("roundToInt = " + Utilities.roundIntToString(a));
		System.out.println("round1st = " + Utilities.roundFirstToString(a));
		System.out.println("round2nd = " + Utilities.roundSecondToString(a));
		System.out.println("round3rd = " + Utilities.roundThreeToString(a));
		
	}
}
