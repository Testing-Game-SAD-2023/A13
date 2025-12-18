"""
Test implementati:
- TC-01: Flusso Nominale (Happy Path)
- TC-02: Autenticazione Fallita
- TC-03: Violazione Rate Limiter (Stress Test)
- TC-04: Circuit Breaker Aperto
- TC-05a: Circuit Breaker Half-Open (Successo)
- TC-05b: Circuit Breaker Half-Open (Fallimento)
"""

import requests
import time
from typing import Dict
from colorama import Fore, Style, init
from concurrent.futures import ThreadPoolExecutor, as_completed
from requests.adapters import HTTPAdapter
from collections import Counter

#  moduli custom
from auth_manager import JWTAuthManager
from config import (
    BASE_URL, COMPILE_ENDPOINT, HEALTH_ENDPOINT, PAYLOAD,
    USERNAME, PASSWORD, EXPECTED_SCHEMAS, REQUEST_TIMEOUT,
    REQUEST_TIMEOUT_EXTENDED, RATE_LIMIT_TEST_CONFIG,
    HTTP_ADAPTER_CONFIG, print_config_summary
)

#  colorama
init(autoreset=True)

#  auth manager globale
auth_manager = JWTAuthManager(BASE_URL)

# funzioncine di utilità

def get_auth_cookies() -> Dict[str, str]:

    # funzione per ottenere i cookie di auth. Gestisce anche caching, refresh e login
    token = auth_manager.get_valid_token(USERNAME, PASSWORD)
    if not token:
        raise Exception("Impossibile ottenere un token JWT valido")
    return {"jwt": token}

def print_test_header(test_id: str, title: str):
    # Stampa l'header del test
    print(f"\n{'='*80}")
    print(f"{Fore.CYAN}{Style.BRIGHT}TEST {test_id}: {title}")
    print(f"{'='*80}")

def compare_json_fields(expected: Dict, actual: Dict) -> bool:
    # funzione di confronto tra expected e actual, stampa le differenze
    print(f"\n{Fore.CYAN}--- Analisi Campi JSON ---{Style.RESET_ALL}")
    
    match_all = True
    
    for key, expected_val in expected.items():
        if key not in actual:
            print(f"{Fore.YELLOW}⚠️  Campo '{key}' MANCANTE nella risposta (Atteso: {expected_val}){Style.RESET_ALL}")
            match_all = False
            continue
            
        actual_val = actual[key]
        
        # Logica di confronto
        is_match = False
        if str(expected_val) == str(actual_val):
            is_match = True
        elif isinstance(expected_val, str) and isinstance(actual_val, str):
            if expected_val.strip() in actual_val.strip():
                is_match = True

        if is_match:
            print(f"{Fore.GREEN}✅ {key}: {actual_val}{Style.RESET_ALL}")
        else:
            print(f"{Fore.RED}❌ {key}: DIVERSO{Style.RESET_ALL}")
            print(f"   ├─ Atteso:  {expected_val}")
            print(f"   └─ Ricevuto: {actual_val}")
            match_all = False
            
    # Gestione di eventuali campi extra non previsti
    extra_keys = set(actual.keys()) - set(expected.keys())
    if extra_keys:
        print(f"{Fore.BLUE}ℹ️  Campi extra ricevuti: {', '.join(extra_keys)}{Style.RESET_ALL}")
        
    return match_all

def analyze_response(response: requests.Response, schema_key: str) -> bool:

    # Funzione "finale" di test. Fa sia il controllo sullo status code, che quello comparando i campi del json
    # Il ritorno è un booleano: true se lo STATUS CODE matcha, false altrimenti
    if schema_key not in EXPECTED_SCHEMAS:
        print(f"{Fore.RED}Errore config: Schema {schema_key} non trovato.{Style.RESET_ALL}")
        return False
        
    expected_schema = EXPECTED_SCHEMAS[schema_key]
    expected_status = expected_schema["status_code"]
    actual_status = response.status_code
    
    # Analisi Status Code
    status_match = (expected_status == actual_status)
    color = Fore.GREEN if status_match else Fore.RED
    print(f"Status Code: {color}{actual_status}{Style.RESET_ALL} (Atteso: {expected_status})")
    
    if not status_match:
        print(f"{Fore.RED}❌ FALLITO SULLO STATUS CODE{Style.RESET_ALL}")
        return False

    # Analisi JSON 
    try:
        actual_json = response.json()
        expected_json = expected_schema.get("json_body", {})
        
        if expected_json:
            json_match = compare_json_fields(expected_json, actual_json)
            if not json_match:
                print(f"\n{Fore.YELLOW}⚠ ATTENZIONE: Status Code corretto, ma JSON diverso dallo standard.{Style.RESET_ALL}")
        else:
            print(f"{Fore.GREEN}JSON Body: Ignorato o vuoto nello schema atteso.{Style.RESET_ALL}")
            
    except ValueError:
        print(f"{Fore.RED}❌ Impossibile parsare il JSON.{Style.RESET_ALL}")
        print(f"Raw body start: {response.text[:100]}...")
        if expected_schema.get("json_body"):
            print(f"{Fore.YELLOW}⚠ ATTENZIONE: Atteso JSON, ricevuto altro.{Style.RESET_ALL}")

    print(f"\n{Fore.GREEN}✓ TEST SUPERATO (Basato solo su Status Code){Style.RESET_ALL}")
    return True

