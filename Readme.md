# **Prueba Wembley Android**

Proyecto desarrollado con:

>Kotlin 1.5.31.
Android Studio Arctic Fox | 2020.3.1 Patch 2
Gradle 7.0.2.
MinSdkVersion: 21.
TargetSdkVersion: 31

# **PREVIEW**
Este proyecto es una prueba técnica para Wembley.
En la app se muestran dos tabs. Una para mostrar las peliculas más populares de una api y otra para mostrar al usuario la pelis que ha guardado en favoritos.

# **ARQUITECTURA**
El proyecto esta desarrollado en MVVM + Kotlin.
Para la inyección de dependencias se usa Hilt (https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419) y las inyecciones de los diferentes modulos se declaran en el fichero NetworkModules.

La aplicación se inicializa en la clase WembleyApplication y cuenta con una actividad (MainActivity) y dos fragmentos (MoviesFragment y FavoritesFragment)
Para globalizar el codigo se han creado dos clases bases de las que heredan las actividades y los fragmentos, en ellas se inicializa el viewbinding (la vistas) y los viewmodels.
Clases base: BaseBindingActivity y BaseBindingFragment

Para las llamadas al backend se usa Retrofit, declarado como un singleton en el fichero _NetworkModule_.
Las respuestas del api rest se parsean en una sealed class, _RequestResponse_, para de forma generalizada la respuesta en las corrutinas de los caso de uso (usecases) encargados de comunicar los repositorios con los viewmodels.
Para controlar que todas las llamadas van bien y tener control sobre los errores se ha creeado la clase NetworkHelper que es la encargada de manejar las respuestas de retrofit.

MainActivity -> Se inicializa un viewpager2 para poder alternar entre los diferentes tabs de la aplicación.
MoviesFragment -> Se pinta el listado de peliculas populares, esto se hace con un recyclerview en forma de GridLayout de 2 columnas.
FavoritesFragment -> Se pinta el listado de peliculas favoritas.

Para pedir las peliculas populares se hace una llamada al api y se le devuelven los datos al mainviewmodel. Mediante livedata este pasa el listado al fragmento.
Para guardar las peliculas favoritas se ha usado room. En la interfaz MovieDao se declaran todas las querys para acceder a los datos (MovieItem)

# **STORE**
La app cuenta con dos build variants, debug y release. Release esta configurada para que se pueda crear una apk firmada y subirla al store.
Todas las properties necesarias se encuentran en el fichero keystore.properties