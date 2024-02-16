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

var sidebarEditor = CodeMirror.fromTextArea(document.getElementById("sidebar-textarea"), {
    mode: "text/x-java",
    indentWithTabs: true,
    lineWrapping: true,
    theme: "lucario",
    lineNumbers: true,
    smartIndent: true,
    electricChars: true,
    readOnly: true,
    autoCloseBrackets: true,
    matchBrackets: true,
    disableInput: true,
    autofocus: true
});
var consoleArea = CodeMirror.fromTextArea(document.getElementById("console-textarea"), {
    mode: "text/x-java",
    lineWrapping: true,
    theme: "lucario",
    electricChars: true,
    readOnly: true,
    disableInput: true,
    autofocus: true
});
var consoleArea2 = CodeMirror.fromTextArea(document.getElementById("console-textarea2"), {
    mode: "javascript",
    lineWrapping: true,
    theme: "lucario",
    electricChars: true,
    readOnly: true,
    disableInput: true,
    autofocus: true
});
var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
    mode: "text/x-java",
    indentWithTabs: true,
    smartIndent: true,
    lineWrapping: true,
    theme: "lucario",
    lineNumbers: true,
    autoMatchBrackets: true,
    autoCloseBrackets: true,
    autofocus: true,
    foldGutter: true,
    gutters: ['CodeMirror-foldgutter']
});

editor.on('click', function (cm, event) {
    var gutterWidth = cm.getGutterElement().offsetWidth;
    var gutterClick = event.clientX - gutterWidth;
    if (gutterClick >= (gutterWidth - 10)) {
        var gutterHeight = cm.defaultTextHeight() * cm.lineCount();
        var line = cm.lineAtHeight(event.clientY + cm.getScrollInfo().top, "client");
        var linePos = cm.charCoords({ line: line, ch: 0 }, "local").y;
        var arrowPos = linePos + cm.defaultTextHeight() / 2;
        var arrowSize = cm.defaultCharWidth();
        if (event.clientX >= gutterWidth && event.clientX <= gutterWidth + arrowSize &&
            event.clientY >= arrowPos - arrowSize && event.clientY <= arrowPos + arrowSize) {
            cm.foldCode(line);
        }
    }
});

var startReplaceButtons = document.getElementsByClassName("startReplace");
for (var i = 0; i < startReplaceButtons.length; i++) {
    startReplaceButtons[i].addEventListener("click", function () {
        var replaceTermInput = document.getElementById("replaceTerm");
        var nuovoTesto = replaceTermInput.value;
        if (nuovoTesto) {
            var testoSelezionato = editor.getSelection();
            if (testoSelezionato) {
                var cursor = editor.getCursor();
                var codice = editor.getValue();
                var nuovoCodice = codice.replaceAll(testoSelezionato, nuovoTesto);
                editor.setValue(nuovoCodice);
                editor.setCursor(cursor);
            }
        }
        closeReplaceModal();
    });
}

//compilazione verso T7
function getEditorContent() {
    return editor.getValue(); // Restituisce il contenuto del codice nell'editor
}
function getCUTContent() {
    return sidebarEditor.getValue(); // Restituisce il contenuto del codice nella CUT
}

//funzione handler del tasto di logout
var logout = document.getElementById("logout");
logout.addEventListener("click", function () {
    if (confirm("Sei sicuro di voler effettuare il logout?")) {
        if (localStorage.getItem("modalita") === "Allenamento") {
            removeAllenamentoFolders();
        }
        $.ajax({
            url: '/logout',
            method: 'GET',
            success: function (data, textStatus, xhr) {
                window.location.href = "/login";
            },
            error: function (xhr, textStatus, error) {
                console.error('Error:', textStatus, error);
            }
        });

    }
});

// TASTO COMPILA

var compileButton = document.getElementById("compileButton");
compileButton.addEventListener("click", function () {
    // Logica da eseguire quando il pulsante viene cliccato
    // Ad esempio, esegui una chiamata AJAX al tuo controller Spring per inviare i dati
    var formData = new FormData();
    formData.append("testingClassName", "Test" + localStorage.getItem("classe") + ".java");
    formData.append("testingClassCode", editor.getValue());
    formData.append("underTestClassName", localStorage.getItem("classe") + ".java");
    formData.append("underTestClassCode", sidebarEditor.getValue());

    document.getElementById('loading-editor').style.display = 'block';
    $.ajax({
        url: "/api/sendInfo", //URL del controller Spring
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: "text",
        success: function (response) {
            document.getElementById('loading-editor').style.display = 'none';
            // Logica da eseguire in caso di successo
            consoleArea.setValue(response);
            console.log("Richiesta inviata con successo. Risposta del server:", response);
        },
        error: function (xhr, status, error) {
            // Logica da eseguire in caso di errore
            document.getElementById('loading-editor').style.display = 'none';
            alert("Si è verificato un errore!");
            console.log("Errore durante l'invio della richiesta:", error);
        }
    });
});