# TESTS: 

def tc_01_flusso_nominale():
    """
    TC-01: Flusso Nominale (Happy Path)
    Pre-condizioni: Token valido, Rate limit disponibile, CB Closed
    Risultato atteso: HTTP 200
    """
    print_test_header("TC-01", "Flusso Nominale (Happy Path)")
    
    try:
        response = requests.post(
            COMPILE_ENDPOINT,
            json=PAYLOAD,
            cookies=get_auth_cookies(),
            timeout=REQUEST_TIMEOUT
        )
        return analyze_response(response, "SUCCESS")
    except Exception as e:
        print(f"{Fore.RED}❌ Errore durante il test: {e}{Style.RESET_ALL}")
        return False

def tc_02_autenticazione_fallita():
    """
    TC-02: Autenticazione Fallita
    Pre-condizioni: Token mancante/invalido
    Risultato atteso: HTTP 401
    """
    print_test_header("TC-02", "Autenticazione Fallita")
    
    print(f"{Fore.YELLOW}Invio richiesta con token invalido...{Style.RESET_ALL}")
    
    try:
        response = requests.post(
            COMPILE_ENDPOINT,
            json=PAYLOAD,
            cookies={"jwt": "token_invalido"},
            timeout=REQUEST_TIMEOUT
        )
        return analyze_response(response, "UNAUTHORIZED")
    except Exception as e:
        print(f"{Fore.RED}❌ Errore durante il test: {e}{Style.RESET_ALL}")
        return False

def tc_03_violazione_rate_limiter():
    """
    TC-03: Violazione Rate Limiter (Stress Test)
    Pre-condizioni: Token valido, CB Closed
    Risultato atteso: HTTP 429
    """
    print_test_header("TC-03", "Violazione Rate Limiter (Stress Test)")
    
    config = RATE_LIMIT_TEST_CONFIG
    print(f"{Fore.YELLOW}🚀 Avvio ({config['max_workers']} thread, max {config['max_requests']} richieste)...{Style.RESET_ALL}")
    
    # Setup sessione con connection pooling
    session = requests.Session()
    adapter = HTTPAdapter(
        pool_connections=HTTP_ADAPTER_CONFIG["pool_connections"],
        pool_maxsize=HTTP_ADAPTER_CONFIG["pool_maxsize"]
    )
    session.mount('http://', adapter)
    
    cookies = get_auth_cookies()
    success_response = None
    stats = Counter()
    
    def send_request():
        """Invia singola richiesta"""
        try:
            return session.post(
                COMPILE_ENDPOINT,
                json=PAYLOAD,
                cookies=cookies,
                timeout=config["request_timeout"]
            )
        except Exception:
            return None
    
    # Esecuzione parallela
    with ThreadPoolExecutor(max_workers=config["max_workers"]) as executor:
        futures = [executor.submit(send_request) for _ in range(config["max_requests"])]
        
        for future in as_completed(futures):
            try:
                result = future.result()
                if isinstance(result, requests.Response):
                    stats[result.status_code] += 1
                    
                    # Appena troviamo un 429, abbiamo finito
                    if result.status_code == 429:
                        success_response = result
                        break
            except Exception:
                pass
    
    session.close()
    
    # Verifica risultato
    if success_response is not None:
        print(f"\n{Fore.GREEN}[PASSATO] Status: {success_response.status_code} | Rate Limit intercettato correttamente.{Style.RESET_ALL}")
        print(f"{Fore.CYAN}Statistiche Status Code: {dict(stats)}{Style.RESET_ALL}")
        return analyze_response(success_response, "RATE_LIMIT")
    else:
        print(f"\n{Fore.RED}❌ Rate limit NON attivato su {sum(stats.values())} risposte valide.{Style.RESET_ALL}")
        print(f"{Fore.RED}Statistiche Status Code: {dict(stats)}{Style.RESET_ALL}")
        return False

