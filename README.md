# Challenge BCI - Tomás González V.

Es una aplicación desarrollada en Java con SpringBoot y base de datos h2.

## Compilación

```bash
mvn clean package
```

## Ejecución

```
java -jar ./target/challenger-1.0.0.jar
```
Luego, ir a localhost:8080/api/v1/user


### Para crear Usuario:
Post localhost:8080/api/v1/user 

Request Body:
{
    "email": "juan.perico@rodriguez.org",
    "name": "Juan Rodriguez11",
	"userName": "Juanito02321",
    "password": "Testi.ng&19!",
    "passwordConfirm": "Testi.ng&19!",
    "phones": [
        {
            "cityCode": "1",
            "countryCode": "57",
            "number": "1234567"
        }
    ]
}

### Para Update
Put localhost:8080/api/v1/user/{userUuuid}
{
    "userUuid": "a69a0386-0d78-4046-af0d-a7e6c29ab044",
    "email": "juan@rodriguez.org",
    "name": "Juan Rodriguez11",
	"userName": "Juanito01",
    "password": "Testi.ng&19!",
    "passwordConfirm": "Testi.ng&19!",
    "phones": [
        {
            "cityCode": "1",
            "countryCode": "57",
            "number": "123465789"
        }
    ]
}

### Para Disabled
Put localhost:8080/api/v1/user/{userUuuid}/disabled# challgenr-bci
Challenger BCI TGV


### Para revisar swagger
http://localhost:8080/api/v1/swagger-ui/index.html#/

### URl para console h2
localhost:8080/api/v1/console-h2

##Configurado en properties:
Base de datos
Patrón para regex de password
Expiracion de jwt token

## Documentación del proyecto
En la carpeta resouces se dejaron png y fuente de los diagramas de secuencia y de clases del proyecto. Se hicieron con plantUML


