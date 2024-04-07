# File share TG bot

Services:

- dispactcher - receives messages from TG and sends to rabbitmq queue
- node - receives messages from rabbitmq queue and processes it
- mail-service - receives message from rabbitmq queue and sends messages to email
