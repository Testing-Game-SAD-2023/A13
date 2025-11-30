function assignUrls(links) {
    Object.entries(links).forEach(([id, url]) => {
        const el = document.getElementById(id);
        if (!el) return;

        if (el.tagName.toLowerCase() === 'a') {
            el.href = url;
        } else {
            el.addEventListener("click", () => {
                window.location.href = url;
            });
        }
    });
}


const API_USER_SERVICE_PREFIX = "/api/userService";
const API_GAMEREPO_SERVICE_PREFIX = "/api/gamerepo";
const API_ADMIN_SERVICE_PREFIX = "/api/adminService";

const VIEWS = {
    LOGIN_USER: "/login",
    DASHBOARD_ADMIN: "/dashboard",
    ADMINS_TABLE: "/dashboard/admins",
    USERS_TABLE: "/dashboard/players",

    OPPONENTS_MAIN: "/opponents/main",

    OPPONENTS_SORT_BY_DATE: "/opponents/main?sortBy=Date",
    OPPONENTS_SORT_BY_NAME: "/opponents/main?sortBy=Name",
    OPPONENTS_FILTER_DIFFICULTY_EASY: "/opponents/main?filterByDifficulty=EASY",
    OPPONENTS_FILTER_DIFFICULTY_MEDIUM: "/opponents/main?filterByDifficulty=MEDIUM",
    OPPONENTS_FILTER_DIFFICULTY_HARD: "/opponents/main?filterByDifficulty=HARD",

    OPPONENTS_UPLOAD: "/opponents/upload",
    OPPONENTS_EDIT: "/opponents/edit",

    ADD_GUIDELINES: "#",

    TEAMS_MAIN: "/team/main",
    TEAMS_DETAILS: "/team/details",

    SCALATA_MAIN: "/scalata/main",
};

const APIS = {
    USER_SERVICE: {
        LOGOUT_ADMIN: `${API_USER_SERVICE_PREFIX}/auth/logout`,
        ALL_ADMINS: `${API_USER_SERVICE_PREFIX}/admins`,
        ALL_PLAYERS: `${API_USER_SERVICE_PREFIX}/players`,
    },
    GAMEREPO_SERVICE: {
        ALL_GAMES: `${API_GAMEREPO_SERVICE_PREFIX}/games`,
    },

    DOWNLOAD_CLASSUT: (name) => `/opponents/download/${encodeURIComponent(name)}`,
    DELETE_OPPONENT: (classUT) => `/opponents/${encodeURIComponent(classUT)}`,
    UPLOAD_OPPONENT: `/opponents`,
    UPLOAD_GUIDELINES: '/opponents/guidelines/upload',
    GET_GUIDELINES: '/opponents/guidelines',
    DELETE_GUIDELINE: (guidelineTitle) =>`/opponents/guidelines/${encodeURIComponent(guidelineTitle)}`,
    UPLOAD_SUGGESTIONS: (className) => `/opponents/suggestions/upload/${encodeURIComponent(className)}`,
    GET_SUGGESTIONS: (name) => `/opponents/suggestions/${encodeURIComponent(name)}`,
    DELETE_SUGGESTION: (className, suggestionTitle) =>
        `/opponents/suggestions/${encodeURIComponent(className)}/suggestion?suggestionTitle=${encodeURIComponent(suggestionTitle)}`
};
