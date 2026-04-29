<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle} – LibraryMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/views/style.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">📚 <span>Library</span>MS</div>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/books">Books</a></li>
        <li><a href="${pageContext.request.contextPath}/authors" class="active">Authors</a></li>
    </ul>
</nav>

<div class="container">
    <div class="page-header">
        <h1>${author.id == null ? '➕ Add New Author' : '✏️ Edit Author'}</h1>
        <a href="${pageContext.request.contextPath}/authors" class="btn btn-outline">← Back to Authors</a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="form-card">

        <c:choose>
            <c:when test="${author.id == null}">
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/authors/save"/>
            </c:when>
            <c:otherwise>
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/authors/update/${author.id}"/>
            </c:otherwise>
        </c:choose>

        <form:form modelAttribute="author" action="${actionUrl}" method="post">

            <div class="form-group">
                <form:label path="name">Full Name *</form:label>
                <form:input path="name" cssClass="form-control" placeholder="e.g. George Orwell"/>
                <form:errors path="name" cssClass="field-error"/>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <form:label path="nationality">Nationality *</form:label>
                    <form:select path="nationality" cssClass="form-control">
                        <form:option value="" label="-- Select --"/>
                        <form:option value="American"   label="American"/>
                        <form:option value="British"    label="British"/>
                        <form:option value="French"     label="French"/>
                        <form:option value="Russian"    label="Russian"/>
                        <form:option value="Colombian"  label="Colombian"/>
                        <form:option value="German"     label="German"/>
                        <form:option value="Indian"     label="Indian"/>
                        <form:option value="Japanese"   label="Japanese"/>
                        <form:option value="Other"      label="Other"/>
                    </form:select>
                    <form:errors path="nationality" cssClass="field-error"/>
                </div>
                <div class="form-group">
                    <form:label path="birthYear">Birth Year</form:label>
                    <form:input path="birthYear" type="number" cssClass="form-control" placeholder="e.g. 1903"/>
                    <form:errors path="birthYear" cssClass="field-error"/>
                </div>
            </div>

            <div class="form-group">
                <form:label path="bio">Biography</form:label>
                <form:textarea path="bio" cssClass="form-control" placeholder="A short biography of the author…"/>
                <form:errors path="bio" cssClass="field-error"/>
            </div>

            <div class="form-actions">
                <c:choose>
                    <c:when test="${author.id == null}">
                        <button type="submit" class="btn btn-primary">💾 Save Author</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="btn btn-success">✅ Update Author</button>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/authors" class="btn btn-outline">Cancel</a>
            </div>

        </form:form>
    </div>
</div>
<footer>© 2024 Library Management System</footer>
</body>
</html>
