# ADR-003: Mechanizm autentykacji i autoryzacji użytkowników

## Opis Problemu
W mikroserwisowej architekturze aplikacji konieczne jest zarządzanie autentykacją i autoryzacją użytkowników.  
Mikroserwis `user-auth-service` ma obsługiwać dane użytkownika i dostęp do zasobów, natomiast gateway (`gateway-api`) kieruje ruch do odpowiednich mikroserwisów.  
Problem polega na tym, aby zapewnić bezpieczeństwo danych użytkownika, umożliwić autoryzację tylko tam, gdzie jest potrzebna np. powiadomienia.

## Rozważane opcje
- **Pojedynczy mikroserwis z wbudowaną autoryzacją i logiką użytkownika:**
  Wszystko w jednym serwisie, proste, ale trudne do skalowania i utrzymania później na kubernetesie.
- **Gateway z autoryzacją OAuth2:**
  Autoryzacja realizowana w gatewayu, wybrane mikroserwisy otrzymują już zweryfikowane żądania.
- **Dedykowany mikroserwis `user-auth-service`:**
  Odpowiada za logowanie, rejestrację i autoryzację użytkownika (OAuth2 z Google/GitHub), zapewnia prywatne endpointy dla innych mikroserwisów.
- **Wewnętrzne przesyłanie danych użytkownika:**
  Przesyłanie minimalnych danych (id, roles) w nagłówkach do mikroserwisów, reszta danych przez asynchroniczne kanały (Kafka).
- **Rozszerzenie o JWT i role admin w przyszłości:**
  Możliwość dodania standardowej autoryzacji login/hasło z JWT m.in w przypadku potrzeby dodania roli admin.

## Decyzja
- Wdrożono **dedykowany mikroserwis `user-auth-service`** do obsługi użytkowników i autoryzacji OAuth2 (Google/GitHub).
- Gateway (`gateway-api`) pozostaje oddzielnym komponentem, większość endpointów jest publiczna, tylko niektóre wymagają autoryzacji.
- Gateway wstrzykuje minimalne dane użytkownika (`id` i `roles`) w nagłówkach żądań do mikroserwisów, np. do `notification-service`.
- Reszta danych użytkownika przesyłana jest asynchronicznie (Kafka), co umożliwia bezpieczne i niezależne użycie w mikroserwisach.
- `user-auth-adapter` mapuje nagłówki na wygodną formę w mikroserwisach, umożliwiając spójne wykorzystanie danych użytkownika.
- Opcja dodania JWT i roli admin została uwzględniona na przyszłość, bez konieczności przebudowy istniejącej architektury.

## Uzasadnienie
- Oddzielenie gatewaya od autoryzacji zmniejsza ryzyko niepotrzebnej ekspozycji danych.
- Przesyłanie minimalnych danych w nagłówkach jest lekkie, bezpieczne i wystarczające do autoryzacji w mikroserwisach.
- Adapter pozwala na spójną interpretację danych użytkownika w różnych serwisach.
- Podejście daje elastyczność do dodawania nowych mechanizmów autoryzacji i ról w przyszłości bez ingerencji w istniejące mikroserwisy.
- Minimalizacja ryzyka wycieku danych użytkownika i uproszczenie integracji między mikroserwisami.


