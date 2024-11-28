
echo "Installing T23-G1"
cd "./T23-G1"
call mvn package || ( echo "Error in T23-G1 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T23-G1 installation during docker compose up" && exit /b 1 )
exit /b 0