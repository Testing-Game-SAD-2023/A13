

echo "Installing T5-G2"
cd "./T5-G2/t5"
call mvn package || ( echo "Error in T5-G2 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T5-G2 installation during docker compose up" && exit /b 1 )
exit /b 0