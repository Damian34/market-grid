Feature: Ingest currencies and resources

  Scenario: Successful ingest of currencies and resources
    When the service ingested currencies
    Then the currencies should be sent by Kafka to "currency-rates-ingested"
    And currencies should be stored in the database
    When the service ingested and normalize all market resources
    Then the market resources should be sent by Kafka to "market-resource-ingested"