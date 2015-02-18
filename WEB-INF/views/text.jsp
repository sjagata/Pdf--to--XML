<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:forEach var="list" items="${list}"> 
${list}~
</c:forEach>

<%-- <c:forEach var="list1" items="${list}"> 
${list}
</c:forEach> --%>

<%-- JSON : ${map} <br><br>

JSON : ${map1} <br><br>
<br><br>
${str} --%>



<c:forEach var="str" items="${result}">
	<option>${str}<option />
</c:forEach>