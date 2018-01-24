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

<c:set var="failureRedirect" value="${renderContext.mainResource.node.url}" scope="application"/>

<c:set var="loginError" value="${param.loginError}"/>
<c:set var="redirectTo" value="/cms/login"/>

<c:if test="${loginError eq 'unknown_user'}">
    <h3>
        <fmt:message key="unknown_user"/>
    </h3>
</c:if>

<form method="post" action="/cms/login" name="loginForm">
    <input type="hidden" name="failureRedirect" value="${failureRedirect}"/>
    <input type="hidden" name="redirectTo" value="${redirectTo}"/>

    <h3><fmt:message key="user"/></h3>
    <input name="username" id="username"/>


    <h3><fmt:message key="password"/></h3>
    <input type="password" name="password" id="password"/>
    <h3></h3>
    <button type="submit" id="submit"><fmt:message key="submit"/></button>

</form>
