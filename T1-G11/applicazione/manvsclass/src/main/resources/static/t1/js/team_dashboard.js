let teamId;  // Variabile globale per il teamId
let allStudents = [];  // Variabile globale per tutti gli studenti
let teamStudents = [];  // Variabile globale per gli studenti del team

// Funzione per ottenere il teamId dal percorso URL
function getTeamIdFromPathname() {
    const pathname = window.location.pathname;  // Ottieni il percorso dell'URL
    const parts = pathname.split('/');  // Divide il percorso in base al separatore "/"
    return parts[parts.length - 1];  // Restituisce l'ultimo segmento del percorso (il teamId)
}

window.onload = async () => {
    teamId = getTeamIdFromPathname();  // Ottieni il teamId dalla URL
    if (teamId) {
        await fetchTeamDetails();
        await fetchTeamStudents();
        await fetchTeamExercises();  // Recupera gli esercizi del team

        const addStudentsButton = document.getElementById("addStudentsButton");
        addStudentsButton.addEventListener("click", () => {
            toggleStudentsContainer();
            if (document.getElementById("studentsContainer").style.display === "block") {
                fetchAllStudents();  // Recupera tutti gli studenti se il contenitore è visibile
            }
        });

        const deleteStudentsButton = document.getElementById("deleteStudentsButton");
        deleteStudentsButton.addEventListener("click", () => {
            toggleDeleteStudentsContainer();
            if (document.getElementById("deleteStudentsContainer").style.display === "block") {
                fetchTeamStudentsForDelete();  // Recupera gli studenti del team per la cancellazione
            }
        });

    } else {
        alert(`Team ID is missing in the URL`);
    }
};

async function fetchTeamDetails() {
    try {
        const response = await fetch(`/api/team/${teamId}`);
        if (response.ok) {
            const team = await response.json();
            document.getElementById("teamId").textContent = teamId;
            document.getElementById("teamName").textContent = team.name;
            document.getElementById("teamDescription").textContent = team.description;
        } else {
            alert(`Failed to fetch team details`);
        }
    } catch (error) {
        alert(`Error while fetching team details: ` + error.message);
    }
}

