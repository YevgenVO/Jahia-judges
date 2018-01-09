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
<%@ taglib prefix="ost" uri="http://www.ye.ost/jahia/training/tags/ost" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<c:set var="userKey" value="${renderContext.user.userKey}"/>
<jcr:sql var="userQuery" sql="select * from [jnt:user] as u where ISDESCENDANTNODE(u, '/users')"/>
<c:forEach items="${userQuery.nodes}" var="element">
    <%--<c:if test="${element.path == '/users/ci/ii/ee/journalist-data'}">--%>
        <c:if test="${element.path == userKey}">
        <c:set var="user" value="${element}"/>
    </c:if>
</c:forEach>
<%--<jcr:sql var="queryResult"--%>
         <%--sql="select * from [jnt:jurnalistData] as j where j.[userUUID]='07e7ffa8-3267-4c81-b225-98ad13baf2d5'"/>--%>
<jcr:sql var="queryResult" sql="select * from [jnt:jurnalistData] as j where j.[userUUID]='${user.identifier}'"/>
<c:forEach items="${queryResult.nodes}" var="element">
    <c:set var="journalist" value="${element}"/>
</c:forEach>
<ost:jourGet node="${journalist}" name="Title" var="Title"/>
<ost:jourGet node="${journalist}" name="AcademicTitle" var="AcademicTitle"/>
<ost:jourGet node="${journalist}" name="Languages" var="languages"/>
<ost:jourGet node="${journalist}" name="Email" var="Email"/>
<ost:jourGet node="${journalist}" name="Name" var="Name"/>
<ost:jourGet node="${journalist}" name="Surname" var="Surname"/>
<ost:jourGet node="${journalist}" name="Adress" var="Adress"/>
<ost:jourGet node="${journalist}" name="NPA" var="NPA"/>
<ost:jourGet node="${journalist}" name="Place" var="Place"/>
<ost:jourGet node="${journalist}" name="PhoneNum" var="PhoneNum"/>
<ost:jourGet node="${journalist}" name="CellphoneNumber" var="CellphoneNumber"/>
<ost:jourGet node="${journalist}" name="AdditionalEmail" var="AdditionalEmail"/>
<ost:jourGet node="${journalist}" name="NewspapersConcerned" var="NewspapersConcerned"/>
<ost:jourGet node="${journalist}" name="Password" var="Password"/>
<ost:jourGet node="${journalist}" name="userUUID" var="userUUID"/>
<ost:jourGet node="${journalist}" name="state" var="state"/>
<ost:jourGet node="${journalist}" name="uuid" var="uuid"/>
<ost:jourGet node="${journalist}" name="errorMsg" var="errorMsg"/>
<div id="title">
    <h1>Edition de mon profil</h1>
    <%--<form action="http://jahia-training.lxc:8080/sites/bger/home/judge2/judge-info.html" onsubmit="return validateForm()">--%>
    <form action="${url.base}${currentNode.path}.EditJournalist.do" onsubmit="return validateForm()">
        <input type="hidden" name="currentPageUrl" value="${fn:replace(url.resourcePath, '.html', '')}"/>
        <input type="hidden" name="mailSenderData" id="mailSenderData" value="mailSenderData"/>
        <div id="change_password">
            <h3>Mot de pase</h3>
            <div id="pass_fields">
                <ul style="list-style-type: none;">
                    <h1 id="errorMessage" style="color:red;font-size:20px;"></h1>
                    <li>
                        <label>Mot de passe existant</label>
                        <input name="oldPassword" id="oldPassword" type="text" value="${Password.string}">
                    </li>
                    <li>
                        <label>nouveau mot de passe</label>
                        <input name="newPassword" id="newPassword" type="password" value="">
                    </li>
                    <li>
                        <label>Verifier votre nouveau mot de passe</label>
                        <input name="passConfirmation" id="passConfirmation" type="password" value="">
                    </li>
                </ul>
            </div>
        </div>
        <div id="acriditation">
            <h3>Mon profil d`accreditation</h3>
            <h6 class="left">Journaux concernes</h6>
            <h6 class="left">Langue de travail *</h6>
            <ul style="list-style-type: none;">
                <h1>Email: ${Email.string}</h1>
                <c:forEach items="${languages}" var="language">
                    <c:if test="${language.string == 'Allemand'}"><c:set var="allenand" value="checked"></c:set></c:if>
                    <c:if test="${language.string == 'Franch'}"><c:set var="francais" value="checked"></c:set></c:if>
                    <c:if test="${language.string == 'Italy'}"><c:set var="italien" value="checked"></c:set></c:if>
                </c:forEach>
                <li><input ${allenand} name="Allemand" id="Allemand" type="checkbox"
                                       class="left"><label>Allemand</label></li>
                <li><input ${francais} name="Francais" id="Francais" type="checkbox"
                                       class="left"><label>Francais</label></li>
                <li><input ${italien} name="Italien" id="Italien" type="checkbox" class="left"><label>Italien</label>
                </li>
            </ul>
        </div>
        <div id="coordonnees">
            <h3>Vos coordonnees</h3>
            <ul style="list-style-type: none;">
                <input name="uuid" type="hidden" value="${journalist.UUID}"/>
                <li><label class="left">Titre: </label><label>${Title.string}</label></li>
                <li><label class="left">Titre academique: </label><label>${AcademicTitle.string}</label></li>
                <li><label class="left">Prenom: </label><label>${Surname.string}</label></li>
                <li><label class="left">Nom: </label><label>${Name.string}</label></li>
                <li><label class="left">Adresse *</label><input name="Adress" id="Address" type="text" value="${Adress.string}"
                                                                class="right"/></li>
                <li><label class="left">NPA *</label><input name="NPA" id="NPA" type="text" value="${NPA.string}" class="right"/>
                </li>
                <li><label class="left">Lieu *</label><input name="Place" id="Place" type="text" value="${Place.string}"
                                                             class="right"/></li>
                <li><label class="left">No telephone</label><input name="PhoneNum" id="PhoneNum" type="text"
                                                                   value="${PhoneNum.string}" class="right"/></li>
                <li><label class="left">No telephone partable</label><input name="CellphoneNumber" id="celPhone" type="text"
                                                                            value="${CellphoneNumber.string}"
                                                                            class="right"/></li>
                <li><label class="left">Email *</label><input name="email" type="email" id="email"
                                                              value="${Email.string}"
                                                              class="right"/></li>
                <c:if test="${AdditionalEmail == null}">
                    <li><label class="left"></label><input name="AdditionalEmail" type="email" value="" class="right"/>
                    </li>
                    <li><label class="left"></label><input name="AdditionalEmail" type="email" value="" class="right"/>
                    </li>
                </c:if>
                <c:forEach items="${AdditionalEmail}" var="adEmail">
                    <c:if test="${adEmail != null && adEmail.string != ''}">
                        <li><label class="left"></label><input name="AdditionalEmail" type="email"
                                                               value="${adEmail.string}" class="right"/></li>
                    </c:if>
                </c:forEach>
            </ul>
            <input type="submit" value="Submit"/>
        </div>
    </form>
</div>

<script>
    function validateForm() {
        document.getElementById("errorMessage").innerHTML = "";
        var returnResult= true;
        var oldPassword = document.getElementById("oldPassword").value;
        var newPassword = document.getElementById("newPassword").value;
        var passConfirmation = document.getElementById("passConfirmation").value;
        var adress = document.getElementById("Address").value;
        var npa = document.getElementById("NPA").value;
        var Place = document.getElementById("Place").value;
        var Email = document.getElementById("Email").value;
        var npaValidation = /\d+/;
        if (oldPassword != "${Password.string}") {
            document.getElementById("errorMessage").innerHTML += "Wrong password!";
            returnResult=false;
        }
        if (Email == "") {
            document.getElementById("errorMessage").innerHTML += "Email field is mandatory!";
            returnResult=false;
        }
        if (newPassword != passConfirmation) {
            document.getElementById("errorMessage").innerHTML += "<br>Wrong confirmation!!!";
            returnResult=false;
        }
        if (adress.length < 5) {
            document.getElementById("errorMessage").innerHTML += "</br>Address field is mandatory and must be longer then 5 symbols!";
            returnResult=false;
        }
        if (!npaValidation.test(npa)) {
            document.getElementById("errorMessage").innerHTML += "</br>NPA field is mandatory and must integer!";
            returnResult=false;
        }
        if (Place.length < 5) {
            document.getElementById("errorMessage").innerHTML += "</br>Place field is mandatory and must be longer then 5 symbols!";
            returnResult=false;
        }
        return returnResult;
    }
</script>