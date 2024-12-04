const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
}

const parseJwt = (token) => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

document.addEventListener("DOMContentLoaded", function () {
  // Gestione dei tab dei trofei
  const trophyTabs = document.querySelectorAll('#trophyTabs button[data-bs-toggle="tab"]');
  trophyTabs.forEach(tab => {
      tab.addEventListener('shown.bs.tab', function (event) {
          console.log(`Tab attivo: ${event.target.id}`);
      });
  });

  // Gestione delle notifiche
  const notificationsList = document.querySelector('.notifications-list');

    if (notificationsList) {
        const userEmail = document.querySelector('#user-email')?.textContent.trim();

        // Gestione click sui pulsanti delle notifiche
        notificationsList.addEventListener('click', async function (e) {
            const notificationItem = e.target.closest('.notification-item');
            if (!notificationItem) return;

            const notificationId = notificationItem ? notificationItem.getAttribute('data-notification-id') : null;
            // Gestione pulsante "Leggi"
            if (e.target.classList.contains('read-btn')) {
                try {
                    const formData = new URLSearchParams();
                    formData.append("email", userEmail);
                    formData.append("id", notificationId);

                    const response = await fetch("/read-notification", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded",
                        },
                        body: formData.toString(),
                    });

                    if (response.ok) {
                        const statusBadge = notificationItem.querySelector('.notification-status-badge');
                        statusBadge.textContent = 'Letta';
                        statusBadge.classList.remove('status-unread');
                        statusBadge.classList.add('status-read');
                    } else {
                        const errorMessage = await response.text();
                        alert("Errore nella lettura della notifica: " + errorMessage);
                    }
                } catch (error) {
                    console.error("Errore di rete:", error);
                    alert("Errore di rete. Riprova più tardi.");
                }
            }

            // Gestione pulsante "Elimina"
            if (e.target.classList.contains('delete-btn')) {
                try {
                    const formData = new URLSearchParams();
                    formData.append("email", userEmail);
                    formData.append("id", notificationId);

                    const response = await fetch("/delete-notification", {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded",
                        },
                        body: formData.toString(),
                    });

                    if (response.ok) {
                        notificationItem.remove();

                        // Se non ci sono notifiche, aggiungi il messaggio "Nessuna notifica disponibile"
                        if (!notificationsList.querySelector('.notification-item')) {
                            const noNotificationsMsg = document.createElement('p');
                            noNotificationsMsg.className = 'no-notifications';
                            noNotificationsMsg.textContent = 'Nessuna notifica disponibile.';
                            notificationsList.parentElement.appendChild(noNotificationsMsg);
                        }
                    } else {
                        const errorMessage = await response.text();
                        alert("Errore nell'eliminazione della notifica: " + errorMessage);
                    }
                } catch (error) {
                    console.error("Errore di rete:", error);
                    alert("Errore di rete. Riprova più tardi.");
                }
            }
        });
    }
});