async function fetchTeamStudents() {
    try {
        const teamStudentsContainer = document.getElementById("teamStudentsContainer");
        const loadingStudentsIndicator = document.getElementById("loadingStudentsIndicator");
        const studentsTableBody = document.querySelector("#teamStudentsTable tbody");

        // Mostra l'indicatore di caricamento e nasconde la tabella
        loadingStudentsIndicator.style.display = "block";
        teamStudentsContainer.style.display = "none";
        studentsTableBody.innerHTML = '';

        const response = await fetch(`/api/team/${teamId}/students`);
        if (response.ok) {
            teamStudents = await response.json();

            if (teamStudents.length === 0) {
                const row = document.createElement("tr");
                row.innerHTML = `<td colspan="5">No students in this team.</td>`;
                studentsTableBody.appendChild(row);
            } else {
                teamStudents.forEach(student => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${student.ID}</td>
                        <td>${student.name}</td>
                        <td>${student.surname}</td>
                        <td>${student.email}</td>
                        <td>${student.studies}</td>
                    `;
                    studentsTableBody.appendChild(row);
                });
            }

            // Nasconde l'indicatore di caricamento e mostra la tabella
            loadingStudentsIndicator.style.display = "none";
            teamStudentsContainer.style.display = "block";
        } else {
            alert(`Failed to fetch team students`);
        }
    } catch (error) {
        alert(`Error while fetching team students: ` + error.message);
    }
}

document.getElementById("addButton").addEventListener("click", () => {
    const selectedStudents = [];
    const checkboxes = document.querySelectorAll(".selectStudent:checked");

    checkboxes.forEach(checkbox => {
        const studentId = checkbox.getAttribute("data-id");
        selectedStudents.push(studentId);
    });

    // Esegui una richiesta PUT per ogni studente selezionato
    selectedStudents.forEach(studentId => {
        addStudentToTeam(studentId);
    });

    // Nascondi la finestra
    const studentsContainer = document.getElementById("studentsContainer");
    studentsContainer.style.display = "none";
});

async function fetchAllStudents() {
    try {
        const response = await fetch(`/api/students_list`);
        if (response.ok) {
            allStudents = await response.json();
            updateStudentsList(); // Dopo aver ottenuto tutti gli studenti, recupera quelli del team
        } else {
            alert(`Failed to fetch students`);
        }
    } catch (error) {
        alert(`Error while fetching students: ` + error.message);
    }
}

function updateStudentsList() {
    // Filtra gli studenti che non sono già nel team
    const students = allStudents.filter(student => 
        !teamStudents.some(teamStudent => teamStudent.ID === student.ID)
    );

    const studentsTableBody = document.querySelector("#studentsTable tbody");
    studentsTableBody.innerHTML = ''; // Pulisce il contenuto precedente

    if (students.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="6">No available students to add to the team.</td>`;
        studentsTableBody.appendChild(row);
    } else {
        students.forEach(student => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${student.ID}</td>
                <td>${student.name}</td>
                <td>${student.surname}</td>
                <td>${student.email}</td>
                <td>${student.studies}</td>
                <td><input type="checkbox" class="selectStudent" data-id="${student.ID}"></td>
            `;
            studentsTableBody.appendChild(row);
        });
    }
}

function toggleStudentsContainer() {
    const studentsContainer = document.getElementById("studentsContainer");
    if (studentsContainer.style.display === "none" || studentsContainer.style.display === "") {
        studentsContainer.style.display = "block";
    } else {
        studentsContainer.style.display = "none";
    }
}

async function addStudentToTeam(studentId) {
    try {
        const response = await fetch(`/api/team/${teamId}/students`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ ID: studentId })  // Passa l'ID dello studente
        });

        if (response.ok) {
            // alert(`Student with ID ${studentId} added to team ${teamId}`);
            // Ricarica gli studenti e gli esercizidel team
            fetchTeamStudents();
            fetchTeamExercises();
        } else {
            alert(`Failed to add student with ID ${studentId} to team`);
        }
    } catch (error) {
        alert(`Error while adding student: ` + error.message);
    }
}

function toggleDeleteStudentsContainer() {
    const deleteStudentsContainer = document.getElementById("deleteStudentsContainer");
    if (deleteStudentsContainer.style.display === "none" || deleteStudentsContainer.style.display === "") {
        deleteStudentsContainer.style.display = "block";
    } else {
        deleteStudentsContainer.style.display = "none";
    }
}

document.getElementById("deleteButton").addEventListener("click", () => {
    const selectedStudents = [];
    const checkboxes = document.querySelectorAll(".deleteStudent:checked");

    checkboxes.forEach(checkbox => {
        const studentId = checkbox.getAttribute("data-id");
        selectedStudents.push(studentId);
    });

    // Esegui una richiesta DELETE per ogni studente selezionato
    selectedStudents.forEach(studentId => {
        deleteStudentFromTeam(studentId);
    });

    // Nascondi la finestra
    const deleteStudentsContainer = document.getElementById("deleteStudentsContainer");
    deleteStudentsContainer.style.display = "none";
});

// Funzione per recuperare gli studenti nel team da eliminare
async function fetchTeamStudentsForDelete() {
    try {
        const response = await fetch(`/api/team/${teamId}/students`);
        if (response.ok) {
            teamStudents = await response.json();
            updateTeamStudentsTableForDelete();
        } else {
            alert("Failed to fetch team students for deletion");
        }
    } catch (error) {
        alert("Error while fetching team students for deletion: " + error.message);
    }
}

// Funzione per aggiornare la tabella degli studenti da eliminare
function updateTeamStudentsTableForDelete() {
    const deleteStudentsTableBody = document.querySelector("#deleteStudentsTable tbody");
    deleteStudentsTableBody.innerHTML = '';  // Pulisce la tabella precedente

    if (teamStudents.length === 0) {
        const row = document.createElement("tr");
        row.innerHTML = `<td colspan="6">No students to delete in this team.</td>`;
        deleteStudentsTableBody.appendChild(row);
    } else {
        teamStudents.forEach(student => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${student.ID}</td>
                <td>${student.name}</td>
                <td>${student.surname}</td>
                <td>${student.email}</td>
                <td>${student.studies}</td>
                <td><input type="checkbox" class="deleteStudent" data-id="${student.ID}"></td>
            `;
            deleteStudentsTableBody.appendChild(row);
        });
    }
}

async function deleteStudentFromTeam(studentId) {
    try {
        const response = await fetch(`/api/team/${teamId}/students`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ ID: studentId })  // Passa l'ID dello studente
        });

        if (response.ok) {
            // alert(`Student with ID ${studentId} removed from team ${teamId}`);
            // Ricarica gli studenti e gli esercizi del team
            fetchTeamStudents();
            fetchTeamExercises();
        } else {
            alert(`Failed to delete student with ID ${studentId} from team`);
        }
    } catch (error) {
        alert(`Error while deleting student: ` + error.message);
    }
}

