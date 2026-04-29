<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${pageTitle} – LibraryMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/views/style.css">
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
        <h1>📚 All Books</h1>
        <a href="${pageContext.request.contextPath}/books/new" class="btn btn-primary">+ Add New Book</a>
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
                        <th>Title</th>
                        <th>ISBN</th>
                        <th>Author</th>
                        <th>Nationality</th>
                        <th>Genre</th>
                        <th>Year</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:set var="count" value="1"/>
                <c:forEach var="row" items="${books}">
                    <tr>
                        <td style="color:#636e72;">${count}</td>
                        <td><strong>${row.bookTitle}</strong></td>
                        <td style="font-family:monospace; font-size:.85rem;">${row.isbn}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/authors/${row.authorId}"
                               style="color:#2c3e50; font-weight:600;">${row.authorName}</a>
                        </td>
                        <td><span class="badge">${row.authorNationality}</span></td>
                        <td><span class="badge badge-orange">${row.genre}</span></td>
                        <td>${row.publishedYear}</td>
                        <td>$<fmt:formatNumber value="${row.price}" pattern="0.00"/></td>
                        <td style="white-space:nowrap;">
                            <a href="${pageContext.request.contextPath}/books/${row.bookId}"
                               class="btn btn-outline btn-sm">View</a>
                            <a href="${pageContext.request.contextPath}/books/edit/${row.bookId}"
                               class="btn btn-primary btn-sm">Edit</a>
                        </td>
                    </tr>
                    <c:set var="count" value="${count + 1}"/>
                </c:forEach>
                <c:if test="${empty books}">
                    <tr><td colspan="9" style="text-align:center; padding:2rem; color:#636e72;">
                        No books found. <a href="${pageContext.request.contextPath}/books/new">Add one!</a>
                    </td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <p style="margin-top:1rem; color:#636e72; font-size:.85rem;">
        ℹ️ This list uses an <strong>INNER JOIN</strong> between the Books and Authors tables.
    </p>
</div>

<footer>© 2024 Library Management System</footer>
</body>
</html>
