package googletest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class exel {	
	FileInputStream in = null;
	Workbook wb = null;
	private String filename_="testExample.xls";
	private int startGyo=7;//最初のデータが入っている行番号
	private int startRetsu=1;//最初のデータが入っている列番号
	//private int gyoSize=24;
	public exel() {
	}
	public exel(String filename) {
		filename_=filename;
	}
	public int getStartGyo(){
		return startGyo;
	}
	public int getStartRetsu(){
		return startRetsu;
	}
	public void setStartgyo(int a){
		startGyo=a;
	}
	/*
	 * セルに書き揉むメソッド
	 */
	public void makeCell(int x,int y,String value){
		try {
			in = new FileInputStream(filename_);
			wb = WorkbookFactory.create(in);
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (InvalidFormatException e) {
			System.out.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(y);
		Cell cell1 = row.createCell(x);
		cell1.setCellValue(value);
		FileOutputStream out = null;
	    try{
	      out = new FileOutputStream(filename_);
	      wb.write(out);
	    }catch(IOException e){
	      System.out.println(e.toString());
	    }finally{
	      try {
	        out.close();
	      }catch(IOException e){
	        System.out.println(e.toString());
	      }
	    }
		
	}
	public String getSEString(int x,int y){
		String nun;
		try {
			in = new FileInputStream("testExample.xls");
			wb = WorkbookFactory.create(in);
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (InvalidFormatException e) {
			System.out.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(y);
		if(row==null){
			return null;
		}
		Cell cell1 = row.getCell(x);
		int celltype=cell1.getCellType();
		if(celltype==Cell.CELL_TYPE_STRING) {

				nun= cell1.getStringCellValue();
		}else{
			return null;
		}
		return nun;
	}
	public int getSE(int x,int y){//エクセルの任意のセルのintを返すメソッド
		int nun=0;
		try {
			in = new FileInputStream("testExample.xls");
			wb = WorkbookFactory.create(in);
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (InvalidFormatException e) {
			System.out.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(y);
		Cell cell1 = row.getCell(x);
		int celltype=cell1.getCellType();
		//System.out.println(Cell.CELL_TYPE_NUMERIC);
		if(celltype==Cell.CELL_TYPE_NUMERIC) {

			if (DateUtil.isCellDateFormatted(cell1)) {
				// System.out.println("Date:" +
				// cell1.getDateCellValue());
			} else {
				// System.out.println("Numeric:" +
				// Math.round(cell1.getNumericCellValue()));
				nun= (int) Math.round(cell1.getNumericCellValue());
			}
		}else{
			return -1;
		}
		return nun;
	}

	public ArrayList<String> get(int gyo, int gyoend) {
		int gyoSize = gyoend - gyo;
		try {
			in = new FileInputStream("testExample.xls");
			wb = WorkbookFactory.create(in);
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (InvalidFormatException e) {
			System.out.println(e.toString());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		Sheet sheet = wb.getSheetAt(0);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < gyoSize; i++) {
			System.out.println(gyo-1+i);
			Row row = sheet.getRow(gyo-1+i);// i行を取得
			if (row == null) {
				return null;
				// row = sheet.createRow(retu);
			}
				Cell cell1 = row.getCell(startGyo);// 引数は列番号
				if (cell1 == null) {
					continue;
				}

				switch (cell1.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					// System.out.println("String:" +
					// cell1.getStringCellValue());
					list.add(cell1.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell1)) {
						// System.out.println("Date:" +
						// cell1.getDateCellValue());
					} else {
						// System.out.println("Numeric:" +
						// Math.round(cell1.getNumericCellValue()));
						list.add(String.valueOf(Math.round(cell1.getNumericCellValue())));
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.println("Boolean:" + cell1.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					System.out.println("Formula:" + cell1.getCellFormula());
					break;
				case Cell.CELL_TYPE_ERROR:
					System.out.println("Error:" + cell1.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_BLANK:
					System.out.println("Blank:");
					break;
				}
				
			}
		return list;
	}
}

