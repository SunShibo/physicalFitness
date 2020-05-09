package com.ichzh.physicalFitness.web.controller;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.ExportDemoDto;
import com.ichzh.physicalFitness.dto.ExportDemoDto2;
import com.ichzh.physicalFitness.util.*;
import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wiscess.exporter.dto.AssignedCell;
import com.wiscess.exporter.dto.ExportExcelParameter;
import com.wiscess.exporter.exception.ManagerException;

import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Controller
@Slf4j
public class ExportDemoController {
	
	@RequestMapping("/exportDetail")
	public Object exportDetail(HttpServletRequest req,HttpServletResponse res){
		
		//要导出的数据
		List<ExportDemoDto2>  costIssuanceDetail =  new ArrayList<ExportDemoDto2>();
		
		for (int i = 1; i <= 58; i++) {
			ExportDemoDto2 oneDetail = new ExportDemoDto2();
			oneDetail.setName("姓名"+i);
			oneDetail.setCardNo("证件号"+i);
			oneDetail.setAmount("1,500");
			oneDetail.setTax("140");
			oneDetail.setAmoutFinal("1,360");
			costIssuanceDetail.add(oneDetail);
		}
		
		ExportDemoDto oneDemoDto = new ExportDemoDto();
		oneDemoDto.setExamKind("高考体育单考单招");
		oneDemoDto.setSchoolName("XX中学高中部（东大桥校区）");
		oneDemoDto.setExamTime("2018.04.20-2018.04.22");
		//应发金额合计
		oneDemoDto.setAmountTotal(CommonUtil.formatNumber3(""+(1500*58)));
		//扣税合计
		oneDemoDto.setTaxTotal(CommonUtil.formatNumber3(""+(140*58)));
		//实发金额合计
		oneDemoDto.setAmoutFinalTotal(CommonUtil.formatNumber3(""+(1360*58)));
		//实发金额合计大写
		oneDemoDto.setAmoutFinalTotalCN(RmbUtil.number2CNMontrayUnit(new BigDecimal(""+(1360*50))));
		oneDemoDto.setCostIssuanceDetail(costIssuanceDetail);
		
		ExportExcelParameter para = new ExportExcelParameter();
        para.setTemplateName("/template/template_demo.xlsx");
        para.setDataRow(new AssignedCell(4, 0, ""));
        para.setTotalCol(6);
        List<AssignedCell> assignedCell=new ArrayList<>();
        //指定考试类别
        AssignedCell  oneRowZeroCol = new AssignedCell(1,0,"考试类别:"+ oneDemoDto.getExamKind());
        assignedCell.add(oneRowZeroCol);
        //指定考点校名称
        AssignedCell towRowZeroCol = new AssignedCell(2,0,"考点校名称:"+ oneDemoDto.getSchoolName());
        assignedCell.add(towRowZeroCol);
        
        //没有数据的情况下，导出的excel中有几个空行，这里规定为一个空行
        int leastEmptyLineNum = 1;
        //数据条数
        int dataLineNum = oneDemoDto.getCostIssuanceDetail().size();
        //计算合计显示的行数
        int totalRowNum = 4 + leastEmptyLineNum;
        //如果数据条数大于空行数
        if (dataLineNum > leastEmptyLineNum) {
        	totalRowNum = 4 + dataLineNum;
        }
		//合计行的证件号码列，输出横线
        AssignedCell totalRowTwoCol = new AssignedCell(totalRowNum,2,"————");
        assignedCell.add(totalRowTwoCol);
        //合计行的应发金额合计
        AssignedCell totalRowThreeCol = new AssignedCell(totalRowNum,3,oneDemoDto.getAmountTotal());
        assignedCell.add(totalRowThreeCol);
        //合计行的代扣税金合计
        AssignedCell totalRowFourCol = new AssignedCell(totalRowNum,4,oneDemoDto.getTaxTotal());
        assignedCell.add(totalRowFourCol);
        //合计行的实发金额合计
        AssignedCell totalRowFiveCol = new AssignedCell(totalRowNum,5,oneDemoDto.getAmoutFinalTotal());
        assignedCell.add(totalRowFiveCol);
        //指定实发合计大写金额
        AssignedCell uppercaseRowTowCol = new AssignedCell(totalRowNum+1,2,oneDemoDto.getAmoutFinalTotalCN());
        assignedCell.add(uppercaseRowTowCol);
        
        //指定某些特殊行的高度
        List<Integer[]> autoHeights = new ArrayList<>();
        //指定第一行的高度为30
        autoHeights.add(new Integer[]{0,30});
        //指定最后一行的高度为40
        autoHeights.add(new Integer[]{totalRowNum+2,40});
        
        List<AssignedCell[]> data = new ArrayList<AssignedCell[]>();
        for (int j = 0; j < oneDemoDto.getCostIssuanceDetail().size(); j++) {
            ExportDemoDto2 oneDetail = oneDemoDto.getCostIssuanceDetail().get(j);
            int col = 0;
            //处理列数据
            AssignedCell[] oneRow = {
                    new AssignedCell(0, col++, j+1+"" ,1),
                    new AssignedCell(0, col++, oneDetail.getName(),1),
                    new AssignedCell(0, col++, oneDetail.getCardNo(),1),
                    new AssignedCell(0, col++, oneDetail.getAmount(),1),
                    new AssignedCell(0, col++, oneDetail.getTax(),1),
                    new AssignedCell(0, col++, oneDetail.getAmoutFinal(),1)
            };
            data.add(oneRow);
        }
        para.setAssignedCells(assignedCell);
        //导出的PDF文件的文件名
        String pdfFileName = "2018高考体育单考单招XX中学高中部（东大桥校区）考务费分配明细.pdf";
        try
        {
        	ExportDemoUtil.exportDemoWithPdf(para, pdfFileName, res, data, leastEmptyLineNum, autoHeights, 3, false);
        }
        catch(ManagerException me) {
        	//不用处理
        }
		 return null;
	}

