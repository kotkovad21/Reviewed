

// 1. NOVÉ: Logika pro předvyplnění formuláře při příchodu z detailu podniku
document.addEventListener('DOMContentLoaded', function() {
    const typeSelect = document.getElementById('typPodnikuSelect');
    const nameSelect = document.getElementById('podnikSelect');
    const hiddenType = document.getElementById('hiddenPredvybranyTyp');
    const hiddenPodnik = document.getElementById('hiddenPredvybranyPodnik');

    // Pokud máme předvybraný typ (přišli jsme z detailu podniku)
    if (hiddenType && hiddenType.value && hiddenPodnik && hiddenPodnik.value) {
        // Nastavíme typ podniku (např. Kavárna)
        typeSelect.value = hiddenType.value;

        // Vyvoláme událost "change", aby se spustil tvůj kód níže,
        // který vyfiltruje seznam podniků
        typeSelect.dispatchEvent(new Event('change'));

        // Nakonec vybereme ten konkrétní podnik
        nameSelect.value = hiddenPodnik.value;
    }
});
    const typeSelect = document.getElementById('typPodnikuSelect');
    const nameSelect = document.getElementById('podnikSelect');
    const allOptions = Array.from(nameSelect.options);

    typeSelect.addEventListener('change', function() {
        const selectedTypeId = this.value; // ID vybraného typu (např. 1)
        nameSelect.innerHTML = '';
        
        allOptions.forEach(option => {
            const optionTypeId = option.getAttribute('data-type');
            // Pokud je typ nevybrán, nebo se shoduje ID typu u podniku, nebo je to prázdná volba
            if (selectedTypeId === "" || optionTypeId === selectedTypeId || option.value === "") {
                nameSelect.appendChild(option);
            }
        });
    });

    nameSelect.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        const typeIdOfPodnik = selectedOption.getAttribute('data-type');
        
        if (typeIdOfPodnik) {
            typeSelect.value = typeIdOfPodnik;
        }
    });

function previewImages(event) {
    const uploadArea = document.getElementById('upload-area');
    const uploadText = document.getElementById('upload-text');
    const files = event.target.files;

    // Odstraníme staré fotky (vše kromě textu a inputu)
    const oldImages = uploadArea.querySelectorAll('img');
    oldImages.forEach(img => img.remove());

    if (files && files.length > 0) {
        if (uploadText) uploadText.style.display = 'none';

        Array.from(files).forEach(file => {
            if (!file.type.startsWith('image/')) return;

            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                uploadArea.appendChild(img);
            };
            reader.readAsDataURL(file);
        });
    } else {
        if (uploadText) uploadText.style.display = 'block';
    }
}