def tc_04_circuit_breaker_aperto():
    """
    TC-04: Circuit Breaker Aperto
    Pre-condizioni: Token valido, Backend DOWN
    Risultato atteso: HTTP 503
    """
    print_test_header("TC-04", "Circuit Breaker - Aperto")
    
    print(f"{Fore.YELLOW}⚠️  Requisito: BACKEND deve essere DOWN{Style.RESET_ALL}")
    input(f"{Fore.CYAN}Premi INVIO quando il backend è spento...{Style.RESET_ALL}")
    
    try:
        response = requests.post(
            COMPILE_ENDPOINT,
            json=PAYLOAD,
            cookies=get_auth_cookies(),
            timeout=REQUEST_TIMEOUT_EXTENDED
        )
        return analyze_response(response, "SERVICE_UNAVAILABLE")
    except Exception as e:
        print(f"{Fore.RED}❌ Errore durante il test: {e}{Style.RESET_ALL}")
        return False

def tc_05a_cb_half_open_success():
    """
    TC-05a: Circuit Breaker Half-Open (Successo)
    Pre-condizioni: Token valido, CB in Half-Open, Backend UP
    Risultato atteso: HTTP 200
    """
    print_test_header("TC-05a", "Circuit Breaker Half-Open (Successo)")
    
    print(f"{Fore.YELLOW}⚠️  Requisito: CB in Half-Open + Backend UP{Style.RESET_ALL}")
    input(f"{Fore.CYAN}Premi INVIO quando pronto...{Style.RESET_ALL}")
    
    try:
        response = requests.post(
            COMPILE_ENDPOINT,
            json=PAYLOAD,
            cookies=get_auth_cookies(),
            timeout=REQUEST_TIMEOUT
        )
        return analyze_response(response, "SUCCESS")
    except Exception as e:
        print(f"{Fore.RED}❌ Errore durante il test: {e}{Style.RESET_ALL}")
        return False

def tc_05b_cb_half_open_fail():
    """
    TC-05b: Circuit Breaker Half-Open (Fallimento)
    Pre-condizioni: Token valido, CB in Half-Open, Backend DOWN
    Risultato atteso: HTTP 503
    """
    print_test_header("TC-05b", "Circuit Breaker Half-Open (Fallimento)")
    
    print(f"{Fore.YELLOW}⚠️  Requisito: CB in Half-Open + Backend DOWN{Style.RESET_ALL}")
    input(f"{Fore.CYAN}Premi INVIO quando pronto...{Style.RESET_ALL}")
    
    try:
        response = requests.post(
            COMPILE_ENDPOINT,
            json=PAYLOAD,
            cookies=get_auth_cookies(),
            timeout=REQUEST_TIMEOUT
        )
        return analyze_response(response, "SERVICE_UNAVAILABLE")
    except Exception as e:
        print(f"{Fore.RED}❌ Errore durante il test: {e}{Style.RESET_ALL}")
        return False

# Grafica

TESTS = {
    "1": {
        "id": "TC-01",
        "name": "Flusso Nominale (Happy Path)",
        "function": tc_01_flusso_nominale
    },
    "2": {
        "id": "TC-02",
        "name": "Autenticazione Fallita",
        "function": tc_02_autenticazione_fallita
    },
    "3": {
        "id": "TC-03",
        "name": "Violazione Rate Limiter",
        "function": tc_03_violazione_rate_limiter
    },
    "4": {
        "id": "TC-04",
        "name": "Circuit Breaker Aperto",
        "function": tc_04_circuit_breaker_aperto
    }
    # "5": {
    #     "id": "TC-05a",
    #     "name": "CB Half-Open (Successo)",
    #     "function": tc_05a_cb_half_open_success
    # },
    # "6": {
    #     "id": "TC-05b",
    #     "name": "CB Half-Open (Fallimento)",
    #     "function": tc_05b_cb_half_open_fail
    # }
}

