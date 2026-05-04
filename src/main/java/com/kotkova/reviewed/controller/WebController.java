package com.kotkova.reviewed.controller;

import com.kotkova.reviewed.model.*;
import com.kotkova.reviewed.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    private final PodnikService podnikService;
    private final RecenzeService recenzeService;
    private final UzivatelService uzivatelService;
    private final TypPodnikuService typPodnikuService;
    private final ViditelnostService viditelnostService;
    private final StitekService stitekService;
    private final FotkaService fotkaService;
    private final PratelstviService pratelstviService;

    public WebController(PodnikService podnikService,
                         RecenzeService recenzeService,
                         UzivatelService uzivatelService,
                         TypPodnikuService typPodnikuService,
                         ViditelnostService viditelnostService,
                         StitekService stitekService,
                         FotkaService fotkaService,
                         PratelstviService pratelstviService) {

        this.podnikService = podnikService;
        this.recenzeService = recenzeService;
        this.uzivatelService = uzivatelService;
        this.typPodnikuService = typPodnikuService;
        this.viditelnostService = viditelnostService;
        this.stitekService = stitekService;
        this.fotkaService = fotkaService;
        this.pratelstviService = pratelstviService;
    }

    @GetMapping("/")
    public String showHomepage(Model model, java.security.Principal principal) { // PŘIDÁNO: Principal
        // 1. Podniky (tohle máš správně)
        var seznamPodniku = podnikService.ziskejNejnovejsiPodniky();
        model.addAttribute("podnikyZDatabaze", seznamPodniku);

        // 2. Zjistíme, kdo je u počítače (idPrihlaseneho)
        Uzivatel idPrihlaseneho = null;
        if (principal != null) {
            Uzivatel u = uzivatelService.ziskejUzivatelePodleEmailu(principal.getName());
            if (u != null) idPrihlaseneho = u;
        }

        // 3. TADY JE TA HLAVNÍ ZMĚNA:
        // Místo té staré metody použijeme ziskejStrankuRecenzi s limitem 6
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 6);

        // Tato služba už v sobě má ten chytrý filtr, co jsme psali minule!
        var stranka = recenzeService.ziskejStrankuRecenzi(pageable, idPrihlaseneho);

        // Pošleme do HTML jen těch bezpečných 6 kousků
        model.addAttribute("seznamRecenzi", stranka.getContent());

        // NOVÉ: Pošleme ID do HTML, abychom poznali vlastní anonymní recenze
        model.addAttribute("prihlasenyId", idPrihlaseneho);

        return "homepage";
    }
    @GetMapping("/place")
    public String showPlacePage() {
        return "place"; // Toto hledá soubor src/main/resources/templates/place.html
    }

    @GetMapping("/insert")
    public String showInsertForm(@RequestParam(required = false) Long podnikId,
                                 @RequestParam(required = false) Long typId,
                                 Model model) {
        // 1. Načteme data ze všech služeb
        List<Podnik> podniky = podnikService.ziskejVsechnyPodniky();
        List<TypPodniku> typy = typPodnikuService.ziskejVsechnyTypy();
        List<Viditelnost> viditelnosti = viditelnostService.ziskejVsechnyViditelnosti();
        viditelnosti.removeIf(v -> v.getIdViditelnosti() == 5L);

        model.addAttribute("podniky", podniky);
        model.addAttribute("typyPodniku", typy);
        model.addAttribute("viditelnosti", viditelnosti);
        model.addAttribute("vsechnyStitky", stitekService.ziskejVsechnyStitky());
        model.addAttribute("novaRecenze", new Recenze());
        model.addAttribute("predvybranyPodnikId", podnikId);
        model.addAttribute("predvybranyTypId", typId);
        return "insert";
    }
    @GetMapping("/profile")
    public String showProfilePage(Model model, java.security.Principal principal) {
        String email = principal.getName();

        Uzivatel prihlasenyUzivatel = uzivatelService.ziskejUzivatelePodleEmailu(email);
        model.addAttribute("uzivatel", prihlasenyUzivatel);

        return "profile";
    }
    @GetMapping("/visits")
    public String showVisitsPage(@RequestParam(required = false, defaultValue = "latest") String order,
                                 @RequestParam(required = false, defaultValue = "all") String business,
                                 @RequestParam(required = false) List<String> tags,
                                 @AuthenticationPrincipal CustomUserDetails customUser,
                                 Model model) {

        Long idPrihlaseneho = (customUser != null) ? customUser.getIdUzivatele() : null;

        org.springframework.data.domain.Sort sort;
        switch (order) {
            case "oldest": sort = org.springframework.data.domain.Sort.by("idObsahu").ascending(); break;
            case "rating": sort = org.springframework.data.domain.Sort.by("hodnoceni").descending(); break;
            case "abc":    sort = org.springframework.data.domain.Sort.by("podnik.nazev").ascending(); break;
            default:       sort = org.springframework.data.domain.Sort.by("idObsahu").descending(); break;
        }

        // OPRAVA 1: Přidali jsme 'sort' jako třetí parametr
Pageable pageable = PageRequest.of(0, 12);

        var prvniStranka = recenzeService.ziskejFiltrovaneMojeRecenze(pageable, idPrihlaseneho, business, tags);

        // OPRAVA 2: Posíláme VŠECHNY potřebné věci do HTML
        model.addAttribute("seznamRecenzi", prvniStranka.getContent());
        model.addAttribute("vsechnyStitky", stitekService.ziskejVsechnyStitky());
        model.addAttribute("typyPodniku", typPodnikuService.ziskejVsechnyTypy()); // Pro select s podniky
        model.addAttribute("aktivniStitky", tags != null ? tags : new java.util.ArrayList<>()); // Pro žetony s křížkem

        return "visits";
    }

    @GetMapping("/visits/load-more")
    public String loadMore(@RequestParam(defaultValue = "0") int page, Model model,
                           @RequestParam(required = false, defaultValue = "latest") String order,
                           @RequestParam(required = false, defaultValue = "all") String business,
                           @RequestParam(required = false) List<String> tags,
                           @AuthenticationPrincipal CustomUserDetails customUser) {

        Long idPrihlaseneho = (customUser != null) ? customUser.getIdUzivatele() : null;

        org.springframework.data.domain.Sort sort;
        switch (order) {
            case "oldest": sort = org.springframework.data.domain.Sort.by("idObsahu").ascending(); break;
            case "rating": sort = org.springframework.data.domain.Sort.by("hodnoceni").descending(); break;
            case "abc":    sort = org.springframework.data.domain.Sort.by("podnik.nazev").ascending(); break;
            default:       sort = org.springframework.data.domain.Sort.by("idObsahu").descending(); break;
        }

        // OPRAVA 3: I tady musíme přidat 'sort'
        Pageable pageable = PageRequest.of(page, 12);

        Page<Recenze> recenzePage = recenzeService.ziskejFiltrovaneMojeRecenze(pageable, idPrihlaseneho, business, tags);

        model.addAttribute("seznamRecenzi", recenzePage.getContent());
        model.addAttribute("prihlasenyId", idPrihlaseneho);

        return "fragments/visitsLoad :: visits-fragment";
    }

    @GetMapping("/place/{id}")
    public String zobrazDetailPodniku(@PathVariable Long id, Model model, java.security.Principal principal) {
        Podnik vybranyPodnik = podnikService.ziskejPodnikPodleId(id);
        model.addAttribute("podnik", vybranyPodnik);

        // 1. Získáme přihlášeného uživatele[cite: 11]
        Uzivatel prihlaseny = (principal != null) ?
                uzivatelService.ziskejUzivatelePodleEmailu(principal.getName()) : null;

        // 2. Předáme ho do servisy, která teď díky roli 2 nebo 3 „odemkne“ vše[cite: 11]
        List<Recenze> recenzeKPodniku = recenzeService.ziskejViditelneRecenzeProPodnik(id, prihlaseny);
        model.addAttribute("seznamRecenzi", recenzeKPodniku);

        model.addAttribute("prumer", podnikService.ziskejPrumernyRating(id));
        model.addAttribute("prihlasenyId", prihlaseny != null ? prihlaseny.getIdUzivatele() : null);

        // Kontrola pro tlačítko uložení[cite: 11]
        model.addAttribute("jeUlozen", podnikService.jePodnikUlozen(id, prihlaseny != null ? prihlaseny.getIdUzivatele() : null));

        return "place";
    }
    @GetMapping("/review/{id}")
    public String showReviewPage(@PathVariable Long id, Model model, java.security.Principal principal) {
        // 1. Najdeme konkrétní recenzi v databázi (to už máš)
        Recenze vybranaRecenze = recenzeService.ziskejRecenziPodleId(id);
        model.addAttribute("recenze", vybranaRecenze);

        // 2. Zjistíme ID přihlášeného uživatele
        // Proměnnou si připravíme nahoře, aby byla viditelná pro model.addAttribute níže
        Long prihlasenyId = null;

        if (principal != null) {
            Uzivatel u = uzivatelService.ziskejUzivatelePodleEmailu(principal.getName());
            if (u != null) {
                prihlasenyId = u.getIdUzivatele();
            }
        }

        // 3. Pošleme ID do HTML pod jménem "prihlasenyId"
        // Teď už proměnná 'u' nesvítí červeně, protože používáme 'prihlasenyId'
        model.addAttribute("prihlasenyId", prihlasenyId);

        // 4. Otevřeme soubor review.html
        return "review";
    }

    @PostMapping("/insert")
    public String processNewReview(
            @ModelAttribute("novaRecenze") Recenze recenze,
            @RequestParam("fotkySoubory") MultipartFile[] soubory,
            Model model,
            java.security.Principal principal,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes){
        try {

            if (recenze.getPodnik() == null || recenze.getObsah().getText() == null || recenze.getObsah().getText().trim().isEmpty()) {
                model.addAttribute("errorMessage", "Vyplňte prosím všechna povinná pole (Podnik a Text).");
                znovuNactiDataProFormular(model); // Aby nespadl Thymeleaf
                return "insert";
            }

            if (recenze.getHodnoceni() == null) {
                recenze.setHodnoceni(0);
            }

            // OPRAVA ŠTÍTKŮ: Odstraníme duplikáty, pokud nám je HTML formulář poslal omylem vícekrát
            if (recenze.getObsah() != null && recenze.getObsah().getStitky() != null) {
                java.util.Map<Long, Stitek> unikatniStitky = new java.util.LinkedHashMap<>();
                for (Stitek s : recenze.getObsah().getStitky()) {
                    if (s.getIdStitku() != null) {
                        unikatniStitky.put(s.getIdStitku(), s);
                    }
                }
                recenze.getObsah().setStitky(new java.util.ArrayList<>(unikatniStitky.values()));
            }

            String email = principal.getName();
            Uzivatel autor = uzivatelService.ziskejUzivatelePodleEmailu(email);
            recenze.getObsah().setUzivatel(autor);
            recenze.getObsah().setDatumVytvoreni(LocalDate.now());
            recenze.getObsah().setTypObsahu("RECENZE");

            if (recenze.getObsah().getViditelnost() == null) {
                Viditelnost defaultViditelnost = viditelnostService.ziskejVsechnyViditelnosti().get(0);
                recenze.getObsah().setViditelnost(defaultViditelnost);
            }

            recenze.getObsah().setRecenze(recenze);

            // 2. Uložíme recenzi (tím získáme ID_OBSAHU, které potřebujeme pro fotky)
            Recenze ulozena = recenzeService.ulozRecenzi(recenze);

            // 3. Zpracování nahraných fotek
            if (soubory != null) {
                for (MultipartFile soubor : soubory) {
                    if (!soubor.isEmpty()) {
                        Fotka novaFotka = new Fotka();
                        novaFotka.setData(soubor.getBytes()); // Převede soubor na byte[] (BLOB)
                        novaFotka.setIdRecenze(ulozena.getIdObsahu()); // Propojí fotku s recenzí

                        novaFotka.setNazevSouboru(soubor.getOriginalFilename());
                        // Uložíme fotku (předpokládám, že máš fotkaService nebo fotkaRepository)

                        String unikatniNazev = java.util.UUID.randomUUID().toString().substring(0, 20);
                        novaFotka.setNazevSouboru(unikatniNazev);

                        fotkaService.ulozFotku(novaFotka);
                    }
                }
            }
            redirectAttributes.addFlashAttribute("successMessage", "Super! Recenze byla úspěšně nahrána.");
            return "redirect:/visits";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Jejda, něco se pokazilo a recenze se nevytvořila. Zkuste to prosím znovu.");
            znovuNactiDataProFormular(model);
            return "insert";
        }
    }
    private void znovuNactiDataProFormular(Model model) {
        model.addAttribute("podniky", podnikService.ziskejVsechnyPodniky());
        model.addAttribute("typyPodniku", typPodnikuService.ziskejVsechnyTypy());
        model.addAttribute("viditelnosti", viditelnostService.ziskejVsechnyViditelnosti());
        model.addAttribute("vsechnyStitky", stitekService.ziskejVsechnyStitky());
    }
    // PŘIDAT DO WebControlleru
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Zobrazí templates/login.html
    }

    @PostMapping("/review/{id}/delete")
    @ResponseBody // Říkáme, že nevracíme HTML stránku, ale jen potvrzení (OK)
    public ResponseEntity<String> smazatRecenzi(@PathVariable Long id, java.security.Principal principal) {
        // 1. Získáme přihlášeného uživatele
        Uzivatel prihlaseny = uzivatelService.ziskejUzivatelePodleEmailu(principal.getName());

        // 2. Najdeme recenzi
        Recenze r = recenzeService.ziskejRecenziPodleId(id);

        // 3. BEZPEČNOSTNÍ KONTROLA: Patří ta recenze jemu?
        if (r.getObsah().getUzivatel().getIdUzivatele().equals(prihlaseny.getIdUzivatele())) {
            recenzeService.oznacJakoSmazanou(id);
            return ResponseEntity.ok("Smazáno");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nemáte oprávnění");
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("uzivatel", new Uzivatel());
        return "register"; // vrací register.html
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email,
                               @RequestParam String prezdivka,
                               @RequestParam String heslo,
                               Model model) { // PŘIDÁNO: Model

        boolean maChybu = false;

        // 1. KONTROLA UNIKÁTNOSTI
        if (uzivatelService.existujeEmail(email)) {
            model.addAttribute("chybaEmail", "Tento e-mail už je zaregistrovaný.");
            maChybu = true;
        }
        if (uzivatelService.existujePrezdivka(prezdivka)) {
            model.addAttribute("chybaPrezdivka", "Tato přezdívka už je zabraná.");
            maChybu = true;
        }

        // 2. POKUD JE CHYBA, VRÁTÍME HO ZPĚT NA FORMULÁŘ
        if (maChybu) {
            model.addAttribute("uzivatel", new Uzivatel()); // Aby nespadl Thymeleaf[cite: 16]
            // Předvyplníme mu to, co už zadal (aby to nemusel psát znovu)
            model.addAttribute("zadanyEmail", email);
            model.addAttribute("zadanaPrezdivka", prezdivka);
            return "register";
        }

        // 3. VŠE JE OK, ULOŽÍME (tvůj původní kód)
        Uzivatel novy = new Uzivatel();
        novy.setEmail(email);
        novy.setPrezdivka(prezdivka);
        novy.setHeslo(heslo);

        novy.setKrestniJmeno("Jméno");
        novy.setPrijmeni("Příjmení");
        novy.setDatumRegistrace(java.time.LocalDate.now());

        uzivatelService.registrujNovehoUzivatele(novy);
        return "redirect:/login";
    }
    @GetMapping("/profil/{id}")
    public String verejnyProfil(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails customUser) {
        // 1. Uživatel, na jehož profil koukáme
        Uzivatel autor = uzivatelService.ziskejUzivatelePodleId(id);

        // 2. Uživatel, který u počítače sedí a dívá se
        Uzivatel prihlaseny = (customUser != null) ? uzivatelService.ziskejUzivatelePodleEmailu(customUser.getUsername()) : null;
        Long mojeId = (prihlaseny != null) ? prihlaseny.getIdUzivatele() : null;

        // 3. Voláme novou zabezpečenou metodu (posíláme ID autora a objekt přihlášeného)
        List<Recenze> recenzeAutora = recenzeService.ziskejRecenzePodleUzivatele(id, prihlaseny);

        model.addAttribute("autor", autor);
        model.addAttribute("recenze", recenzeAutora);
        model.addAttribute("mojeId", mojeId);

        // Zbytek metody s přátelstvím (zadostOdeslana, uzJsmePratele) zůstává úplně beze změny...
        boolean uzJsmePratele = false;
        boolean zadostOdeslana = false;

        if (mojeId != null && !mojeId.equals(id)) {
            String stavVazby = pratelstviService.zjistiStavVazby(mojeId, id);
            if ("PRIJATO".equals(stavVazby)) uzJsmePratele = true;
            if ("ZADOST".equals(stavVazby)) zadostOdeslana = true;
        }

        model.addAttribute("uzJsmePratele", uzJsmePratele);
        model.addAttribute("zadostOdeslana", zadostOdeslana);

        return "profilePublic";
    }

    @PostMapping("/friends/add/{id}")
    public String addFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        if (user != null && !user.getIdUzivatele().equals(id)) {
            pratelstviService.posliZadost(user.getIdUzivatele(), id);
        }
        // Po kliknutí uživatele přesměrujeme zpět na ten samý profil
        return "redirect:/profil/" + id;
    }

    // Seznam uložených podniků
    @GetMapping("/saved")
    public String showSavedPlaces(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) return "redirect:/login";

        // Získáme všechny uložené vazby pro přihlášeného uživatele
        List<UlozenyPodnik> ulozene = podnikService.ziskejUlozenePodnikyUzivatele(user.getIdUzivatele());
        model.addAttribute("ulozenePodniky", ulozene);

        return "saved";
    }

    // Akce uložení (volaná tlačítkem u detailu podniku)
    @PostMapping("/place/{id}/save")
    public String savePlace(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        if (user != null) {
            podnikService.ulozPodnikProUzivatele(id, user.getIdUzivatele());
        }
        return "redirect:/place/" + id;
    }

    @GetMapping("/reviews")
    public String showReviewsPage(@RequestParam(required = false, defaultValue = "latest") String order,
                                  @RequestParam(required = false, defaultValue = "all") String business,
                                  @RequestParam(required = false) List<String> tags,
                                  @AuthenticationPrincipal CustomUserDetails customUser,
                                  Model model) {

        // 1. Místo pouhého ID získáme celého uživatele z DB
        Uzivatel prihlaseny = (customUser != null) ?
                uzivatelService.ziskejUzivatelePodleEmailu(customUser.getUsername()) : null;

        Pageable pageable = PageRequest.of(0, 12);

        // 2. Do servisy posíláme objekt 'prihlaseny'
        // (Ujisti se, že jsi v RecenzeService upravila i metodu pro filtrování, aby brala Uzivatele)
        var stranka = recenzeService.ziskejGlobalniFiltrovaneRecenze(pageable, prihlaseny, business, tags);

        model.addAttribute("seznamRecenzi", stranka.getContent());
        model.addAttribute("vsechnyStitky", stitekService.ziskejVsechnyStitky());
        model.addAttribute("typyPodniku", typPodnikuService.ziskejVsechnyTypy());
        model.addAttribute("aktivniStitky", tags != null ? tags : new java.util.ArrayList<>());

        // Pro fragmenty a anonymitu stále posíláme ID
        model.addAttribute("prihlasenyId", prihlaseny != null ? prihlaseny.getIdUzivatele() : null);
        model.addAttribute("zobrazitAutora", true);

        return "reviews";
    }

    // Pomocná metoda pro sjednocení řazení (aby nebyl kód 2x)
    private org.springframework.data.domain.Sort ziskejSortPodleStringu(String order) {
        return switch (order) {
            case "oldest" -> org.springframework.data.domain.Sort.by("idObsahu").ascending();
            case "rating" -> org.springframework.data.domain.Sort.by("hodnoceni").descending();
            case "abc"    -> org.springframework.data.domain.Sort.by("podnik.nazev").ascending();
            default       -> org.springframework.data.domain.Sort.by("idObsahu").descending();
        };
    }

    // Ve WebController.java pro Explore Load More
    @GetMapping("/reviews/load-more")
    public String loadMoreReviews(@RequestParam(defaultValue = "0") int page,
                                  @AuthenticationPrincipal CustomUserDetails customUser,
                                  Model model,
                                  @RequestParam(defaultValue = "latest") String order,
                                  @RequestParam(defaultValue = "all") String business,
                                  @RequestParam(required = false) List<String> tags) {

        Uzivatel prihlaseny = (customUser != null) ?
                uzivatelService.ziskejUzivatelePodleEmailu(customUser.getUsername()) : null;

        Pageable pageable = PageRequest.of(page, 12);
        var stranka = recenzeService.ziskejGlobalniFiltrovaneRecenze(pageable, prihlaseny, business, tags);

        model.addAttribute("seznamRecenzi", stranka.getContent());
        model.addAttribute("prihlasenyId", prihlaseny != null ? prihlaseny.getIdUzivatele() : null);
        model.addAttribute("zobrazitAutora", true);

        return "fragments/visitsLoad :: visits-fragment";
    }

    @GetMapping("/places")
    public String showPlacesPage(@RequestParam(required = false, defaultValue = "popular") String order,
                                 @RequestParam(required = false, defaultValue = "all") String business,
                                 @RequestParam(required = false) List<String> tags,
                                 Model model) {
        Pageable pageable = PageRequest.of(0, 12);
        var stranka = podnikService.ziskejFiltrovanePodniky(pageable, business, tags, order);

        model.addAttribute("seznamPodniku", stranka.getContent());
        model.addAttribute("vsechnyStitky", stitekService.ziskejVsechnyStitky());
        model.addAttribute("typyPodniku", typPodnikuService.ziskejVsechnyTypy());
        model.addAttribute("aktivniStitky", tags != null ? tags : new java.util.ArrayList<>());

        return "places";
    }

    @GetMapping("/places/load-more")
    public String loadMorePlaces(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "popular") String order,
                                 @RequestParam(required = false, defaultValue = "all") String business,
                                 @RequestParam(required = false) List<String> tags,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, 12);
        var stranka = podnikService.ziskejFiltrovanePodniky(pageable, business, tags, order);

        model.addAttribute("seznamPodniku", stranka.getContent());
        return "fragments/placesLoad :: places-fragment";
    }

    @GetMapping("/friends")
    public String showFriendsPage(@RequestParam(required = false) String search,
                                  @AuthenticationPrincipal CustomUserDetails user,
                                  Model model) {
        Long mojeId = user.getIdUzivatele();
        model.addAttribute("zadosti", pratelstviService.ziskejZadosti(mojeId));
        model.addAttribute("pratele", pratelstviService.ziskejPratele(mojeId));

        // NOVÉ: Pokud uživatel něco hledá v Searchbaru
        if (search != null && !search.trim().isEmpty()) {
            List<Uzivatel> nalezene = uzivatelService.hledejUzivatele(search);

            // Vyhodíme z výsledků sami sebe (nechceme si posílat žádost sami sobě)
            nalezene.removeIf(u -> u.getIdUzivatele().equals(mojeId));

            // Zjistíme, jestli s nalezenými uživateli už nemáme nějakou vazbu
            java.util.Map<Long, String> stavy = new java.util.HashMap<>();
            for (Uzivatel u : nalezene) {
                stavy.put(u.getIdUzivatele(), pratelstviService.zjistiStavVazby(mojeId, u.getIdUzivatele()));
            }

            model.addAttribute("vysledkyHledani", nalezene);
            model.addAttribute("stavyVazeb", stavy);
            model.addAttribute("hledanyText", search);
        }

        return "friends";
    }

    // NOVÉ: API pro živý našeptávač v searchbaru
    @GetMapping("/api/users/search")
    @ResponseBody
    public ResponseEntity<List<java.util.Map<String, Object>>> liveSearchUsers(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }

        List<Uzivatel> nalezeni = uzivatelService.hledejUzivatele(query);

        // Očistíme to a pošleme ven jen ID a přezdívku, abychom zbytečně nezatěžovali síť
        List<java.util.Map<String, Object>> vysledky = nalezeni.stream()
                .map(u -> {
                    java.util.Map<String, Object> mapa = new java.util.HashMap<>();
                    mapa.put("id", u.getIdUzivatele());
                    mapa.put("prezdivka", u.getPrezdivka());
                    return mapa;
                })
                .limit(5) // Omezíme počet výsledků v bublině našeptávače max na 5
                .toList();

        return ResponseEntity.ok(vysledky);
    }

    @PostMapping("/friends/accept/{id}")
    public String acceptFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        pratelstviService.prijmiZadost(id, user.getIdUzivatele());
        return "redirect:/friends";
    }

    @PostMapping("/friends/reject/{id}")
    public String rejectFriend(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        pratelstviService.smazPratelstvi(id, user.getIdUzivatele());
        return "redirect:/friends";
    }
    @PostMapping("/review/{id}/visibility")
    @ResponseBody // Znamená to, že nevracíme HTML stránku, ale jen potvrzení pro JavaScript
    public ResponseEntity<String> zmenViditelnost(@PathVariable Long id,
                                                  @RequestParam Long idViditelnosti,
                                                  java.security.Principal principal) {
        // 1. Získáme přihlášeného uživatele
        Uzivatel prihlaseny = uzivatelService.ziskejUzivatelePodleEmailu(principal.getName());

        // 2. Najdeme recenzi
        Recenze r = recenzeService.ziskejRecenziPodleId(id);

        // 3. BEZPEČNOSTNÍ KONTROLA: Patří ta recenze jemu?
        if (r.getObsah().getUzivatel().getIdUzivatele().equals(prihlaseny.getIdUzivatele())) {
            recenzeService.zmenViditelnost(id, idViditelnosti);
            return ResponseEntity.ok("Viditelnost úspěšně změněna");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nemáte oprávnění");
        }
    }
}