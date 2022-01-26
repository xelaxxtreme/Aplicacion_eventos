package my.wab.festejaper.db;

public class TablaEventos {
    public static final String CrearTablaEventos =
            "CREATE TABLE "+"tEventos"+"("+
                    "ID varchar(200),"+
                    "Artistas varchar(500)," +
                    "Comprar varchar(500),"+
                    "Departamento varchar(100),"+
                    "Direccion varchar(200),"+
                    "Fecha varchar(10),"+
                    "Hora varchar(7),"+
                    "Imagen varchar(500)," +
                    "Local varchar(100)," +
                    "Nombre varchar(150)," +
                    "Provincia varchar(50)," +
                    "TipoEvento varchar(50))";
}
