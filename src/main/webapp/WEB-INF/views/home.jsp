<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Library Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/views/style.css">
</head>
<body>

<%-- ── Navbar ── --%>
<nav class="navbar">
    <div class="navbar-brand">📚 <span>Library</span>MS</div>
    <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/" class="active">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/books">Books</a></li>
        <li><a href="${pageContext.request.contextPath}/authors">Authors</a></li>
    </ul>
</nav>

<div class="container">

    <div class="page-header">
        <h1>📖 Dashboard</h1>
    </div>

    <%-- Flash messages --%>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <%-- Stat cards --%>
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-number">${totalBooks}</div>
            <div class="stat-label">Total Books</div>
        </div>
        <div class="stat-card green">
            <div class="stat-number">${totalAuthors}</div>
            <div class="stat-label">Total Authors</div>
        </div>
    </div>

    <%-- Recent books table (inner-join data) --%>
    <div class="card">
        <div style="padding:1.2rem 1.5rem; border-bottom:1px solid #dce1e7; display:flex; justify-content:space-between; align-items:center;">
            <h2 style="font-size:1.1rem; color:#2c3e50;">All Books &amp; Authors</h2>
            <div style="display:flex; gap:.6rem;">
                <a href="${pageContext.request.contextPath}/books/new"   class="btn btn-primary btn-sm">+ Add Book</a>
                <a href="${pageContext.request.contextPath}/authors/new" class="btn btn-success btn-sm">+ Add Author</a>
            </div>
        </div>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Nationality</th>
                        <th>Genre</th>
                        <th>Year</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="row" items="${recentBooks}">
                    <tr>
                        <td><strong>${row.bookTitle}</strong></td>
                        <td>${row.authorName}</td>
                        <td><span class="badge">${row.authorNationality}</span></td>
                        <td><span class="badge badge-orange">${row.genre}</span></td>
                        <td>${row.publishedYear}</td>
                        <td>$<fmt:formatNumber value="${row.price}" pattern="0.00"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/books/edit/${row.bookId}" class="btn btn-outline btn-sm">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty recentBooks}">
                    <tr><td colspan="7" style="text-align:center; color:#636e72; padding:2rem;">No books found.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<footer>© 2024 Library Management System – Spring Boot + JPA + JSP</footer>
</body>
</html>
