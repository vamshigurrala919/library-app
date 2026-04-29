<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Authors – LibraryMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
        <h1>🖊️ All Authors</h1>
        <a href="${pageContext.request.contextPath}/authors/new" class="btn btn-primary">+ Add New Author</a>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="card">
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Nationality</th>
                        <th>Birth Year</th>
                        <th>Books</th>
                        <th>Bio (excerpt)</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:set var="count" value="1"/>
                <c:forEach var="author" items="${authors}">
                    <tr>
                        <td style="color:#636e72;">${count}</td>
                        <td><strong>${author.name}</strong></td>
                        <td><span class="badge">${author.nationality}</span></td>
                        <td>${author.birthYear}</td>
                        <td>
                            <span class="badge badge-green">${author.books.size()} book(s)</span>
                        </td>
                        <td style="max-width:250px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; color:#636e72;">
                            ${author.bio}
                        </td>
                        <td style="white-space:nowrap;">
                            <a href="${pageContext.request.contextPath}/authors/${author.id}"
                               class="btn btn-outline btn-sm">View</a>
                            <a href="${pageContext.request.contextPath}/authors/edit/${author.id}"
                               class="btn btn-primary btn-sm">Edit</a>
                        </td>
                    </tr>
                    <c:set var="count" value="${count + 1}"/>
                </c:forEach>
                <c:if test="${empty authors}">
                    <tr><td colspan="7" style="text-align:center; padding:2rem; color:#636e72;">
                        No authors found. <a href="${pageContext.request.contextPath}/authors/new">Add one!</a>
                    </td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<footer>© 2024 Library Management System</footer>
</body>
</html>
