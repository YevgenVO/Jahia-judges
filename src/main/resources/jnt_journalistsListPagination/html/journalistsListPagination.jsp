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

<template:include view="hidden.header"/>

<table  style="width: 100%;">
    <tr style="width: 25%;">
        <th>First Name</th>
        <th>Last Name</th>
        <th>Language(s)</th>
        <th>email address(es)</th>
    </tr>
    <c:forEach items="${moduleMap.currentList}" var="journalist" varStatus="status" begin="${moduleMap.begin}"
               end="${moduleMap.end}">
        <tr>
            <td>${journalist.properties['Name'].string}</td>
            <td>${journalist.properties['Surname'].string}</td>
            <td>

                <c:forEach items="${journalist.properties['Languages']}" var="lan">
                    <h5>${lan.string}</h5>
                </c:forEach>
            </td>
            <td>
                <h3>${journalist.properties['Email'].string}</h3>
                <c:forEach items="${journalist.properties['AdditionalEmail']}" var="em">
                    <h5>${em.string}</h5>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
</table>

<template:include view="hidden.footer"/>
