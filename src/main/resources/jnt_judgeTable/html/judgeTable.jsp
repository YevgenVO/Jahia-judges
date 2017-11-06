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

<c:if test="${orderBy == null || orderType == null}">
    <jcr:sql var="judgeList" sql="select * from [jnt:judgeInform]"/>
</c:if>
<c:if  test=" ${!empty param.orderBy && !empty param.orderType}">
    <jcr:sql var="judgeList" sql="select * from [jnt:judgeInform] as j order by j.[${param.orderBy}] ${param.orderType}"/>
</c:if>
<table>
    <tr style="">
        <th>Name</th>
        <th>Surname</th>
        <th>Year in office</th>
        <th>Year of registration</th>
        <th>Canton</th>
        <th>Party</th>
        <th>Court</th>
        <th>Birth/Death date</th>
    </tr>
    <tr style="">
        <th><a href="${contentNode.url}?orderBy=name&orderType=desc" style="text-decoration: none;"><span>&#8593;</span></a><a
                href="${contentNode.url}?orderBy=name&orderType=asc" style="text-decoration: none;"><span>&#8595;</span></a>
        </th>
        <th></th>
        <th><a href="${contentNode.url}?orderBy=yearInOffice&orderType=desc" style="text-decoration: none;"><span>&#8593;</span></a><a
                href="${contentNode.url}?orderBy=yearInOffice&orderType=asc" style="text-decoration: none;"><span>&#8595;</span></a>
        </th>
        <th></th>
        <th><a href="${contentNode.url}?orderBy=canton&orderType=desc" style="text-decoration: none;"><span>&#8593;</span></a><a
                href="${contentNode.url}?orderBy=canton&orderType=asc" style="text-decoration: none;"><span>&#8595;</span></a>
        </th>
        <th></th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${judgeList.nodes}" var="judge">
        <c:if test="${judge != null}">
        <h1>judge.nodeType</h1>
        <tr height="100px">
            <td>${judge.properties['name'].string}</td>

            <td><a onclick="window.open('${contentNode.url}judge2/judge-info.html?id=${judge.identifier}', 'newwindow',
                    'width=800, height=400'); return false;" title="${judge.properties['Surname'].string}"
                   href="<c:url value="#" />">${judge.properties['Surname'].string}</a></td>
                <%--<td>${judge.properties['Surname'].string}</td>--%>
            <td>${judge.properties['yearInOffice'].string}</td>
            <td>${judge.properties['yearOfRegistration'].string}</td>
            <td>${judge.properties['canton'].string}</td>
            <td>${judge.properties['parti'].string}</td>
            <td><fmt:message key="jnt_judgeInform.court.${judge.properties.court.string}"/></td>
            <fmt:formatDate var="deathDate" value="${judge.properties['deathDate'].date.time}" pattern="yyyy"/>
            <fmt:formatDate var="birthDate" value="${judge.properties['partyBirth'].date.time}" pattern="yyyy"/>
            <td>${birthDate}-${deathDate}</td>
        </tr>
        </c:if>
    </c:forEach>
</table>
