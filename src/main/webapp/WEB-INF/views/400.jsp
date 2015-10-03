<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="application/json; charset=UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
{
"error":"true"
<c:if test="${not empty msgCode}">
,"msgCode":"${msgCode}"
</c:if>
<c:if test="${not empty message}">
,"message":"${message}"
</c:if>
<c:if test="${not empty exclass}">
,"exception":"${exclass}"
</c:if>
<c:if test="${not empty stack}">
,"stack":"${stack}"
</c:if>
<c:forEach items="${nparams}" var="nmap">
,"${nmap.key}":${nmap.value}
</c:forEach>
}
