document.addEventListener('DOMContentLoaded', function() {
    const grid = document.getElementById('reviewsGrid');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (grid && prevBtn && nextBtn) {
        // Funkce pro výpočet posunu (šířka karty + mezera)
        const getStep = () => {
            const card = grid.querySelector('.card');
            return card ? card.offsetWidth + 32 : 332; // 32px odpovídá 2rem gap
        };

        nextBtn.addEventListener('click', () => {
            grid.scrollTo({
                left: grid.scrollLeft + getStep(),
                behavior: 'smooth'
            });
        });

        prevBtn.addEventListener('click', () => {
            grid.scrollTo({
                left: grid.scrollLeft - getStep(),
                behavior: 'smooth'
            });
        });
    } else {
        console.error("Slider: Chybí ID prvků v HTML!");
    }
});