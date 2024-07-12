

function changeLanguage(lang) {

    const url = new URL(window.location.href);

    url.searchParams.set('lang', lang);


    window.location.href = url.toString();
}


document.getElementById('en-flag').addEventListener('click', function() {
    changeLanguage('en');
});

document.getElementById('it-flag').addEventListener('click', function() {
    changeLanguage('it');
});
