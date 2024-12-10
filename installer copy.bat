echo "Installing ui_gateway"
cd "./ui_gateway"
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && exit /b 1 )
exit /b 0
