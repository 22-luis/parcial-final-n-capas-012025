# Indicaciones para levantar el entorno

## Pasos para levantar el entorno

### 1. Clonar o descargar el proyecto
```bash
git clone <url-del-repositorio>
cd parcial-final-n-capas-012025
```

### 2. Levantar los servicios con Docker Compose
```bash
docker-compose up --build
```


### 3. Verificar que todo esté funcionando

#### Base de datos PostgreSQL
- Puerto: 5432
- Base de datos: midb
- Usuario: admin
- Contraseña: admin123

#### Aplicación Spring Boot
- Puerto: 8080
- URL: http://localhost:8080

