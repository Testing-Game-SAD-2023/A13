$(document).ready(function() {
    // Funzioni utili
    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };

    const parseJwt = (token) => {
        try {
            return JSON.parse(atob(token.split('.')[1]));
        } catch (e) {
            return null;
        }
    };

    // Inizializzazione CodeMirror
    const editorOptions = {
        mode: "text/x-java",
        indentWithTabs: true,
        smartIndent: true,
        lineWrapping: true,
        theme: "lucario",
        lineNumbers: true,
        autoCloseBrackets: true,
        matchBrackets: true,
        autofocus: true,
        foldGutter: true,
        gutters: ['CodeMirror-foldgutter']
    };

    const sidebarEditor = CodeMirror.fromTextArea($("#sidebar-textarea")[0], editorOptions);
    const consoleArea = CodeMirror.fromTextArea($("#console-textarea")[0], editorOptions);
    const consoleArea2 = CodeMirror.fromTextArea($("#console-textarea2")[0], { ...editorOptions, mode: "javascript" });
    const editor = CodeMirror.fromTextArea($("#editor")[0], editorOptions);

    // Handler per logout
    $("#logout").on("click", function() {
        if (confirm("Sei sicuro di voler effettuare il logout?")) {
            if (localStorage.getItem("modalita") === "Allenamento") {
                const userId = parseJwt(getCookie("jwt")).userId;
                const className = localStorage.getItem("classe");
                removeAllenamentoFolders(className, userId);
            }
            $.ajax({
                url: '/logout',
                method: 'GET',
                success: function() {
                    window.location.href = "/login";
                },
                error: function(xhr, textStatus, error) {
                    console.error('Error:', textStatus, error);
                }
            });
        }
    });

    // Compilazione del codice
    $("#compileButton").on("click", function() {
        const formData = new FormData();
        formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
        formData.append("testingClassCode", editor.getValue());
        formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
        formData.append("underTestClassCode", sidebarEditor.getValue());

        $('#loading-editor').show();

        $.ajax({
            url: "/api/sendInfo",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            dataType: "text",
            success: function(response) {
                $('#loading-editor').hide();
                consoleArea.setValue(response);
                alert("Compilazione completata con successo.");
            },
            error: function(xhr, status, error) {
                $('#loading-editor').hide();
                alert("Si è verificato un errore!");
            }
        });
    });

    // Funzioni per ottenere il contenuto dell'editor
    function getEditorContent() {
        return editor.getValue();
    }

    function getCUTContent() {
        return sidebarEditor.getValue();
    }

    // Sostituzione del testo selezionato
    $(".startReplace").on("click", function() {
        const replaceTermInput = $("#replaceTerm").val();
        const selectedText = editor.getSelection();
        if (replaceTermInput && selectedText) {
            const cursor = editor.getCursor();
            const newCode = editor.getValue().replaceAll(selectedText, replaceTermInput);
            editor.setValue(newCode);
            editor.setCursor(cursor);
        }
        closeReplaceModal();
    });

    // Funzioni di ricerca
    function openSearchModal() {
        $("#searchModal").show();
    }

    function closeSearchModal() {
        $("#searchModal").hide();
    }

    function cercaParola() {
        const searchTerm = $("#searchTerm").val();
        const cursor = editor.getSearchCursor(searchTerm, null, true);
        editor.getAllMarks().forEach(mark => mark.clear());

        while (cursor.findNext()) {
            editor.markText(cursor.from(), cursor.to(), { className: "highlight" });
        }

        if (editor.getAllMarks().length > 0) {
            editor.scrollIntoView(editor.getAllMarks()[0].find());
        }

        editor.on("change", function() {
            editor.getAllMarks().forEach(mark => mark.clear());
        });

        closeSearchModal();
    }

    // Cambio tema
    let currentTheme = 'lucario';
    $(".theme-button").on("click", function() {
        currentTheme = currentTheme === 'lucario' ? 'elegant' : 'lucario';
        [sidebarEditor, consoleArea, consoleArea2, editor].forEach(cm => cm.setOption('theme', currentTheme));
    });

    // Gestione ridimensionamento
    $(window).on('resize', function() {
        if ($(window).width() < 800 || $(window).height() < 600) {
            window.resizeTo(Math.max(800, $(window).width()), Math.max(600, $(window).height()));
        }
    });

    // Autocompletamento
    const suggestionList = ["ArrayList", "LinkedList", "HashMap", "String", "Integer", "Boolean", /* altri suggerimenti */];
    autocomplete(editor, { startTag: ".", suggestionList: suggestionList });

    // Gestione del gioco
    let turno = 0;
    let orderTurno = 0;

    function processTurn() {
        const formData = new FormData();
        formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
        formData.append("testingClassCode", editor.getValue());
        formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
        formData.append("underTestClassCode", sidebarEditor.getValue());

        $.ajax({
            url: "/api/run",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function(response) {
                consoleArea.setValue(response.outCompile);
                highlightCodeCoverage($.parseXML(response.coverage));
            },
            error: function() {
                $('#loading-editor').hide();
                alert("Errore durante la compilazione.");
            }
        });
        orderTurno++;
    }

    $("#runButton").on("click", processTurn);

});
