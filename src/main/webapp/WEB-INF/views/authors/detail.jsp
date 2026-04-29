<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${author.name} – LibraryMS</title>
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
        <h1>🖊️ ${author.name}</h1>
        <div style="display:flex; gap:.6rem;">
            <a href="${pageContext.request.contextPath}/authors/edit/${author.id}" class="btn btn-primary">✏️ Edit</a>
            <a href="${pageContext.request.contextPath}/authors" class="btn btn-outline">← Back</a>
        </div>
    </div>

    <div class="card" style="padding:1.8rem; margin-bottom:1.5rem;">
        <div class="detail-grid">
            <div class="detail-item"><label>Full Name</label><p>${author.name}</p></div>
            <div class="detail-item"><label>Nationality</label>
                <p><span class="badge">${author.nationality}</span></p></div>
            <div class="detail-item"><label>Birth Year</label><p>${author.birthYear}</p></div>
            <div class="detail-item"><label>Books Published</label>
                <p><span class="badge badge-green">${author.books.size()}</span></p></div>
        </div>
        <c:if test="${not empty author.bio}">
            <div style="margin-top:1.2rem; padding-top:1.2rem; border-top:1px solid #dce1e7;">
                <label style="font-size:.8rem; color:#636e72; text-transform:uppercase; letter-spacing:.5px;">Biography</label>
                <p style="margin-top:.4rem; line-height:1.7;">${author.bio}</p>
            </div>
        </c:if>
    </div>

    <%-- Books by this author --%>
    <h2 style="font-size:1.1rem; color:#2c3e50; margin-bottom:1rem;">Books by ${author.name}</h2>
    <div class="card">
        <div class="table-wrap">
            <table>
                <thead><tr><th>Title</th><th>ISBN</th><th>Genre</th><th>Year</th><th>Price</th><th>Action</th></tr></thead>
                <tbody>
                <c:forEach var="book" items="${author.books}">
                    <tr>
                        <td><strong>${book.title}</strong></td>
                        <td style="font-family:monospace; font-size:.85rem;">${book.isbn}</td>
                        <td><span class="badge badge-orange">${book.genre}</span></td>
                        <td>${book.publishedYear}</td>
                        <td>$<fmt:formatNumber value="${book.price}" pattern="0.00"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/books/edit/${book.id}" class="btn btn-primary btn-sm">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty author.books}">
                    <tr><td colspan="6" style="text-align:center; padding:1.5rem; color:#636e72;">No books found for this author.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<footer>© 2024 Library Management System</footer>
</body>
</html>
