function toggleReviewMenu(button) {
    console.log("1. Kliknuto na 3 tečky!"); // Kontrolka v konzoli

    // NEPRŮSTŘELNÉ HLEDÁNÍ: Podíváme se do "obalu" tlačítka a najdeme v něm přesně to menu
    const parent = button.parentElement;
    const dropdown = parent.querySelector('.review-dropdown');

    // Pokud menu v HTML fyzicky chybí nebo má špatnou třídu, zařve to v konzoli
    if (!dropdown) {
        console.error("2. CHYBA: Nepodařilo se najít prvek s třídou 'review-dropdown'!");
        return;
    }

    console.log("2. Menu nalezeno, jdu ho otevřít/zavřít.");

    const isCurrentlyOpen = dropdown.classList.contains('show');

    // Zavřeme úplně všechna otevřená menu na stránce
    document.querySelectorAll('.review-dropdown').forEach(d => {
        d.classList.remove('show');
        d.style.display = 'none';
    });

    // Pokud naše menu NEBYLO otevřené, teď ho otevřeme
    if (!isCurrentlyOpen) {
        dropdown.classList.add('show');
        dropdown.style.display = 'block';
        console.log("3. Menu úspěšně otevřeno.");
    }
}

// 2. CHYTRÉ ZAVÍRÁNÍ PŘI KLIKNUTÍ JINAM
// Tohle zajistí, že když uživatel klikne kamkoliv na stránku, otevřená menu se schovají
document.addEventListener('click', function(event) {
    // OPRAVA: Přepsali jsme '.icon-button' na '.settings' podle tvého HTML!
    if (!event.target.closest('.settings') && !event.target.closest('.review-dropdown')) {
        // ...tak zavřeme všechna menu
        document.querySelectorAll('.review-dropdown').forEach(d => {
            d.classList.remove('show');
            d.style.display = 'none';
        });
    }
});

// 3. FUNKCE PRO MAZÁNÍ (Tvůj opravený kód)
function deleteReview(idObsahu) {
    if (confirm('Opravdu chcete tuto recenzi smazat?')) {
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        fetch(`/review/${idObsahu}/delete`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Chyba při mazání recenze. Možná k tomu nemáte oprávnění.');
            }
        });
    }
}

// 4. FUNKCE PRO ZMĚNU VIDITELNOSTI
function zmenitViditelnost(idRecenze, idNoveViditelnosti) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const formData = new URLSearchParams();
    formData.append('idViditelnosti', idNoveViditelnosti);

    fetch(`/review/${idRecenze}/visibility`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: formData.toString()
    })
    .then(response => {
        if (response.ok) {
            alert('Viditelnost uložena!');
            // Volitelně můžeme i tady zavřít menu, pokud to chceme
             document.querySelectorAll('.review-dropdown').forEach(d => {
                d.style.display = 'none';
            });
        } else {
            alert('Chyba při ukládání viditelnosti.');
        }
    });
}