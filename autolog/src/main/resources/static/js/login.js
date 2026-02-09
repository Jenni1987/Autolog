document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    if (username && password) {
        alert("Login correcto (modo demo)");
        
        // FUTURO:
        // fetch("/api/auth/login", { ... })
    }
});