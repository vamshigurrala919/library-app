<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${book.title} – LibraryMS</title>
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
        <h1>📖 ${book.title}</h1>
        <div style="display:flex; gap:.6rem;">
            <a href="${pageContext.request.contextPath}/books/edit/${book.id}" class="btn btn-primary">✏️ Edit</a>
            <a href="${pageContext.request.contextPath}/books" class="btn btn-outline">← Back</a>
        </div>
    </div>

    <div class="card" style="padding:1.8rem;">
        <div class="detail-grid">
            <div class="detail-item"><label>Title</label><p>${book.title}</p></div>
            <div class="detail-item"><label>ISBN</label><p style="font-family:monospace;">${book.isbn}</p></div>
            <div class="detail-item"><label>Genre</label><p><span class="badge badge-orange">${book.genre}</span></p></div>
            <div class="detail-item"><label>Published Year</label><p>${book.publishedYear}</p></div>
            <div class="detail-item">
                <label>Price</label>
                <p>$<fmt:formatNumber value="${book.price}" pattern="0.00"/></p>
            </div>
            <div class="detail-item">
                <label>Author</label>
                <p><a href="${pageContext.request.contextPath}/authors/${book.author.id}">${book.author.name}</a></p>
            </div>
        </div>
    </div>
</div>
<footer>© 2024 Library Management System</footer>
</body>
</html>
