document.addEventListener("DOMContentLoaded", () => {
  const editBioButton = document.getElementById("editBioButton");
  const saveBioButton = document.getElementById("saveBioButton");
  const bioDisplaySection = document.getElementById("bioDisplaySection");
  const bioEditSection = document.getElementById("bioEditSection");
  const bioText = document.getElementById("bioText");
  const biographyInput = document.getElementById("biography");

  // Passa alla modalità modifica
  editBioButton.addEventListener("click", () => {
      biographyInput.value = bioText.textContent.trim();
      bioDisplaySection.style.display = "none";
      bioEditSection.style.display = "block";
  });
  //--Modifica (28/11) by GabMan 'Event Listener per le sezioni della biografia'-->
  // Salva la biografia
  saveBioButton.addEventListener("click", async () => {
      const bio = biographyInput.value;

      try {
          const response = await fetch("/updateBiography", {
              method: "POST",
              headers: {
                  "Content-Type": "application/x-www-form-urlencoded"
              },
              body: new URLSearchParams({ biography: bio })
          });

          if (response.ok) {
              bioText.textContent = bio;
              bioEditSection.style.display = "none";
              bioDisplaySection.style.display = "block";
              document.getElementById("saveMessage").style.display = "block";
              setTimeout(() => {
                  document.getElementById("saveMessage").style.display = "none";
              }, 3000);
          } else {
              const error = await response.text();
              alert("Errore nel salvataggio: " + error);
          }
      } catch (error) {
          alert("Errore nella connessione al server.");
      }
  });
});
