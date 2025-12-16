import {addErrorDiv, clearPreviousErrorsMsgs} from "./utils/renderErrors.js";
import {callLoginAdmin} from "./utils/api.js";
import {VIEWS} from "./utils/endpoints.js";

document.getElementById("home_redirect_btn").addEventListener("click", () => {
    window.location.href=VIEWS.HOME;
});

document.getElementById("password_reset_admin_redirect_btn").addEventListener("click", () => {
    window.location.href=VIEWS.RESET_PASSWORD_ADMIN;
});

document.getElementById("adminLogin_form").addEventListener("submit", async function (e) {
    e.preventDefault(); // blocco il submit classico

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const fields_id = ["email_label_container", "password_label_container"];
    clearPreviousErrorsMsgs(fields_id);

    if (email === '') {
        addErrorDiv(document.getElementById("email_label_container"), errors.emailEmpty);
        return;
    }

    if (password === '') {
        addErrorDiv(document.getElementById("password_label_container"), errors.passwordEmpty);
        return;
    }

    await callLoginAdmin({email, password});
});