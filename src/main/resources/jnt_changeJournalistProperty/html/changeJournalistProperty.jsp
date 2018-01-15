<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ost" uri="http://www.ye.ost/jahia/training/tags/ost" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<c:set var="journalistName" value="${currentNode.properties['journalistName'].string}"/>

<jcr:sql var="queryResult" sql="select * from [jnt:jurnalistData] as j where j.[j:nodename]='${journalistName}'"/>
<c:forEach items="${queryResult.nodes}" var="element">
    <c:set var="journalist" value="${element}"/>
</c:forEach>

<c:set var="journalistPropertyName" value="${currentNode.properties['journalistPropertyName'].string}"/>
<c:set var="journalistPropertyValue" value="${currentNode.properties['journalistPropertyValue'].string}"/>



<ost:jourChange name="${journalistPropertyName}" journalist="${journalist}" propertyValue="${journalistPropertyValue}" liveMode="${renderContext.liveMode}"/>

<h1>${journalist.properties['AdditionalEmail'][0].string}</h1>
<h1>${journalistPropertyName}</h1>
<h1>${journalistPropertyValue}</h1>


