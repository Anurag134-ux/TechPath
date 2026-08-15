TECHPATH — Java + HTML + CSS

A career discovery website for technology/engineering domains.

TECHNOLOGIES
- HTML
- CSS
- Java
- NO JAVASCRIPT

PROJECT STRUCTURE

TechPath/
├── public/
│   ├── index.html
│   ├── assessment.html
│   ├── result.html
│   └── style.css
├── src/
│   ├── CareerProfile.java
│   └── TechPathServer.java
└── README.txt

DOMAINS
1. Software Engineering
2. Data Science
3. Data Analytics
4. AI / ML
5. Web Development
6. Cybersecurity
7. Cloud / DevOps
8. UI/UX + Product
9. Robotics
10. Embedded Systems
11. Blockchain
12. Game Development
13. Network Engineering
14. Cloud Architecture
15. Quantitative Finance
16. Product Management

RUN

Requirements:
- Java 17 or newer

Windows:
1. Open Command Prompt in the TechPath folder.
2. Compile:
   javac -d out src\CareerProfile.java src\TechPathServer.java
3. Start:
   java -cp out TechPathServer
4. Open:
   http://localhost:8080

Linux/macOS:
javac -d out src/CareerProfile.java src/TechPathServer.java
java -cp out TechPathServer

HOW IT WORKS

The assessment uses standard HTML forms. Each answer is submitted to Java.
Java keeps the answers in hidden form fields, renders the next question, and
calculates the final ranking across all 16 domains.

No JavaScript is required.
