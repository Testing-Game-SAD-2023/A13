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
    OPPONENTS_FILTER_DIFFICULTY_EASY: "/opponents/main?filterByDifficulty=Beginner",
    OPPONENTS_FILTER_DIFFICULTY_MEDIUM: "/opponents/main?filterByDifficulty=Intermediate",
    OPPONENTS_FILTER_DIFFICULTY_HARD: "/opponents/main?filterByDifficulty=Advanced",

    OPPONENTS_UPLOAD: "/opponents/upload",
    OPPONENTS_EDIT: "/opponents/edit",

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
    DOWNLOAD_CLASSUT: `${API_ADMIN_SERVICE_PREFIX}/opponents/download`,
    DELETE_OPPONENT: `${API_ADMIN_SERVICE_PREFIX}/opponents`,
    UPLOAD_OPPONENT: `${API_ADMIN_SERVICE_PREFIX}/opponents`,

    OPPONENTS_SERVICE: {
        GETALL_OPPONENTS: `${API_ADMIN_SERVICE_PREFIX}/opponents`,
    },

    SCALATA_SERVICE: {
        CREATE_SCALATA: `${API_ADMIN_SERVICE_PREFIX}/scalata/create`,
        GETALL_SCALATE: `${API_ADMIN_SERVICE_PREFIX}/scalata/getAll`,
        DELETE_SCALATA: `${API_ADMIN_SERVICE_PREFIX}/scalata/delete`,
    }
};

