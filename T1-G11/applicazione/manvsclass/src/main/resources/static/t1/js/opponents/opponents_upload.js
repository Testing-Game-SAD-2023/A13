
async function uploadOpponent() {
    const name = document.getElementById('className').value;
    const date = document.getElementById('date').value;
    const difficulty = document.getElementById('difficulty').value;
    const url = "";
    const description = document.getElementById('description').value;
    const category = [
        document.getElementById('category1').value,
        document.getElementById('category2').value,
        document.getElementById('category3').value
    ];

    const classInput = document.getElementById('fileInput');
    const file = classInput.files[0];

    const testInput = document.getElementById('zipRobotInput');
    const tests = testInput.files[0];

    // Validazione lato client
    if (!name || name.trim() === '') {
        alert('Inserire il nome della classe.');
        return;
    }

    if (!file) {
        alert('Selezionare il file della classe .java da caricare.');
        return;
    }

    if (!tests) {
        alert('Selezionare il file ZIP dei test robot da caricare.');
        return;
    }

    const formData = new FormData();
    formData.append('classUTFile', file);
    formData.append('classUTDetails', JSON.stringify({
        name: name,
        date: date,
        difficulty: difficulty,
        uri: url,
        description: description,
        category: category
    }));
    formData.append('robotTestsZip', tests);

    document.getElementById('loadingOverlay').style.display = 'block';

    const data = await callUploadOpponent(formData);

    document.getElementById('loadingOverlay').style.display = 'none';

    if (data !== null) {
        console.log('Success:', data);
        $('#successModal').modal('show');
        //-------------------------------------
        // Se la classe è stata caricata correttamente, allora resetta il form.
        const form = classInput?.form;
        if (form) {
            form.reset();
        }
    }
}
