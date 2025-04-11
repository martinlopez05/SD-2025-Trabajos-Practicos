# Descripción de los ejercicios del Trabajo Práctico 1

## *Hit #1*

**Consigna:** Elabore un código de servidor TCP para B que espere el saludo de A y lo responda. Elabore un código de cliente TCP para A que se conecte con B y lo salude.

**Resolución:**

- Para resolver este ejercicio nosotros decidimos implementar dos clases, una clase **Cliente** y una clase **Server**. La clase cliente se va a encargar de intentar conectarse al servidor de manera persistente y reintenta la conexión en caso de que esta falle hasta alcanzar un máximo de reintentos. La clase server se va a encargar de aceptar conexiones, intercambiar mensajes con los clientes y cerrar la conexión cuando el cliente finaliza la comunicación.

Para poder probar la resolución de este HIT es necesario ejecutar los siguientes comandos:

**Paso 1:** Clonar el tag del HIT 1 de nuestro repositorio git.

```git
git clone --branch hit.1 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT1
```

**Paso 2 (NECESARIO):** Extraer los archivos *Server.java* y *Cliente.java* de sus respectivas carpetas y ponerlos en la carpeta */SD-2025-TrabajosPracticos*. (Esto va a ser solo necesario en este ejercicio debido a un error nuestro). Además dentro del código de Cliente y de Server es necesario borrar la primera linea que dice

```Java
package Server;
```

```Java
package Cliente;
```

**Paso 3:** Luego nos debemos de situar dentro de la carpeta para poder ejecutar el archivo docker

```git
cd .\HIT1\
```

**Paso 4:** Levantamos el contenedor de docker a partir del comando

```terminal
docker-compose up --build
```

Una vez terminada la prueba poner ```docker-compose down``` en la terminal para dar de baja el contenedor.

## *Hit #2*

**Resolución:**

**Consigna:** Revise el código de A para implementar una funcionalidad que permita la reconexión y el envío del saludo nuevamente en caso de que el proceso B cierre la conexión, como por ejemplo, al ser terminado abruptamente.

- Para resolver este ejercicio modificamos las clases existentes para que el Cliente pueda intentar conectarse al servidor. Una vez conectado el cliente comenzará a enviar un mensaje cada 5 segundos y va a esperar la respuesta del servidor. Si la conexión falla, entonces va a intentar reconectarse 5 veces antes de abortar.

**Paso 1:** Clonar el tag del HIT 2 de nuestro repositorio git.

```git
git clone --branch hit.2 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT2
```

**Paso 2:** Luego nos debemos de situar dentro de la carpeta para poder ejecutar el archivo docker

```git
cd .\HIT2\tp1
```

**Paso 3:** Levantamos el contenedor de docker a partir del comando

```terminal
docker-compose up --build
```

Se puede probar este ejercicio dando de baja el servidor a tráves del comando `docker stop tp1-server-1`. Una vez terminada la prueba poner `docker-compose down` en la terminal para dar de baja el contenedor.

## *Hit #3*

**Consigna:** Modifique el código de B para que si el proceso A cierra la conexión (por ejemplo matando el proceso) siga funcionando.

**Resolución:**

- Para resolver este ejercicio modificamos la clase Servidor para que permita aceptar conexiones de clientes e intercambiar mensajes entre ellos. Además va a permitir que el Servidor mantega su conexión abierta en el caso de que el cliente no este conectado.

**Paso 1:** Clonar el tag del HIT 3 de nuestro repositorio git.

```git
git clone --branch hit.3 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT3
```

**Paso 2:** Nos situamos dentro de la carpeta para poder ejecutar el archivo docker.

```git
cd .\HIT3\tp1
```

**Paso 3:** Levantamos el contenedor.

```terminal
docker-compose up --build
```

Se puede probar este ejercicio dando de baja el cliente a tráves del comando `docker stop tp1-client-1`. Y luego dar de alta `docker start tp1-client-1`. Una vez terminada la prueba poner `docker-compose down` en la terminal para dar de baja el contenedor.

## *Hit #4*

