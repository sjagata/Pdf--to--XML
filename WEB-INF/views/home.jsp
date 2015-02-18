<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
	<title>PDF TO Json</title>
	<script src="<c:url value="/resources/jquery-2.1.0.min.js"/>"></script>
	<script src="<c:url value="/resources/xml2json.js"/>"></script>
    <link href="<c:url value="/resources/stylesheet.css" />" rel="stylesheet" media="screen">
	<script src="<c:url value="/resources/jquery.xslt.js"/>"></script>
	<script src="<c:url value="/resources/xslt.js"/>"></script>
	
	
	<script src="<c:url value="/resources/stand.js"/>"></script>
        <script type="text/javascript" language="JavaScript" src="<c:url value="/resources/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" language="JavaScript" src="<c:url value="/resources/connection-min.js"/>"></script>
	
	
	<script src="<c:url value="/resources/jsxml.js"/>"></script>
	<%-- <script src="<c:url value="/resources/prototype.js"/>"></script> --%>
	<!-- <script src="http://yui.yahooapis.com/3.18.1/build/yui/yui-min.js"></script> -->
</head>

<body>
	<div id="maindiv">
		<h2>PDF to Json to XML</h2>
		<P>TimeStamp: ${serverTime}.</P>
	
		<form id="formpdf">
			<strong>Choose File:</strong> <input type="file" id="upload" name="file[]" multiple><br><br>
			<button type="submit" class="btn">Submit</button>
		</form>
		
		<div id="text" style="display: none">			
			<textarea id="textbox" ></textarea>		
			<br><br>
			<button id="createxml" class="btn">Generate xml</button>
			<button id="create" class="btn" style="display:none;">Transform to xml using xslt</button> <br><br> 
			
			<div> 
				<textarea id="textbox1" class="tal"></textarea>
				<textarea id="textbox2" class="tar"></textarea>
				
				<br clear="all">
			</div><br><br>
			
				
			<!-- <div class="button" style="display: none"><div class="outer"><div class="height"><div class="inner"><a download="Pdf2XMLFile.xml" id="downloadlink" style="display: none">Download</a></div></div></div></div> -->
			<button id="downloadlink" style="display: none;margin-left: 55%;" class="btn">Download</button>
			
		</div>	
	</div>	
	
	
	<div id="transformResult"></div>
</body>

