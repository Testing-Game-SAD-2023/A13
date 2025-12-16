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

    GUIDELINES_UPLOAD: "/opponents/guidelines/main",

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
    UPLOAD_GUIDELINE_IMAGE: (order) => `/opponents/guidelines/upload/${encodeURIComponent(order)}`,
    DOWNLOAD_GUIDELINES: '/opponents/guidelines',
    GET_GUIDELINES: '/opponents/guidelines',
    DELETE_GUIDELINE: (order) =>`/opponents/guidelines/${encodeURIComponent(order)}`,
    DELETE_GUIDELINE_IMAGE: (order) => `/opponents/guidelines/image/${encodeURIComponent(order)}`,

    UPLOAD_SUGGESTIONS: '/opponents/suggestions/upload',
    UPLOAD_SUGGESTION_IMAGE: (className, order) => `/opponents/suggestions/upload/${encodeURIComponent(className)}/${encodeURIComponent(order)}`,
    DOWNLOAD_SUGGESTIONS: (className) => `/opponents/suggestions/${encodeURIComponent(className)}`,
    GET_SUGGESTIONS: (name) => `/opponents/suggestions/${encodeURIComponent(name)}`,
    DELETE_SUGGESTION: (className, order) => `/opponents/suggestions/${encodeURIComponent(className)}/${encodeURIComponent(order)}`,
    DELETE_SUGGESTION_IMAGE: (className, order) => `/opponents/suggestions/image/${encodeURIComponent(className)}/${encodeURIComponent(order)}`
};

const IMAGE_BASE_URL = '/t1/images/';