package com.example.miagendacsv

//Agenda
import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Agenda(private val context: Context) {
    private val archivoCSV = "contactos.csv"
    var contactos = mutableListOf<Contacto>()

    init {
        cargarContactos()
    }

    fun agregarContacto(contacto: Contacto) {
        contactos.add(contacto)
        guardarContactos()
    }

    fun borrarContacto(nombre: String): Boolean {
        val eliminado = contactos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
        if (eliminado) guardarContactos()
        return eliminado
    }

    fun editarContacto(nombreAntiguo: String, nuevoContacto: Contacto): Boolean {
        val index = contactos.indexOfFirst { it.nombre.equals(nombreAntiguo, ignoreCase = true) }
        if (index >= 0) {
            contactos[index] = nuevoContacto
            guardarContactos()
            return true
        }
        return false
    }

    private fun guardarContactos() {
        try {
            val output = OutputStreamWriter(context.openFileOutput(archivoCSV, Context.MODE_PRIVATE))
            for (contacto in contactos) {
                output.write("${contacto.nombre},${contacto.telefono}\n")
            }
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cargarContactos() {
        contactos.clear()
        try {
            val input = BufferedReader(InputStreamReader(context.openFileInput(archivoCSV)))
            var line: String?
            while (input.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                if (parts.size == 2) {
                    val nombre = parts[0]
                    val telefono = parts[1].toIntOrNull()
                    if (telefono != null) {
                        contactos.add(Contacto(nombre, telefono))
                    }
                }
            }
            input.close()
        } catch (e: Exception) {
            // Si el archivo no existe aún, no hacer nada
        }
    }
}