<script type="text/javascript">
$(document).ready(function(){
	$('#formpdf').submit(function(e) {
		var values = $(this).serializeArray();
		values = jQuery.param(values);
	    var files = $('#upload')[0].files;	    
	    var filenames=[];
	    for (var i = 0; i < files.length; i++) {
	    	filenames.push(files[i].name);
	    }
	    
		$.post('file', $.param({'file': filenames}, true), function(response) {
			//alert(response);
			var res1 = response.replace(/=/g, ':');
			//alert(res1);
			var res2 = res1.replace(/[/\\*]/g, "");
			//alert(res2);
			var res3 = res2.replace(/"{/g, '{');
			var res4 = res3.replace(/}"/g, '}');
			res4 = res4.replace(/\s{2,}/g, ' ');
			/* var res5 = res4.replace(/[^\d\.\-\ ]/g, ''); */
			/* var splitted = res2.split("~");
			var finalObj = splitted[0].concat(splitted[1]); */
			$('#textbox').html(res4);
			$('div#text').show();
		}); 
		e.preventDefault(); // prevent actual form submit and page reload
	});
});

function cleanArray(actual){
  var newArray = new Array();
  for(var i = 0; i<actual.length; i++){
      if (actual[i]){
        newArray.push(actual[i]);
    }
  }
  return newArray;
}

var inte = 1;
function downloadAll(files){ 
    if(files.length == 0) return;  
    file = files.pop();  
    //Get name from data
    var name = file.match(/<Name>(.*)<\/Name>/);
    
    //Replace Name tag with employee
    var file = file.replace(/<Name>/g, '<EMPLOYEEE><Name>');
  
    var file = file.replace(/<\/Department>/g, '</Department></EMPLOYEEE>');
    var file=file.replace(/<BI-WEEKLY>/g,'<PAYROLL><BI-WEEKLY>');
    var file=file.replace(/<\/Ytd1>/g,'</Ytd1></PAYROLL>');
    var file=file.replace(/<\/PAYROLL>/g,'</PAYROLL><DEDUCTION>');
    var file=file.replace(/<TAXX>/g,'</DEDUCTION><TAX><TAXX>');
    var file=file.replace(/<\/Object1>/g,'</TAX></Object1>');
    var file = file.replace(/u2021/g,'');
    var filexsl = '<?xml version="1.0" encoding="ISO-8859-1"?>'+
	  				'<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:template match="/">'+
						'<Object1>'+
						'<EMPLOYEEE>'+
							'<xsl:for-each select="Object1/EMPLOYEEE">'+
								'<Name><xsl:value-of select="Name"/></Name>'+
								'<StartDate><xsl:value-of select="StartDate"/></StartDate>'+
								'<EndDate><xsl:value-of select="EndDate"/></EndDate>'+
							   ' <PayDate><xsl:value-of select="PayDate"/></PayDate>'+
								'<SSN><xsl:value-of select="SSN"/></SSN>'+
								'<FITWH><xsl:value-of select="FITWH"/></FITWH>'+
							    '<State><xsl:value-of select="State"/></State>'+
								'<Employee><xsl:value-of select="Employee"/></Employee>'+
								'<Department><xsl:value-of select="Department"/></Department>'+
							'</xsl:for-each>'+
						'</EMPLOYEEE>'+
						'<PAYROLL>'+
							'<xsl:for-each select="Object1/PAYROLL">'+
								'<BI-WEEKLY><xsl:value-of select="BI-WEEKLY"/></BI-WEEKLY>'+
								'<RATE><xsl:value-of select="RATE"/></RATE>'+
								'<HOURUNIT><xsl:value-of select="HOURUNIT"/></HOURUNIT>'+
							    '<Current1><xsl:value-of select="Current1"/></Current1>'+
								'<Ytd-HRUNIT><xsl:value-of select="Ytd-HRUNIT"/></Ytd-HRUNIT>'+
								'<Ytd1><xsl:value-of select="Ytd1"/></Ytd1>'+
							'</xsl:for-each>'+
						'</PAYROLL>'+
						'<DEDUCTION>'+
							  '<xsl:for-each select="Object1/DEDUCTION">'+
								'<Ded401k><xsl:value-of select="Ded401k"/></Ded401k>'+
								'<YTD401k><xsl:value-of select="YTD401k"/></YTD401k>'+
								'<Ded401kRoth><xsl:value-of select="Ded401kRoth"/></Ded401kRoth>'+
								'<YTD401kRoth><xsl:value-of select="YTD401kRoth"/></YTD401kRoth>'+
								'<DedAdvance><xsl:value-of select="DedAdvance"/></DedAdvance>'+
								'<YTDAdvance><xsl:value-of select="YTDAdvance"/></YTDAdvance>'+
								'<DedMed125><xsl:value-of select="DedMed125"/></DedMed125>'+
								'<YTDMed125><xsl:value-of select="YTDMed125"/></YTDMed125>'+
								'<DedMisc><xsl:value-of select="DedMisc"/></DedMisc>'+
							    '<YTDMisc><xsl:value-of select="YTDMisc"/></YTDMisc>'+
								'<DedNet><xsl:value-of select="DedNet"/></DedNet>'+
							    '<YTDNet><xsl:value-of select="YTDNet"/></YTDNet>'+
								'<DedPartial><xsl:value-of select="DedPartial"/></DedPartial>'+
								'<YTDPartial><xsl:value-of select="YTDPartial"/></YTDPartial>'+
								'<DedExpNt><xsl:value-of select="DedExpNt"/></DedExpNt>'+
								'<YTDExpNt><xsl:value-of select="YTDExpNt"/></YTDExpNt>'+
							  '</xsl:for-each>'+
						'</DEDUCTION>'+
						'<TAX>'+
							'<xsl:for-each select="Object1/TAX">'+
								'<TAXFITWH><xsl:value-of select="TAXFITWH"/></TAXFITWH>'+
								'<CURRENTFITWH><xsl:value-of select="CURRENTFITWH"/></CURRENTFITWH>'+
								'<YTDFITWH><xsl:value-of select="YTDFITWH"/></YTDFITWH>'+
							    '<TAXMED><xsl:value-of select="TAXMED"/></TAXMED>'+
								'<CURRENTMED><xsl:value-of select="CURRENTMED"/></CURRENTMED>'+
								'<YTDMED><xsl:value-of select="YTDMED"/></YTDMED>'+
							    '<TAXSOC><xsl:value-of select="TAXSOC"/></TAXSOC>'+
								'<CURRENTSOC><xsl:value-of select="CURRENTSOC"/></CURRENTSOC>'+
								'<YTDSOC><xsl:value-of select="YTDSOC"/></YTDSOC>'+
							   '<TAXSTATE><xsl:value-of select="TAXSTATE"/></TAXSTATE>'+
								'<CURRENTSTATE><xsl:value-of select="CURRENTSTATE"/></CURRENTSTATE>'+
								'<YTDSTATE><xsl:value-of select="YTDSTATE"/></YTDSTATE>'+ 
							   '<TAXSTATE1><xsl:value-of select="TAXSTATE1"/></TAXSTATE1>'+
								'<CURRENTSTATE1><xsl:value-of select="CURRENTSTATE1"/></CURRENTSTATE1>'+
								'<YTDSTATE1><xsl:value-of select="YTDSTATE1"/></YTDSTATE1>'+ 
							   '<TAXSTATE2><xsl:value-of select="TAXSTATE2"/></TAXSTATE2>'+
								'<CURRENTSTATE2><xsl:value-of select="CURRENTSTATE2"/></CURRENTSTATE2>'+
								'<YTDSTATE2><xsl:value-of select="YTDSTATE2"/></YTDSTATE2>'+ 
								   '<TAXSTATEADDL><xsl:value-of select="TAXSTATEADDL"/></TAXSTATEADDL>'+
									'<CURRENTSTATEADDL><xsl:value-of select="CURRENTSTATEADDL"/></CURRENTSTATEADDL>'+
									'<YTDSTATEADDL><xsl:value-of select="YTDSTATEADDL"/></YTDSTATEADDL>'+
							'</xsl:for-each>'+
						'</TAX>'+
					'</Object1>'+
					'</xsl:template>'+
					'</xsl:stylesheet>';
   
	var file = JSXML.transReady(file, filexsl);
	//console.log(file); 
	
	
	var file = file.replace(/<object1 xmlns="http:\/\/www.w3.org\/1999\/xhtml">/g,'<object'+inte+'>');	
	var file = file.replace(/<\/object1>/g,'</object'+inte+'>');
	
	var box1 = $("#textbox2");
	box1.val( box1.val() + "\n" + file +"\n");
	
   /*  var textFile = null;
    var data = new Blob([file], {type: 'text/plain'});
    // If we are replacing a previously generated file we need to manually revoke the object URL to avoid memory leaks.
    if (textFile !== null) {
      window.URL.revokeObjectURL(textFile);
    }
    textFile = window.URL.createObjectURL(data);    
    var filename = name[1]+".xml";    
    var theAnchor = $('<a />').attr('href', textFile).attr('download',filename);  
    theAnchor[0].click();   
    theAnchor.remove();   */
    
    inte++;
    downloadAll(files);   
    
    $('#downloadlink').show();
} 
$('#create').click(function(){
	var files = textbox1.value.split('\n');
	//console.log(files);
	//console.log(cleanArray(files));
	//alert(files);
    downloadAll(cleanArray(files));  	
});
$('#downloadlink').click(function(){
	var textFile = null;
	var file = $('#textbox2').val();
	var str1 = '<?xml version="1.0" encoding="UTF-8"?><PDFtoXML>';
	var file = str1.concat(file);
	var file = file+"</PDFtoXML>";
	//alert(file);
	//console.log(file);
	
     var data = new Blob([file], {type: 'text/plain'});
    // If we are replacing a previously generated file we need to manually revoke the object URL to avoid memory leaks.
    if (textFile !== null) {
      window.URL.revokeObjectURL(textFile);
    }
    textFile = window.URL.createObjectURL(data);    
    var filename = "PdftoXml.xml";    
    var theAnchor = $('<a />').attr('href', textFile).attr('download',filename);  
    theAnchor[0].click();   
    theAnchor.remove();  
});

$("#createxml").click(function(){
	var x2js = new X2JS();	
	var value = $("#textbox").val();
	var array = value.split('~ ');
	//console.log(array);
	$.each(array, function( i, l ){
		if(l!=""){
			//alert( "Index #" + i + ": " + l );
			value1 = x2js.json2xml_str($.parseJSON(l));
			var value2 = value1.replace(/(<\/Object1><Object2>)/g, '');
			var value3 = value2.replace(/(<\/Object2>)/g, '</Object1>');
			//alert(value3);
			var str1 = '<?xml version="1.0" encoding="UTF-8"?>';
			var value4 = str1.concat(value3);
			
			//concat each xml data
			var txt = value4;
		    var box = $("#textbox1");
		    
			box.val( box.val() + "\n" + txt + "\n");
		}
	});
	$('#create').show();
});
</script> 
</html>