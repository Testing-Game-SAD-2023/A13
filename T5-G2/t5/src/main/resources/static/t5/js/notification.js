document.addEventListener("DOMContentLoaded", function () {
    const userId = parseJwt(getCookie("jwt")).userId;
    const url = `http://localhost/read_notifications?userId=${encodeURIComponent(userId)}`;
    let notificationsArray = [];
    const badge = document.getElementById("notification_badge");
    const notificationMenu = document.getElementById("notification_menu");

    if (!badge || !notificationMenu) {
        console.warn("Elemento HTML per le notifiche non trovato.");
        return;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (Array.isArray(data) && data.length > 0) {
                notificationsArray = data;
                badge.style.display = "inline-block";
                badge.textContent = data.length > 99 ? "99+" : data.length;
                updateUI();
            } else {
                badge.style.display = "none";
                notificationMenu.innerHTML = "<li><p class='dropdown-item'>Nessuna notifica disponibile</p></li>";
            }
        })
        .catch(error => console.error("Errore nel recupero delle notifiche:", error));

    function updateUI() {
        notificationMenu.innerHTML = "";
        
        notificationsArray.forEach(notification => {
            const notificationItem = document.createElement("li");
            notificationItem.classList.add("dropdown-item", "d-flex", "justify-content-between", "align-items-start");
            notificationItem.innerHTML = `
                <div class="ms-2 me-auto">
                    <div class="fw-bold">${notification.titolo}</div>
                    ${notification.message}
                </div>
            `;
            notificationMenu.appendChild(notificationItem);
        });
    }
});
