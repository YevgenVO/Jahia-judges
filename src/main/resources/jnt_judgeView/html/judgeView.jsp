<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<h1>Judges</h1>
<jcr:sql var="res" sql="select * from [jnt:judgeInform] as j where j.['jcr:uuid']='${param.id}'"/>
<h2>${currentNode.properties['jcr:title'].string}</h2>

<table>
    <c:forEach items="${res.nodes}" var="judge">
        <c:set var="fullname" value="${judge.properties.name.string} ${judge.properties.Surname.string}"/>
        <h1>${fullname} - *</h1>
        <tr>
            <td>
                <br>
                <c:if test="${judge.properties.photo.node.url != null}">
                    <img src="${judge.properties.photo.node.url}" width="156px"/>
                </c:if>
            </td>
            <td>
                <h3>${judge.properties.biography.string}</h3>
            </td>
        </tr>
    </c:forEach>
</table>
