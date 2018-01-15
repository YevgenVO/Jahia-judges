<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<%--<c:set var=""/>--%>

<c:set var="fullPath" value="${renderContext.mainResource.node.url}"/>

<c:set var="resourcePath" value="${url.resourcePath}"/>

<c:set var="baseWithLanguage" value="${fn:substring(fullPath, 0, fn:indexOf(fullPath,resourcePath))}"/>
<c:choose>
    <c:when test="${fn:indexOf(baseWithLanguage, '/en') > -1 || fn:indexOf(baseWithLanguage, '/de') > -1
     || fn:indexOf(baseWithLanguage, '/it') > -1 || fn:indexOf(baseWithLanguage, '/fr') > -1}">
        <c:set var="base" value="${fn:substring(baseWithLanguage, 0, fn:length(baseWithLanguage)-3)}"/>
        <c:set var="language" value="${fn:substring(baseWithLanguage, fn:length(baseWithLanguage)-3, fn:length(baseWithLanguage))}"/>
    </c:when>
    <c:otherwise>
        <c:set var="base" value="${baseWithLanguage}"/>
        <c:set var="language" value="fr"/>
    </c:otherwise>
</c:choose>
<c:set var="italian" value="${base}/it"/>
<c:set var="french" value="${base}/fr"/>
<c:set var="german" value="${base}/de"/>

<c:set var="parameters" value=""/>

<c:forEach items="${renderContext.request.parameterNames}" var="parameter" varStatus="status">
    <c:choose>
        <c:when test="${status.first}">
            <c:set var="parameters" value="?${parameter}=${param[parameter]}"/>
        </c:when>
        <c:otherwise>
            <c:set var="parameters" value="${parameters}&${parameter}=${param[parameter]}"/>
        </c:otherwise>
    </c:choose>
</c:forEach>


<c:if test="${language eq '/it'}">
    <a href="${french}${url.resourcePath}${parameters}">french</a><br/>
    <a href="${german}${url.resourcePath}${parameters}">german</a><br/>
</c:if>
<c:if test="${language eq '/fr'}">
    <a href="${italian}${url.resourcePath}${parameters}">italian</a><br/>
    <a href="${german}${url.resourcePath}${parameters}">german</a><br/>
</c:if>
<c:if test="${language eq '/de'}">
    <a href="${italian}${url.resourcePath}${parameters}">italian</a><br/>
    <a href="${french}${url.resourcePath}${parameters}">french</a><br/>
</c:if>

