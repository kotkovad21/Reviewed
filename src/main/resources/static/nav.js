const menuToggle = document.getElementById("menu-toggle");
    const navSecond = document.querySelector(".nav-second");

    menuToggle.addEventListener("click", () => {
        navSecond.classList.toggle("open");
    });