def print_menu():
    """Stampa il menu principale"""
    print(f"\n{Fore.MAGENTA}{Style.BRIGHT}{'='*80}")
    print(f"TEST SUITE API GATEWAY - MENU PRINCIPALE")
    print(f"{'='*80}{Style.RESET_ALL}\n")
    
    for key, test in TESTS.items():
        print(f"{Fore.GREEN}{key}.{Style.RESET_ALL} {Fore.YELLOW}{test['id']}{Style.RESET_ALL} - {test['name']}")
    
    print(f"\n{Fore.CYAN}Opzioni Speciali:{Style.RESET_ALL}")
    print(f"{Fore.GREEN}0.{Style.RESET_ALL} Esegui TUTTI i test (automatico: TC-01, TC-02, TC-03)")
    print(f"{Fore.GREEN}a.{Style.RESET_ALL} Informazioni autenticazione")
    print(f"{Fore.GREEN}c.{Style.RESET_ALL} Pulisci cache JWT")
    print(f"{Fore.RED}q.{Style.RESET_ALL} Esci\n")

def run_all_tests():
    """Esegue i test automatizzabili"""
    print(f"\n{Fore.MAGENTA}{Style.BRIGHT}{'='*80}")
    print(f"ESECUZIONE AUTOMATICA TEST")
    print(f"{'='*80}{Style.RESET_ALL}\n")
    
    # Solo test che non richiedono setup manuale
    auto_tests = ["1", "2", "3"]
    results = {}
    
    for key in auto_tests:
        test = TESTS[key]
        print(f"\n{Fore.CYAN}Esecuzione {test['id']}...{Style.RESET_ALL}")
        results[test['id']] = test["function"]()
        time.sleep(1)
    
    # Riepilogo
    print(f"\n{Fore.MAGENTA}{Style.BRIGHT}{'='*80}")
    print(f"RIEPILOGO TEST")
    print(f"{'='*80}{Style.RESET_ALL}\n")
    
    passed = sum(1 for v in results.values() if v)
    total = len(results)
    
    for test_id, result in results.items():
        status = f"{Fore.GREEN}✓ PASSED" if result else f"{Fore.RED}✗ FAILED"
        print(f"{test_id}: {status}{Style.RESET_ALL}")
    
    print(f"\n{Fore.CYAN}Totale: {passed}/{total} test passati{Style.RESET_ALL}")

def show_auth_info():
    """Mostra informazioni sull'autenticazione corrente"""
    print(f"\n{Fore.CYAN}{'='*80}")
    print(f"INFORMAZIONI AUTENTICAZIONE")
    print(f"{'='*80}{Style.RESET_ALL}\n")
    
    jwt_token, refresh_token = auth_manager.load_tokens_from_cache()
    
    if jwt_token:
        print(f"{Fore.GREEN}JWT Token trovato in cache:{Style.RESET_ALL}")
        print(f"  {auth_manager.get_token_info(jwt_token)}")
        
        if auth_manager.is_token_expired(jwt_token):
            print(f"  {Fore.RED}⚠️  Token SCADUTO{Style.RESET_ALL}")
        else:
            print(f"  {Fore.GREEN}✓ Token VALIDO{Style.RESET_ALL}")
    else:
        print(f"{Fore.YELLOW}Nessun JWT in cache{Style.RESET_ALL}")
    
    if refresh_token:
        print(f"\n{Fore.GREEN}Refresh Token:{Style.RESET_ALL}")
        if auth_manager.is_token_expired(refresh_token):
            print(f"  {Fore.RED}⚠️  SCADUTO{Style.RESET_ALL}")
        else:
            print(f"  {Fore.GREEN}✓ VALIDO{Style.RESET_ALL}")
    else:
        print(f"\n{Fore.YELLOW}Nessun Refresh Token in cache{Style.RESET_ALL}")

def main():
    """Funzione principale con menu interattivo"""
    print(f"\n{Fore.CYAN}{Style.BRIGHT}{'='*80}")
    print(f"TEST SUITE API GATEWAY MICROSERVIZI")
    print(f"{'='*80}{Style.RESET_ALL}\n")
    
    # Mostra configurazione
    print_config_summary()
    
    while True:
        print_menu()
        
        choice = input(f"{Fore.YELLOW}Seleziona un'opzione: {Style.RESET_ALL}").strip().lower()
        
        if choice == 'q':
            print(f"\n{Fore.CYAN}Uscita dal programma. Arrivederci!{Style.RESET_ALL}\n")
            break
        elif choice == '0':
            run_all_tests()
        elif choice == 'a':
            show_auth_info()
        elif choice == 'c':
            auth_manager.clear_cache()
        elif choice in TESTS:
            TESTS[choice]["function"]()
        else:
            print(f"\n{Fore.RED}Opzione non valida! Riprova.{Style.RESET_ALL}")
        
        input(f"\n{Fore.CYAN}Premi INVIO per tornare al menu principale...{Style.RESET_ALL}")


if __name__ == "__main__":
    main()
