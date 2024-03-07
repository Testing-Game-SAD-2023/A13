
#!/usr/bin/mongo
use manvsclass;

db.ClassUT.insertMany([
  {
    "_id": ObjectId("646ccff20e4c5452c63de06a"),
    "name": "Classe10",
    "date": ISODate("2024-02-28T00:00:00Z"),
    "difficulty": "Beginner",
    "code_Uri": "Files-Upload/Classe10",
    "description": "La Classe10 fornisce ...",
    "category": ["Java", "Web Development", "Spring"]
  },
  {
    "_id": ObjectId("746ccff20e4c5452c63de06b"),
    "name": "Classe11",
    "date": ISODate("2024-03-15T00:00:00Z"),
    "difficulty": "Intermediate",
    "code_Uri": "Files-Upload/Classe11",
    "description": "La Classe11 fornisce ...",
    "category": ["Java", "Web Development", "Spring"]
  },
  {
    "_id": ObjectId("846ccff20e4c5452c63de06c"),
    "name": "Classe12",
    "date": ISODate("2024-04-01T00:00:00Z"),
    "difficulty": "Advanced",
    "code_Uri": "Files-Upload/Classe12",
    "description": "La Classe12 fornisce ...",
    "category": ["Java", "Web Development", "Spring"]
  }
]);

db.ClassUT.createIndex({ difficulty: 1 })
db.Interaction.createIndex({ name: "text", type: 1 })
db.interaction.createIndex({ name: "text" })