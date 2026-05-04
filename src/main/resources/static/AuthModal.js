function showAuthModal(event) {
    if (event) event.preventDefault(); // Zabrání přesměrování
    document.getElementById('authModal').style.display = 'flex';
}

function closeAuthModal() {
    document.getElementById('authModal').style.display = 'none';
}

// Zavření okna při kliknutí mimo něj (na ztmavené pozadí)
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('authModal');
    if (modal) {
        modal.addEventListener('click', function(event) {
            if (event.target === modal) {
                closeAuthModal();
            }
        });
    }
});