	@RequestMapping("/exportDetail4Tpl2")
	public Object exportDetail4Tpl2(HttpServletRequest req,HttpServletResponse res) {
		//要导出的数据
		List<ExportDemoDto2>  costIssuanceDetail =  new ArrayList<ExportDemoDto2>();
		
		for (int i = 1; i <= 58; i++) {
			ExportDemoDto2 oneDetail = new ExportDemoDto2();
			oneDetail.setName("姓名"+i);
			oneDetail.setCardNo("证件号"+i);
			oneDetail.setAmount("1,500");
			oneDetail.setTax("140");
			oneDetail.setAmoutFinal("1,360");
			costIssuanceDetail.add(oneDetail);
		}
		ExportExcelParameter para = new ExportExcelParameter();
        para.setTemplateName("/template/template_demo2.xlsx");
        para.setDataRow(new AssignedCell(1, 0, ""));
        para.setTotalCol(6);
        List<AssignedCell> assignedCell=new ArrayList<>();
        
        //没有数据的情况下，导出的excel中有几个空行，这里规定为一个空行
        int leastEmptyLineNum = 0;
        
      //指定某些特殊行的高度
        List<Integer[]> autoHeights = new ArrayList<>();
        //指定第一行的高度为30
        autoHeights.add(new Integer[]{0,30});
        
        List<AssignedCell[]> data = new ArrayList<AssignedCell[]>();
        for (int j = 0; j <costIssuanceDetail.size(); j++) {
            ExportDemoDto2 oneDetail = costIssuanceDetail.get(j);
            int col = 0;
            //处理列数据
            AssignedCell[] oneRow = {
                    new AssignedCell(0, col++, j+1+"" ,0),
                    new AssignedCell(0, col++, oneDetail.getName(),0),
                    new AssignedCell(0, col++, oneDetail.getCardNo(),0),
                    new AssignedCell(0, col++, oneDetail.getAmount(),0),
                    new AssignedCell(0, col++, oneDetail.getTax(),0),
                    new AssignedCell(0, col++, oneDetail.getAmoutFinal(),0)
            };
            data.add(oneRow);
        }
        para.setAssignedCells(assignedCell);
        
      //导出的PDF文件的文件名
        String pdfFileName = "2018高考体育单考单招XX中学高中部（东大桥校区）考务费分配明细.pdf";
        try
        {
        	ExportDemo2Util.exportDemoWithPdf(para, pdfFileName, res, data, leastEmptyLineNum, autoHeights, Constant.PDF_TEMPLATE_KIND_DEFAULT, false);
        }
        catch(ManagerException me) {
        	//不用处理
        }
		
        return null;
	}

	@RequestMapping("/convert_html_to_pdf")
	public void htmlToPdf(String name, HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException, DocumentException {
	    // html模板路径、字体路径、以及图片路径
        String html = "zhengshu.html";
        String font = this.getClass().getResource("/template/huawenxingkai.ttf").getFile(); // 华文行楷 STXingkai
        String simHeiFont = this.getClass().getResource("/template/simhei.ttf").getFile();  // 黑体 SimHei
        String fangSongFont = this.getClass().getResource("/template/fangsong.ttf").getFile(); // 华文仿宋 STFangsong
        String img = this.getClass().getResource("/template/bg.jpg").toString();
        // 初始化模板目录,并设置编码方式
        Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_23);
        freemarkerCfg.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/template").getPath()));
        // freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/");
        freemarkerCfg.setEncoding(Locale.getDefault(), "UTF-8");
        // 获取模板
        Template template = freemarkerCfg.getTemplate(html);
        // 动态渲染html模板
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        Writer out = new StringWriter();
        template.process(map, out);
        // 写入流，并获取渲染后的内容
        out.flush();
        String htmlStr = out.toString();
        // ITextRenderer 可以渲染css高级特效
        ITextRenderer render = new ITextRenderer();
        // 有图片，需要运行以下2行，将图片标签转换为Itext自己的图片对象
//        render.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
//        render.getSharedContext().getTextRenderer().setSmoothingThreshold(0);
        // 加载字体
        ITextFontResolver fontResolver = render.getFontResolver();
        fontResolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont(simHeiFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont(fangSongFont, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // 解析html生成pdf
        render.setDocumentFromString(htmlStr);
        // 解决图片相对路径的问题
        render.getSharedContext().setBaseURL(img);
        render.layout();
        // 下载文件名乱码处理和响应处理
        String fileName = "html生成pdf.pdf";
        String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        if (userAgent.contains("firefox")) {
            finalFileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            finalFileName = URLEncoder.encode(fileName, "UTF-8");
        }
        // 下载
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + finalFileName);

        ServletOutputStream outputStream = response.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        render.createPDF(bufferedOutputStream);
        bufferedOutputStream.close();
        outputStream.close();
    }
	
}
