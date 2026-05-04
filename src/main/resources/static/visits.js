// 1. Odeslání nového štítku (To jsi měla perfektně)
function addTag(name) {
    const form = document.getElementById('filterForm');
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'tags';
    input.value = name;
    form.appendChild(input);
    form.submit();
}

// Odstranění štítku (kliknutím na křížek)
function removeTag(btn) {
    btn.closest('.filter').remove(); // Smaže štítek (žeton)
    document.getElementById('filterForm').submit(); // Odešle formulář a přenačte stránku
}

// 2. Chytré tlačítko Load More (Sjednocená verze)
document.getElementById('load-more-btn').addEventListener('click', function() {
    const btn = this;

    // Získáme číslo stránky z HTML atributu (např. data-page="1")
    const page = btn.getAttribute('data-page');

    // DŮLEŽITÉ: Přečteme filtry z URL (aby se při načítání další stránky neztratily)
    const currentParams = window.location.search;

    // Připravíme URL pro server
    const separator = currentParams.includes('?') ? '&' : '?';
    const currentPath = window.location.pathname; // Zjistí, zda jsme na /visits nebo /explore
    const url = `${currentPath}/load-more${currentParams}${separator}page=${page}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Chyba při načítání');
            return response.text();
        })
        .then(html => {
            // Pokud už nejsou další recenze, tlačítko pěkně zešedne
            if (html.trim() === "") {
                btn.innerHTML = "Všechny recenze načteny";
                btn.disabled = true;
                btn.style.opacity = "0.5";
                btn.style.cursor = "default";
                return;
            }

            // Vložení nových kartiček na konec gridu
            document.getElementById('recenze-container').insertAdjacentHTML('beforeend', html);

            // Nastavíme tlačítku další číslo stránky pro příští kliknutí
            btn.setAttribute('data-page', parseInt(page) + 1);
        })
        .catch(error => {
            console.error(error);
            btn.style.display = 'none';
        });
});

// 3. Rozbalení menu (Přidávám, protože jsi to měla v HTML, ale chybělo to v JS)
function toggleTagMenu() {
    const menu = document.getElementById('tagMenu');
    menu.style.display = menu.style.display === 'none' ? 'block' : 'none';
}