package com.tabner.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.TaggedPdfReaderTool;

@Controller
public class PdfItext {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/pdfitext", method = RequestMethod.GET)
	public String home(Locale locale, Model model){
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,	DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		return "pdfitext";
	}
	
	
	@RequestMapping(value = "/pdfitext/file", method = RequestMethod.POST )
	@ResponseBody
	public ModelAndView PDFtoXml(@RequestParam(value="file") String[] file, ModelAndView mv, HttpServletRequest request) throws IOException, JSONException, ParseException {
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		
		
		/** The resulting XML file. */
	    final String RESULT   = desktop+"/chapter15/moby_extracted.xml";

	    /**
	     * Creates a PDF file using a previous example,
	     * then parses the document.
	     * @param    args    no arguments needed
	     */
	    for(String filename:file){
			System.out.println(filename);
			
			File f = new File(desktop+"/"+filename); 
			FileInputStream fis = null;
//	        StructuredContent.main(args);
	        fis = new FileInputStream(f);
	        TaggedPdfReaderTool readertool = new TaggedPdfReaderTool();
	        PdfReader reader = new PdfReader(fis);
	        readertool.convertToXml(reader, new FileOutputStream(RESULT));
	        reader.close();
	    }
		
		return mv;
	
	}
}
