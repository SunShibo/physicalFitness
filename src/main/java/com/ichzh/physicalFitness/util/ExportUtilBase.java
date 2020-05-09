package com.ichzh.physicalFitness.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.ichzh.physicalFitness.conf.Constant;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportUtilBase {

	protected static void writeFile2Out(File file,ServletOutputStream os){
		try {
			FileInputStream inputStream = new FileInputStream(file);

			int b = 0;
			byte[] buffer = new byte[512];
			while ((b = inputStream.read(buffer)) != -1){
				os.write(buffer,0,b);
			}
			inputStream.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try
			{
				os.close();
			}
			catch(Exception ex) {
				//不用处理
			}
		}
	}
	
    protected static PdfPTable specialWidthTableByTemplateType(int type,float[] widths){
        if (Constant.PDF_TEMPLATE_KIND_ONE == type){
            widths[1] = widths[1]*0.9f;
            widths[2] = widths[2]*0.9f;
            widths[3] = widths[3]*0.9f;
            widths[4] = widths[4]*0.9f;
            widths[5] = widths[5]*0.9f;
            widths[6] = widths[6]*0.9f;
            widths[7] = widths[7]*0.9f;
        }else if (Constant.PDF_TEMPLATE_KIND_TWO == type){
            widths[0] = widths[0]*0.4f;
        }else if(Constant.PDF_TEMPLATE_KIND_DEFAULT == type) {
        	widths[0] = widths[0]*0.9f;
        }

        return new PdfPTable(widths);
    }    
    
    //增加空行
    protected static List<PdfPCell> addNullRowForPaging(int rowNum, int cellNum, float fixedHeight){
    	List<PdfPCell> ret = new ArrayList<>();
    	try
    	{
    		for (int i = 0; i < rowNum * cellNum; i++){
    			PdfPCell pdfpCell = new PdfPCell();
    			pdfpCell.setColspan(1);
                pdfpCell.setRowspan(1);
                pdfpCell.setPhrase(new Paragraph(" "));
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
                pdfpCell.setFixedHeight(fixedHeight);
                
                ret.add(pdfpCell);
    		}
    	}
    	catch(Exception ex){
    		log.error(ex.getMessage() + ex.fillInStackTrace());
    	}
    	return ret;
    } 
    protected static  Font getFontByExcelByTemplate(Cell cell,float size) {
        try {
            Resource resource = new DefaultResourceLoader().getResource("/font/msyh.ttc");
            String fontPath = resource.getFile().getAbsolutePath();
            BaseFont baseFont1 = BaseFont.createFont(fontPath+",0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font result = new Font(baseFont1,size);
            Workbook wb = cell.getSheet().getWorkbook();
            short index = cell.getCellStyle().getFontIndex();
            org.apache.poi.ss.usermodel.Font font = wb.getFontAt(index);
            if (font.getBoldweight() == org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD) {
                result.setStyle(Font.BOLD);
            }
            if (font.getItalic()) {
                result.setStyle(Font.ITALIC);
            }
            //下划线
            FontUnderline underline = FontUnderline.valueOf(font.getUnderline());
            if (underline == FontUnderline.SINGLE) {
                String ulString = Font.FontStyle.UNDERLINE.getValue();
                result.setStyle(ulString);
            }
            return result;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }    
    public  static void addFontByExcelByTemplate(Row row, PdfPCell pdfpCell, Cell cell,int type,int i,int rowLength,int j , int totalNum) {
        float baseFontSize = 10f;
        if (Constant.PDF_TEMPLATE_KIND_ONE==type){
            if (i==0){
                baseFontSize = 13f;
            }else if(i==2){
                baseFontSize = 9f;
            }else if (i>2&&i<rowLength-3){
                baseFontSize = 9f;
            }else{
                baseFontSize = 9f;
            }

        }else if(Constant.PDF_TEMPLATE_KIND_TWO==type){
            baseFontSize = 9f;
            if (i==0){
                baseFontSize = 13f;
            }else if(i==rowLength-1){
                baseFontSize=9f;
            }
        }else if(Constant.PDF_TEMPLATE_KIND_DEFAULT == type) {
        	baseFontSize = 9f;
        }
        Font cellfont =  getFontByExcelByTemplate(cell,baseFontSize);

        Phrase phrase = new Phrase(cell.getStringCellValue(), cellfont);
        if (Constant.PDF_TEMPLATE_KIND_ONE==type){

            if (i==rowLength-3){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfpCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (i==rowLength-2&&j>0){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfpCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (i>2&&i<rowLength-3){
//
//
            }
        }else if(Constant.PDF_TEMPLATE_KIND_TWO==type){
            if (i==rowLength-3){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfpCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (i>3&&i<rowLength-3){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfpCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (i==rowLength-2&&j>1){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfpCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (i==rowLength-1){
                pdfpCell.setUseAscender(true);
                pdfpCell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                pdfpCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfpCell.setVerticalAlignment(Element.ALIGN_TOP);
            }
        }

        pdfpCell.setPhrase(phrase);
        pdfpCell.setFixedHeight(row.getHeightInPoints() * 0.96f);//实际生成的高度比excel中要高
        if (Constant.PDF_TEMPLATE_KIND_ONE==type){
            if (i==1){
                pdfpCell.setFixedHeight(row.getHeightInPoints() * 2f);
            }

            if (i==2){
                pdfpCell.setFixedHeight(row.getHeightInPoints() * 1.5f);//实际生成的高度比excel中要高
            }

            if (i>2&&i<rowLength-1){
                if (cell.getStringCellValue()!=null&&cell.getStringCellValue().length()>19){
                    pdfpCell.setFixedHeight(row.getHeightInPoints() * 2f);//实际生成的高度比excel中要高
                }else{
                    pdfpCell.setFixedHeight(row.getHeightInPoints() * 1.5f);//实际生成的高度比excel中要高
                }
            }
        }else if(Constant.PDF_TEMPLATE_KIND_TWO==type){
            if (i==0){
                pdfpCell.setFixedHeight(row.getHeightInPoints() * 2f);
            }else if (i>0&&i<4){
                pdfpCell.setFixedHeight(row.getHeightInPoints() * 1.5f);
            }else if(i>3&&i<rowLength-1){
                if (cell.getStringCellValue()!=null&&cell.getStringCellValue().length()>19){
                    pdfpCell.setFixedHeight(row.getHeightInPoints() * 2f);//实际生成的高度比excel中要高
                }else{
                    pdfpCell.setFixedHeight(row.getHeightInPoints() * 1.5f);//实际生成的高度比excel中要高
                }
            }
        }else if(Constant.PDF_TEMPLATE_KIND_DEFAULT == type) {
        	//这里编写对特殊行高的处理
        }
    }    
    protected static int getVAlignByExcel(short align) {
        int result = 0;
        if (align == CellStyle.VERTICAL_BOTTOM) {
            result = Element.ALIGN_BOTTOM;
        }
        if (align == CellStyle.VERTICAL_CENTER) {
            result = Element.ALIGN_MIDDLE;
        }
        if (align == CellStyle.VERTICAL_JUSTIFY) {
            result = Element.ALIGN_JUSTIFIED;
        }
        if (align == CellStyle.VERTICAL_TOP) {
            result = Element.ALIGN_TOP;
        }
        return result;
    }    
    protected static void addBorderByExcel(PdfPCell pCell, Cell cell) {
        Workbook wb = cell.getSheet().getWorkbook();
        CellStyle style = cell.getCellStyle();
        int leftBC = getBorderRBG(wb, style.getLeftBorderColor());
        int rightBC = getBorderRBG(wb, style.getRightBorderColor());
        int topBC = getBorderRBG(wb, style.getTopBorderColor());
        int bottomBC = getBorderRBG(wb, style.getBottomBorderColor());
        if (leftBC == 0 && rightBC == 0 && topBC == 0 && bottomBC == 0) {
            pCell.setBorder(PdfPCell.NO_BORDER);
        } else {
            leftBC = leftBC == 0 ? 16777215 : leftBC;
            rightBC = rightBC == 0 ? 16777215 : rightBC;
            topBC = topBC == 0 ? 16777215 : topBC;
            bottomBC = bottomBC == 0 ? 16777215 : bottomBC;
        }
        pCell.setBorderColorLeft(new BaseColor(leftBC));
        pCell.setBorderColorRight(new BaseColor(rightBC));
        pCell.setBorderColorTop(new BaseColor(topBC));
        pCell.setBorderColorBottom(new BaseColor(bottomBC));
    }    
    private static int getBorderRBG(Workbook wb, short index) {
        int result = 0;

        if (wb instanceof HSSFWorkbook) {
            HSSFWorkbook hwb = (HSSFWorkbook) wb;
            HSSFColor color = hwb.getCustomPalette().getColor(index);
            if (color != null) {
                result = getRGB(color);
            }
        }

        if (wb instanceof XSSFWorkbook) {
            XSSFColor color = new XSSFColor();
            color.setIndexed(index);
            result = getRGB(color);
        }

        return result;
    }    
    private static int getRGB(Color color) {
        int result = 0x00FFFFFF;

        int red = 0;
        int green = 0;
        int blue = 0;

        if (color instanceof HSSFColor) {
            HSSFColor hssfColor = (HSSFColor) color;
            short[] rgb = hssfColor.getTriplet();
            if (rgb!=null){
                red = rgb[0];
                green = rgb[1];
                blue = rgb[2];
            }
        }

        if (color instanceof XSSFColor) {
            XSSFColor xssfColor = (XSSFColor) color;
            byte[] rgb = xssfColor.getRGB();
            if (rgb!=null){
                red = (rgb[0] < 0) ? (rgb[0] + 256) : rgb[0];
                green = (rgb[1] < 0) ? (rgb[1] + 256) : rgb[1];
                blue = (rgb[2] < 0) ? (rgb[2] + 256) : rgb[2];
            }
        }

        if (red != 0 || green != 0 || blue != 0) {
            result = new java.awt.Color(red, green, blue).getRGB();
        }
        return result;
    }      
    protected static int getHAlignByExcel(short align) {
        int result = 0;
        if (align == CellStyle.ALIGN_LEFT) {
            result = Element.ALIGN_LEFT;
        }
        if (align == CellStyle.ALIGN_RIGHT) {
            result = Element.ALIGN_RIGHT;
        }
        if (align == CellStyle.ALIGN_JUSTIFY) {
            result = Element.ALIGN_JUSTIFIED;
        }
        if (align == CellStyle.ALIGN_CENTER) {
            result = Element.ALIGN_CENTER;
        }
        return result;
    } 
    protected static boolean isUsed(Cell cell, int rowIndex, int colIndex) {
        boolean result = false;
        Sheet sheet = cell.getSheet();
        int num = sheet.getNumMergedRegions();
        for (int i = 0; i < num; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            if (firstRow < rowIndex && lastRow >= rowIndex) {
                if (firstColumn <= colIndex && lastColumn >= colIndex) {
                    result = true;
                }
            }
        }
        return result;
    }    
    protected static CellRangeAddress getColspanRowspanByExcel(Cell cell, int rowIndex, int columnIndex) {
        CellRangeAddress result = null;
        Sheet sheet = cell.getSheet();
        int num = sheet.getNumMergedRegions();
        for (int i = 0; i < num; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if (range.getFirstRow() == rowIndex && range.getFirstColumn() == columnIndex) {
                result = range;
            }
        }
        return result;
    }    
    protected static int getPOIColumnWidth(Cell cell) {
        int poiCWidth = cell.getSheet().getColumnWidth(cell.getColumnIndex());
        int colWidthpoi = poiCWidth;
        int widthPixel = 0;
        if (colWidthpoi >= 416) {
            widthPixel = (int) (((colWidthpoi - 416.0) / 256.0) * 8.0 + 13.0 + 0.5);
        } else {
            widthPixel = (int) (colWidthpoi / 416.0 * 13.0 + 0.5);
        }
        return widthPixel;
    } 
    private static void addPicMark(String pdfFilePath, String imgfilepath, String position, String big)
            throws IOException, DocumentException {
        String tempfilepath = pdfFilePath + ".tmp";
        File tempFile = new File(tempfilepath);
        PdfReader reader = new PdfReader(pdfFilePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tempfilepath));
        int total = reader.getNumberOfPages() + 1;
        String[] pArr = position.replaceAll("[ \r\n]", "").split(",");
        String[] bArr = big.replaceAll("[ \r\n]", "").split(",");
        int p1 = Integer.parseInt(pArr[0]);
        int p2 = Integer.parseInt(pArr[1]);
        int b1 = Integer.parseInt(bArr[0]);
        int b2 = Integer.parseInt(bArr[1]);
        Image image = Image.getInstance(imgfilepath);
        // 图片位置
        image.setAbsolutePosition(p1, p2);
//		image.scaleAbsolute(b1, b2);// 自定义大小
        PdfContentByte content;
        for (int i = 1; i < total; i++)//循环对每页插入水印
        {
            content = stamper.getUnderContent(i);//水印的起始
            // 添加水印图片
            content.addImage(image);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.8f);// 设置透明度为0.2
            gs.setStrokeOpacity(0.8f);
            gs.setOverPrintStroking(true);

            content.setGState(gs);
//            // 画个圈
//            content.ellipse(250, 450, 350, 550);
//            content.setLineWidth(1f);
//            content.stroke();
        }
        stamper.close();
        reader.close();
        File destFile = new File(pdfFilePath);
        destFile.delete();
        tempFile.renameTo(destFile);
    }  
    protected static void doAddPicMark(String pdfFilePath, String imgfilepath, String position, String big) {

        for (String filepath : pdfFilePath.split(",")) {
            try {
                addPicMark(filepath, imgfilepath, position, big);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
    protected static void specialPdfcellStyleByTemplateType(PdfPCell pdfpCell , int type,int i,int rowLength,int j , int totalNum){
        if (Constant.PDF_TEMPLATE_KIND_ONE == type){
//            pdfpCell.setPhrase();
            if (i==0){//第一行，设置边框为空
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
            }else if (i==1){//第二行，设置只保留底部的边框样式
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
            }else if(i==rowLength-1){//最后行，只设置保留上边框
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
            }
        }else if (Constant.PDF_TEMPLATE_KIND_TWO == type){
            if (i==0){//第一行，设置边框为空
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
            }else if (i==1){//第二行，设置只保留底部的边框样式
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
            }else if (i==2){//第二行，设置只保留底部的边框样式
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.TOP);
            }else if(i==rowLength-1){//最后行，只设置保留上边框
                pdfpCell.disableBorderSide(Rectangle.LEFT);
                pdfpCell.disableBorderSide(Rectangle.RIGHT);
                pdfpCell.disableBorderSide(Rectangle.BOTTOM);
            }
        }
    }    
}
