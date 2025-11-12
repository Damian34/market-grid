# ADR-002: Plan pipeline’ów CI/CD dla projektu

## Opis Problemu
Projekt składa się z kilku mikroserwisów z różnymi stackami (m.in. Java, Kotlin, Spring Boot, Quarkus, Micronaut), wszystkie umieszczone formie aplikacji modułowej w jednym repozytorium.  
Konieczne jest stworzenie mechanizmu weryfikacji poprawności kodu oraz działania testów, bez nadmiernego skomplikowania infrastruktury CI/CD.  
Nie planuje się na tym etapie deploymentu na serwery produkcyjne ani generowania pełnych raportów jakości kodu (SonarQube/SonarCloud).

## Rozważane opcje
- **Pipeline do weryfikacji testów per typ builda (Maven/Gradle) z matrixem po nazwach mikroserwisów:**
  Po jedenym pipeline'ie ogólnym z matrixem po mikroserwisach, uruchamiający odpowiedni typ builda dla każdego mikroserwisu.
    - Zalety: zachowanie prostoty przy obsłudze wielu mikroserwisów w jednym repozytorium, łatwe rozszerzanie o nowe mikroserwisy.
    - Wady: konieczność dostosowania nazw mikroserwisów do matrixu i weryfikacji dla różnych stacków.

- **Pipeline zbiorczy z rozpoznawaniem języka/frameworka:**  
  Jeden pipeline próbuje automatycznie wykrywać, czy projekt używa Maven, Gradle, Spring Boot, Quarkus itd.
    - Zalety: większa elastyczność, mniej ręcznej konfiguracji dla nowych mikroserwisów.
    - Wady: ryzyko błędnej detekcji, wymaga dodatkowej logiki w skryptach, trudniejszy do debugowania.

- **Dodatkowe pipeline'y na np. SonarQube/SonarCloud, deploy, bump wersji:**  
  Pipeline'y na analizę jakości kodu, deployment i automatyczne podbijanie wersji.
    - Zalety: pełna automatyzacja procesów CI/CD, możliwość kontroli jakości i spójności wersji.
    - Wady: zwiększona złożoność, wymaga dodatkowej infrastruktury i serwerów, trudniejszy do utrzymania przy lokalnym uruchamianiu.

## Decyzja
- W projekcie zostanie wdrożony **pipeline do weryfikacji testów per typ builda (Maven/Gradle)**.
- Na początek zostaną utworzone dwa pipeline’y, po jednym dla typów builda Maven i Gradle.
- Nie przewiduje się na tym etapie pipeline’ów do raportowania (SonarQube/SonarCloud), deploymentu ani automatycznego podbijania wersji. Mogą być dodane w przyszłości jako osobne, niezależne pipeline’y.
- Podejście umożliwia łatwe dodawanie nowych mikroserwisów bez konieczności tworzenia dedykowanego pipeline’u dla każdego z nich.

## Uzasadnienie
- Zachowuje prostotę i czytelność przy obsłudze wielu mikroserwisów w jednym repozytorium.
- Matrix per mikroserwis pozwala testować wszystkie serwisy jednocześnie w spójnym przepływie.
- Ogranicza nadmiarową konfigurację i utrzymanie wielu pipeline’ów.
- Pozostawia możliwość rozbudowy CI/CD w przyszłości o bardziej zaawansowane mechanizmy (analiza jakości kodu, deployment, bump wersji) bez ingerencji w podstawowy pipeline testowy.

