package ip.signature.com.signatureapps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ip.signature.com.signatureapps.database.annotation.Delete;
import ip.signature.com.signatureapps.database.annotation.Field;
import ip.signature.com.signatureapps.database.annotation.Query;
import ip.signature.com.signatureapps.database.annotation.Reference;
import ip.signature.com.signatureapps.database.annotation.Table;
import ip.signature.com.signatureapps.database.annotation.Update;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public class DBLite extends SQLiteOpenHelper {
    private ArrayList<String> sqlTables;

    public DBLite(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
        sqlTables = new ArrayList<>();
    }

    /**
     * Fungsi untuk membuat SQL create table
     * <br/>
     * Parameter class model harus mengandung anotasi @Table untuk param table
     * <br/>
     * dan anotasi @Field untuk setiap objek field yang ingin dibuat
     * <br/>
     * Note : setiap objek harus memiliki inisialisasi value
     * <br/>
     * Contoh : int id = 0 atau String content = "ini isi content"
     * <br/>
     *
     * @param c class model yang akan dibuat menjadi tabel
     * @return this
     */
    public DBLite create(Class<?> c) {
        String SQL = "CREATE TABLE ";

        // Get annotation table
        Annotation annotations = c.getAnnotation(Table.class);

        int requestCode = (int) getAnnotationValue("whenExists", annotations);
        switch (requestCode) {
            case Exists.DROP_TABLE_IF_EXISTS:
                sqlTables.add("DROP TABLE IF EXISTS " + getAnnotationValue("tableName", annotations).toString());
                break;
            case Exists.SKIP_CREATE_IF_EXISTS:
                SQL += "IF NOT EXISTS ";
                break;
            default:
                break;
        }

        SQL += getAnnotationValue("tableName", annotations).toString() + "(";

        // Get annotation field
        java.lang.reflect.Field[] fields = c.getDeclaredFields();
        for (java.lang.reflect.Field f : fields) {
            Field field = f.getAnnotation(Field.class);
            f.setAccessible(true);
            if (field != null) {
                String notNull = field.notNull() ? "NOT NULL " : "";
                String primaryKey = field.primaryKey() ? "PRIMARY KEY " : "";
                String autoIncrement = field.autoIncrement() ? "AUTOINCREMENT" : "";

                SQL += field.name() + " "
                        + field.type() + "("
                        + field.length() + ") "
                        + notNull
                        + "'" + field.defaultValue() + "' "
                        + primaryKey
                        + autoIncrement;

                SQL += ",";
            }
        }

        //Clear coma (,) on last
        SQL = SQL.substring(0, SQL.length() - 1);

        // Get annotation field for foreign key
        for (java.lang.reflect.Field f : fields) {
            Reference references = f.getAnnotation(Reference.class);
            if (references != null) {
                Annotation annotation = references.tableReference().getDeclaredAnnotation(Table.class);
                Table t = (Table) annotation;
                SQL += ", FOREIGN KEY(" + references.fieldName() + ") REFERENCES " + t.tableName() + "(" + references.referenceName() + ")";
            }
        }

        SQL += ");";
        sqlTables.add(SQL);

        System.out.println("SQL : " + SQL);

        return this;
    }

    /**
     *
     * @param c class model tabel
     * @param methodName nama method yang di panggil, yang mengandung anotasi @Query
     * @param <T> dinamis objek
     * @return list objek sesuai class model yang didefinisiakan pada anotasi @Query
     */
    public <T> List<T> query(Class<?> c, String methodName) {
        return query(c, methodName, null);
    }

    /**
     *
     * @param c class model tabel
     * @param methodName nama method yang di panggil, yang mengandung anotasi @Query
     * @param parameters parameter-parameter yang berada pada fungsi yang dipanggil (methodName)
     * @param parameterType class parameters, contoh : String.class
     * @param <T> dinamis objek
     * @return list objek sesuai class model yang didefinisiakan pada anotasi @Query
     */
    public <T> List<T> query(Class<?> c, String methodName, Object[] parameters, Class<?>... parameterType) {
        try {
            List<T> d = new ArrayList<>();
            Method m;
            if (parameters == null) {
                m = c.getMethod(methodName);
            } else {
                m = c.getMethod(methodName, parameterType);
            }

            Annotation annotation = m.getAnnotation(Query.class);
            String query = getAnnotationValue("query", annotation).toString();

//            // Handle query and parameters
            if (parameters != null) {
                String[] params = query.split("\\{");
                String[] attr = query.split("\\{");
                for (int i = 1; i < params.length; i++) {
                    params[i] = params[i].split("\\}")[0];
                    attr[i] = attr[i].split("\\}")[0];
                }

                for (int i = 0; i < parameters.length; i++) {
                    params[i + 1] = String.valueOf(parameters[i]);
                }

                for (int i = 1; i < params.length; i++) {
                    query = query.replaceFirst("\\{" + attr[i] + "\\}", "'" + params[i] + "'");
                }
            }
//
//            // Execute query
            Cursor resultSet = this.getWritableDatabase().rawQuery(query, null);
            while (resultSet.moveToNext()) {
                // Handle result data
                try {
                    // Get Fields
                    Class<?> classes = Class.forName(getAnnotationValue("entity", annotation).toString().replace("class", "").trim());
                    Object clas = classes.newInstance();
                    java.lang.reflect.Field[] fields = classes.getDeclaredFields();
                    for (java.lang.reflect.Field f : fields) {
                        if (f.isAnnotationPresent(Field.class)) {
                            Annotation field = f.getAnnotation(Field.class);
                            String fieldName = getAnnotationValue("name", field).toString();
                            f.setAccessible(true);

                            // set data object
                            if (resultSet.getColumnIndex(fieldName) > -1) {
                                Class<?> dataType = f.getType();
                                if (dataType == String.class) {
                                    f.set(clas, resultSet.getString(resultSet.getColumnIndex(fieldName)));
                                } else if (dataType == Integer.class || dataType == int.class) {
                                    f.set(clas, resultSet.getInt(resultSet.getColumnIndex(fieldName)));
                                } else if (dataType == Double.class || dataType == double.class) {
                                    f.set(clas, resultSet.getDouble(resultSet.getColumnIndex(fieldName)));
                                } else if (dataType == Long.class || dataType == long.class) {
                                    f.set(clas, resultSet.getLong(resultSet.getColumnIndex(fieldName)));
                                }
                            }
                        }
                    }

                    d.add(((T) clas));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return d;

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return new ArrayList<>();
    }

    public long insert(Class<?> c, String methodName, Object... data) {
        try {
            Method m = c.getMethod(methodName, data.getClass());
//            Annotation insert = m.getAnnotation(Insert.class);
//            Class<?> clas = Class.forName(getAnnotationValue("entity", insert).toString().replace("class", "").trim());
//
////            // Get annotation table
//            Annotation annotations = clas.getDeclaredAnnotation(Table.class);
//            ContentValues contentValue = new ContentValues();
//
//            // Get annotation field
//            int i = 0;
//            java.lang.reflect.Field[] fields = clas.getDeclaredFields();
//            for (java.lang.reflect.Field f : fields) {
//                if (data[i] != null) {
//                    Annotation field = f.getAnnotation(Field.class);
//                    contentValue.put(getAnnotationValue("name", field).toString(), String.valueOf(data[i]));
//                    i++;
//                }
//            }
//
//            return this.getWritableDatabase().insert(
//                    getAnnotationValue("tableName", annotations).toString(),
//                    null,
//                    contentValue
//            );

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public int update(Class<?> c, String methodName, String[] fieldSet, Object[] fieldValue, String[] args, String[] operation, Object[] valArgs) {
        String query = "UPDATE ";
        try {
            Method m = c.getMethod(
                    methodName,
                    fieldSet.getClass(),
                    fieldValue.getClass(),
                    args.getClass(),
                    operation.getClass(),
                    valArgs.getClass()
            );
            Update update = m.getAnnotation(Update.class);
            Class clas = update.entity();

            // Get annotation table
            Annotation annotations = clas.getDeclaredAnnotation(Table.class);
            Table table = (Table) annotations;

            ContentValues contentValues = new ContentValues();
            String whereClause = "";
            String[] whereArgs = null;

            // Set where args
            if (args.length > 0 && operation.length > 0 && valArgs.length > 0) {
                whereArgs = new String[valArgs.length];

                for (int i = 0; i < args.length; i++) {
                    whereClause += args[i] + "" + operation[i] + "? AND ";
                    whereArgs[i] = String.valueOf(valArgs[i]);
                }

                whereClause = whereClause.substring(0, whereClause.length() - 4);
            }

            // Set content values
            for (int i = 0; i < fieldSet.length; i++) {
                contentValues.put(fieldSet[i], String.valueOf(fieldValue[i]));
            }

            return this.getWritableDatabase().update(table.tableName(), contentValues, whereClause, whereArgs);

        } catch (NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public int delete(Class<?> c, String methodName, String[] args, String[] operations, Object[] valArgs) {
        try {
            Method m = c.getMethod(methodName, args.getClass(), operations.getClass(), valArgs.getClass());
            Delete delete = m.getAnnotation(Delete.class);
            Class clas = delete.entity();

            // Get annotation table
            Annotation annotations = clas.getDeclaredAnnotation(Table.class);
            Table table = (Table) annotations;

            String whereClause = "";
            String[] whereArgs = null;
            if (args.length > 0 && operations.length > 0 && valArgs.length > 0) {
                whereArgs = new String[valArgs.length];

                for (int i = 0; i < args.length; i++) {
                    whereClause += args[i] + operations[i] + "? AND ";
                    whereArgs[i] = String.valueOf(valArgs[i]);
                }

                whereClause = whereClause.substring(0, whereClause.length() - 4);
            }

            return this.getWritableDatabase().delete(table.tableName(), whereClause, whereArgs);

        } catch (NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private Object getAnnotationValue(String key, Annotation annotation) {
        try {
            Class<? extends Annotation> type = annotation.annotationType();
            for (Method mm : type.getDeclaredMethods()) {
                if (mm.getName().equals(key)) {
                    return mm.invoke(annotation, (Object[])null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
         Fungsi untuk membangun tabel-tabel yang telah di definisikan sebelumnya pada fungsi create(Class)
     */
    public DBLite build() {
        this.getWritableDatabase();
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            for (int i = 0; i < sqlTables.size(); i++) {
                Log.d("SQL", sqlTables.get(i));
                sqLiteDatabase.execSQL(sqlTables.get(i));
            }
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            onCreate(sqLiteDatabase);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