function getJavaClassContent() {
    // Assume che tu abbia un elemento HTML con un ID univoco che contiene il codice della classe Java
    var javaClassElement = document.getElementById("editor");
    if (javaClassElement) {
        return javaClassElement.value; // Restituisce il valore del campo di input o l'HTML interno dell'elemento
    } else {
        return ""; // Restituisce una stringa vuota se l'elemento non è presente o non ha un valore
    }
}

function highlightCodeCoverage(reportContent) {
    // Analizza il contenuto del file di output di JaCoCo per individuare le righe coperte, non coperte e parzialmente coperte
    // Applica lo stile appropriato alle righe del tuo editor

    var coveredLines = [];
    var uncoveredLines = [];
    var partiallyCoveredLines = [];

    reportContent.querySelectorAll("line").forEach(function (line) {
        if (line.getAttribute("mi") == 0) coveredLines.push(line.getAttribute("nr"));
        else if (line.getAttribute("cb") / (line.getAttribute("mb") + line.getAttribute("cb")) == (2 / 4)) partiallyCoveredLines.push(line.getAttribute("nr"));
        else uncoveredLines.push(line.getAttribute("nr"));
    });

    coveredLines.forEach(function (lineNumber) {
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "covered-line");
    });

    uncoveredLines.forEach(function (lineNumber) {
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "partially-covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "uncovered-line");
    });

    partiallyCoveredLines.forEach(function (lineNumber) {
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "uncovered-line");
        sidebarEditor.removeLineClass(lineNumber - 2, "background", "covered-line");
        sidebarEditor.addLineClass(lineNumber - 2, "background", "partially-covered-line");
    });
}

var openButton = document.querySelector(".open-button");
openButton.addEventListener("click", function () {
    var fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = ".java";

    // Funzione di gestione dell'evento di caricamento del file
    fileInput.addEventListener("change", function (e) {
        var file = e.target.files[0];
        var reader = new FileReader();

        reader.onload = function (e) {
            var content = e.target.result;
            editor.setValue(content); // Imposta il contenuto del file nell'editor
        };

        reader.readAsText(file); // Leggi il contenuto del file come testo
    });

    fileInput.click(); // Simula il click sull'input del file
});

// Cambio Tema
var themeButtons = document.getElementsByClassName('theme-button');
var currentTheme = 'lucario'; // Tema iniziale
for (var i = 0; i < themeButtons.length; i++) {
    themeButtons[i].onclick = function () {
        if (currentTheme === 'lucario') {
            sidebarEditor.setOption('theme', 'elegant');
            consoleArea.setOption('theme', 'elegant');
            consoleArea2.setOption('theme', 'elegant');
            editor.setOption('theme', 'elegant');
            currentTheme = 'elegant';
        } else {
            sidebarEditor.setOption('theme', 'lucario');
            consoleArea.setOption('theme', 'lucario');
            consoleArea2.setOption('theme', 'lucario');
            editor.setOption('theme', 'lucario');
            currentTheme = 'lucario';
        }
    };
}
window.addEventListener('resize', function () {
    if (window.innerWidth < 800 || window.innerHeight < 600) {
        window.resizeTo(Math.max(800, window.innerWidth), Math.max(600, window.innerHeight));
    }
});
document.querySelector('.undo-button').addEventListener('click', function () {
    editor.undo();
});
document.querySelector('.redo-button').addEventListener('click', function () {
    editor.redo();
});
function showName(nome) {
    document.getElementById("nome-pulsante").innerHTML = nome;
}
function hideName() {
    document.getElementById("nome-pulsante").innerHTML = "";
}
function runCoverage() {
    var code = editor.getValue();
    var jacoco = new Jacoco();
    jacoco.setOptions({
        language: "java",
        sourceCode: code
    });
    var report = jacoco.run();
    console.log(report);
}

