# ADR-004: Wybór źródeł i mechanizmu zbierania/produkowania danych

## Opis Problemu
Mikroserwis `ingest-service` ma zbierać dane rynkowe (akcje) i kursy walut z różnych źródeł zewnętrznych oraz normalizować je do wspólnego formatu.  
Problem polega na tym, że dostępne źródła danych różnią się sposobem udostępniania danych, wymaganiami bezpieczeństwa (klucze API) oraz złożonością techniczną (scraping vs API).

## Rozważane opcje
- **Źródło danych:**
    - REST API zewnętrznych dostawców.
    - Scrapowanie stron HTML bezpośrednio.

- **Obsługa źródeł wymagających kluczy API:**
    - Uzyskanie klucza i integracja.
    - Odłożenie/ignorowanie źródeł wymagających klucza w początkowej fazie (wybrano tę opcję).

- **Użycie Selenium:**
    - Dedykowany mikroserwis per źródło z Selenium.
    - Alternatywnie: lock'owanie dostępu do Selenium w celu uniknięcia konfliktów przy równoczesnym użyciu oraz 
    dopisanie nowych mechanizmów scrapowania do `ingest-service`.
    - Osobny kontener Selenium w tym samym podzie kubernetesa co dedykowany mikroserwis.
    - Ostatecznie na razie zrezygnowano, wystarczy prosty GET i parsowanie HTML.

- **Przechowywanie danych w bazie:**
    - Zapis wszystkich danych w mikroserwisie.
    - Zapis jedynie kursów walut do normalizacji danych akcji (wybrano tę opcję).

- **Wewnętrzne API:**
    - Proste API dla użytku wewnętrznego mikroserwisu, opcjonalnie do integracji z rolą Admin w przyszłości.

## Decyzja
- Dane będą pobierane z:
    - `api.nbp.pl` - kursy walut
    - `www.bankier.pl/surowce/notowania` - notowania akcji
- Pomijamy źródła wymagające klucza API.
- Pobieranie danych odbywa się poprzez prosty GET i parsowanie HTML.
- W bazie mikroserwisu zapisywane będą tylko kursy walut, dane akcji będą przetwarzane w locie i wysyłane na kolejkę kafki.
- Udostępniono proste wewnętrzne API dla przyszłych potrzeb (np. integracja z rolą Admin).

## Uzasadnienie
- Wybrane źródła są publiczne, nie wymagają sekretów ani rejestracji kluczy API.
- Prosty GET i parsowanie HTML minimalizuje złożoność infrastruktury.
- Ograniczenie zapisu w bazie zmniejsza koszty przechowywania i upraszcza mikroserwis.
- Podejście daje elastyczność w dodawaniu nowych źródeł danych w przyszłości.

