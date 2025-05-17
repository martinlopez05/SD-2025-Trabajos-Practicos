# Descripción de los ejercicios del Trabajo Práctico 4

- > Link al pdf del [informe](https://github.com/martinlopez05/SD-2025-Trabajos-Practicos/blob/main/tp4/informe-completo.md)

## *Hit #1*

**Consigna:** Visite *<https://en.wikipedia.org/wiki/Shader#Pixel_shaders>* en su versión en inglés y lea atentamente los apartados. Comience a elaborar un informe donde documente someramente los tipos de shaders. En particular nos enfocaremos en los Píxel Shaders que operan en 2D.

* * *

### Informe: Introducción a los Shaders 2D y Pixel Shaders

#### Introducción

Los shaders son programas fundamentales que controlan cómo se representan visualmente los objetos en pantalla. Existen distintos tipos de shaders, cada uno con funciones específicas sobre imágenes o geometrías. En este pequeño informe vamos a describir el funcionamiento general de los shaders 2D y de los pixel shaders.

#### Shaders 2D

Este tipo de shaders actúa directamente sobre imágenes digitales o lo que conocemos como texturas, y se utilizan principalmente para modificar atributos de los píxeles, como el color, la transparencia o la iluminación. También pueden influir en cómo se ve la geometría 3D cuando se aplican como efecto sobre texturas.

#### Pixel Shaders

Los pixel shaders o fragment shaders son un tipo de programa que se va a ejecutar para cada fragmento de imagen que se genera en el renderizado. Principalmente se encargan de calcular el color final y otros atributos de cada píxel de la imagen.

Estos pueden realizar tareas tales como:

- Aplicar color de manera uniforme.
- Aplicar efectos de:
  - Iluminación y sombreado.
  - Mapping y reflejos especulares.
  - Translucidez y desenfoque.
  - Detección y realce de bordes.

Los pixel shaders pueden ser utilizar la información como entrada de pantalla para poder aplicarle efectos visuales posteriores, lo que se conoce como *posprocesamiento*. A diferencia de otros, pueden aplicarse en imagenes estáticas o secuencias de video si necesidad de utilizar 3D.

***Comentario adicional sobre los Shaders 3D*:** operan directamente sobre modelos tridimensionales. Entre ellos se destacan los vertex shaders, geometry shaders y tessellation shaders, que permiten transformar vértices, generar nueva geometría y aumentar el nivel de detalle de los modelos.

* * *

**Consigna:** Corto y conciso. Visite *<https://medium.com/trabe/a-brief-introduction-to-webgl-5b584db3d6d6>* y agregue a su informe información sobre el pipeline de renderizado. En particular busque relacionarlo con el artículo anterior.

* * *

#### Pipeline de Renderizado

El pipeline de renderizado es un conjunto de pasos que sigue la GPU para convertir datos geométricos en píxeles visuales. Este pipeline se divide en varias etapas, dos de estas son programables y permiten definir cómo se procesan los vértices y se colorean los fragmentos.

El pipeline sigue una serie de pasos donde cada etapa procesa información gráfica:

1. **Vertex Shader:** transforma los vértices de los objetos aplicando rotaciones, traslaciones y escalados. Produce datos listos para formar primitivas (como triángulos).
2. **Rasterización:** convierte estas primitivas en fragmentos, que son las unidades que determinarán qué píxeles se verán afectados.
3. **Fragment Shader (Pixel Shader):** toma cada fragmento y determina su color, textura, transparencia u otros efectos visuales. Aquí es donde actúan los shaders 2D.

Los shaders 2D, en especial los pixel shaders, tienen un rol clave en la etapa final del pipeline (fragment shader), ya qué, actúan directamente sobre cada fragmento de imagen, manipulando los atributos visuales de los píxeles.

El pipeline de renderizado y los shaders trabajan en conjunto para generar gráficos complejos de forma eficiente. Mientras el pipeline define las etapas del procesamiento gráfico, los shaders permiten un control detallado sobre la apariencia final de cada píxel.

* * *

**Consigna:** Corto y conciso.
Divida los 6 pasos del pipeline en aquellos que corresponden al procesamiento 3D, los que corresponden al 2D.
Visite *<https://en.wikipedia.org/wiki/Video_post-processing>* y agregue a su informe conceptos básicos de post-processing, ¿en que etapa del pipeline se ejecutan?

* * *

#### Etapas del Pipeline de Renderizado

El pipeline de renderizado en WebGL consta de las siguientes etapas:

**Procesamiento 3D**:

1. **Vertex Shader:** Transforma las coordenadas de los vértices aplicando operaciones como traslación, rotación y escalado.
2. **Primitive Assembly:** Agrupa los vértices transformados en primitivas geométricas.
3. **Rasterización:** Convierte las primitivas en fragmentos, determinando qué píxeles serán afectados en la imagen final.

**Procesamiento 2D**:

1. **Fragment Shader (Pixel Shader):** Calcula el color y otros atributos de cada fragmento, aplicando texturas, iluminación y otros efectos visuales.
2. **Pruebas y Mezcla (Tests & Blending):** Determina la visibilidad de los fragmentos mediante pruebas de profundidad y mezcla los colores con los del framebuffer existente.
3. **Escritura en Framebuffer:** Almacena el resultado final en el framebuffer para su visualización en pantalla.

#### Posprocesamiento

El **posprocesamiento** son efectos aplicados a la imagen final después de completar el pipeline de renderizado. Estos efectos se ejecutan en la etapa de Fragment Shader o en etapas posteriores, utilizando shaders adicionales que operan sobre el framebuffer completo.
Permiten mejorar la calidad visual y estilizar la imagen final, siendo ampliamente utilizados en videojuegos y aplicaciones gráficas.

* * *

**Consigna:** Corto y conciso.

Diríjase a la web <https://www.shadertoy.com> la cual nos permite programar de forma interactiva shaders gráficos que corren en GPU gracias al uso de WebGL. Toque en la opción “Nuevo” arriba a la derecha, amplíe las “Entradas del shader”, agregue a su informe un listado de las entradas posibles indicando su tipo, nombre y una descripción breve de qué representa cada una.

Diríjase a <https://www.shadertoy.com/howto> y agregue a su informe un listado de las salidas posibles de los Pixel Shaders así como su tipo y una breve descripción de qué representa cada una.

Cuando crea un nuevo shadertoy el código de ejemplo que le sugiere la web es el siguiente:

```GLSL
void mainImage( out vec4 fragColor, in vec2 fragCoord ) 
{ 
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = fragCoord/iResolution.xy;
        
    // Time varying pixel color 
    vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));

    // Output to screen 
    fragColor = vec4(col,1.0); 
}
```

Con apoyo de internet o lo que considere, explique en profundidad cada parte de este shader “hello world”. Debe explicar como mínimo:

- Que representa uv.
- Porque es necesario trabajar en uv y no en xy.
- Cómo se logra que el resultado sea una animación siendo que las entradas son estáticas.
- Cómo es posible que col sea de tipo vec3 siendo que esta igualado a una operacion aritmetica a priori entre flotantes.
- Cuales son los constructores posibles para vec4, que representan los componentes de fragColor, uv se presenta como vec2 pero se utiliza su propiedad xyx, ¿que es eso? ¿Qué otras propiedades tiene vec2? ¿y vec3? ¿y vec4?

* * *

#### Entradas al Shader

Descripción breve de cada una de las variables de entrada que puede recibir un shader:

- **`iResolution (vec3)`**  
  Resolución del viewport en píxeles.  
  - `x`: ancho  
  - `y`: alto  
  - `z`: puede usarse para proporción u otras referencias 3D.

- **`iTime (float)`**  
  Tiempo en segundos desde que el shader comenzó a ejecutarse.

- **`iTimeDelta (float)`**  
  Tiempo que tardó en renderizar el último frame. Útil para animaciones.

- **`iFrameRate (float)`**  
  Velocidad de renderizado en cuadros por segundo (FPS).

- **`iFrame (int)`**  
  Número de cuadro actual (empieza desde 0).

- **`iChannelTime[4] (float[4])`**  
  Tiempo de reproducción (en segundos) de cada canal de entrada (`iChannel0` a `iChannel3`).

- **`iChannelResolution[4] (vec3[4])`**  
  Resolución de las texturas de entrada por canal.  
  - Cada `vec3` contiene `x` (ancho), `y` (alto), `z` (profundidad si aplica).

- **`iMouse (vec4)`**  
  Información del mouse:  
  - `xy`: posición actual (si se mantiene presionado el botón izquierdo)  
  - `zw`: posición donde se hizo clic por última vez.

- **`iChannel0..3 (sampler2D / samplerCube)`**  
  Canales de entrada con imágenes o texturas que el shader puede usar.

- **`iDate (vec4)`**  
  Fecha y hora del sistema:  
  - `x`: año  
  - `y`: mes  
  - `z`: día  
  - `w`: segundos desde medianoche.

- **`iSampleRate (float)`**  
  Frecuencia de muestreo de audio, típicamente 44100 Hz. Se usa en shaders que reaccionan al sonido.

#### Salidas de los Pixel Shaders

En el caso de los shaders de imagen, esta salida se genera mediante la función `mainImage(out vec4 fragColor, in vec2 fragCoord)`. La variable `fragColor` representa el color resultante como un vector de cuatro componentes (rojo, verde, azul y alfa). Aunque la componente alfa puede ser ignorada por algunas aplicaciones, en general se utiliza para controlar la transparencia. La variable `fragCoord` indica las coordenadas del píxel que se está procesando, en unidades de píxeles, y es útil para determinar la posición espacial de cada fragmento en pantalla.

En el caso de los shaders de sonido, la función principal es `mainSound(float time)` y devuelve un vector de dos componentes (`vec2`) que representan la amplitud de la onda sonora para los canales izquierdo y derecho. Estos valores se generan a partir de la variable `time`, que indica el instante temporal del sample de audio. De esta manera, los shaders pueden producir sonido en estéreo, directamente desde código GLSL, utilizando el mismo principio de cálculo por fragmento pero aplicado al dominio temporal en lugar del espacial.

Por último, los shaders de realidad virtual (VR) extienden el modelo tradicional con la función `mainVR(out vec4 fragColor, in vec2 fragCoord, in vec3 fragRayOri, in vec3 fragRayDir)`. Aquí, además de la salida de color `fragColor` y las coordenadas del fragmento `fragCoord`, se reciben dos vectores que describen el rayo visual en el espacio tridimensional: `fragRayOri` es el origen del rayo y `fragRayDir` es su dirección. Esta información permite generar imágenes estereoscópicas y simular una cámara en movimiento dentro de un entorno virtual.

#### Explicación detallada del shader "Hello World"

`uv` es un vector de dos componentes (`vec2`) que representa las coordenadas del píxel actual normalizadas respecto a la resolución de pantalla (`iResolution.xy`). Sus valores van de 0 a 1. Esto permite que los efectos visuales no dependan de la resolución real del dispositivo.

Trabajar con coordenadas normalizadas (`uv`) hace que los efectos se vean igual en diferentes resoluciones. Si se trabajara directamente en píxeles (`xy`), el resultado variaría según el tamaño de pantalla. Usar `uv` garantiza escalabilidad y consistencia visual.

La clave está en la variable uniforme `iTime`, que contiene el tiempo transcurrido desde que comenzó el shader. Al incluir `iTime` en cálculos como `cos(iTime + ...)`, los colores cambian con el tiempo, generando así una animación continua incluso si las coordenadas (`fragCoord`) no cambian.

GLSL permite operar entre escalares y vectores: los escalares se aplican componente por componente. La función `cos` recibe un `vec3` (resultado de `iTime + uv.xyx + vec3(0,2,4)`) y devuelve otro `vec3` con el coseno aplicado a cada componente. Luego, se suma y multiplica por escalares (`0.5`), generando un `vec3` válido.

`vec4` puede construirse de varias formas:

- Con 4 valores escalares: `vec4(1.0, 0.0, 0.0, 1.0)`
- Con un solo valor escalar: `vec4(1.0)` (se copia a todos los componentes)
- Combinando vectores: `vec4(vec3(1.0, 0.0, 0.0), 1.0)`
- Con dos `vec2`: `vec4(vec2(1.0, 0.0), vec2(0.0, 1.0))`

En este shader se usa: `vec4(col, 1.0)` donde `col` es `vec3` (RGB) y `1.0` es el canal alfa (opacidad).

`uv.xyx` es una técnica llamada swizzling: permite reordenar y duplicar componentes de un vector. En este caso, `uv.xyx` genera un `vec3` usando la componente `x`, luego `y`, y otra vez `x`.

Otras combinaciones posibles incluyen:

- Para vec2: `.x, .y / .r, .g / .s, .t`
- Para vec3: `.x, .y, .z / .r, .g, .b / .s, .t, .p`
- Para vec4: `.x, .y, .z, .w / .r, .g, .b, .a / .s, .t, .p, .q`
