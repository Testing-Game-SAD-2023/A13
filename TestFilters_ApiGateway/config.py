"""
Configurazioni per la test suite.
"""

import os
from dotenv import load_dotenv

# Carica variabili da file .env
load_dotenv()

# ---- Config server ----

BASE_URL = os.getenv("BASE_URL", "http://localhost:8090")

# Endpoint utili
COMPILE_ENDPOINT = f"{BASE_URL}/compile/jacoco/coverage/player"
HEALTH_ENDPOINT = f"{BASE_URL}/compile/jacoco/actuator/health"
LOGIN_ENDPOINT = f"{BASE_URL}/userService/auth/login"

# --- Gestione credenziali ---
USERNAME = os.getenv("TEST_USERNAME", "")
PASSWORD = os.getenv("TEST_PASSWORD", "")

# Decommentare qui se non si vuole usare un .env (Da NON farae in produzione)
# USERNAME = "tester.uno@gmail.com"
# PASSWORD = "your_password_here"

# Payload per i test
PAYLOAD = {
    "testingClassName": "Test.java",
    "testingClassCode": "public class Test { }",
    "underTestClassName": "Class.java",
    "underTestClassCode": "public class Class { }"
}

# --- SCHEMAS ---

EXPECTED_SCHEMAS = {
    "SUCCESS": {
        "status_code": 200,
        "json_body": {}  # Al momento non è prevista una validazione del body per casi di successo
    },
    "UNAUTHORIZED": {
        "status_code": 401,
        "json_body": {
            "type": "about:blank",
            "title": "Unauthorized",
            "status": 401,
            "detail": "Autenticazione mancante o non valida",
            "instance": "/compile/jacoco/coverage/player"
        }
    },
    "RATE_LIMIT": {
        "status_code": 429,
        "json_body": {
            "type": "about:blank",
            "title": "Too Many Requests",
            "status": 429,
            "detail": "Il limite di richieste è stato superato. Riprova più tardi.",
            "instance": "/compile/jacoco/coverage/player"
        }
    },
    "SERVICE_UNAVAILABLE": {
        "status_code": 503,
        "json_body": {
            "type": "about:blank",
            "title": "Service Unavailable",
            "status": 503,
            "detail": "Il servizio di compilazione (T7) è momentaneamente sovraccarico o non raggiungibile.",
            "instance": "/fallback/t7"
        }
    }
}

# --- CONFIG per i Test ---

# Timeout per le richieste HTTP (secondi)
REQUEST_TIMEOUT = 10

# Timeout per test che potrebbero richiedere più tempo (es. circuit breaker)
REQUEST_TIMEOUT_EXTENDED = 190


# Configurazione per test rate limiter
RATE_LIMIT_TEST_CONFIG = {
    "max_workers": 50,      # Thread paralleli
    "max_requests": 150,    # Richieste totali da inviare
    "request_timeout": 5    # Timeout per singola richiesta
}

# Configurazione HTTP adapter per connection pooling
HTTP_ADAPTER_CONFIG = {
    "pool_connections": 50,
    "pool_maxsize": 50
}

# --- Gestione di files e cache ---

# File cache per JWT
JWT_CACHE_FILE = ".jwt_cache.json"

# File di log
LOG_FILE = "test_results.log"

# --- funzioni di utilità ---

def validate_config():
    """
    Valida la configurazione e restituisce eventuali warning.
    
    Returns:
        Lista di warning come stringhe
    """
    warnings = []
    
    if not USERNAME:
        warnings.append("TEST_USERNAME non configurato (variabile d'ambiente o .env)")
    
    if not PASSWORD:
        warnings.append("TEST_PASSWORD non configurato (variabile d'ambiente o .env)")
    
    return warnings

def print_config_summary():
    """Stampa un riepilogo della configurazione corrente"""
    from colorama import Fore, Style, init
    init(autoreset=True)
    
    print(f"\n{Fore.CYAN}{'='*60}")
    print(f"CONFIGURAZIONE ATTUALE")
    print(f"{'='*60}{Style.RESET_ALL}")
    print(f"Base URL: {Fore.YELLOW}{BASE_URL}{Style.RESET_ALL}")
    print(f"Username: {Fore.YELLOW}{USERNAME if USERNAME else '❌ NON CONFIGURATO'}{Style.RESET_ALL}")
    print(f"Password: {Fore.YELLOW}{'✓ Configurata' if PASSWORD else '❌ NON CONFIGURATA'}{Style.RESET_ALL}")
    print(f"JWT Cache: {Fore.YELLOW}{JWT_CACHE_FILE}{Style.RESET_ALL}")
    
    warnings = validate_config()
    if warnings:
        print(f"\n{Fore.YELLOW}⚠ Warning:{Style.RESET_ALL}")
        for warning in warnings:
            print(f"  - {warning}")
    else:
        print(f"\n{Fore.GREEN}✓ Configurazione completa{Style.RESET_ALL}")
    
    print(f"{Fore.CYAN}{'='*60}{Style.RESET_ALL}\n")


if __name__ == "__main__":
    
    print_config_summary()
    
    print("Endpoint configurati:")
    print(f"  - Compile: {COMPILE_ENDPOINT}")
    print(f"  - Health: {HEALTH_ENDPOINT}")
    print(f"  - Login: {LOGIN_ENDPOINT}")
