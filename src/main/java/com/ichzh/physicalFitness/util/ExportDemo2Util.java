package com.ichzh.physicalFitness.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.DefaultResourceLoader;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wiscess.exporter.dto.AssignedCell;
import com.wiscess.exporter.dto.ExportExcelParameter;
import com.wiscess.exporter.exception.ManagerException;

public class ExportDemo2Util extends ExportUtilBase {

	public static Object exportDemoWithPdf(ExportExcelParameter para, String filename, HttpServletResponse res,
			List<AssignedCell[]> data,Integer atLeastDataCount,List<Integer[]> aheights,int type,boolean isA4Rotate){
		try {
			res.setContentType("APPLICATION/ms-excel");
			res.setHeader("Content-Disposition", "attachment; filename="
					+ new String(filename.getBytes("gbk"), "iso8859-1"));
		
			File outTemp = File.createTempFile("demo-2-",".xlsx");
			FileOutputStream fos = new FileOutputStream(outTemp);
			ExcelExportUtil2.export(para,fos, data,atLeastDataCount,aheights);
			fos.close();
			String pdfPath = doConvertPdfForDemo(outTemp.getPath(), filename, "author", "mb", "m", "l", isA4Rotate,type);
			String waterPngPath = new DefaultResourceLoader().getResource("/").getFile().getPath()+File.separator+"template"+File.separator+(isA4Rotate?"water2.jpg":"water.jpg");
			doAddPicMark(pdfPath, waterPngPath, "0, 0", "595, 842");
			File file = new File(pdfPath);
			writeFile2Out(file,res.getOutputStream());
			return null;
		} catch (Exception e) {
			throw new ManagerException("导出出错。", e);
		}
	}	
	
	private static String doConvertPdfForDemo(String excelFilePath, String title, String author, String subject,
            String keyword, String creator, boolean isA4Rotate,int type) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        try {
            File excelFile = new File(excelFilePath);
            if (!excelFile.exists()) {
                return null;
            }
            String folderName = excelFile.getName().substring(0, excelFile.getName().lastIndexOf("."));
            File folderFile = new File(excelFile.getParentFile().getAbsolutePath() + File.separator + folderName);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }
            is = new FileInputStream(excelFilePath);
            Workbook wb = WorkbookFactory.create(is);
            int sheetNum = wb.getNumberOfSheets();
            for (int m = 0; m < sheetNum; m++) {
                Sheet sheet = wb.getSheetAt(m);
                String pdfFilePath = folderFile.getAbsolutePath() + File.separator
                        + excelFile.getName() + "-" + (m + 1) + ".pdf";
                excelSheetToPdfForDemo(m, sheet, pdfFilePath, title, author, subject, keyword, creator, isA4Rotate,type);
                sb.append(sb.length() == 0 ? "" : ",").append(pdfFilePath);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;    	
    }
	
