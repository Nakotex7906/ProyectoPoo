Cerebro en accion (Juego de preguntas)

Pre-requisitos 📋

Que cosas se necesitan para jugar:

-Intellij Idea (Recomendada la version del 2023 en adelante)

-MySql Workbench (Version 8.0 en adelante)

Instalación 🔧

Creacion de la base de datos

Crea un schema para crear las tablas

Crea la tabla categoria con las siguientes columnas

-Id_categoria
-Nombre_categoria

Crea la tabla pregunta con las siguientes columnas

-Id_preguta
-Id_categoria (FK)
-Texto_Pregunta

Crea la tabla respuesta con las siguientes columnas

-Id_respuesta
-Id_categoria (FK)
-texto_pregunta

Conectar de la base de datos

1.Descargar el conector de la base de datos https://dev.mysql.com/downloads/connector/j/

2.Dentro del proyecto crea un directorio donde pondremos este archivo jar

3.En la clase JDBC cambia los atributos DB_URL si es que la ruta de la base de datos es diferente

4.Cambiar DB_USERNAME en el caso de que se cambie el nombre del usuario

5.Por ultimo cambiar DB_PASSWORD si es que tienes una contraseña puesta en la base de datos

Construido con 🛠️

IDE utilizado https - //www.jetbrains.com/idea/

Biblioteca pasa hacer las pruebas unitarias - https://www.jetbrains.com/idea/

Base de datos utilizada - https://www.mysql.com/products/workbench/

Autores ✒️

Ignacio Essus - https://github.com/Nakotex7906

W.Alonso.chavez - https://github.com/AlonsoRom


