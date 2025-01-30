# Usa l'immagine ufficiale di Nginx come base
FROM nginx:1.23.1

# Copia la configurazione principale di Nginx nel container
COPY ./default.conf /etc/nginx/conf.d/default.conf

# Copia le configurazioni modulari delle route
COPY ./routes /etc/nginx/routes

# Copia la cartella 'includes' per error.html e proxy.conf
COPY ./includes/ /etc/nginx/includes/

# Espone la porta 80 per l'accesso HTTP
EXPOSE 80