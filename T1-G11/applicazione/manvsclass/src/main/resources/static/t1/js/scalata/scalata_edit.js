function getCookie(name) {
    const cookies = document.cookie.split('; ');
    for (let cookie of cookies) {
        const [key, value] = cookie.split('=');
        if (key === name) return value;
    }
    return null;
}

function parseJwt(token) {
    if (!token) return null;
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload)); // decodifica base64
}

function getCurrentDateDDMMYY() {
    const today = new Date();

    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = String(today.getFullYear()).slice(-2);

    return `${day}/${month}/${year}`;
}

$(document).ready(async function () {

    const levelsContainer = $(".levelsContainer");
    const numberSpan = $("#numberOfLevels");

    // ottiene gli opponents disponibili
    // Sostituito fetch con callGetOpponents
    const listOfOpponents = await callGetOpponents();

    // Se listOfOpponents è null (errore gestito da api.js), usciamo o gestiamo come necessario
    if (!listOfOpponents) {
        console.error("Impossibile caricare la lista degli opponents.");
        // Aggiungere qui eventuale logica di fallback o disabilitazione form
        return;
    }

    // Aggiorna il numero dei livelli e i badge
    function refreshLevels() {
        const levels = $(".level");
        numberSpan.text(levels.length);

        levels.each(function(index) {
            $(this).find(".badge").text(`Livello ${index + 1}`);

            $(this).find("select").attr("name", `levels[${index}].opponent`);
            $(this).find("select").attr("id", `levels[${index}].opponent`);

            $(this).find("input[type='number']").attr("name", `levels[${index}].tempoMax`);
            $(this).find("input[type='number']").attr("id", `levels[${index}].tempoMax`);
        });

    }

    // Aggiungi livello
    $("#addLevel").on("click", function (e) {
        const number = $(".level").length;
        e.preventDefault();

        const newLevel = $(`
            <div class="level border rounded-pill d-flex justify-content-between">
                <div class="badge bg-primary rounded-pill">${newscalata_level_badge} ${number + 1}</div>
                <div class="">
                    <label for="levels[${number}].opponent"></label>
                    <select name="levels[${number}].opponent" id="levels[${number}].opponent" required>
                        <option value="" disabled selected hidden>${newscalata_opponent_placeholder}</option>
                        ${listOfOpponents
            .map(o => `<option value="${o.id}">${o.classUT}vs${o.opponentType}(${o.opponentDifficulty})</option>`)
            .join("")
        }
                    </select>
                </div>

                <div class="">
                    <label for="levels[${number}].tempoMax">${newscalata_tempo_max_label}</label>
                    <input type="number" name="levels[${number}].tempoMax" id="levels[${number}].tempoMax" min="2" value="0" required>
                </div>
    
                <a class="btn btn-danger rounded-pill removeLevel">
                    <i class="bi bi-dash"></i>
                </a>


            </div>
        `);

        levelsContainer.append(newLevel);
        refreshLevels();
    });

    // Rimuovi livello
    levelsContainer.on("click", ".removeLevel", function () {
        $(this).closest(".level").remove();
        refreshLevels();
    });

    // ----------------------------
    //  INVIO FORM AL BACKEND
    // ----------------------------
    $("#form").on("submit", async function (e) {
        e.preventDefault();

        // Recupera il token jwt
        const token = getCookie('jwt');
        const payload = parseJwt(token);

        // Costruzione oggetto scalata
        const data = {
            name: $("#name").val(),
            description: $("#description").val(),
            numberOfLevels: $(".level").length,
            username: payload.sub,
            date: getCurrentDateDDMMYY(),
            listOfLevels: []
        };

        // Per ogni livello
        $(".level").each(function (i) {

            const opponentId = $(this).find("select").val();

            data.listOfLevels.push({
                opponent: listOfOpponents.find(o => String(o.id) === opponentId),
                tempoMax: parseInt($(this).find("input[type='number']").val())
            });
        });

        console.log("Scalata inviata:", data);

        // Chiamata al backend
        await callCreateScalata(data);

    });

    refreshLevels();
});