**Consigna:** Refactoriza el código de los programas A y B en un único programa, que funcione simultáneamente como cliente y servidor. Esto significa que al iniciar el programa C, se le deben proporcionar por parámetros la dirección IP y el puerto para escuchar saludos, así como la dirección IP y el puerto de otro nodo C. De esta manera, al tener dos instancias de C en ejecución, cada una configurada con los parámetros del otro, ambas se saludan mutuamente a través de cada canal de comunicación.

**Resolución:**

- Para resolver este ejercicio creamos una nueva clase llamada *Nodo.java* esta clase se va a encargar de actuar simultánemente como un **servidor** que escucha conexiones entrantes en un puerto determinado y como un **cliente** que intenta conectarse a otro nodo especificado por una dirección IP y un puerto.
- El **servidor** `(startServer())` espera conexiones en su puerto de escucha, acepta clientes entrantes y les envía un mensaje de bienvenida. Luego, lee y muestra cualquier mensaje recibido hasta que la conexión se cierra. Por otro lado, el **cliente** `(startClient())` intenta conectarse al nodo especificado, envía mensajes periódicamente y muestra las respuestas del servidor.

**Paso 1:** Clonar el tag del HIT 4 de nuestro repositorio git.

```git
git clone --branch hit.4 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT4
```

**Paso 2:** Nos situamos dentro de la carpeta para poder ejecutar el contenedor docker.

```git
cd .\HIT4\tp1
```

**Paso 3:** Levantamos el contenedor.

```terminal
docker-compose up --build
```

Una vez terminada la prueba poner `docker-compose down` en la terminal para dar de baja el contenedor.

## *Hit #5*

**Consigna:** Modifique el programa C de manera tal que los mensajes se envíen en formato JSON, serializar y deserializar los mismos al enviar/recibir.

**Resolución:**

- Para resolver este ejercicio modificamos la clase *Nodo.java* introduciendole el uso de JSON para estructurar los mensajes intercambiados entre los nodos, facilitando el procesamiento de datos.
- En lugar de enviar y recibir texto plano, los mensajes ahora se envían como objetos JSON, utilizando la biblioteca `org.json.JSONObject`.

**Paso 1:** Clonar el tag del HIT 5 de nuestro repositorio git.

```git
git clone --branch hit.5 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT5
```

**Paso 2:** Nos situamos dentro de la carpeta para poder ejecutar el contenedor docker.

```git
cd .\HIT5\tp1
```

**Paso 3:** Levantamos el contenedor.

```terminal
docker-compose up --build
```

Una vez terminada la prueba poner `docker-compose down` en la terminal para dar de baja el contenedor.

## *Hit #6*

**Consigna:** Cree un programa D, el cual, actuará como un “Registro de contactos”. Para ello, en un array en ram, inicialmente vacío, este nodo D llevará un registro de los programas C que estén en ejecución.
Modifique el programa C de manera tal que reciba por parámetros únicamente la ip y el puerto del programa D. C debe iniciar la escucha en un puerto aleatorio y debe comunicarse con D para informarle su ip y su puerto aleatorio donde está escuchando. D le debe responder con las ips y puertos de los otros nodos C que estén corriendo, haga que C se conecte a cada uno de ellos y envíe el saludo.
Es decir, el objetivo de este HIT es incorporar un nuevo tipo de nodo (D) que actúe como registro de contactos para que al iniciar cada nodo C no tenga que indicar las ips de sus pares. Esto debe funcionar con múltiples instancias de C, no solo con 2.

**Resolución:**

- Para resolver este ejercicio creamos una nueva clase llamada *RegistroContactos.java* la cual implementa un servidor que actúa como un nodo de registro central, permitiendo que otros nodos se registren enviando su dirección IP y puerto mediante mensajes en formato JSON. Al recibir una conexión, el servidor procesa la solicitud, almacena la información del nodo en un mapa de nodos registrados, y responde con un mensaje de confirmación en JSON indicando que el registro fue exitoso.
- En la clase *Nodo.java*, el método `registerInD()` establece una conexión con el nodo D enviando un mensaje en formato JSON que contiene su dirección IP y puerto local para registrarse. Luego, `startListening()` inicia un servidor en el puerto local del nodo C, permitiéndole aceptar conexiones entrantes y responder a los mensajes recibidos.

