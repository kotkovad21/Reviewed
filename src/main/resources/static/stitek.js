const tagInput = document.getElementById('tagInput');
const suggestions = document.getElementById('tagSuggestions');
const selectedTagsDiv = document.getElementById('selectedTags');
const hiddenInputsDiv = document.getElementById('hiddenTagInputs');

tagInput.addEventListener('input', function() {
    const val = this.value;
    const options = suggestions.options;

    for (let i = 0; i < options.length; i++) {
        if (options[i].value === val) {
            addTag(options[i].value, options[i].getAttribute('data-id'));
            this.value = ''; // Vyčistíme pole pro další psaní
            break;
        }
    }
});

function addTag(name, id) {
    // 1. Vytvoříme vizuální štítek (bublinu)
    const tagSpan = document.createElement('span');
    tagSpan.className = 'tag';
    tagSpan.innerHTML = `${name} &times;`;

    // 2. Vytvoříme skrytý input pro Spring (propojení s listem v Javě)
    // Důležité: name musí odpovídat cestě k poli v objektu
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.name = 'obsah.stitky'; // Musí odpovídat th:field v Javě
    hiddenInput.value = id;

    // Odstranění štítku při kliknutí
    tagSpan.onclick = function() {
        tagSpan.remove();
        hiddenInput.remove();
    };

    selectedTagsDiv.appendChild(tagSpan);
    hiddenInputsDiv.appendChild(hiddenInput);
}