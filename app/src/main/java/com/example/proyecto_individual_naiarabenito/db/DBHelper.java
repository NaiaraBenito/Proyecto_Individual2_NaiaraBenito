
// _____________________________________ UBICACIÓN DEL PAQUETE _____________________________________
package com.example.proyecto_individual_naiarabenito.db;

// ______________________________________ PAQUETES IMPORTADOS ______________________________________
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.proyecto_individual_naiarabenito.ui.cesta.Orden;
import java.util.ArrayList;


/* ######################################## CLASE DBHELPER #########################################
    *) Descripción:
        La función de esta clase es comunicar la aplicación con una BBDD local de SQLite.
        Los métodos que implementa se comportan como un puente realizando distintas peticiones
        como crear la BBDD ademáss de añadir, eliminar y actualizar los datos de las tablas.

    *) Tipo: Helper
*/
public class DBHelper extends SQLiteOpenHelper {

// ___________________________________________ Variables ___________________________________________
    private static final int DATABASE_VERSION = 1;  // Versión de la BBDD
    private static final String DATABASE_NOMBRE = "daleUnMordisco.db";  // Nombre de la BBDD

    // Nombre de la tabla que almacena los datos de los usuarios registrados
    private static final String TABLE_USUARIOS = "t_usuarios";

    // Nombre de la tabla que almacena los productos que tienen los usuarios en la cesta
    private static final String TABLE_ORDENES = "t_orden";


// __________________________________________ Constructor __________________________________________
    public DBHelper(@Nullable Context context) {
        // Crea la BBDD (daleUnMordisco.db) sobre la que trabajaremos en caso de que no exista
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

// ____________________________________________ Métodos ____________________________________________

/*  Método onCreate:
    ----------------
        *) Parámetos (Input):
                1) (SQLiteDatabase) sqLiteDatabase: BBDD local de la aplicación sobre la que
                   trabajaremos.
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta la primera vez que se crea la BBDD.
                Crea las tablas que almacenan los datos de los usuarios registrados (t_usuarios)
                y la información de los productos que tienen añadidos en la cesta (t_orden).
*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear la tabla de usuarios
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "email TEXT PRIMARY KEY," +
                "password TEXT NOT NULL)");

        // Crear la tabla de órdenes (productos de la cesta)
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ORDENES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProd TEXT NOT NULL," +
                "precioProd NUMERIC NOT NULL," +
                "imagenProd NUMERIC NOT NULL," +
                "cantidadProd NUMERIC NOT NULL," +
                "emailUsuario TEXT NOT NULL," +
                "FOREIGN KEY (emailUsuario) REFERENCES "+ TABLE_USUARIOS + "(email))");
    }

// -------------------------------------------------------------------------------------------------

/*  Método onUpgrade:
    -----------------
        *) Parámetos (Input):
                1) (SQLiteDatabase) sqLiteDatabase: BBDD local de la aplicación sobre la que
                    trabajaremos.
                2) (int) oldVersion: Versión vieja de la BBDD
                3) (int) newVersion: Nueva versión de la BBDD
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta cuando es necesario actualizar la BBDD, como podría ser
                al añadir, quitar o editar la estructura de una tabla.
                Crea las tablas que almacenan los datos de los usuarios registrados (t_usuarios)
                y la información de los productos que tienen añadidos en la cesta (t_orden).
*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Borrar las tablas preexistentes
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDENES);

        // Volver a crearlas
        onCreate(sqLiteDatabase);
    }

// -------------------------------------------------------------------------------------------------

/*  Método verificarUsuarioLogin:
    -----------------------------
        *) Parámetos (Input):
                1) (String) pEmail: Email del usuario que intenta loguearse en la aplicación.
                2) (String) pPassword: Contraseña del usuario que intenta loguearse en la aplicación.
        *) Parámetro (Output):
                String[] datosUsuario: Lista con toda la información del usuario.
        *) Descripción:
                Éste método se ejecuta cuando el usuario intenta loguearse en la aplicación.
                Se comprueba que el usuario se encuentre registrado en la BBDD.
                    - Si está registrado: Devuelve una lista de Strings con toda la información del
                      usuario.
                    - Si no está registrado: Devuelve NULL.
*/
    public String[] verificarUsuarioLogin(String pEmail, String pPassword){
        try{
            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar si el usuario se encuentra registrado
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE email='" + pEmail + "' AND password='" + pPassword + "'", null);

            if(cursor.moveToFirst()) {    // Si existe: Devuelve una lista con los datos del usuario
                String[] datosUsuario = new String[4];
                datosUsuario[0] = cursor.getString(0);     // Nombre
                datosUsuario[1]  = cursor.getString(1);    // Apellido
                datosUsuario[2] = cursor.getString(2);     // Email
                datosUsuario[3] = cursor.getString(3);     // Contraseña

                // Cerrar la BBDD
                db.close();

                // Cerrar el cursor
                cursor.close();
                return datosUsuario;
            }
            else{                       // Si no existe: Devuelve NULL
                // Cerrar la BBDD
                db.close();

                // Cerrar el cursor
                cursor.close();
                return null;
            }
        } catch (Exception e){     // En caso de excepción con la BBDD
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
            return null;
        }
    }

// -------------------------------------------------------------------------------------------------

/*  Método registrarUsuario:
    -----------------------------
        *) Parámetos (Input):
                1) (String) pNombre: Nombre del usuario que intenta registrarse en la aplicación.
                2) (String) pApellido: Apellido del usuario que intenta registrarse.
                3) (String) pEmail: Email del usuario que intenta registrarse.
                4) (String) pPassword: Contraseña del usuario que intenta registrarse.
        *) Parámetro (Output):
                String msg: Texto informativo sobre el estado del proceso de registro.
        *) Descripción:
                Éste método se ejecuta cuando el usuario intenta registrarse en la aplicación.
                Se comprueba que el usuario no se encuentre registrado en la BBDD.
                    - Si está registrado: Devuelve "ERROR: Este usuario ya se encuentra registrado".
                    - Si no está registrado: Añade el nuevo usuario a la BBDD y devuelve "El usuario
                      [nombre] ha sido registrado con exito".
*/
    public String registrarUsuario(String pNombre, String pApellido, String pEmail, String pPassword){
        try{
            // Variable de retorno
            String msg;

            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar si el usuario se encuentra registrado
            Cursor cursor = db.rawQuery("SELECT email FROM " + TABLE_USUARIOS + " WHERE email='" + pEmail + "'", null);

            if(cursor.moveToFirst()){     // Si existe: Devuelve mensaje de error
                // Mensaje de error
                msg = "ERROR: Este usuario ya se encuentra registrado";
            }
            else{                         // Si no existe: Crea el nuevo usuario
                // Guardar la información introducida por del usuario en la BBDD (t_usuarios)
                ContentValues datosUsuario = new ContentValues();
                datosUsuario.put("nombre", pNombre);
                datosUsuario.put("apellido", pApellido);
                datosUsuario.put("email", pEmail);
                datosUsuario.put("password", pPassword);

                db.insert(TABLE_USUARIOS,null, datosUsuario);

                // Mensaje de información
                msg = "El usuario "+ pNombre + " ha sido registrado con exito";
            }
            // Cerrar la BBDD
            db.close();

            // Cerrar el cursor
            cursor.close();
            return msg;
        }
        catch (Exception e){        // En caso de excepción
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
            return "ERROR: Ha ocurrido un problema con la BBDD";
        }
    }

// -------------------------------------------------------------------------------------------------

/*  Método anadirOrden:
    -------------------
        *) Parámetos (Input):
                1) (String) pProducto: Nombre del producto que se desea añadir a la cesta.
                2) (double) pPrecio: Precio del producto que se desea añadir.
                3) (int) pImagen: Foto del producto que se desea añadir.
                4) (int) pCantidad: Número de unidades  que se desea añadir del producto.
                5) (String) pEmail: Email del usuario logueado.
        *) Parámetro (Output):
                Boolean anadido: Booleano que informa del estado del registro de la orden.
        *) Descripción:
                Éste método se ejecuta cuando el usuario intenta añadir algún producto al carrito.
                Se comprueba si el producto ya se encontraba en la cesta.
                    - Si está registrado: Actualiza la cantidad de dicho producto.
                    - Si no está registrado: Añade el nuevo pedido a la BBDD.
*/
    public boolean anadirOrden(String pProducto, double pPrecio, int pImagen, int pCantidad, String pEmail){
        try{
            // Variable de retorno
            boolean anadido;

            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar si el producto ya se encuentra en la cesta
            Cursor cursor = db.rawQuery("SELECT cantidadProd FROM " + TABLE_ORDENES + " WHERE nombreProd='" + pProducto + "' AND emailUsuario='" + pEmail + "'", null);

            if(cursor.moveToFirst()){        // Si existe: Actualizar la cantidad del pedido
                // Actualizar cantidad
                int cantidadAux = Integer.parseInt(cursor.getString(0)) + pCantidad;

                // Actualizar orden con la nueva cantidad
                ContentValues datosOrden = new ContentValues();
                datosOrden.put("cantidadProd", cantidadAux);

                db.update(TABLE_ORDENES, datosOrden,"nombreProd='" + pProducto + "' AND emailUsuario='" + pEmail + "'",null);

                // Cerrar la BBDD
                db.close();

                // Cerrar el cursor
                cursor.close();

                anadido = true;
            }
            else {                              // Si no existe: Crear el pedido
                // Añadir la orden del nuevo producto en la BBDD (t_ordenes)
                ContentValues datosOrden = new ContentValues();
                datosOrden.put("nombreProd", pProducto);
                datosOrden.put("precioProd", pPrecio);
                datosOrden.put("imagenProd", pImagen);
                datosOrden.put("cantidadProd", pCantidad);
                datosOrden.put("emailUsuario", pEmail);

                long id = db.insert(TABLE_ORDENES, null, datosOrden);

                // Cerrar la BBDD
                db.close();

                // Cerrar el cursor
                cursor.close();

                // Comprobar que el pedido se ha añadido correctamente
                if (id <= 0) {      // Si no se ha añadido: Escribir el error en el registro Log
                    Log.e("ERROR", "El pedido no se ha añadido, id < = 0");
                    anadido = false;
                } else {
                    anadido = true;
                }
            }
            return anadido;
        } catch (Exception e){     // En caso de excepción
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
            return false;
        }
    }

// -------------------------------------------------------------------------------------------------

/*  Método getOrdenes:
    ------------------
        *) Parámetos (Input):
                1) (String) pEmail: Email del usuario logueado.
        *) Parámetro (Output):
                ArrayList<Orden> lista_ordenes: Lista con todas la información del pedido del
                usuario
        *) Descripción:
                Éste método se ejecuta cuando el usuario accede a la vista del carrito.
                Se realiza una petición a la tabla que contiene los datos de todas las órdenes y
                sólo devuelve los pedidos del usuario actual.
*/
    public ArrayList<Orden> getOrdenes(String pEmail) {
        try {
            // Variable de retorno
            ArrayList<Orden> lista_ordenes = new ArrayList<>();

            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Obtener todas las ordenes del usuario
            Cursor cursor = db.rawQuery("SELECT id,nombreProd,precioProd,imagenProd,cantidadProd,emailUsuario FROM " + TABLE_ORDENES + " WHERE emailUsuario='" + pEmail + "'", null);

            // Comprobar si el usuario actual ha realizado algún pedido
            if(cursor.moveToFirst()) {     // Si tiene pedidos en el carrito: Devolver el listado
                do {        // Por cada pedido
                    Orden orden = new Orden();                  // Crear una Órden
                    orden.setId(cursor.getInt(0));                // Asignar id
                    orden.setNombreProd(cursor.getString(1));     // Asignar nombre
                    orden.setPrecioProd(cursor.getDouble(2));     // Asignar precio
                    orden.setImagenProd(cursor.getInt(3));        // Asignar foto
                    orden.setCantidadProd(cursor.getInt(4));      // Asignar cantidad
                    orden.setEmailUsuario(cursor.getString(5));   // Asignar email del usuario

                    lista_ordenes.add(orden);       // Añadir la nueva orden al listado
                } while (cursor.moveToNext());
            }

            // Cerrar la BBDD
            db.close();

            // Cerrar el cursor
            cursor.close();
            return lista_ordenes;

        } catch (Exception e) {     // En caso de excepción
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
            return null;
        }
    }

// -------------------------------------------------------------------------------------------------

/*  Método actualizarOrden:
    ------------------
        *) Parámetos (Input):
                1) (String) pProducto: Nombre del producto que se desea actualizar en la cesta.
                2) (int) pCantidad:
                    - Positiva: Unidades que se desea aumentar la cantidad del producto.
                    - Negativa: Unidades que se desea reducir la cantidad del producto.
                3) (String) pEmail: Email del usuario logueado.
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta cuando el usuario cambia el pedido desde a la vista del
                carrito.
                Se comprueba que el producto se encuentre registrado:
                    - Si está registrado: Actualiza la cantidad del producto
*/
    public void actualizarOrden(String pProducto, int pCantidad, String pEmail){
        try{
            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el producto ya se encuentre en la cesta
            Cursor cursor = db.rawQuery("SELECT cantidadProd FROM " + TABLE_ORDENES + " WHERE nombreProd='" + pProducto + "' AND emailUsuario='" + pEmail + "'", null);

            if(cursor.moveToFirst()){       // Si existe: Actualizar la cantidad del pedido
                int cantidadAux = Integer.parseInt(cursor.getString(0)) + pCantidad;
                ContentValues datosOrden = new ContentValues();
                datosOrden.put("cantidadProd", cantidadAux);

                db.update(TABLE_ORDENES, datosOrden,"nombreProd='" + pProducto + "' AND emailUsuario='" + pEmail + "'",null);

                // Cerrar la BBDD
                db.close();

                // Cerrar el cursor
                cursor.close();
            }
        } catch (Exception e){     // En caso de excepción
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
        }
    }

// -------------------------------------------------------------------------------------------------

/*  Método borrarOrden:
    ------------------
        *) Parámetos (Input):
                1) (String) pProducto: Nombre del producto que se desea eliminar de la cesta.
                2) (String) pEmail: Email del usuario logueado.
        *) Parámetro (Output):
                void
        *) Descripción:
                Éste método se ejecuta cuando el usuario elimina un producto del carrito.
                Se comprueba que el producto se encuentre registrado:
                    - Si está registrado: Borra el producto
*/
    public void borrarOrden(String pProducto, String pEmail){
        try{
            // Obtener la BBDD
            SQLiteDatabase db = this.getWritableDatabase();

            // Eliminar el producto del registro de órdenes del usuario (t_ordenes)
            db.execSQL("DELETE FROM " + TABLE_ORDENES +  " WHERE nombreProd='" + pProducto + "' AND emailUsuario='" + pEmail + "'");

            // Cerrar la BBDD
            db.close();

        } catch (Exception e){     // En caso de excepción
            // Escribir el error en el registro Log
            Log.e("ERROR BBDD", e.toString());
        }
    }
}
