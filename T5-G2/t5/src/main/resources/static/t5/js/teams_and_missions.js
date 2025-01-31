// Get the student ID from the URL
const urlParams = new URLSearchParams(window.location.search);
const studentID = window.location.pathname.split("/")[2]; // Extracts the ID from URL path

// Function to log messages to the debug area
function logDebugMessage(message) {
    const debugContainer = document.getElementById('debug-container');
    const logMessage = document.createElement('p');
    logMessage.textContent = message;
    debugContainer.appendChild(logMessage);
}

// Function to fetch exercise details
async function fetchExerciseDetails(teamID, missionID) {
    try {
        const response = await fetch(`/api/team/${teamID}/exercise/${missionID}`);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        return {
            description: data.description,
            startingTime: data.startingTime,
            expiryTime: data.expiryTime,
            goalTypes: data.goalTypes.map(gt => JSON.parse(gt)) // Parse each goalType string to an object
        };
    } catch (error) {
        console.error('Error fetching exercise details:', error);
        return null;
    }
}

// Function to fetch student teams
function fetchStudentTeams(studentID) {
    const url = `/api/student_teams/${studentID}`;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(async data => {
            const teamsContainer = document.getElementById('teams-container');
            if (data.length === 0) {
                teamsContainer.innerHTML = '<p>You are not part of any teams yet.</p>';
            } else {
                const teamList = document.createElement('ul');
                teamList.className = 'team-list';
                for (const team of data) {
                    const teamItem = document.createElement('li');
                    teamItem.className = 'team-item';
                    teamItem.innerHTML = `
                        <h2>${team.name}</h2>
                        <p>${team.description}</p>
                    `;

                    const exerciseList = document.createElement('ul');
                    exerciseList.className = 'exercise-list';

                    for (const missionID of team.exerciseList) {
                        const exerciseDetails = await fetchExerciseDetails(team.id, missionID);
                        if (exerciseDetails) {
                            const exerciseItem = document.createElement('li');
                            exerciseItem.className = 'exercise-item';
                            exerciseItem.innerHTML = `
                                <p><strong>Description:</strong> ${exerciseDetails.description}</p>
                                <p><strong>Starting Time:</strong> ${new Date(exerciseDetails.startingTime).toLocaleString()}</p>
                                <p><strong>Expiry Time:</strong> ${new Date(exerciseDetails.expiryTime).toLocaleString()}</p>
                                <p><strong>Goal Types:</strong><br>${exerciseDetails.goalTypes.map(gt => `Type: ${gt.type}, Expected Score: ${gt.expectedScore}, Class: ${gt.className}`).join('<br>')}</p>
                            `;
                            exerciseList.appendChild(exerciseItem);
                        }
                    }

                    teamItem.appendChild(exerciseList);
                    teamList.appendChild(teamItem);
                }
                teamsContainer.innerHTML = '';
                teamsContainer.appendChild(teamList);
            }
        })
        .catch(error => {
            const teamsContainer = document.getElementById('teams-container');
            teamsContainer.innerHTML = `<p>Error loading teams: ${error.message}</p>`;
            console.error('Error fetching teams:', error);
        });
}

// Fetch student teams on page load
fetchStudentTeams(studentID);