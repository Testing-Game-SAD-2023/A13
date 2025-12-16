import {VIEWS} from "./utils/endpoints.js";

document.getElementById("login_user_redirect_btn").addEventListener("click", () => {
    window.location.href=VIEWS.LOGIN_USER;
});

document.getElementById("login_admin_redirect_btn").addEventListener("click", () => {
    window.location.href=VIEWS.LOGIN_ADMIN;
});
