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
</head>

<body>
	<div id="maindiv">
		<h2>PDF TO Json</h2>
		<P>TimeStamp: ${serverTime}.</P>
	
		<form id="formpdf">
			<strong>Choose File:</strong> <input type="file" id="upload" name="file[]" multiple><br><br>
			<button type="submit" class="btn">Submit</button>
		</form>
		
		<div id="text" style="display: none">			
			<textarea id="textbox" ></textarea>		
			<br><br>
			<button id="createxml" class="btn">Create xml file</button>
			<button id="create" class="btn" style="display:none;">Download files</button> <br><br> 
			<textarea id="textbox1" ></textarea>	
			<div class="button" style="display: none"><div class="outer"><div class="height"><div class="inner"><a download="Pdf2XMLFile.xml" id="downloadlink" style="display: none">Download</a></div></div></div></div>
			
		</div>	
	</div>	
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
	    
		$.post('pdfitext/file', $.param({'file': filenames}, true), function(response) {
			alert(response);
			
		}); 
		e.preventDefault(); // prevent actual form submit and page reload
	});
});
</script>
</html>
