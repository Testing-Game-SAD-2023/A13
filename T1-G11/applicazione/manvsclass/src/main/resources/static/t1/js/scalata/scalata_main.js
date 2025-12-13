document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delete-button").forEach(btn => {

        btn.addEventListener("click", async () => {
            const name = btn.getAttribute("data-name");

            if (!confirm(`Sei sicuro di voler eliminare la scalata '${name}'?`)) {
                return;
            }

            await callDeleteScalata(name);
        });
    });
});