function openSearchModal() {
    var searchModal = document.getElementById("searchModal");
    searchModal.style.display = "block";
}
function openReplaceModal() {
    var replaceModal = document.getElementById("replaceModal");
    replaceModal.style.display = "block";
}
function closeReplaceModal() {
    var replaceModal = document.getElementById("replaceModal");
    replaceModal.style.display = "none";
}
function closeSearchModal() {
    var searchModal = document.getElementById("searchModal");
    searchModal.style.display = "none";
}
function cercaParola() {
    var searchTerm = document.getElementById("searchTerm").value;
    var marks = editor.getAllMarks();
    // Rimuovi tutti i segnalibri precedenti
    for (var i = 0; i < marks.length; i++) {
        marks[i].clear();
    }

    var cursor = editor.getSearchCursor(searchTerm, null, true);

    // Trova tutte le occorrenze non case sensitive
    while (cursor.findNext()) {
        var startPos = cursor.from();
        var endPos = cursor.to();

        // Evidenzia l'occorrenza
        editor.markText(startPos, endPos, { className: "highlight" });
    }

    // Scrolla alla posizione della prima occorrenza
    if (editor.getAllMarks().length > 0) {
        editor.scrollIntoView(editor.getAllMarks()[0].find());
    }

    // Rimuovi i segnalibri quando l'editor viene modificato
    editor.on("change", function () {
        marks = editor.getAllMarks();
        for (var i = 0; i < marks.length; i++) {
            marks[i].clear();
        }
    });
    closeSearchModal(); // Chiudi la finestra modale dopo la ricerca
}
function saveCode() {
    var code = document.getElementById("editor").value;
    var fileName = prompt("Inserisci il nome del file:", "MyFile.java");
    if (fileName != null && fileName != "") {
        var blob = new Blob([code], { type: "text/plain;charset=utf-8" });
        saveAs(blob, fileName);
    }
}
function saveAs(blob, fileName) {
    var link = document.createElement("a");
    link.download = fileName;
    link.href = URL.createObjectURL(blob);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
function sostituisciTesto(editor) {
    var testoSelezionato = editor.getSelection();
    if (testoSelezionato) {
        var replaceTermInput = document.getElementById("replaceTerm");
        var nuovoTesto = replaceTermInput.value;
        if (nuovoTesto) {
            var cursor = editor.getCursor();
            var codice = editor.getValue();
            var nuovoCodice = codice.replaceAll(testoSelezionato, nuovoTesto);
            editor.setValue(nuovoCodice);
            editor.setCursor(cursor);
        }
    }
}
function autocomplete(cm, options) {
    var startTag = options.startTag || ".";
    var suggestionList = options.suggestionList || [];

    var currentPrefix = "";
    var dropdown = null;

    cm.on("inputRead", function (cm, change) {
        if (change.text.length && change.text[0] === startTag) {
            currentPrefix = "";
            showSuggestions(cm, currentPrefix);
        }
    });

    cm.on("keyup", function (cm, event) {
        var inputValue = cm.getValue();
        var cursor = cm.getCursor();
        var line = cm.getLine(cursor.line);
        var endPos = cursor.ch;
        var startPos = line.lastIndexOf(startTag, endPos - 1);
        if (startPos !== -1) {
            currentPrefix = line.slice(startPos + 1, endPos).toLowerCase();
            showSuggestions(cm, currentPrefix);
        } else {
            closeAllLists();
        }
    });

    function showSuggestions(cm, prefix) {
        var suggestions = suggestionList.filter(function (item) {
            return item.toLowerCase().indexOf(prefix) === 0;
        });

        closeAllLists();

        if (suggestions.length === 0) {
            return;
        }

        dropdown = document.createElement("div");
        dropdown.className = "autocomplete-items";
        dropdown.style.position = "absolute";
        dropdown.style.zIndex = 9999; // Imposta il valore dello z-index per farlo apparire in primo piano
        cm.addWidget(cm.getCursor(), dropdown);

        for (var i = 0; i < suggestions.length; i++) {
            var option = document.createElement("div");
            option.textContent = suggestions[i];
            option.className = "autocomplete-item";
            option.addEventListener("click", function (e) {
                cm.replaceRange(this.textContent.slice(currentPrefix.length), cm.getCursor());
                closeAllLists();
                cm.focus();
            });
            dropdown.appendChild(option);
        }
    }

    function closeAllLists() {
        if (dropdown) {
            dropdown.parentNode.removeChild(dropdown);
            dropdown = null;
        }
    }

    document.addEventListener("click", function (e) {
        closeAllLists();
    });
}

var suggestionList = ["ArrayList", "LinkedList", "HashMap", "HashSet", "String", "Integer", "Boolean", "Double", "Float", "Character", "Byte", "Short", "Long", "Array", "List", "Set", "Map", "Queue", "Stack", "TreeSet", "TreeMap", "PriorityQueue", "Comparator", "Comparable", "Iterator", "Enumeration", "AbstractList", "AbstractSet", "AbstractMap", "AbstractQueue", "AbstractSequentialList", "LinkedListNode", "LinkedListIterator", "LinkedListSpliterator", "LinkedListDescendingIterator", "LinkedListDescendingSpliterator", "ArrayListNode", "ArrayListIterator", "ArrayListSpliterator", "ArrayListReverseIterator", "ArrayListReverseSpliterator", "HashSetNode", "HashSetIterator", "HashSetSpliterator", "HashMapEntry", "HashMapNode", "HashMapIterator", "HashMapSpliterator", "HashMapKeyIterator", "HashMapKeySpliterator", "HashMapValueIterator", "HashMapValueSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNode", "TreeSetIterator", "TreeSetSpliterator", "TreeSetDescendingIterator", "TreeSetDescendingSpliterator", "TreeMapEntry", "TreeMapNode", "TreeMapIterator", "TreeMapSpliterator", "TreeMapKeyIterator", "TreeMapKeySpliterator", "TreeMapValueIterator", "TreeMapValueSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNode", "PriorityQueueIterator", "PriorityQueueSpliterator", "AbstractCollection", "AbstractQueueIterator", "AbstractQueueSpliterator", "AbstractQueueDescendingIterator", "AbstractQueueDescendingSpliterator", "AbstractDeque", "LinkedListDeque", "LinkedListDequeNode", "LinkedListDequeIterator", "LinkedListDequeSpliterator", "LinkedListDequeDescendingIterator", "LinkedListDequeDescendingSpliterator", "ArrayDeque", "ArrayDequeNode", "ArrayDequeIterator", "ArrayDequeSpliterator", "ArrayDequeDescendingIterator", "ArrayDequeDescendingSpliterator", "StackNode", "StackIterator", "StackSpliterator", "StackDescendingIterator", "StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator", "ArrayListListIterator", "ArrayListListSpliterator", "LinkedListListIterator", "LinkedListListSpliterator", "AbstractSetIterator", "AbstractSetSpliterator", "HashSetSetIterator", "HashSetSetSpliterator", "TreeSetSetIterator", "TreeSetSetSpliterator", "AbstractMapIterator", "AbstractMapSpliterator", "HashMapMapIterator", "HashMapMapSpliterator", "TreeMapMapIterator", "TreeMapMapSpliterator", "AbstractSequentialListIterator", "AbstractSequentialListSpliterator", "LinkedListSequentialListIterator", "LinkedListSequentialListSpliterator", "ArrayListSequentialListIterator", "ArrayListSequentialListSpliterator", "LinkedListNodeIterator", "LinkedListNodeSpliterator", "ArrayListNodeIterator", "ArrayListNodeSpliterator", "HashSetNodeIterator", "HashSetNodeSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNodeIterator", "TreeSetNodeSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNodeIterator", "PriorityQueueNodeSpliterator", "AbstractCollectionIterator", "AbstractCollectionSpliterator", "AbstractQueueIterator", "AbstractQueueSpliterator", "AbstractQueueDescendingIterator", "AbstractQueueDescendingSpliterator", "AbstractDequeIterator", "AbstractDequeSpliterator", "LinkedListDequeIterator", "LinkedListDequeSpliterator", "LinkedListDequeDescendingIterator", "LinkedListDequeDescendingSpliterator", "ArrayDequeIterator", "ArrayDequeSpliterator", "ArrayDequeDescendingIterator", "ArrayDequeDescendingSpliterator", "StackNodeIterator", "StackNodeSpliterator", "StackDescendingIterator", "StackDescendingSpliterator", "AbstractListIterator", "AbstractListSpliterator", "ArrayListListIterator", "ArrayListListSpliterator", "LinkedListListIterator", "LinkedListListSpliterator", "AbstractSetIterator", "AbstractSetSpliterator", "HashSetSetIterator", "HashSetSetSpliterator", "TreeSetSetIterator", "TreeSetSetSpliterator", "AbstractMapIterator", "AbstractMapSpliterator", "HashMapMapIterator", "HashMapMapSpliterator", "TreeMapMapIterator", "TreeMapMapSpliterator", "AbstractSequentialListIterator", "AbstractSequentialListSpliterator", "LinkedListSequentialListIterator", "LinkedListSequentialListSpliterator", "ArrayListSequentialListIterator", "ArrayListSequentialListSpliterator", "LinkedListNodeIterator", "LinkedListNodeSpliterator", "ArrayListNodeIterator", "ArrayListNodeSpliterator", "HashSetNodeIterator", "HashSetNodeSpliterator", "HashMapEntryIterator", "HashMapEntrySpliterator", "HashSetDescendingIterator", "HashSetDescendingSpliterator", "TreeSetNodeIterator", "TreeSetNodeSpliterator", "TreeMapEntryIterator", "TreeMapEntrySpliterator", "PriorityQueueNodeIterator"];
autocomplete(editor, { startTag: ".", suggestionList: suggestionList });