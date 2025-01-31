async function createTeam() {
    const teamName = document.getElementById("teamName").value;
    const teamDescription = document.getElementById("teamDescription").value;

    const teamData = {
        name: teamName,
        description: teamDescription
    };

    try {
        const response = await fetch("/api/team", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(teamData)
        });

        if (response.ok) {
            const createdTeam = await response.json();
            alert(`Team created: Name=${createdTeam.name}`);
            fetchTeams();  // Aggiorna la lista dei team
        } else {
            const errorText = await response.text();
            alert("Failed to create team: " + errorText);
        }
    } catch (error) {
        alert("Error while creating team: " + error.message);
    }
}

function updateTeamsTable(teams) {
    const tableBody = document.getElementById("teamsTable").getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';  // Pulisce la tabella prima di aggiungere nuovi dati

    teams.forEach(team => {
        const row = tableBody.insertRow();
        const idCell = row.insertCell(0);
        const nameCell = row.insertCell(1);
        const descriptionCell = row.insertCell(2);
        const actionCell = row.insertCell(3);

        idCell.textContent = team.id;

        // Celle non editabili
        nameCell.textContent = team.name;
        descriptionCell.textContent = team.description;

        // Pulsante Modifica
        const editButton = document.createElement("button");
        editButton.textContent = "Edit";
        editButton.className = "edit-button";
        editButton.onclick = () => enableEditing(row, team.id);
        actionCell.appendChild(editButton);

        // Pulsante Studenti
        const studentsButton = document.createElement("button");
        studentsButton.textContent = "Students";
        studentsButton.className = "students-button";
        studentsButton.onclick = () => openTeamPage(team.id);
        actionCell.appendChild(studentsButton);

        // Pulsante Elimina
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-button";
        deleteButton.onclick = () => deleteTeam(team.id);
        actionCell.appendChild(deleteButton);
    });
}

function openTeamPage(teamId) {
    window.location.href = `/team_dashboard/${teamId}`;
}


function enableEditing(row, teamId) {
    const nameCell = row.cells[1];
    const descriptionCell = row.cells[2];

    const nameInput = document.createElement("input");
    nameInput.type = "text";
    nameInput.value = nameCell.textContent;

    const descriptionInput = document.createElement("input");
    descriptionInput.type = "text";
    descriptionInput.value = descriptionCell.textContent;

    nameCell.innerHTML = '';
    descriptionCell.innerHTML = '';
    nameCell.appendChild(nameInput);
    descriptionCell.appendChild(descriptionInput);

    nameInput.addEventListener("keydown", event => {
        if (event.key === "Enter") saveChanges(teamId, nameInput.value, descriptionInput.value);
    });
    descriptionInput.addEventListener("keydown", event => {
        if (event.key === "Enter") saveChanges(teamId, nameInput.value, descriptionInput.value);
    });
}

async function saveChanges(teamId, name, description) {
    const teamData = { name, description };

    try {
        const response = await fetch(`/api/team/${teamId}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(teamData)
        });

        if (response.ok) {
            alert(`Team ${teamId} updated successfully.`);
            fetchTeams();
        } else {
            const errorText = await response.text();
            alert("Failed to update team: " + errorText);
        }
    } catch (error) {
        alert("Error while updating team: " + error.message);
    }
}

async function deleteTeam(teamId) {
    try {
        const response = await fetch(`/api/team/${teamId}`, {
            method: "DELETE"
        });

        if (response.ok) {
            alert(`Team with ID ${teamId} has been deleted.`);
            fetchTeams();  // Ricarica la lista dei team dopo l'eliminazione
        } else {
            const errorText = await response.text();
            alert("Failed to delete team: " + errorText);
        }
    } catch (error) {
        alert("Error while deleting team: " + error.message);
    }
}

async function fetchTeams() {
    try {
        const response = await fetch("/api/team", {
            method: "GET" 
        });
        if (response.ok) {
            const teams = await response.json();
            updateTeamsTable(teams);
            document.getElementById("fetchTeamsButton").style.display = "none";
            document.getElementById("teamsTable").style.display = "table";
        } else {
            const errorText = await response.text();
            alert("Failed to fetch teams: " + errorText);
        }
    } catch (error) {
        alert("Error while fetching teams: " + error.message);
    }
}

document.getElementById("backButton").addEventListener("click", () => {
    window.history.back();
});

function resetFields() {
    // Reset dei campi di input
    document.getElementById('teamName').value = '';
    document.getElementById('teamDescription').value = '';
}