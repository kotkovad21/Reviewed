document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('liveSearchInput');
    const resultsBox = document.getElementById('liveSearchResults');

    searchInput.addEventListener('input', function() {
        const query = this.value.trim();

        if (query.length >= 1) {
            fetch(`/api/users/search?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    resultsBox.innerHTML = '';

                    if (data.length > 0) {
                        data.forEach(user => {
                            // 1. ZMĚNA: Vytvoříme obyčejný div, ne odkaz <a>
                            const item = document.createElement('div');
                            item.className = 'search-suggestion';
                            item.innerHTML = `<span class="material-symbols-outlined">person</span> ${user.prezdivka}`;

                            // 2. ZMĚNA: Akce, která se stane po kliknutí
                            item.addEventListener('click', function() {
                                // Vložíme jméno kliknutého uživatele do inputu
                                searchInput.value = user.prezdivka;

                                // Schováme bublinu našeptávače
                                resultsBox.style.display = 'none';

                                // Automaticky odešleme formulář, jako by uživatel klikl na "Hledat"
                                searchInput.closest('form').submit();
                            });

                            resultsBox.appendChild(item);
                        });
                        resultsBox.style.display = 'block';
                    } else {
                        resultsBox.innerHTML = '<div class="search-no-results">Žádný uživatel nenalezen</div>';
                        resultsBox.style.display = 'block';
                    }
                });
        } else {
            resultsBox.style.display = 'none';
        }
    });

    document.addEventListener('click', function(e) {
        if (!searchInput.contains(e.target) && !resultsBox.contains(e.target)) {
            resultsBox.style.display = 'none';
        }
    });
});