    private static void excelSheetToPdfForDemo(int sheetIndex, Sheet sheet, String pdfFilePath,
            String title, String author, String subject, String keyword,
            String creator, boolean isA4Rotate,int type)
            		throws IOException, DocumentException, InvalidFormatException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(pdfFilePath);
        PdfWriter.getInstance(document, os);
        document.addTitle(title);//标题
        document.addAuthor(author);//作者
        document.addSubject(subject);//主题
        document.addKeywords(keyword);//关键词
        document.addCreator(creator); //创建者
        if (isA4Rotate) {
            document.setPageSize(PageSize.A4.rotate());//横向
        } else {
            document.setPageSize(PageSize.A4);//纵向
        }
        document.open();
        //开始转换并添加内容到document
        PdfPTable table = sheetToPdfTableForDemo(sheetIndex, sheet,type);
        document.add(table);
        //关闭文档对象
        document.close();
        os.close();    	
    }    
    private static  PdfPTable sheetToPdfTableForDemo(int sheetIndex, Sheet sheet,int type)
            throws BadElementException, IOException {
        int rowLength = sheet.getLastRowNum() + 1;//行数
        //计算最大总列数
        int totalCNum = 0;
        for (int i = 0; i < rowLength; i++) {
            Row row = sheet.getRow(i);//当前行
            if (row == null) {
                continue;
            }
            totalCNum = row.getLastCellNum() > totalCNum ? row.getLastCellNum() : totalCNum;
        }
//        System.out.println("sheetIndex=" + (sheetIndex + 1) + "  rowLength=" + rowLength + "  colLeng=" + totalCNum);
        float[] widths = new float[totalCNum];//每列宽度组合
        List<PdfPCell> cells = new ArrayList<PdfPCell>();
        //依次循环每一行，添加单元格信息
        
        /**
         * 需要手动处理分页时将一下代码的注释去掉
         */
        /*
        //计算总页数
        int totalPageNum = 1;
        //序号所在行往前计数的行数（含序号所在行）
        int prefixRowNum = 4;
        //第一页最多允许的行数
        int firstPageRowNum = 26;
        //非第一页最多允许的行数
        int notFirstPageRowNum = 32;
        
        if (rowLength > (firstPageRowNum-prefixRowNum)){
        	totalPageNum = NoServiceUtil.div(new BigDecimal(rowLength - firstPageRowNum), new BigDecimal(notFirstPageRowNum)) + 1;
        }
        */
        for (int i = 0; i < rowLength; i++) {
            Row row = sheet.getRow(i);//当前行
            if (row == null) {
                for (int j = 0; j < totalCNum; j++) {
                    int rowspan = 1;
                    int colspan = 1;
                    PdfPCell pdfpCell = new PdfPCell();
//                    BaseColor color = new BaseColor(255,255,255,0);
//                    pdfpCell.setBackgroundColor(color);
                    pdfpCell.setColspan(colspan);
                    pdfpCell.setRowspan(rowspan);
                    pdfpCell.setPhrase(new Paragraph(" "));
                    specialPdfcellStyleByTemplateType(pdfpCell,type,i,rowLength,j,totalCNum);
                    cells.add(pdfpCell);
                }
                continue;
            }

            for (int j = 0; j < totalCNum; j++) {
                Cell cell = row.getCell(j);
                int rowspan = 1;
                int colspan = 1;
                if (cell == null) {
                    PdfPCell pdfpCell = new PdfPCell();
//                    BaseColor color = new BaseColor(255,255,255,0);
//                    pdfpCell.setBackgroundColor(color);
                    pdfpCell.setColspan(colspan);
                    pdfpCell.setRowspan(rowspan);
                    pdfpCell.setPhrase(new Paragraph(" "));
                    specialPdfcellStyleByTemplateType(pdfpCell,type,i,rowLength,j,totalCNum);
                    cells.add(pdfpCell);
                    continue;
                }
                if (isUsed(cell, row.getRowNum(), cell.getColumnIndex())) {//该单元格被合并，则跳过
                    continue;
                }
                //读取该单元格的信息
                cell.setCellType(Cell.CELL_TYPE_STRING);
                CellRangeAddress range = getColspanRowspanByExcel(cell, row.getRowNum(), cell.getColumnIndex());
                if (range != null) {
                    rowspan = range.getLastRow() - range.getFirstRow() + 1;
                    colspan = range.getLastColumn() - range.getFirstColumn() + 1;
                } else {
                    float cw = getPOIColumnWidth(cell);
                    widths[j] = cw;
                }
                //PDF单元格
                PdfPCell pdfpCell = new PdfPCell();
//                BaseColor color = new BaseColor(255,255,255,0);
//                pdfpCell.setBackgroundColor(color);
//                pdfpCell.setBackgroundColor(new BaseColor(getBackgroundColorByExcel(cell.getCellStyle())));
                pdfpCell.setColspan(colspan);
                pdfpCell.setRowspan(rowspan);
               
                pdfpCell.setVerticalAlignment(getVAlignByExcel(cell.getCellStyle().getVerticalAlignment()));//设置单元格垂直方向对其方式
                pdfpCell.setHorizontalAlignment(getHAlignByExcel(cell.getCellStyle().getAlignment()));//设置单元格水平方向对其方式
                pdfpCell.setFixedHeight(row.getHeight());
                
                //System.out.println(row.getHeight()+" "+row.getHeightInPoints());
                addBorderByExcel(pdfpCell, cell);
//                addFontByExcel(row, pdfpCell, cell);
//                addImageByPOICell(pdfpCell, cell);//todo 由于图片是浮动的，不太好定位写入，可以考虑图片水印写入

                addFontByExcelByTemplate(row, pdfpCell, cell,type,i,rowLength,j , totalCNum);
                specialPdfcellStyleByTemplateType(pdfpCell,type,i,rowLength,j,totalCNum);

                cells.add(pdfpCell);
                j += colspan - 1;
            }
            /**
             * 需要手动处理分页时将一下代码的注释去掉
             */
            //处理分页  第一页显示 26行， 从第二页开始显示32页
            /*
            if (i >= (firstPageRowNum - 1+prefixRowNum) ){
            	//计算当前是第几页(从第二页开始)
            	 int currentPage = NoServiceUtil.div(new BigDecimal(i - (firstPageRowNum - 1) - prefixRowNum), new BigDecimal(notFirstPageRowNum));
            	 //不是最后一页且是当前页的最后一行
            	 if (currentPage < totalPageNum){
            		 //当前页的最后一行
            		 //计算最后一行的行号
            		 int lastRowOfCurrentPage = currentPage * notFirstPageRowNum + firstPageRowNum + prefixRowNum - 1;
            		 //当前行是当前页的最后一行
            		 if (i == lastRowOfCurrentPage){
            			 //插入空行使人为分页
            			 //row.getHeightInPoints() * 1.5f 使空行的行高与内容的行高一致 
            			 List<PdfPCell> pdfPCelles  = addNullRowForPaging(4, totalCNum, row.getHeightInPoints() * 1.5f);
            			 cells.addAll(pdfPCelles);
            		 }
            	 }
            }
            */
            
        }
        PdfPTable table = specialWidthTableByTemplateType(type,widths);
        table.setWidthPercentage(100);

        for (PdfPCell pdfpCell : cells) {
            table.addCell(pdfpCell);
        }
        return table;    	
    }
}
