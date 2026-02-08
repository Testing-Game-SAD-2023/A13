let isEditingBio = false;

const dom = {
    bioText: document.getElementById("bioText"),
    bioInput: document.getElementById("bioInput"),
    saveProfileBtn: document.getElementById("saveProfileBtn"),
    profileName: document.getElementById("profileName"),
    nicknameInput: document.getElementById("nicknameInput"),
    editNicknameBtn: document.getElementById("editNicknameBtn"),
    profileImage: document.getElementById("profileImage"),
};

const availableAvatars = [
    "default.png",
    "men-1.png",
    "men-2.png",
    "men-3.png",
    "men-4.png",
    "women-1.png",
    "women-2.png",
    "women-3.png",
    "women-4.png"
];

const LIMITS = {
    NICKNAME: 20,
    BIO: 200
};

export function selectAvatar(filename) {
    const avatarImg = dom.profileImage;

    if (avatarImg) {
        avatarImg.src = `/t5/images/profileImages/${filename}`;
    }

    const hiddenInput = document.getElementById("selectedAvatarInput");
    if (hiddenInput) {
        hiddenInput.value = filename;
    }

}

export function openAvatarModal() {
    const modal = document.getElementById("avatarPickerModal");
    const list = document.getElementById("avatarList");

    list.innerHTML = "";

    availableAvatars.forEach(img => {
        const element = document.createElement("img");
        element.src = `/t5/images/profileImages/${img}`;
        element.classList.add("avatar-choice");

        element.addEventListener("click", () => selectAvatar(img));

        list.appendChild(element);
    });

    modal.classList.add("active");

}

export function closeAvatarModal() {
    const modal = document.getElementById("avatarPickerModal");
    modal.classList.remove("active");
}

export function closeBioEdit(saveChanges) {
    const bioText = dom.bioText;
    const bioInput = dom.bioInput;
    const saveBtn = dom.saveProfileBtn;

    if (saveChanges) {
        if (bioInput.value.length > LIMITS.BIO) {
                    alert(`La bio non può superare i ${LIMITS.BIO} caratteri.`);
                    return;
                }
        bioText.innerText = bioInput.value.trim();
    }

    bioInput.style.display = "none";
    bioText.style.display = "block";

    saveBtn.disabled = false;
    isEditingBio = false;
}

export async function saveProfile() {
    if (isEditingBio) {
        alert("Chiudi prima la modifica della bio");
        return;
    }

    const bio = dom.bioText.innerText.trim();
    const selectedAvatarPath = dom.profileImage.src;
    const nickname = dom.profileName.innerText.trim();
    // Validazione finale di sicurezza prima del fetch
    if (nickname.length > LIMITS.NICKNAME || bio.length > LIMITS.BIO) {
        alert("Errore: Nickname o Bio superano il limite consentito.");
        return;
    }
    const email = document.getElementById('userEmail').value;

    try {
        const formData = new URLSearchParams();
        formData.append("bio", bio);
        formData.append("avatar", selectedAvatarPath || '');
        formData.append("nickname", nickname);
        formData.append("email", email);

        const response = await fetch(`/profile/save`, {
            method: "POST",
            body: formData
        });

        if (response.ok) {
            alert("Profilo salvato correttamente!");
        } else {
            alert("Errore nel salvataggio del profilo.");
        }
    } catch (err) {
        console.error(err);
        alert("Errore di connessione al server.");
    }
}

export function startBioEdit() {
    const bioText = dom.bioText;
    const bioInput = dom.bioInput;
    const saveBtn = dom.saveProfileBtn;

    bioInput.value = bioText.innerText.trim();

    bioText.style.display = "none";
    bioInput.style.display = "block";
    bioInput.focus();

    saveBtn.disabled = true;
    isEditingBio = true;
}

export function handleBioInputKeydown(e) {
    if (e.key === "Enter") {
        e.preventDefault();
        closeBioEdit(true);
    }
}

export function startNicknameEdit() {
    const profileName = dom.profileName;
    const editNicknameBtn = dom.editNicknameBtn;
    const nicknameInput = dom.nicknameInput;
    const saveProfileBtn = dom.saveProfileBtn;

    profileName.style.display = 'none';
    editNicknameBtn.style.display = 'none';

    nicknameInput.style.display = 'block';
    nicknameInput.focus();

    saveProfileBtn.disabled = true;
}

export function handleNicknameInputKeypress(e) {
    if (e.key === 'Enter') {
        e.preventDefault();

        const profileName = dom.profileName;
        const editNicknameBtn = dom.editNicknameBtn;
        const nicknameInput = dom.nicknameInput;
        const saveProfileBtn = dom.saveProfileBtn;

        const newNickname = nicknameInput.value.trim();

        // Validazione lunghezza Nickname
        if (newNickname.length > LIMITS.NICKNAME) {
            alert(`Il nickname non può superare i ${LIMITS.NICKNAME} caratteri.`);
            return;
        }

        if (newNickname !== '') {
            profileName.innerText = newNickname;

            profileName.style.display = 'block';
            editNicknameBtn.style.display = 'inline-block';
            nicknameInput.style.display = 'none';

            saveProfileBtn.disabled = false;
        }
    }
}