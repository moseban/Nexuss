package com.nexus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/*
    * Punto de entrada absoluto de la app.
    * la anotacion desencadena la generacion de codigo del framwork dagger hilt
    * a nivel de componentes
*/
@HiltAndroidApp
class NexusApplication : Application()