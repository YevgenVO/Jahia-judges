<%@ page import="java.util.Calendar" %>
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

<jcr:sql var="judgeList" sql="select * from [jnt:judgeInform]" />
<table>
    <tr style="text">
        <th>Name</th>
        <th>Surname</th>
        <th>Year in office</th>
        <th>Year of registration</th>
        <th>Canton</th>
        <th>Party</th>
        <th>Court</th>
        <th>Birth/Death date</th>
    </tr>
    <c:forEach items="${judgeList.nodes}" var="judge">
        <tr height="100px">
            <td>${judge.properties['name'].string}</td>

            <td><a onclick="window.open('${contentNode.url}judge2/judge-info.html?id=${judge.identifier}', 'newwindow',
                    'width=800, height=400'); return false;" title="${judge.properties['Surname'].string}}"
                   href="<c:url value="#" />">${judge.properties['Surname'].string}</a></td>
                <%--<td>${judge.properties['Surname'].string}</td>--%>
            <td>${judge.properties['yearInOffice'].string}</td>
            <td>${judge.properties['yearOfRegistration'].string}</td>
            <td>${judge.properties['canton'].string}</td>
            <td>${judge.properties['parti'].string}</td>
            <td>${judge.properties['court'].string}</td>
            <fmt:formatDate var="deathDate" value="${judge.properties['deathDate'].date.time}" type="DATE" pattern="yyyy"/>
            <fmt:formatDate var="birthDate" value="${judge.properties['partyBirth'].date.time}" type="DATE" pattern="yyyy"/>
            <td>${birthDate}-${deathDate}</td>
        </tr>
    </c:forEach>
</table>