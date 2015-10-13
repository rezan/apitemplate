<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="application/json; charset=UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jf" uri="http://company/taglibs/jf" %>
{
"success":"true"
<c:if test="${not empty message}">
,"message":"${message}"
</c:if>
<c:forEach items="${params}" var="map">
,"${map.key}":"<jf:jsonFormat>${map.value}</jf:jsonFormat>"
</c:forEach>
<c:forEach items="${nparams}" var="nmap">
,"${nmap.key}":${nmap.value}
</c:forEach>
}
