package com.example.proyecto_individual_naiarabenito.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    // Valiables auxiliares
    private static final int DATABASE_VERSION = 1;  // Versión de la BBDD
    //Nombre de la BBDD y de la tabla
    private static final String DATABASE_NOMBRE = "daleUnMordisco.db";  // Nombre de la BBDD
    private static final String TABLE_USUARIOS = "t_usuarios";  // Nombre de la tabla que almacena los usuarios registrados

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
    }

    // Este método se ejecuta cuando se cambia la versión de la BBDD (cambiar a estructura: añadir, quitar o editar una tabla...)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIOS);
        onCreate(sqLiteDatabase);
    }

    public String[] verificarUsuarioLogin(String email, String password){

        try{
            // Obtener la base de datos editable para realizar cambios
            SQLiteDatabase db = this.getWritableDatabase();

            // Comprobar que el usuario y constraseña se encuentra registrado en la BBDD
            Cursor fila = db.rawQuery("SELECT * FROM t_usuarios WHERE email='" + email + "' AND password='" + password + "'", null);

            // Comprobar si ha encontrado al usuario
            if(fila.moveToFirst()) {     // Si existe --> Devolver la lista con los datos
                String[] aux = new String[4];
                aux[0] = fila.getString(0);
                aux[1]  = fila.getString(1);
                aux[2] = fila.getString(2);
                aux[3] = fila.getString(3);

                Log.d("DATOS",aux[0] + " " + aux[1] + " " + aux[2] + " " + aux[3]);
                //} while(fila.moveToNext());

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
            Cursor fila = db.rawQuery("SELECT email FROM t_usuarios WHERE email='" + pEmail + "'", null);

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

                try{
                    // Añadir en la tabla de usuarios los datos del nuevo usuario
                    db.insert("t_usuarios",null, datosUsuario);
                    db.close();

                    // Mensaje de información
                    return "Usuario "+ pNombre + " ingresado con exito";
                    //Toast.makeText(this,"Usuario "+ pNombre + " ingresado con exito", Toast.LENGTH_LONG).show();

                } catch (Exception e){      // En caso de excepción
                    db.close();
                    return "ERROR  BASE DE DATOS";
                    //Toast.makeText(this,"ERROR  BASE DE DATOS", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e){     // En caso de excepción
            return "ERROR  BASE DE DATOS";
            //Toast.makeText(this,"ERROR  BASE DE DATOS", Toast.LENGTH_LONG).show();
        }
    }
}