document.getElementById("backButton").addEventListener("click", () => {
    window.history.back();
});


// Assicurati che ci sia un solo listener per il pulsante "AddExercise"
document.getElementById("addExerciseButton").addEventListener("click", () => {
    const exerciseContainer = document.getElementById("exerciseContainer");
    if (exerciseContainer.style.display === "none" || exerciseContainer.style.display === "") {
        exerciseContainer.style.display = "block";
    } else {
        exerciseContainer.style.display = "none";
        resetExerciseForm(); // Resetta il form quando viene chiuso
    }
});

// Funzione per resettare il form di aggiunta degli esercizi
function resetExerciseForm() {
    document.getElementById("exerciseForm").reset();
    document.getElementById("goalTypesContainer").innerHTML = ''; // Pulisce i goal types aggiunti
}

// Listener per aggiungere goal types
document.getElementById("addGoalTypeButton").addEventListener("click", () => {
    const goalTypesContainer = document.getElementById("goalTypesContainer");
    const goalTypeDiv = document.createElement("div");
    goalTypeDiv.innerHTML = `
        <div class="goalTypeSection">
            <label>Type:</label>
            <input type="number" class="goalType" name="type" required>
        </div>
        <div class="goalExpectedScoreSection">
            <label>Expected Score:</label>
            <input type="range" class="goalExpectedScore" name="expectedScore" min="1" max="100" value="50" required>
            <span class="expectedScoreValue">50</span>
        </div>
        <div class="goalClassNameSection">
            <label>Class Name:</label>
            <input type="text" class="goalClassName" name="className" required>
        </div>
        <button type="button" class="removeGoalTypeButton" style="border: 1px solid grey; padding: 5px;">Remove Goal Type</button>
    `;
    goalTypesContainer.appendChild(goalTypeDiv);

    // Aggiorna il valore dello span quando si cambia il valore dello slider
    goalTypeDiv.querySelector(".goalExpectedScore").addEventListener("input", (event) => {
        goalTypeDiv.querySelector(".expectedScoreValue").textContent = event.target.value;
    });

    goalTypeDiv.querySelector(".removeGoalTypeButton").addEventListener("click", () => {
        goalTypesContainer.removeChild(goalTypeDiv);
    });
});

// Funzione per inviare la richiesta HTTP PUT all'API
async function addExerciseToTeam(exerciseData) {
    try {
        const response = await fetch(`/api/team/${teamId}/exercise`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(exerciseData)
        });

        const contentType = response.headers.get("content-type");
        if (contentType) {
            const result = await response.json();
            if (response.ok) {
                fetchTeamExercises();  // Aggiorna la tabella degli esercizi
                // Nascondi il contenitore dell'esercizio
                document.getElementById("exerciseContainer").style.display = "none";
                resetExerciseForm(); // Resetta il form quando viene chiuso
                // alert('Exercise added successfully'); 
            } else {
                alert(`Failed to add exercise: ${result.message}`);
            }
        } else {
            const result = await response.text();
            alert(`Unexpected response format: ${result}`);
        }
    } catch (error) {
        alert(`Error while adding exercise: ${error.message}`);
    }
}

