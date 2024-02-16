# RunCommands.ps1

$commands = @'
use manvsclass
db.createCollection("ClassUT");
db.createCollection("interaction");
db.createCollection("Admin");
db.createCollection("Operation");
db.ClassUT.createIndex({ difficulty: 1 });
db.interaction.createIndex({ name: "text", type: 1 });
db.Operation.createIndex({ name: "text" });
db.Admin.createIndex({ username: 1 });
'@

$commands | docker exec -i manvsclass-mongo_db-1 mongosh

# Aggiungi una pausa alla fine per mantenere aperta la finestra del prompt
pause