**Paso 1:** Clonar el tag del HIT 6 de nuestro repositorio git.

```git
git clone --branch hit.6 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT6
```

**Paso 2:** Nos situamos dentro de la carpeta para poder ejecutar el contenedor docker.

```git
cd .\HIT6\tp1
```

**Paso 3:** Levantamos el contenedor.

```terminal
docker-compose up --build
```

Para hacer una prueba podríamos utilizar `docker exec nodo_c1 bash -c "echo 'Hola desde C1' | nc nodo_c2 9001"` el cual se encargara de enviar un mensaje desde un nodo hacia el otro. Además podríamos agregar más nodos de la siguiente manera:

```Dockerfile
  nodo_cN: # N refiriendose al número de nodo
    build: .
    container_name: nodo_cN
    environment:
      LISTEN_PORT: "nro_puerto" # A partir de 9000
      NODE_D_HOST: "nodo_d"
      NODE_D_PORT: "5000"
    command: ["java", "-cp", "/app/app.jar", "unlu.Nodo", "nro_puerto", "nodo_d", "5000"]
    depends_on:
      - nodo_d
    networks:
      - nodos_net
```

Una vez terminada la prueba poner `docker-compose down` en la terminal para dar de baja el contenedor.

## *Hit #7*

**Consigna:** Modifique el programa C y D, de manera tal de implementar un “sistema de inscripciones”, esto es, se define una ventana de tiempo fija de 1 MIN, coordinada por D, y los nodos C deben registrarse para participar de esa ventana, cuando un nodo C se registra a las 11:28:34 en D, el registro se hace efectivo para la próxima ventana de tiempo que corresponde a las 11:29. Cuando se alcanza las 11:29:00 el nodo D cierra las inscripciones y todo nodo C que se registre será anotado para la ventana de las 11:30, los nodos C que consulten las inscripciones activas solo pueden ver las inscripciones de la ventana actual, es decir, los nodos C no saben a priori cuales son sus pares para la próxima ventana de tiempo, solo saben los que están activos actualmente. Recuerde almacenar las inscripciones en un archivo de texto con formato JSON. Esto facilitará el seguimiento ordenado de las ejecuciones y asegurará la verificación de los resultados esperados.

**Resolución:**

- Para la resolución de este ejercicio modificamos la clase RegistroContactos.java y le permitimos que cuando un nodo se registra, la clase almacene la información del nodo (dirección IP, puerto y hora de registro) en un archivo JSON, junto con la ventana de tiempo calculada en la que el nodo será activo. La ventana de tiempo se actualiza periódicamente, y solo se conservan las inscripciones futuras en el archivo. Además, el sistema puede devolver las inscripciones activas para las ventanas de tiempo actuales mediante una consulta.
- Ademas modificamos la clase nodo para obtener información sobre las inscripciones activas. La clase envía una solicitud de inscripción con su propia IP, puerto y hora de registro al Nodo D, y también permite consultar las inscripciones activas a través de un socket TCP, recibiendo las respuestas del servidor.

**Paso 1:** Clonar el tag del HIT 7 de nuestro repositorio git.

```git
git clone --branch hit.7 --depth 1 https://github.com/martinlopez05/SD-2025-Trabajos-Practicos HIT7
```

**Paso 2:** Nos situamos dentro de la carpeta para poder ejecutar el contenedor docker.

```git
cd .\HIT7\tp1
```

**Paso 3:** Levantamos el contenedor.

```terminal
docker-compose up --build
```

Para hacer una prueba podríamos utilizar `docker exec nodo_d cat inscripciones.json` que nos permitirá ver las inscripciones activas de la ventana actual. También podremos ir levantando los nodos a tráves del comando `docker start nodo_cN` (donde N es el número de nodo) y podremos ver como en la terminal muestra cuales son las inscripciones activas.

![alt text](../image.png)