// Funzione per inviare la richiesta HTTP POST all'API per aggiornare un esercizio
async function updateExercise(exerciseId, description, expiryTime) {
    try {
        // Formatta correttamente il valore expiryTime
        const formattedExpiryTime = new Date(expiryTime).toISOString();

        const response = await fetch(`/api/team/${teamId}/exercise/${exerciseId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ description, expiryTime: formattedExpiryTime })
        });

        if (response.ok) {
            // Ricarica gli esercizi del team
            fetchTeamExercises();
            // alert('Exercise updated successfully');  
        } else {
            alert(`Failed to update exercise with ID ${exerciseId}`);
        }
    } catch (error) {
        alert(`Error while updating exercise: ${error.message}`);
    }
}

// Funzione per inviare la richiesta HTTP DELETE all'API per eliminare un esercizio
async function deleteExerciseFromTeam(exerciseId) {
    try {
        const response = await fetch(`/api/team/${teamId}/exercise/${exerciseId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            // Ricarica gli esercizi del team
            fetchTeamExercises();
            // alert('Exercise deleted successfully');  
        } else {
            alert(`Failed to delete exercise with ID ${exerciseId} from team`);
        }
    } catch (error) {
        alert(`Error while deleting exercise: ${error.message}`);
    }
}

// Listener per il submit del form
document.getElementById("exerciseForm").addEventListener("submit", (event) => {
    event.preventDefault();

    const goalTypes = [];
    document.querySelectorAll("#goalTypesContainer > div").forEach(goalTypeDiv => {
        const type = parseInt(goalTypeDiv.querySelector(".goalType").value, 10);
        const expectedScore = parseInt(goalTypeDiv.querySelector(".goalExpectedScore").value, 10);
        const className = goalTypeDiv.querySelector(".goalClassName").value;
        goalTypes.push({ type, expectedScore, className });
    });

    const expiryTime = new Date(document.getElementById("expiryTime").value).toISOString();
    const startingTime = new Date(document.getElementById("startingTime").value).toISOString();
    const description = document.getElementById("description").value;
    const creationTime = new Date().toISOString();

    const exerciseData = {
        goalTypes: goalTypes.map(gt => JSON.stringify(gt)),
        expiryTime,
        startingTime,
        creationTime,
        description
    };

    // Invia la richiesta HTTP PUT all'API
    addExerciseToTeam(exerciseData);
});

async function fetchTeamExercises() {
    try {
        const teamExercisesContainer = document.getElementById("teamExercisesContainer");
        const loadingIndicator = document.getElementById("loadingIndicator");
        const exercisesTableBody = document.querySelector("#teamExercisesTable tbody");

        // Mostra l'indicatore di caricamento e nasconde la tabella
        loadingIndicator.style.display = "block";
        teamExercisesContainer.style.display = "none";
        exercisesTableBody.innerHTML = '';

        const response = await fetch(`/api/team/${teamId}`);
        if (response.ok) {
            const team = await response.json();
            const exerciseIds = team.exerciseList;

            if (exerciseIds.length === 0) {
                const row = document.createElement("tr");
                row.innerHTML = `<td colspan="5">No exercises assigned to this team.</td>`;
                exercisesTableBody.appendChild(row);
            } else {
                for (const exerciseId of exerciseIds) {
                    await fetchExerciseDetails(exerciseId);
                }
            }

            // Nasconde l'indicatore di caricamento e mostra la tabella
            loadingIndicator.style.display = "none";
            teamExercisesContainer.style.display = "block";
        } else {
            alert(`Failed to fetch team exercises`);
        }
    } catch (error) {
        alert(`Error while fetching team exercises: ` + error.message);
    }
}

async function fetchExerciseDetails(exerciseId) {
    try {
        const response = await fetch(`/api/team/${teamId}/exercise/${exerciseId}`);
        if (response.ok) {
            const exercise = await response.json();
            updateTeamExercisesTable(exercise);
        } else {
            alert(`Failed to fetch exercise details for ID ${exerciseId}`);
        }
    } catch (error) {
        alert(`Error while fetching exercise details: ` + error.message);
    }
}

function updateTeamExercisesTable(exercise) {
    const exercisesTableBody = document.querySelector("#teamExercisesTable tbody");
    const row = document.createElement("tr");

    // Parse goalTypes
    const goalTypes = exercise.goalTypes.map(gt => JSON.parse(gt));
    const goalTypesHtml = goalTypes.map(gt => `
        <div>
            <em>Type:</em> ${gt.type}, 
            <em>Expected Score:</em> ${gt.expectedScore}, 
            <em>Class Name:</em> ${gt.className}
        </div>
    `).join('');

    // Calcola la media degli stati di completamento
    const completions = exercise.goals.map(goal => goal.completition);
    const averageCompletion = completions.reduce((a, b) => a + b, 0) / completions.length;

    row.innerHTML = `
        <td>${exercise.description}</td>
        <td>${goalTypesHtml}</td>
        <td>${new Date(exercise.startingTime).toLocaleString()}</td>
        <td>${new Date(exercise.expiryTime).toLocaleString()}</td>
        <td>${averageCompletion.toFixed(2)}%</td>
        <td>
            <button class="infoExerciseButton" data-id="${exercise.id}"><i class="fas fa-info-circle"></i></button>
            <button class="editExerciseButton" data-id="${exercise.id}"><i class="fas fa-pencil-alt"></i></button>
            <button class="deleteExerciseButton" data-id="${exercise.id}"><i class="fas fa-trash"></i></button>
        </td>
    `;
    exercisesTableBody.appendChild(row);

    // Aggiungi event listener per il pulsante di info
    row.querySelector(".infoExerciseButton").addEventListener("click", (event) => {
        const exerciseId = event.target.closest("button").getAttribute("data-id");
        toggleExerciseDetails(row, exercise);
    });

    // Aggiungi event listener per il pulsante di modifica
    row.querySelector(".editExerciseButton").addEventListener("click", (event) => {
        const exerciseId = event.target.closest("button").getAttribute("data-id");
        editExerciseRow(row, exercise);
    });

    // Aggiungi event listener per il pulsante di cancellazione
    row.querySelector(".deleteExerciseButton").addEventListener("click", async (event) => {
        const exerciseId = event.target.closest("button").getAttribute("data-id");
        await deleteExerciseFromTeam(exerciseId);
    });
}

function toggleExerciseDetails(row, exercise) {
    let detailsRow = row.nextElementSibling;
    if (detailsRow && detailsRow.classList.contains('exercise-details')) {
        // Se i dettagli sono già visibili, nascondili
        detailsRow.remove();
    } else {
        // Altrimenti, crea una nuova riga per i dettagli
        detailsRow = document.createElement('tr');
        detailsRow.classList.add('exercise-details');

        const goalTypes = exercise.goalTypes.map(gt => JSON.parse(gt));
        const goalDetailsHtml = goalTypes.map((goalType, index) => {
            const goalsOfType = exercise.goals.filter(goal => goal.expectedScore === goalType.expectedScore && goal.className === goalType.className);
            const goalCompletion = goalsOfType.reduce((sum, goal) => sum + goal.completition, 0) / goalsOfType.length;

            const studentsHtml = goalsOfType.map(goal => `
                <li>ID: ${goal.playerId}, Status: ${goal.completition}%</li>
            `).join('');

            return `
                <div>
                    <strong>Goal ${index + 1}</strong><br>
                    Completion: ${goalCompletion.toFixed(2)}%<br>
                    <ul>${studentsHtml}</ul>
                </div>
                ${index < goalTypes.length - 1 ? '<hr>' : ''}
            `;
        }).join('');

        detailsRow.innerHTML = `<td colspan="6">${goalDetailsHtml}</td>`;
        row.parentNode.insertBefore(detailsRow, row.nextSibling);
    }
}

function editExerciseRow(row, exercise) {
    const descriptionCell = row.children[0];
    const expiryTimeCell = row.children[3];
    const actionsCell = row.children[4];

    // Trasforma i valori in campi di input
    descriptionCell.innerHTML = `<input type="text" value="${exercise.description}" class="editDescriptionInput">`;
    expiryTimeCell.innerHTML = `<input type="datetime-local" value="${new Date(exercise.expiryTime).toISOString().slice(0, 16)}" class="editExpiryTimeInput">`;

    // Mantieni i pulsanti di modifica e cancellazione
    actionsCell.innerHTML = `
        <button class="editExerciseButton" data-id="${exercise.id}"><i class="fas fa-pencil-alt"></i></button>
        <button class="deleteExerciseButton" data-id="${exercise.id}"><i class="fas fa-trash"></i></button>
    `;

    // Aggiungi event listener per il tasto "Invio" sui campi di input
    row.querySelector(".editDescriptionInput").addEventListener("keydown", async (event) => {
        if (event.key === "Enter") {
            const newDescription = row.querySelector(".editDescriptionInput").value;
            const newExpiryTime = row.querySelector(".editExpiryTimeInput").value;
            await updateExercise(exercise.id, newDescription, newExpiryTime);
        }
    });

    row.querySelector(".editExpiryTimeInput").addEventListener("keydown", async (event) => {
        if (event.key === "Enter") {
            const newDescription = row.querySelector(".editDescriptionInput").value;
            const newExpiryTime = row.querySelector(".editExpiryTimeInput").value;
            await updateExercise(exercise.id, newDescription, newExpiryTime);
        }
    });
}