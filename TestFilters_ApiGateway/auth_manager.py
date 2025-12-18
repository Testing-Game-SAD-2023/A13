"""
Modulo per la gestione dell'autenticazione JWT con sistema di cache e refresh automatico.
"""

import os
import json
import time
import requests
import jwt as pyjwt
from typing import Tuple, Optional, Dict
from datetime import datetime
from colorama import Fore, Style, init

init(autoreset=True)


class JWTAuthManager:
    """Gestisce l'autenticazione JWT con cache e refresh automatico"""
    
    def __init__(self, base_url: str, cache_file: str = ".jwt_cache.json"):
        """
        Inizializza il manager di autenticazione.
        
        Args:
            base_url: URL base del servizio (es. http://localhost:8090)
            cache_file: Path del file cache per salvare i token
        """
        self.base_url = base_url
        self.cache_file = cache_file
        self.login_endpoint = f"{base_url}/userService/auth/login"
        self.refresh_endpoint = f"{base_url}/userService/auth/refresh"
    
    def decode_jwt_payload(self, token: str) -> Dict:
        """
        Decodifica il payload del JWT senza verificare la firma.
        
        Args:
            token: JWT token da decodificare
            
        Returns:
            Dizionario con il payload del token, o {} se errore
        """
        try:
            return pyjwt.decode(token, options={"verify_signature": False})
        except Exception as e:
            print(f"{Fore.RED}❌ Errore decodifica JWT: {e}{Style.RESET_ALL}")
            return {}
    
    def is_token_expired(self, token: str, buffer_seconds: int = 60) -> bool:
        """
        Controlla se un token è scaduto o sta per scadere.
        
        Args:
            token: JWT token da verificare
            buffer_seconds: Secondi di buffer prima della scadenza effettiva
            
        Returns:
            True se il token è scaduto o invalido
        """
        if not token:
            return True
            
        payload = self.decode_jwt_payload(token)
        exp = payload.get("exp")
        
        if not exp:
            return True
        
        # Considera scaduto se manca meno di buffer_seconds
        return time.time() > (exp - buffer_seconds)
    
    def get_token_info(self, token: str) -> str:
        """
        Restituisce informazioni leggibili sul token.
        
        Args:
            token: JWT token
            
        Returns:
            Stringa con info formattate
        """
        payload = self.decode_jwt_payload(token)
        if not payload:
            return "Token invalido"
        
        user = payload.get("sub", "N/A")
        role = payload.get("role", "N/A")
        exp = payload.get("exp")
        
        if exp:
            exp_date = datetime.fromtimestamp(exp).strftime("%Y-%m-%d %H:%M:%S")
            remaining = int(exp - time.time())
            if remaining > 0:
                remaining_str = f"{remaining // 60}m {remaining % 60}s"
            else:
                remaining_str = "SCADUTO"
        else:
            exp_date = "N/A"
            remaining_str = "N/A"
        
        return f"User: {user} | Role: {role} | Scade: {exp_date} | Rimangono: {remaining_str}"
    
    def save_tokens_to_cache(self, jwt_token: str, refresh_token: str):
        """
        Salva i token nel file cache.
        
        Args:
            jwt_token: JWT access token
            refresh_token: Refresh token
        """
        try:
            cache_data = {
                "jwt": jwt_token,
                "jwt_refresh": refresh_token,
                "saved_at": datetime.now().isoformat()
            }
            
            with open(self.cache_file, 'w') as f:
                json.dump(cache_data, f, indent=2)
            
            print(f"{Fore.GREEN}✓ Token salvati in cache ({self.cache_file}){Style.RESET_ALL}")
        except Exception as e:
            print(f"{Fore.YELLOW}⚠ Impossibile salvare cache: {e}{Style.RESET_ALL}")
    
    def load_tokens_from_cache(self) -> Tuple[str, str]:
        """
        Carica i token dal file cache.
        
        Returns:
            Tupla (jwt_token, refresh_token), stringhe vuote se non trovati
        """
        try:
            if os.path.exists(self.cache_file):
                with open(self.cache_file, 'r') as f:
                    data = json.load(f)
                    jwt_token = data.get("jwt", "")
                    refresh_token = data.get("jwt_refresh", "")
                    saved_at = data.get("saved_at", "N/A")
                    
                    if jwt_token and refresh_token:
                        print(f"{Fore.CYAN}📦 Token caricati dalla cache (salvati: {saved_at}){Style.RESET_ALL}")
                        return jwt_token, refresh_token
        except Exception as e:
            print(f"{Fore.YELLOW}⚠ Impossibile leggere cache: {e}{Style.RESET_ALL}")
        
        return "", ""
    
    def clear_cache(self):
        """Elimina il file cache"""
        try:
            if os.path.exists(self.cache_file):
                os.remove(self.cache_file)
                print(f"{Fore.GREEN}✓ Cache eliminata{Style.RESET_ALL}")
        except Exception as e:
            print(f"{Fore.RED}❌ Errore eliminazione cache: {e}{Style.RESET_ALL}")
    
    def do_login(self, username: str, password: str) -> Tuple[str, str]:
        """
        Esegue il login e recupera i token dai cookie della risposta.
        
        Args:
            username: Username/email per il login
            password: Password
            
        Returns:
            Tupla (jwt_token, refresh_token), stringhe vuote se fallito
        """
        print(f"{Fore.CYAN}🔐 Login in corso per {username}...{Style.RESET_ALL}")
        
        try:
            response = requests.post(
                self.login_endpoint,
                json={"email": username, "password": password},
                timeout=10
            )
            
            if response.status_code == 200:
                # Estrae i token dai cookie Set-Cookie
                jwt_token = response.cookies.get("jwt", "")
                refresh_token = response.cookies.get("jwt-refresh", "")
                
                if jwt_token and refresh_token:
                    print(f"{Fore.GREEN}✓ Login riuscito!{Style.RESET_ALL}")
                    print(f"{Fore.CYAN}Token info: {self.get_token_info(jwt_token)}{Style.RESET_ALL}")
                    
                    # Salva in cache
                    self.save_tokens_to_cache(jwt_token, refresh_token)
                    return jwt_token, refresh_token
                else:
                    print(f"{Fore.RED}❌ Cookie jwt/jwt-refresh non trovati nella risposta{Style.RESET_ALL}")
                    print(f"{Fore.YELLOW}Cookie ricevuti: {list(response.cookies.keys())}{Style.RESET_ALL}")
            else:
                print(f"{Fore.RED}❌ Login fallito con status {response.status_code}{Style.RESET_ALL}")
                try:
                    error_msg = response.json()
                    print(f"{Fore.RED}Dettagli: {error_msg}{Style.RESET_ALL}")
                except:
                    print(f"{Fore.RED}Response: {response.text[:200]}{Style.RESET_ALL}")
                    
        except requests.exceptions.Timeout:
            print(f"{Fore.RED}❌ Timeout durante il login{Style.RESET_ALL}")
        except requests.exceptions.ConnectionError:
            print(f"{Fore.RED}❌ Impossibile connettersi a {self.login_endpoint}{Style.RESET_ALL}")
        except Exception as e:
            print(f"{Fore.RED}❌ Errore durante login: {e}{Style.RESET_ALL}")
        
        return "", ""
    
    def refresh_jwt_token(self, refresh_token: str) -> str:
        """
        Usa il refresh token per ottenere un nuovo JWT.
        
        Args:
            refresh_token: Refresh token valido
            
        Returns:
            Nuovo JWT token, stringa vuota se fallito
        """
        print(f"{Fore.YELLOW}🔄 Refresh del JWT in corso...{Style.RESET_ALL}")
        
        try:
            response = requests.post(
                self.refresh_endpoint,
                cookies={"jwt-refresh": refresh_token},
                timeout=10
            )
            
            if response.status_code == 200:
                new_jwt = response.cookies.get("jwt", "")
                
                if new_jwt:
                    print(f"{Fore.GREEN}✓ JWT refreshato con successo!{Style.RESET_ALL}")
                    print(f"{Fore.CYAN}Nuovo token info: {self.get_token_info(new_jwt)}{Style.RESET_ALL}")
                    
                    # Aggiorna cache mantenendo lo stesso refresh token
                    self.save_tokens_to_cache(new_jwt, refresh_token)
                    return new_jwt
                else:
                    print(f"{Fore.RED}❌ Nuovo JWT non trovato nei cookie{Style.RESET_ALL}")
            else:
                print(f"{Fore.RED}❌ Refresh fallito con status {response.status_code}{Style.RESET_ALL}")
                
        except requests.exceptions.ConnectionError:
            print(f"{Fore.RED}❌ Impossibile connettersi a {self.refresh_endpoint}{Style.RESET_ALL}")
        except Exception as e:
            print(f"{Fore.RED}❌ Errore durante refresh: {e}{Style.RESET_ALL}")
        
        return ""
    
    def get_valid_token(self, username: Optional[str] = None, password: Optional[str] = None, 
                       force_login: bool = False) -> str:
        """
        Strategia intelligente per ottenere un JWT valido.
        
        Flusso:
        1. Se force_login=True → login diretto
        2. Controlla cache → se JWT valido, usa quello
        3. Se JWT scaduto ma refresh valido → refresh
        4. Altrimenti → login (chiede credenziali se non fornite)
        
        Args:
            username: Username per login (opzionale se in cache)
            password: Password per login (opzionale se in cache)
            force_login: Se True, forza nuovo login ignorando cache
            
        Returns:
            JWT token valido, stringa vuota se fallito
        """
        # Caso 1: Force login
        if force_login:
            if not username or not password:
                print(f"{Fore.YELLOW}Credenziali necessarie per force login{Style.RESET_ALL}")
                username = input("Username: ").strip()
                password = input("Password: ").strip()
            
            jwt_token, refresh_token = self.do_login(username, password)
            return jwt_token
        
        # Caso 2: Prova cache
        cached_jwt, cached_refresh = self.load_tokens_from_cache()
        
        if cached_jwt:
            # JWT ancora valido
            if not self.is_token_expired(cached_jwt):
                print(f"{Fore.GREEN}✓ JWT valido trovato in cache{Style.RESET_ALL}")
                print(f"{Fore.CYAN}Token info: {self.get_token_info(cached_jwt)}{Style.RESET_ALL}")
                return cached_jwt
            else:
                print(f"{Fore.YELLOW}⚠ JWT in cache scaduto{Style.RESET_ALL}")
                
                # Caso 3: Prova refresh
                if cached_refresh and not self.is_token_expired(cached_refresh):
                    new_jwt = self.refresh_jwt_token(cached_refresh)
                    if new_jwt:
                        return new_jwt
                    print(f"{Fore.YELLOW}⚠ Refresh fallito, procedo con login{Style.RESET_ALL}")
                else:
                    print(f"{Fore.YELLOW}⚠ Refresh token scaduto o mancante{Style.RESET_ALL}")
        
        # Caso 4: Login necessario
        if not username or not password:
            print(f"\n{Fore.CYAN}{'='*60}")
            print(f"Credenziali necessarie per ottenere un nuovo JWT")
            print(f"{'='*60}{Style.RESET_ALL}")
            username = input("Username: ").strip()
            password = input("Password: ").strip()
        
        jwt_token, refresh_token = self.do_login(username, password)
        return jwt_token


