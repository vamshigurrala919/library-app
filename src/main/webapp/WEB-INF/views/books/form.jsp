<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle} – LibraryMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-brand">📚 <span>Library</span>MS</div>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/books" class="active">Books</a></li>
        <li><a href="${pageContext.request.contextPath}/authors">Authors</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>${book.id == null ? '➕ Add New Book' : '✏️ Edit Book'}</h1>
        <a href="${pageContext.request.contextPath}/books" class="btn btn-outline">← Back to Books</a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="form-card">

        <%-- Choose action URL based on create vs update --%>
        <c:choose>
            <c:when test="${book.id == null}">
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/books/save"/>
            </c:when>
            <c:otherwise>
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/books/update/${book.id}"/>
            </c:otherwise>
        </c:choose>

        <form:form modelAttribute="book" action="${actionUrl}" method="post">

            <div class="form-group">
                <form:label path="title">Book Title *</form:label>
                <form:input path="title" cssClass="form-control" placeholder="e.g. The Great Gatsby"/>
                <form:errors path="title" cssClass="field-error"/>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <form:label path="isbn">ISBN *</form:label>
                    <form:input path="isbn" cssClass="form-control" placeholder="e.g. 978-0-7432-7356-5"/>
                    <form:errors path="isbn" cssClass="field-error"/>
                </div>
                <div class="form-group">
                    <form:label path="publishedYear">Published Year *</form:label>
                    <form:input path="publishedYear" type="number" cssClass="form-control" placeholder="e.g. 1925"/>
                    <form:errors path="publishedYear" cssClass="field-error"/>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <form:label path="genre">Genre *</form:label>
                    <form:select path="genre" cssClass="form-control">
                        <form:option value="" label="-- Select Genre --"/>
                        <form:option value="Fantasy"          label="Fantasy"/>
                        <form:option value="Dystopia"         label="Dystopia"/>
                        <form:option value="Satire"           label="Satire"/>
                        <form:option value="Adventure"        label="Adventure"/>
                        <form:option value="Romance"          label="Romance"/>
                        <form:option value="Historical"       label="Historical"/>
                        <form:option value="Magical Realism"  label="Magical Realism"/>
                        <form:option value="Literary Fiction" label="Literary Fiction"/>
                        <form:option value="Science Fiction"  label="Science Fiction"/>
                        <form:option value="Mystery"          label="Mystery"/>
                        <form:option value="Biography"        label="Biography"/>
                        <form:option value="Other"            label="Other"/>
                    </form:select>
                    <form:errors path="genre" cssClass="field-error"/>
                </div>
                <div class="form-group">
                    <form:label path="price">Price ($)</form:label>
                    <form:input path="price" type="number" step="0.01" cssClass="form-control" placeholder="e.g. 14.99"/>
                    <form:errors path="price" cssClass="field-error"/>
                </div>
            </div>

            <div class="form-group">
                <form:label path="author">Author *</form:label>
                <form:select path="author.id" cssClass="form-control">
                    <form:option value="" label="-- Select Author --"/>
                    <c:forEach var="author" items="${authors}">
                        <option value="${author.id}"
                            <c:if test="${book.author != null && book.author.id == author.id}">selected</c:if>>
                            ${author.name} (${author.nationality})
                        </option>
                    </c:forEach>
                </form:select>
                <form:errors path="author" cssClass="field-error"/>
            </div>

            <div class="form-actions">
                <c:choose>
                    <c:when test="${book.id == null}">
                        <button type="submit" class="btn btn-primary">💾 Save Book</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="btn btn-success">✅ Update Book</button>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/books" class="btn btn-outline">Cancel</a>
            </div>

        </form:form>
    </div>
</div>

<footer>© 2024 Library Management System</footer>
</body>
</html>
