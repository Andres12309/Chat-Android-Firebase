# Chat-Android-Firebase-With-GoogleAuth

_Chat con Android Studio con Java, usando los servicios de almacenamiento de firebase_

## Introduccion 🚀

_Este es un proyecto de chat privado con correo usando Google Autentication, ademas posee un envio de imagenes que estaran almacenados en firebase_


**También puedes mirar el video del funcionamiento de este proyecto en [youtube]().**

### Instrucciones 📋

_Puedes clonarte el repositorio o hacer un fork_

```
git clone https://github.com/Andres12309/Chat-Android-Firebase.git
```

### Instalación 🔧

_Una véz clonado puedes abrirlo con Android Studio, este instalara todas las dependencias automaticamente_

## Explicació del proyecto🔩

_El proyecto tiene servicios de autenticacion con Google, ademas de esto tiene el servicio de carga de archivos alamacenados en firebase. El proyecto esta dividido en servicios de carga de archivos desde firebase y servicios para mostrar estos archivos en la aplicacion, que seran visibles para los usuarios_

### ACTIVITIES 📋

_Este tendra todas las funciones principales que usaran los datos obtenidos de firebase para mostrar al usuario, ademas tambien sera el medio de gestionar los datos que los usuarios desean enviar ya sea por chat o historias que deseen compartir._

### PROVIDERS 📋

_Este tendra los **metodos** que seran ejecutados por cada adaptador de la plicacion, aqui se tiene la interaccion directa on firebase ya sea para envio de datos o obtencion de los mismos. Los providers obtienen la información de firebase y la distribuye dependiendo las clase que la nececite, en este caso la de Chat para sabe que usuarios tendran un chat creado, la de mensajes que sera utilizado dentro de metodos de Chat que contendra mensajes y envio de imagenes, etc._

### ADAPTERS 📋

_Este tendra las clases que gestionaran los datos obtenidos por los provaiders para enviar a las distintas actividadeso fragementos de actividades que las soliciten, ademas cada uno tiene su **layout** para obtener los datos y enviar las actividades ya sea en arreglos de vistas para que sean listadas y mostrar por ejemplo los mensajes, esto claro en sus respectivas vistas **layout**, ademas estos podran enviar informacion a los provaider para que sean almacenados en la base de datos_

### MODELS 📋

_Este tendra los diferentes objetos que la aplicacion usara que serian: **CHAT**, **MESSAGE**, **STORY**, **USER**. Cada uno de estos seran los objetos que se podra almacenar en firebase tal cual esten definidos._

### FRAGMENTS 📋

_Estos tendran las responsabilidades de mostrar la informacion en una misma ventana sin necesida de abrir otra Actividad usado para mostrar en diferentes **tabLayout**, en esta aplicacion serian los fragmentos de **CHAT**, **FRIENDS**, **PHOTO**,**STORIES**._

### UTILS 📋

_Este tendra clases que pueden ser usado en cualquier proyecto para comprimir una imagen y asi hacer que firebase no se sobrecargue de archivos de gran tamaño, ademas para obtener el tiempo del equipo y tranformar un timestamp en texto visible para el usuario normal con hh:mm_

### RES/LAYOUT 📋

_Este tendra todos los diseños .XML de todas las vistas que tendra la aplicacion, ya sea fragmentos, cards y todas las actividades diseñadas con sus estilos que estaran en **VALUES** y iconos que estaran en **DRAWABLE**._

**También puedes mirar el video de la explicacion de este proyecto en [youtube]().**

## Ejecución del proyecto🔩

_Compila el proyecto_

```
En un emulador o dispositivo fisico
```

## APK ⚙️

_En el proyecto se encuantra un archivo IAChat.apk, puedes usarla en cualquier dispositivo android o a su vez usar un emulador_

**Puedes encontrar la **APP** en la tienda **[Uptodown]**() 📢**

### Build APK 🔩

_Puedes realizar un build directamente del proyecto_

```
Android Studio
```

## Autores ✒️

_Este proyecto fue realizado_

* **Andrés Proaño** - *Proyecto* - [Andres](https://github.com/Andres12309)
* **Cristian Guamba** - *Proyecto* - [Cristian](https://github.com/Cristiangpbf)

## Gracias 🎁

* Revisa documentacion de [Android](https://developer.android.com/studio/intro) 📢
* Gracias por elegir mi proyecto 🤓.
