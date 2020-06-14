#LabBook Front-end API

## 1. DESCRIPTION
Simple front-end API in Vaadin for LabBook Api -> https://github.com/binkul/Rest-labbook.git,
just to check how the application work.

## 2. USED TECHNOLOGIES
- Vaadin
- Spring Web
- Spring security

## 3. REQUIREMENTS
Locally installed IntelliJ or another JAVA IDE environment.

## 4. HOW TO RUN
1. Clone the Backend -> https://github.com/binkul/Rest-labbook.git
2. Create local database and make the appropriate changes in application.properties
3. Run Backend first
4. Run this Frontend
5. In browser go to http://localhost:8081/
6. Register in API, then login -> you have 'USER' privileges
7. If You want to be an 'ADMIN':
- login with 'user: user', 'password: sata'
- go to menu 'Users' and change role for your account on 'ADMIN'
- changing 'Is observer' to true, You will receive emails from API
- log-out from user and login on Your account.

## 5. FUTURE PLANS
- Improve the functionality of the recipe editor 
- Archiving recipes
- Improving logic for CLP-calculation