# Funzione helper per uso rapido
def get_jwt_token(base_url: str, username: str = None, password: str = None, 
                  force_login: bool = False) -> str:
    """
    Funzione helper per ottenere rapidamente un JWT valido.
    
    Args:
        base_url: URL base del servizio
        username: Username (opzionale se in cache)
        password: Password (opzionale se in cache)
        force_login: Forza nuovo login
        
    Returns:
        JWT token valido
        
    Example:
        token = get_jwt_token("http://localhost:8090", "user@test.com", "pass123")
        cookies = {"jwt": token}
    """
    manager = JWTAuthManager(base_url)
    return manager.get_valid_token(username, password, force_login)


if __name__ == "__main__":
    # Test del modulo
    print(f"{Fore.MAGENTA}{Style.BRIGHT}{'='*60}")
    print(f"TEST AUTH MANAGER")
    print(f"{'='*60}{Style.RESET_ALL}\n")
    
    manager = JWTAuthManager("http://localhost:8090")
    
    # Test recupero token
    token = manager.get_valid_token()
    
    if token:
        print(f"\n{Fore.GREEN}✓ Token ottenuto con successo!{Style.RESET_ALL}")
        print(f"{Fore.CYAN}Token (primi 50 char): {token[:50]}...{Style.RESET_ALL}")
    else:
        print(f"\n{Fore.RED}❌ Impossibile ottenere token{Style.RESET_ALL}")
