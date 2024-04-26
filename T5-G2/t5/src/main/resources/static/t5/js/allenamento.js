function removeAllenamentoFolders(className, userId) {
    $.ajax({
        url: '/remove-allenamento/' + className + '/' + userId,
        type: 'GET',
        timeout: 30000,
        success: function (data, textStatus, xhr) {
            console.log("Cartelle di allenamento rimosse con successo");
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error("Errore durante la rimozione delle cartelle di allenamento:", errorThrown);
        }
    });
}
