package com.example.proyecto_individual_naiarabenito.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.proyecto_individual_naiarabenito.ui.cesta.Orden;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Valiables auxiliares
    private static final int DATABASE_VERSION = 1;  // Versión de la BBDD
    //Nombre de la BBDD y de la tabla
    private static final String DATABASE_NOMBRE = "daleUnMordisco.db";  // Nombre de la BBDD
    private static final String TABLE_USUARIOS = "t_usuarios";  // Nombre de la tabla que almacena los usuarios registrados
    private static final String TABLE_ORDENES = "t_orden";  // Nombre de la tabla que almacena los usuarios registrados

    // Constructor: Este helper crea la BBDD
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    // Método para crear las tablas de la base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "email TEXT PRIMARY KEY," +
                "password TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ORDENES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProd TEXT NOT NULL," +
                "precioProd NUMERIC NOT NULL," +
                "imagenProd NUMERIC NOT NULL," +
                "cantidadProd NUMERIC NOT NULL," +
                "emailUsuario TEXT NOT NULL," +
                "FOREIGN KEY (emailUsuario) REFERENCES "+ TABLE_USUARIOS + "(email))");
    }

    // Este método se ejecuta cuando se cambia la versión de la BBDD (cambiar a estructura: añadir, quitar o editar una tabla...)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDENES);
        onCreate(sqLiteDatabase);
    }

    public String[] verificarUsuarioLogin(String email, String password){

        try{
            // Obtener la base de datos editable para realizar cambios
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el usuario y constraseña se encuentra registrado en la BBDD
            Cursor fila = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE email='" + email + "' AND password='" + password + "'", null);

            // Comprobar si ha encontrado al usuario
            if(fila.moveToFirst()) {     // Si existe --> Devolver la lista con los datos
                String[] aux = new String[4];
                aux[0] = fila.getString(0);
                aux[1]  = fila.getString(1);
                aux[2] = fila.getString(2);
                aux[3] = fila.getString(3);

                Log.d("DATOS",aux[0] + " " + aux[1] + " " + aux[2] + " " + aux[3]);

                db.close();
                return aux;
            }
            else{
                db.close();
                return null;
            }
        } catch (Exception e){     // En caso de excepción
            Log.e("ERROR", e.toString()+"");
            return null;
        }
    }

    public String registrarUsuario(String pNombre, String pApellido, String pEmail, String pPassword){

        try{
            // Obtener una BBDD editable
            //DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el usuario se encuentra registrado en la BBDD
            Cursor fila = db.rawQuery("SELECT email FROM " + TABLE_USUARIOS + " WHERE email='" + pEmail + "'", null);

            // Comprobar si ha encontrado al usuario
            if(fila.moveToFirst()){     // Si existe --> Error
                return "Este usuario ya existe";
                //Toast.makeText(this,"Este usuario ya existe", Toast.LENGTH_LONG).show();
            }
            else{     // Si no existe --> Crea el nuevo usuario

                // Crear un ContentValues que guarde la información de la tabla
                ContentValues datosUsuario = new ContentValues();
                datosUsuario.put("nombre", pNombre);
                datosUsuario.put("apellido", pApellido);
                datosUsuario.put("email", pEmail);
                datosUsuario.put("password", pPassword);

                // Añadir en la tabla de usuarios los datos del nuevo usuario
                db.insert(TABLE_USUARIOS,null, datosUsuario);
                db.close();

                // Mensaje de información
                return "Usuario "+ pNombre + " ingresado con exito";
                //Toast.makeText(this,"Usuario "+ pNombre + " ingresado con exito", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){     // En caso de excepción
            return "ERROR  BASE DE DATOS";
            //Toast.makeText(this,"ERROR  BASE DE DATOS", Toast.LENGTH_LONG).show();
        }
    }

    public boolean anadirOrden(String producto, double precio, int imagen, int cantidad, String email){

        try{
            // Obtener una BBDD editable
            //DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el usuario se encuentra registrado en la BBDD
            Cursor fila = db.rawQuery("SELECT cantidadProd FROM " + TABLE_ORDENES + " WHERE nombreProd='" + producto + "' AND emailUsuario='" + email + "'", null);

            // Comprobar si ha encontrado el producto
            if(fila.moveToFirst()){     // Si existe --> Actualizar cantidad
                int cantidadAux = Integer.parseInt(fila.getString(0)) + cantidad;
                Log.d("CANTIDAD NUEVA: ",String.valueOf(cantidadAux));

                ContentValues datosOrden = new ContentValues();
                datosOrden.put("cantidadProd", cantidadAux);
                db.update(TABLE_ORDENES, datosOrden,"nombreProd='" + producto + "' AND emailUsuario='" + email + "'",null);

                db.close();
                return true;
            }
            else {     // Si no existe --> Crea la nueva orden

                ContentValues datosOrden = new ContentValues();
                datosOrden.put("nombreProd", producto);
                datosOrden.put("precioProd", precio);
                datosOrden.put("imagenProd", imagen);
                datosOrden.put("cantidadProd", cantidad);
                datosOrden.put("emailUsuario", email);

                // Añadir en la tabla de ordenes los datos de la nueva orden
                long id = db.insert(TABLE_ORDENES, null, datosOrden);
                db.close();

                if (id <= 0) {
                    System.out.println("EXCEPCIÓN: ID < 0");
                    return false;
                } else {
                    return true;
                }
            }

        } catch (Exception e){     // En caso de excepción
            System.out.println("EXCEPCIÓN: " + e.toString());
            return false;
        }
    }

    public ArrayList<Orden> getOrdenes(String email) {
        ArrayList<Orden> lista_ordenes = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            // Obtener todas las ordenes del usuario
            Cursor fila = db.rawQuery("SELECT id,nombreProd,precioProd,imagenProd,cantidadProd,emailUsuario FROM " + TABLE_ORDENES + " WHERE emailUsuario='" + email + "'", null);

            // Comprobar si ha encontrado al usuario
            if(fila.moveToFirst()) {     // Si existe --> Devolver la lista con los datos
                do {
                    Orden orden = new Orden();
                    orden.setId(fila.getInt(0));
                    orden.setNombreProd(fila.getString(1));
                    orden.setPrecioProd(fila.getDouble(2));
                    orden.setImagenProd(fila.getInt(3));
                    orden.setCantidadProd(fila.getInt(4));
                    orden.setEmailUsuario(fila.getString(5));
                    lista_ordenes.add(orden);
                } while (fila.moveToNext());
            }
            fila.close();
            db.close();
            return lista_ordenes;

        } catch (Exception e) {     // En caso de excepción
            System.out.println("EXCEPCIÓN: " + e.toString());
            return null;
        }
    }

    public void actualizarOrden(String producto, int cantidad, String email){

        try{
            // Obtener una BBDD editable
            //DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el usuario se encuentra registrado en la BBDD
            Cursor fila = db.rawQuery("SELECT cantidadProd FROM " + TABLE_ORDENES + " WHERE nombreProd='" + producto + "' AND emailUsuario='" + email + "'", null);

            // Comprobar si ha encontrado el producto
            if(fila.moveToFirst()){     // Si existe --> Actualizar cantidad
                int cantidadAux = Integer.parseInt(fila.getString(0)) + cantidad;
                Log.d("CANTIDAD NUEVA: ",String.valueOf(cantidadAux));

                ContentValues datosOrden = new ContentValues();
                datosOrden.put("cantidadProd", cantidadAux);
                db.update(TABLE_ORDENES, datosOrden,"nombreProd='" + producto + "' AND emailUsuario='" + email + "'",null);
                fila.close();
                db.close();
            }
        } catch (Exception e){     // En caso de excepción
            System.out.println("EXCEPCIÓN: " + e.toString());
        }
    }
    public void borrarOrden(String producto, String email){

        try{
            // Obtener una BBDD editable
            //DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("DELETE FROM " + TABLE_ORDENES +  " WHERE nombreProd='" + producto + "' AND emailUsuario='" + email + "'");
            db.close();

        } catch (Exception e){     // En caso de excepción
            System.out.println("EXCEPCIÓN: " + e.toString());
        }
    }

}
